/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.discovery.zookeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.gravitee.discovery.api.ServiceDiscovery;
import io.gravitee.discovery.api.event.Event;
import io.gravitee.discovery.api.event.Handler;
import io.gravitee.discovery.api.service.AbstractServiceDiscovery;
import io.gravitee.discovery.zookeeper.configuration.ZookeeperServiceDiscoveryConfiguration;
import io.gravitee.discovery.zookeeper.service.ZookeeperService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.zookeeper.ZookeeperServiceImporter;

/**
 * @author Daren KLAMER (daren.klamer at toroclouad.com)
 * @author GraviteeSource Team
 */
public class ZookeeperServiceDiscovery extends AbstractServiceDiscovery<ZookeeperService> implements ServiceDiscovery {

    private final static Logger LOGGER = LoggerFactory.getLogger( ZookeeperServiceDiscovery.class );

    private io.vertx.servicediscovery.ServiceDiscovery serviceDiscovery;

    private static final String DISCOVERY_CHANNEL_NAME = "vertx.discovery.announce";

    @Autowired
    private Vertx vertx;

    private final ZookeeperServiceDiscoveryConfiguration configuration;

    public ZookeeperServiceDiscovery( final ZookeeperServiceDiscoveryConfiguration configuration ) {
        this.configuration = configuration;
    }

    public void stop() {
        serviceDiscovery.close();
    }

    @Override
    public void listen( Handler<Event> handler ) {
        if ( configuration.getService() == null || configuration.getService().trim().isEmpty() )
            return;

        serviceDiscovery = io.vertx.servicediscovery.ServiceDiscovery.create( vertx );
        serviceDiscovery.registerServiceImporter(
            new ZookeeperServiceImporter(),
            new JsonObject().put( "connection", configuration.getUrl() )
                .put( "basePath", configuration.getBasePath() )
                .put( "maxRetries", configuration.getMaxRetries() )
                .put( "baseSleepTimeBetweenRetries", configuration.getBaseSleepTimeBetweenRetriesMs() )
                .put( "connectionTimeoutMs", configuration.getConnectionTimeoutMs() ),
            null );

        vertx.eventBus().consumer( DISCOVERY_CHANNEL_NAME, message -> {
            JsonObject jsonObject = (JsonObject) message.body();
            if ( configuration.getService().equals( jsonObject.getString( "name" ) ) ) {
                ZookeeperService zookeeperService = new ZookeeperService( jsonObject );

                // Get previous service reference
                ZookeeperService oldService = getService( zookeeperService::equals );

                if ( "UP".equals( jsonObject.getString( "status" ) ) )
                    if ( oldService == null ) {
                        LOGGER.info( "Register a new service from Zookeeper: id[{}] name[{}]",
                            zookeeperService.id(), configuration.getService() );
                        System.out.println( zookeeperService );
                        handler.handle( registerEndpoint( zookeeperService ) );
                    } else {
                        // Update it only if target has been changed
                        if ( zookeeperService.port() != oldService.port() ||
                            !zookeeperService.host().equals( oldService.host() ) ) {
                            LOGGER.info( "Update an existing service from Zookeeper: id[{}] name[{}] address[{}:{}]",
                                zookeeperService.id(), configuration.getService(), zookeeperService.host(),
                                zookeeperService.port() );
                            handler.handle( unregisterEndpoint( oldService ) );
                            handler.handle( registerEndpoint( zookeeperService ) );
                        }
                    }
                else {
                    //going down
                    LOGGER.info( "Removing an existing service from Zookeeper: id[{}] name[{}] address[{}:{}]",
                        zookeeperService.id(), configuration.getService(), zookeeperService.host(),
                        zookeeperService.port() );
                    handler.handle( unregisterEndpoint( zookeeperService ) );
                }
            }
        } );
    }
}




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
package io.gravitee.discovery.zookeeper.service;

import java.util.Objects;

import io.gravitee.discovery.api.service.Service;
import io.vertx.core.json.JsonObject;

/**
 * @author Daren Klamer (Daren KLAMER at torocloud.com)
 * @author TOROCloud.com
 */
public class ZookeeperService implements Service {

    private final static String ZOOKEEPER_ID_PREFIX = "zookeeper:";
    private final String id;
    private final int port;
    private final String address;

    public ZookeeperService( JsonObject jsonObject ) {
        this.id = ZOOKEEPER_ID_PREFIX + jsonObject.getJsonObject( "metadata" ).getString( "zookeeper-id" );
        JsonObject location = jsonObject.getJsonObject( "location" );
        String uri = "http://";
        if ( location.containsKey( "ssl-port" ) ) {
            this.port = location.getInteger( "ssl-port" );
            uri = "https://";
        } else
            this.port = location.getInteger( "port" );
        this.address = uri + location.getString( "address" );
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String host() {
        return address;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        ZookeeperService that = (ZookeeperService) o;
        return id.equals( that.id );
    }

    @Override
    public String toString() {
        return "ZookeeperService{" +
            "id='" + id + '\'' +
            ", port=" + port +
            ", address='" + address + '\'' +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash( id );
    }
}

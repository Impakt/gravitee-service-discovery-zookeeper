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
package io.gravitee.discovery.zookeeper.configuration;

import io.gravitee.discovery.api.ServiceDiscoveryConfiguration;

/**
 * @author Daren KLAMER (daren.klamer at torocloud.com)
 * @author GraviteeSource Team
 */
public class ZookeeperServiceDiscoveryConfiguration implements ServiceDiscoveryConfiguration {

    private String url;

    private String basePath;

    private String service;

    private int maxRetries;

    private int baseSleepTimeBetweenRetriesMs;

    private int connectionTimeoutMs;

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath( String basePath ) {
        this.basePath = basePath;
    }

    public String getService() {
        return service;
    }

    public void setService( String service ) {
        this.service = service;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries( int maxRetries ) {
        this.maxRetries = maxRetries;
    }

    public int getBaseSleepTimeBetweenRetriesMs() {
        return baseSleepTimeBetweenRetriesMs;
    }

    public void setBaseSleepTimeBetweenRetriesMs( int baseSleepTimeBetweenRetriesMs ) {
        this.baseSleepTimeBetweenRetriesMs = baseSleepTimeBetweenRetriesMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs( int connectionTimeoutMs ) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }
}

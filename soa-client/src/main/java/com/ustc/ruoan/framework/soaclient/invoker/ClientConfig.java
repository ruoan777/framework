package com.ustc.ruoan.framework.soaclient.invoker;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ruoan
 * @date 2022/3/20 11:50 下午
 */
@Setter
@Getter
@Builder
public class ClientConfig {
    private String format;
    private int requestTimeout;
    private int socketTimeout;
    private int maxConnectionPerRoute;
    private int connectTimeout;
    private int idleTime;
    private boolean logEnable;
    private boolean ignoreError;

    public ClientConfig() {
    }

    public ClientConfig(String format, int requestTimeout, int socketTimeout, int maxConnectionPerRoute, int connectTimeout, int idleTime, boolean logEnable, boolean ignoreError) {
        this.format = format;
        this.requestTimeout = requestTimeout;
        this.socketTimeout = socketTimeout;
        this.maxConnectionPerRoute = maxConnectionPerRoute;
        this.connectTimeout = connectTimeout;
        this.idleTime = idleTime;
        this.logEnable = logEnable;
        this.ignoreError = ignoreError;
    }

    public static ClientConfig.ClientConfigBuilder builder() {
        return new ClientConfig.ClientConfigBuilder();
    }

    public static class ClientConfigBuilder {
        private String format;
        private int requestTimeout;
        private int socketTimeout;
        private int maxConnectionPerRoute;
        private int connectTimeout;
        private int idleTime;
        private boolean logEnable;
        private boolean ignoreError;

        public ClientConfigBuilder() {
        }

        public ClientConfigBuilder setFormat(String format) {
            this.format = format;
            return this;
        }

        public ClientConfigBuilder setRequestTimeout(int requestTimeout) {
            this.requestTimeout = requestTimeout;
            return this;
        }

        public ClientConfigBuilder setSocketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public ClientConfigBuilder setMaxConnectionPerRoute(int maxConnectionPerRoute) {
            this.maxConnectionPerRoute = maxConnectionPerRoute;
            return this;
        }

        public ClientConfigBuilder setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public ClientConfigBuilder setIdleTime(int idleTime) {
            this.idleTime = idleTime;
            return this;
        }

        public ClientConfigBuilder setLogEnable(boolean logEnable) {
            this.logEnable = logEnable;
            return this;
        }

        public ClientConfigBuilder setIgnoreError(boolean ignoreError) {
            this.ignoreError = ignoreError;
            return this;
        }

        public ClientConfig build() {
            return new ClientConfig(this.format, this.requestTimeout, this.socketTimeout, this.maxConnectionPerRoute, this.connectTimeout, this.idleTime, this.logEnable, this.ignoreError);
        }
    }
}

package com.ustc.ruoan.framework.redis.provider;

/**
 * @author ruoan
 * @date 2022/4/4 9:42 下午
 */
public class RedisFactory extends AbstractSyncProviderFactory<RedisCacheProvider> {

    private static volatile RedisFactory instance;

    @Override
    protected RedisCacheProvider createProvider(String cluster) {
        return new RedisCacheProvider(cluster);
    }

    public static RedisCacheProvider getProvider(String clusterName) {
        return getInstance().doGetProvider(clusterName);
    }

    private static RedisFactory getInstance() {
        if (instance == null) {
            synchronized (RedisFactory.class) {
                if (instance == null) {
                    instance = new RedisFactory();
                }
            }
        }
        return instance;
    }
}

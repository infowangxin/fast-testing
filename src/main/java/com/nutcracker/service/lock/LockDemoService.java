package com.nutcracker.service.lock;

/**
 * 分布式锁测试接口
 *
 * @author 胡桃夹子
 * @date 2022-12-03 14:25
 */
public interface LockDemoService {

    /**
     * redis 分布式锁测试
     *
     * @param key 参数
     * @return true=操作成功,false=操作失败
     */
    public Boolean redisLockDemo(String key);

    /**
     * redis 分布式锁测试
     *
     * @param key 参数
     * @return true=操作成功,false=操作失败
     */
    public Boolean redisLockByAop(String key);

    /**
     * redis 分布式锁测试
     *
     * @param key 参数
     * @return true=操作成功,false=操作失败
     */
    public void redisLockNoResultByAop(String key);


    /**
     * zookeeper 分布式锁测试
     *
     * @param key 参数
     * @return true=操作成功,false=操作失败
     */
    public Boolean zookeeperLockDemo(String key);
}

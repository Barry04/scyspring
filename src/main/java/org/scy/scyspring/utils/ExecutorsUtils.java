package org.scy.scyspring.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ExecutorsUtils {

    /*
    在 Java 中，ThreadPoolExecutor 提供了以下五种内置的拒绝执行策略（Rejecting Execution Handler），用于处理当线程池无法接受新提交的任务时的情况：
     AbortPolicy (默认策略)
     行为：直接抛出 RejectedExecutionException 异常，阻止任务提交。
     说明：这是最直接的拒绝策略，当任务无法被调度执行时，立即抛出异常通知调用者。
     CallerRunsPolicy
     行为：将任务回退到调用者所在的线程中直接执行。
     说明：这种策略会降低系统吞吐量，因为提交任务的线程必须亲自执行被拒绝的任务，从而阻塞了后续任务的提交。但它保证了任务最终会被执行，并且不会丢失任务。
     DiscardPolicy
     行为：默默地丢弃无法执行的任务，不做任何处理。
     说明：这种策略适用于那些即使丢失个别任务也不会对系统造成严重影响，或者能够容忍任务偶尔丢失的应用场景。
     DiscardOldestPolicy
     行为：抛弃线程池队列中最旧的一个未开始执行的任务（通常是最先入队的那个），然后尝试重新提交当前被拒绝的任务。
     说明：这种策略试图通过牺牲一个旧任务来为新任务腾出空间，有可能导致任务执行顺序的混乱。它适用于对任务执行顺序要求不高，但希望尽可能处理新任务的场景。
     Custom Policy
     行为：用户可以自定义拒绝策略，实现 RejectedExecutionHandler 接口并提供自己的处理逻辑。
     说明：对于有特殊需求的应用，可以编写自定义拒绝策略类，例如记录日志、发送报警、将任务存储到备用队列等待后续处理等。
     在实际使用中，应根据系统的具体需求和容错能力选择合适的拒绝策略。上述代码示例中使用的是 AbortPolicy，即遇到拒绝情况时抛出异常。
    */

    /**
     * 获取一个配置好的固定大小线程池。
     * <p>此线程池的核心线程数和最大线程数都被设置为8，超出任务将被拒绝执行，并抛出异常。</p>
     *
     * @return ThreadPoolExecutor 固定大小的线程池实例
     */
    public static ThreadPoolExecutor getFixedThreadPoolExecutor() {
        // 创建一个固定大小的线程池，初始线程数、最大线程数和队列容量自动配置
        return getFixedThreadPoolExecutor(8, 10);
    }


    /**
     * 获取一个固定大小的线程池执行器。
     *
     * @param corePoolSize    核心线程池大小。即线程池中一直保持的最小线程数。
     * @param maximumPoolSize 最大线程池大小。即线程池中允许的最大线程数量。
     * @return 返回配置好的ThreadPoolExecutor实例，用于执行任务。
     */
    public static ThreadPoolExecutor getFixedThreadPoolExecutor(int corePoolSize, int maximumPoolSize) {
        // 创建一个固定大小的线程池
        ThreadPoolExecutor fixedThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(corePoolSize);
        fixedThreadPool.setCorePoolSize(corePoolSize); // 设置核心线程池大小
        fixedThreadPool.setMaximumPoolSize(maximumPoolSize); // 设置最大线程池大小
        // 设置拒绝执行策略为AbortPolicy，当线程池无法执行任务时抛出异常
        fixedThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        fixedThreadPool.setThreadFactory(r -> {
            Thread thread = new Thread(r);
            thread.setName("fixed-thread-pool-" + thread.getId());
            return thread;
        });
        return fixedThreadPool;
    }


    /**
     * 获取一个配置好的缓存线程池实例。
     * <p>此线程池的核心线程数为10，最大线程数为20。线程空闲时间超过60秒会被回收。
     * 如果提交任务时线程池已满，会抛出RejectedExecutionException异常。</p>
     *
     * @return 配置好的ThreadPoolExecutor线程池实例
     */
    public static ThreadPoolExecutor geCacheThreadPoolExecutor() {
        // 创建一个可根据需要缓存线程的线程池
        ThreadPoolExecutor cacheThreadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        // 设置线程池拒绝执行策略为AbortPolicy，即抛出异常
        cacheThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 设置核心线程数为10
        cacheThreadPool.setCorePoolSize(10);
        // 设置最大线程数为20
        cacheThreadPool.setMaximumPoolSize(20);
        // 设置线程的闲置时间为60秒
        cacheThreadPool.setKeepAliveTime(60, java.util.concurrent.TimeUnit.SECONDS);
        // 允许核心线程超时后被回收
        cacheThreadPool.allowCoreThreadTimeOut(true);

        cacheThreadPool.setThreadFactory(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        return cacheThreadPool;
    }

    /**
     * 创建一个定时执行任务的线程池。
     * <p>此线程池最初包含5个线程，最大线程数为10。如果线程空闲时间超过60秒，线程将被终止并从池中移除。
     * 如果提交的任务超过线程池的处理能力，将使用AbortPolicy策略拒绝执行。</p>
     *
     * @return 配置好的ThreadPoolExecutor实例，可用于提交定时或周期性任务。
     */
    public static ThreadPoolExecutor getScheduledThreadPoolExecutor() {
        // 创建一个定时线程池，初始线程数为5
        ThreadPoolExecutor scheduledThreadPool = (ThreadPoolExecutor) Executors.newScheduledThreadPool(5);
        // 设置拒绝执行处理器为AbortPolicy，即当线程池无法接受任务时抛出异常
        scheduledThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 设置线程池的最大线程数为10
        scheduledThreadPool.setMaximumPoolSize(10);
        // 设置线程的闲置时间，单位为秒
        scheduledThreadPool.setKeepAliveTime(60, java.util.concurrent.TimeUnit.SECONDS);
        // 允许核心线程超时后被回收
        scheduledThreadPool.allowCoreThreadTimeOut(true);
        scheduledThreadPool.setThreadFactory(r -> {
            Thread thread = new Thread(r);
            thread.setName("scheduled-thread-pool-executor");
            return thread;
        });
        return scheduledThreadPool;
    }

    /**
     * 获取一个单线程的线程池执行器。
     * 这个方法配置了一个线程池，它只有一个核心线程并且最大线程数也为1，
     * 使用了AbortPolicy拒绝策略，线程保持活跃时间为60秒，允许核心线程超时。
     *
     * @return 返回配置好的单线程Executor实例。
     */
    public static ThreadPoolExecutor getSingleThreadExecutor() {
        ThreadPoolExecutor singleThreadExecutor = (ThreadPoolExecutor) Executors.newSingleThreadExecutor();

        // 配置线程池参数
        singleThreadExecutor.setCorePoolSize(3); // 设置核心线程数为3，尽管实际只会使用一个
        singleThreadExecutor.setMaximumPoolSize(3); // 设置最大线程数为3，保证线程池规模不会扩大
        singleThreadExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); // 设置拒绝执行策略为中断
        singleThreadExecutor.setKeepAliveTime(60, java.util.concurrent.TimeUnit.SECONDS); // 设置线程的空闲等待时间
        singleThreadExecutor.allowCoreThreadTimeOut(true); // 允许核心线程超时后被回收
        singleThreadExecutor.setThreadFactory(r -> {
            Thread thread = new Thread(r); // 自定义线程工厂，设置线程名称
            thread.setName("single-thread-executor");
            return thread;
        });

        return singleThreadExecutor;
    }

    /**
     * 获取一个单线程定时任务执行器。
     * 这个方法创建并配置了一个单线程的线程池，适用于需要定时或周期性执行任务的场景。
     *
     * @return ThreadPoolExecutor 单线程定时任务执行器
     */
    public static ThreadPoolExecutor getSingleThreadScheduledExecutor() {
        // 创建一个单线程的定时任务执行器
        ThreadPoolExecutor singleThreadScheduledExecutor = (ThreadPoolExecutor) Executors.newSingleThreadScheduledExecutor();

        // 设置核心线程池大小为3，实际上由于是单线程执行器，这个设置并不会生效
        singleThreadScheduledExecutor.setCorePoolSize(3);
        // 设置最大线程池大小为3，同上，对于单线程执行器来说无效
        singleThreadScheduledExecutor.setMaximumPoolSize(3);

        // 设置任务拒绝策略为中断策略，当任务无法提交时，抛出RejectedExecutionException
        singleThreadScheduledExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 设置空闲线程的存活时间为60秒
        singleThreadScheduledExecutor.setKeepAliveTime(60, java.util.concurrent.TimeUnit.SECONDS);
        // 允许核心线程超时后被回收
        singleThreadScheduledExecutor.allowCoreThreadTimeOut(true);
        // 设置线程工厂，统一设置线程名称为"single-thread-scheduled-executor"
        singleThreadScheduledExecutor.setThreadFactory(r -> {
            Thread thread = new Thread(r);
            thread.setName("single-thread-scheduled-executor");
            return thread;
        });

        return singleThreadScheduledExecutor;
    }

}

package com.lizhizhan.relaxedweather.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lizhizhan on 2017/1/11.
 */

public class ThreadManager {
    private static threadPool threadPool;

    public static threadPool getThreadPool() {
        if (threadPool == null) {
            synchronized (ThreadManager.class) {
                if (threadPool == null) {
                    //int cpuCount = Runtime.getRuntime().availableProcessors();//获取Cpu数量的
                    int threadCount = 10;
                    threadPool = new threadPool(threadCount, threadCount, 1L);
                }
            }
        }

        return threadPool;
    }


    public static class threadPool {


        int corePoolSize;
        int maximumPoolSize;
        long keepAliveTime;
        private ThreadPoolExecutor executor;

        private threadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable r) {
            if (executor == null) {
                executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                        TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
            }
            executor.execute(r);
        }

    }

}

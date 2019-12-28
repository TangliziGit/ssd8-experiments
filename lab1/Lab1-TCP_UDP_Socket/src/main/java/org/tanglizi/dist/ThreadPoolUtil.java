package org.tanglizi.dist;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CommandThreadPool provides a thread pool using static method as an utility.
 */
public class ThreadPoolUtil {
    private static Integer THREAD_COUNT_PER_PROCESSOR = 4;
    private static ExecutorService executorService;

    /**
     * Set thread count per processor.
     * @param threadCountPerProcessor
     */
    public static void resetThreadCountPerProcessor(Integer threadCountPerProcessor){
        ThreadPoolUtil.THREAD_COUNT_PER_PROCESSOR = threadCountPerProcessor;
    }

    /**
     * Run a new thread given.
     * @param command
     */
    public static void run(Runnable command){
        if (null == executorService)
            executorService = Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors()*THREAD_COUNT_PER_PROCESSOR);
        executorService.execute(command);
    }
}

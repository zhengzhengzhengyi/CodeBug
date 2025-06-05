package org.example.codereview.util;


public class AsyncUtils {

    //  @Async
    public static void commit(Runnable runnable) {
        runnable.run();
    }

}

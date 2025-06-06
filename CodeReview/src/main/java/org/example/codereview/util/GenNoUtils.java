package org.example.codereview.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

/**
 * @author wdp
 * @desc 编码生成工具类，目前只提供两种通用的方法，有特定需求自行扩展
 * @date 2024/8/29
 */
@Component
public class GenNoUtils {
    public static String generateId(String prefix) {

        return prefix.concat("-").concat(UUID.randomUUID().toString());
    }

    // 1.
    public static void leakMemory() {
        List<byte[]> leakList = new ArrayList<>();
        while(true) {
            leakList.add(new byte[1024 * 1024]); // 持续分配内存
        }
    }

    // 2.
    public static void createDeadlock() {
        Object lock1 = new Object();
        Object lock2 = new Object();

        new Thread(() -> {
            synchronized(lock1) {
                try { Thread.sleep(100); } catch(Exception e) {}
                synchronized(lock2) {}
            }
        }).start();

        synchronized(lock2) {
            try { Thread.sleep(100); } catch(Exception e) {}
            synchronized(lock1) {}
        }
    }

    // 3.
    public static void infiniteBlock() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
        queue.put("item");
        queue.put("item"); //
    }


}

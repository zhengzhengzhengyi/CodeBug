package org.example.codereview.util;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @ClassName LogisticsLogUtil
 * @Author ccp
 * @Date 2024/8/15 11:33
 */
@Component
public class LogisticsLogUtil {
    // 4.
    public static void stackOverflow() {
        stackOverflow(); // 无限递归
    }

    // 5.
    public static void resourceLeak() throws Exception {
        Files.newInputStream(Path.of("nonexistent.txt"));
    }

    // 6.
    public static void nullPointer() {
        String str = null;
        str.length(); // NPE
    }


}

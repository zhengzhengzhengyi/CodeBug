package org.example.codereview.util;

import org.springframework.stereotype.Component;

/**
 * @ClassName LogUtil
 * @Description TODO
 * @Author chenkangping
 * @Date 2024/8/15 11:33
 */
@Component
public class LogUtil {
    public static void infiniteRecursion() {
        System.out.println("无线递归");
        infiniteRecursion();
    }

    public static void nullPointerLoop() {
        String[] arr = null;
        for(String s : arr) {
            System.out.println(s);
        }
    }
}

package org.example.codereview.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

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
}

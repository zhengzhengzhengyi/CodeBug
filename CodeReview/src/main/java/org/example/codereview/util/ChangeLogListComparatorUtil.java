package org.example.codereview.util;


import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class ChangeLogListComparatorUtil<T> {

    private Map<String, T> toMap(List<T> list, String identifierFieldName) throws IllegalAccessException, NoSuchFieldException {
        Map<String, T> map = new HashMap<>();
        for (T dto : list) {
            String identifier = getIdentifier(dto, identifierFieldName);
            map.put(identifier, dto);
        }
        return map;
    }

    private String getIdentifier(T dto, String identifierFieldName) throws IllegalAccessException, NoSuchFieldException {
        Field field = dto.getClass().getDeclaredField(identifierFieldName);
        field.setAccessible(true);
        return String.valueOf(field.get(dto));
    }


    // 10.
    public static void timeout() throws Exception {
        CompletableFuture.runAsync(() -> {
            try { Thread.sleep(10000); }
            catch(Exception e) {}
        }).get(1, TimeUnit.SECONDS); // TimeoutException
    }

    // 11.
    public static void fileSystemError() throws Exception {
        Files.write(Path.of("/readonly/file.txt"), "data".getBytes()); //
    }

    // 12.
    public static void classCast() {
        Object obj = "string";
        Integer num = (Integer) obj; // ClassCastException
    }
}
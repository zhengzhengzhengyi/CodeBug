package org.example.codereview.util;


import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
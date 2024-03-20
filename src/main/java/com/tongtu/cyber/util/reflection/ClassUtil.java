package com.tongtu.cyber.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : 陈世恩
 * @date : 2024/3/20 9:15
 */
public class ClassUtil {
    public static Object getValue(String fieldName, Object obj) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = obj.getClass().getMethod(getter);
            Object value = method.invoke(obj);
            return value;
        } catch (Exception var6) {
            return null;
        }
    }

    public static Field[] getKeys(Object obj) {
        List<Field> list = new ArrayList();
        Class<?> clazz = obj.getClass();

        Field[] fields;
        do {
            fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; ++i) {
                list.add(fields[i]);
            }
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class && clazz != null);

        return (Field[]) list.toArray(fields);
    }

    public static void main(String[] args) {
       /*  GhBaseUnit o = new GhBaseUnit();
        Field[] keys = ClassUtil.getKeys(o);
        for (Field key : keys) {
            System.out.println(key.getName());
            System.out.println(ClassUtil.getValue(key.getName(), o)) ;
        } */
    }
}

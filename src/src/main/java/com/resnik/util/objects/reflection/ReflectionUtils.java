package com.resnik.util.objects.reflection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Class<?> findClosestCommonSuper(Class<?> a, Class<?> b) {
        while (!a.isAssignableFrom(b)) {
            a = a.getSuperclass();
        }
        return a;
    }

    public static boolean isParent(Class<?> a, Class<?> b) {
        return ((a == null || b == null) ? false : a.equals(findClosestCommonSuper(a, b)));
    }

    public static <T> T convertInstanceOfObject(Object o) {
        try {
            return (T) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static <T extends Number, K extends Number> K castNum(T obj1, Class clazz) {
        if (obj1 == null || clazz == null) {
            return null;
        }
        if (!Number.class.equals(clazz.getSuperclass())) {
            return null;
        }
        if (clazz.equals(Byte.class)) {
            return (K) new Byte(((Number) obj1).byteValue());
        } else if (clazz.equals(Short.class)) {
            return (K) new Short(((Number) obj1).shortValue());
        } else if (clazz.equals(Long.class)) {
            return (K) new Long(((Number) obj1).longValue());
        } else if (clazz.equals(Integer.class)) {
            return (K) new Integer(((Number) obj1).intValue());
        } else if (clazz.equals(Float.class)) {
            return (K) new Float(((Number) obj1).floatValue());
        } else if (clazz.equals(Double.class)) {
            return (K) new Double(((Number) obj1).doubleValue());
        } else if (clazz.equals(BigInteger.class)) {
            return (K) new BigInteger(((Number) obj1).intValue() + "");
        } else if (clazz.equals(BigDecimal.class)) {
            return (K) new BigDecimal(((Number) obj1).doubleValue() + "");
        } else {
            return null;
        }
    }

    public static <K extends Number> K parseNum(String str1, Class clazz) {
        if (str1 == null || clazz == null) {
            return null;
        }
        if (!Number.class.equals(clazz.getSuperclass())) {
            return null;
        }
        // Take up to the first decimal without alphabet
        String str2 = "";
        int dec = 0;
        for (int i = 0; i < str1.length(); i++) {
            char curr = str1.charAt(i);
            int num = (int) curr;
            // Early escape on two decimals
            dec = ((num == 46) ? dec + 1 : dec);
            if (dec > 1) {
                break;
            }
            str2 = ((num >= 48 && num <= 57) || (num == 46) ? str2 + curr : str2);
        }
        str2 = (str2.equals("") ? "0" : str2);
        return castNum(Double.parseDouble(str2), clazz);
    }

    public static <K extends Number> K parseNumJava(String str1, Class clazz) {
        if (!Number.class.equals(clazz.getSuperclass())) {
            return null;
        }
        return castNum(new Double(Double.parseDouble(str1)), clazz);
    }

    public static Class<?> classOf(Object obj1) {
        return ((obj1 == null) ? null : obj1.getClass());
    }

    public static String stringOf(Object obj1) {
        if (obj1 == null) {
            return "";
        }
        return ((obj1.getClass().isArray()) ? Arrays.toString((Object[]) obj1) : obj1.toString());
    }

    public static Class fromSimpleName(String simpleName) throws ClassNotFoundException {
        Class clazz = Class.forName(simpleName);
        return clazz;
    }

    public static void main(String[] args) {
        try {
            System.out.println(fromSimpleName("ReflectionUtils"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}

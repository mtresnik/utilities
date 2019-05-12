package util.objects;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import static util.objects.reflection.castNum;
import static util.objects.reflection.classOf;
import static util.objects.reflection.findClosestCommonSuper;
import static util.objects.reflection.isParent;
import static util.objects.reflection.parseNum;
import static util.objects.reflection.stringOf;
import static util.objects.strings.concatDelim;

public final class arrays {

    private arrays() {

    }

    public static <T> T[][] subset(T[][] input,  int iROW, int iCOL, int m, int n, T... defaultValue) {
        Class<T> clazz = null;
        T[][] neu = (T[][]) Array.newInstance(input[0].getClass(), m);
        for (int ROW = 0; ROW < m; ROW++) {
            T[] neuRow = (T[]) Array.newInstance(input[0][0].getClass(), n);
            for (int COL = 0; COL < n; COL++) {
                try {
                    neuRow[COL] = input[iROW + ROW][iCOL + COL];
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    neuRow[COL] = (defaultValue.length > 0 ? defaultValue[0] : null);
                }
            }
            neu[ROW] =neuRow;
        }
        return neu;
    }

    public static <T, K> K[] apply(T[] inputArray, Function<T, K> operator, Function<T, K>... overrideCast) {
        return (K[]) o_apply(inputArray, operator, overrideCast);
    }

    public static <T, K> Object o_apply(T[] inputArray, Function<T, K> operator, Function<T, K>... overrideCast) {
        Class<K> clazz = null;
        ArrayList<K> retList = new ArrayList<K>();
        for (T elem : inputArray) {
            K _elem = operator.apply(elem);
            clazz = ((clazz == null) ? (Class<K>) _elem.getClass() : clazz);
            retList.add(operator.apply(elem));
        }
        if (overrideCast.length > 0) {
            return o_castArray(inputArray, clazz, overrideCast);
        }
        K[] retArray = (K[]) Array.newInstance(clazz, inputArray.length);
        Object[] o_retArray = retList.toArray();
        for (int i = 0; i < o_retArray.length; i++) {
            retArray[i] = (K) o_retArray[i];
        }
        return retArray;
    }

    public static <T, K> K[] castArray(T[] inputArray, Class<K> toCast, Function<T, K>... overrideCast) {
        return (K[]) o_castArray(inputArray, toCast, overrideCast);
    }

    public static <T, K> Object o_castArray(T[] inputArray, Class<K> toCast, Function<T, K>... overrideCast) {
        if (inputArray == null || toCast == null) {
            throw new IllegalArgumentException("Cannot have null parameters." + concatDelim(",", inputArray, toCast));
        }
        K[] retArray = (K[]) Array.newInstance(toCast, inputArray.length);

        if (overrideCast.length > 0) {
            // Use override, assumes coder knows what they are doing.
            for (int i = 0; i < inputArray.length; i++) {
                retArray[i] = (K) overrideCast[0].apply(inputArray[i]);
            }
            // If Number then Cast
        } else if (Number.class.equals(findClosestCommonSuper(classOf(inputArray[0]), toCast))) {
            for (int i = 0; i < inputArray.length; i++) {
                retArray[i] = (K) castNum((Number) inputArray[i], toCast);
            }
            // If String and Number then Parse
        } else if (String.class.equals(classOf(inputArray[0])) && isParent(Number.class, toCast)) {
            for (int i = 0; i < inputArray.length; i++) {
                retArray[i] = (K) parseNum(stringOf(inputArray[i]), toCast);
            }
        }
        return retArray;
    }

    public static byte[] deltaBytes(byte[] b1, byte[] b2) {
        return intsToBytes(deltaInts(bytesToInts(b1), bytesToInts(b2)));
    }

    public static byte[] absDeltaBytes(byte[] b1, byte[] b2) {
        return intsToBytes(absDeltaInts(bytesToInts(b1), bytesToInts(b2)));
    }

    public static int[] deltaInts(int[] arr1, int[] arr2) {
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) {
            throw new IllegalArgumentException("Parameters must be non-null and of the same length. arr1:" + Arrays.toString(arr1) + "\narr2:" + Arrays.toString(arr2));
        }
        int[] retArray = new int[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            retArray[i] = arr2[i] - arr1[i];
        }
        return retArray;
    }

    public static int[] absDeltaInts(int[] arr1, int[] arr2) {
        int[] deltaApplied = deltaInts(arr1, arr2);
        int[] retArray = new int[deltaApplied.length];
        for (int i = 0; i < deltaApplied.length; i++) {
            retArray[i] = Math.abs(deltaApplied[i]);
        }
        return retArray;
    }

    public static int[] bytesToInts(byte[] b1) {
        int[] retArray = new int[b1.length];
        for (int i = 0; i < b1.length; i++) {
            retArray[i] = b1[i];
        }
        return retArray;
    }

    public static byte[] intsToBytes(int[] b1) {
        byte[] retArray = new byte[b1.length];
        for (int i = 0; i < b1.length; i++) {
            retArray[i] = (byte) b1[i];
        }
        return retArray;
    }

}

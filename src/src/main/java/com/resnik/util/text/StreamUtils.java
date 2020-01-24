package com.resnik.util.text;

import java.io.PrintStream;
import static com.resnik.util.text.StringUtils.*;

public final class StreamUtils {

    private StreamUtils() {

    }

    public static void ps_print(PrintStream ps, Object... a) {
        ps.print(concat(a));
    }

    public static void ps_println(PrintStream ps, Object... a) {
        ps.print(concatNL(a));
    }

    public static void ps_printDelim(PrintStream ps, String delim, Object... a) {
        ps.print(concatDelim(delim, a));
    }

    public static void print(Object... a) {
        ps_print(System.out, a);
    }

    public static void println(Object... a) {
        if (a.length == 1 && a[0].getClass().isArray()) {
            ps_println(System.out, (Object) a[0]);
            return;
        } else if (a.length > 1){
            ps_println(System.out, a);
            return;
        }
        ps_println(System.out);
    }

    public static void printDelim(String delim, Object... a) {
        ps_printDelim(System.out, delim, a);
    }

}

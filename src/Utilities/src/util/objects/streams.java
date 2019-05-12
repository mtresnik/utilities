package util.objects;

import java.io.PrintStream;
import static util.objects.strings.*;

public final class streams {

    private streams() {

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
        if (a[0].getClass().isArray() && a.length == 0) {
            ps_println(System.out, (Object) a[0]);
        } else {
            ps_println(System.out, a);
        }
    }

    public static void printDelim(String delim, Object... a) {
        ps_printDelim(System.out, delim, a);
    }

}

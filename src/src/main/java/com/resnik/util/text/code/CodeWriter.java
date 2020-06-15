package com.resnik.util.text.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class CodeWriter {

    public static void main(String[] args) throws Exception{
        // Define code
        String pack = "com.resnik.util.text.code";
        String name = "TestClass";
        String path = "src/src/main/java/" + pack.replace(".", "/") + "/" + name + ".java";
        String code = "";
        code += "package com.resnik.util.text.code;\n";
        code += "\n";
        code += "public class " + name + " {\n";
        code += "\tpublic void hello(){\n" +
                 "\t\tSystem.out.println(\"Hello World\");\n" +
                "\t}" +
                "\n}";
        String fullName = pack + "." + name;

        // Write code
        File file = new File(path);
        try {
            PrintWriter pw = new PrintWriter(file);
            pw.println(code);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Load code
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;
        Class[] parameters = new Class[]{URL.class};
        URL u = file.toURL();
        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{u});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }

        // Execute Code
        Class clazz = Class.forName(fullName);
        Constructor constructor = clazz.getConstructor();
        Object obj = constructor.newInstance();
        Method[] methods = clazz.getMethods();
        Method method = clazz.getMethod("hello");
        method.invoke(obj);
    }

}

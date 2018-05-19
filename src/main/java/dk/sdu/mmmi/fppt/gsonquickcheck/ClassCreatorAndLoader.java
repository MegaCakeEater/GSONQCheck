/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import sun.misc.Unsafe;

/**
 *
 * @author Bogs
 */
public class ClassCreatorAndLoader {

    public String createAndLoadClass(Map<String, String> fields, String className, String path) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String fullClassName = path.replace('.', '/') + "/" + className;
        SimpleJavaFileObject obj = new SimpleJavaFileObject(URI.create(fullClassName + ".java"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean bln) throws IOException {
                return createSource(fields, className, path);
            }

            @Override
            public OutputStream openOutputStream() throws IOException {
                return baos;
            }

        };

        JavaFileManager jfm = new ForwardingJavaFileManager(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null)) {
            @Override
            public JavaFileObject getJavaFileForOutput(JavaFileManager.Location lctn, String string, JavaFileObject.Kind kind, FileObject fo) throws IOException {
                return obj;
            }

        };

        ToolProvider.getSystemJavaCompiler().getTask(null, jfm, null, null, null, Collections.singletonList(obj)).call();

        byte[] bytes = baos.toByteArray();

        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
        unsafe.defineClass(fullClassName, bytes, 0, bytes.length, null, null);
        return fullClassName;
    }

    private StringBuilder createSource(Map<String, String> fields, String className, String path) {
        StringBuilder source = new StringBuilder();
        source.append("package " + path + ";\n");
        source.append("public class " + className + " {\n");
        fields.forEach((key, value) -> {
            source.append("private " + value + " " + key + ";\n");
        });
        source.append("@Override\n");
        source.append("public String toString() {\n");
        if (!fields.isEmpty()) {
            source.append("return ");
            fields.forEach((key, value) -> {
                source.append("\"" + key + ":\"+" + key + "+\"\\n\"+");
            });
            source.deleteCharAt(source.length() - 1);
            source.append(";\n");
        } else {
            source.append("return \"EMPTY CLASS\";");
        }
        source.append("}\n");
        source.append("}\n");
        System.out.println(source);
        return source;
    }

}

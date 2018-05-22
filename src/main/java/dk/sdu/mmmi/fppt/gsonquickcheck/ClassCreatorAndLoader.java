/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import sun.misc.Unsafe;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

/**
 * @author Bogs
 */
public class ClassCreatorAndLoader {

    public String createAndLoadClass(Map<String, String> fields, String className, String path) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String fullClassName = path.replace('.', '/') + "/" + className;
        SimpleJavaFileObject obj = new SimpleJavaFileObject(URI.create(fullClassName + ".java"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean bln) {
                return createSource(fields, className, path);
            }

            @Override
            public OutputStream openOutputStream() {
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

        source.append("import java.lang.reflect.Field;\n" + //Required for equals method
                "import java.util.Arrays;\n" +
                "import java.util.Objects;");
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
        source.append(" @Override\n" +
                "    public boolean equals(Object o) {\n" +
                "        if (this == o) return true;\n" +
                "        if (o == null || getClass() != o.getClass()) return false;\n");
        source.append(className + " that = this.getClass().cast(o);\n");
        source.append(" Field[] flds = getClass().getDeclaredFields();\n" +
                "        return Arrays.stream(flds).allMatch(\n" +
                "                fld -> {\n" +
                "                    try {\n" +
                "                        return Objects.equals(fld.get(this), fld.get(that));\n" +
                "                    } catch (IllegalAccessException e) {\n" +
                "                        e.printStackTrace();\n" +
                "                        return false;\n" +
                "                    }\n" +
                "                }\n" +
                "        );\n" +
                "\n" +
                "    }");
   /*     source.append("  @Override\n" +
                "    public String toString() {\n" +
                "        Field[] flds = getClass().getDeclaredFields();\n" +
                "        return Arrays.stream(flds).map(\n" +
                "                fld -> {\n" +
                "                    try {\n" +
                "                        return fld.getName() + \":\" + fld.get(this);\n" +
                "                    } catch (IllegalAccessException e) {\n" +
                "                        e.printStackTrace();\n" +
                "                        return \"\";\n" +
                "                    }\n" +
                "                }\n" +
                "        ).reduce(\"\", String::concat);\n" +
                "    }");*/
        source.append("}\n");
        return source;
    }

}

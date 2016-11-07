package com.liuj.lsf.utils;

import javassist.CtClass;
import javassist.NotFoundException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReflectionUtils {
     public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final Pattern DESC_PATTERN = Pattern.compile("(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;)|(?:\\[+(?:(?:[VZBCDFIJS])|(?:L(?:[_$a-zA-Z][_$a-zA-Z0-9]*)(?:\\/(?:[_$a-zA-Z][_$a-zA-Z0-9]*))*;))))");
    private static final ConcurrentMap<String, Class<?>> DESC_CLASS_CACHE = new ConcurrentHashMap();
    private static final ConcurrentMap<String, Class<?>> NAME_CLASS_CACHE = new ConcurrentHashMap();

    public ReflectionUtils() {
    }


    private static boolean isPrimitiveType(Class<?> clazz) {
        return clazz.isPrimitive() || Boolean.class == clazz || Character.class == clazz || Number.class.isAssignableFrom(clazz) || String.class == clazz || Date.class.isAssignableFrom(clazz);
    }


    public static String getName(Class<?> c) {
        if (!c.isArray()) {
            return c.getName();
        } else {
            StringBuilder sb = new StringBuilder();

            do {
                sb.append("[]");
                c = c.getComponentType();
            } while (c.isArray());

            return c.getName() + sb.toString();
        }
    }

    public static String getName(Method m) {
        StringBuilder ret = new StringBuilder();
        ret.append(getName(m.getReturnType())).append(' ');
        ret.append(m.getName()).append('(');
        Class[] parameterTypes = m.getParameterTypes();

        for (int i = 0; i < parameterTypes.length; ++i) {
            if (i > 0) {
                ret.append(',');
            }

            ret.append(getName(parameterTypes[i]));
        }

        ret.append(')');
        return ret.toString();
    }


    public static String getDesc(Class<?> c) {
        StringBuilder ret;
        for (ret = new StringBuilder(); c.isArray(); c = c.getComponentType()) {
            ret.append('[');
        }

        if (c.isPrimitive()) {
            String t = c.getName();
            if ("void".equals(t)) {
                ret.append('V');
            } else if ("boolean".equals(t)) {
                ret.append('Z');
            } else if ("byte".equals(t)) {
                ret.append('B');
            } else if ("char".equals(t)) {
                ret.append('C');
            } else if ("double".equals(t)) {
                ret.append('D');
            } else if ("float".equals(t)) {
                ret.append('F');
            } else if ("int".equals(t)) {
                ret.append('I');
            } else if ("long".equals(t)) {
                ret.append('J');
            } else if ("short".equals(t)) {
                ret.append('S');
            }
        } else {
            ret.append('L');
            ret.append(c.getName().replace('.', '/'));
            ret.append(';');
        }

        return ret.toString();
    }


    public static String getDesc(CtClass c) throws NotFoundException {
        StringBuilder ret = new StringBuilder();
        if (c.isArray()) {
            ret.append('[');
            ret.append(getDesc(c.getComponentType()));
        } else if (c.isPrimitive()) {
            String t = c.getName();
            if ("void".equals(t)) {
                ret.append('V');
            } else if ("boolean".equals(t)) {
                ret.append('Z');
            } else if ("byte".equals(t)) {
                ret.append('B');
            } else if ("char".equals(t)) {
                ret.append('C');
            } else if ("double".equals(t)) {
                ret.append('D');
            } else if ("float".equals(t)) {
                ret.append('F');
            } else if ("int".equals(t)) {
                ret.append('I');
            } else if ("long".equals(t)) {
                ret.append('J');
            } else if ("short".equals(t)) {
                ret.append('S');
            }
        } else {
            ret.append('L');
            ret.append(c.getName().replace('.', '/'));
            ret.append(';');
        }

        return ret.toString();
    }

    public static Class<?> forName(String name) {
        try {
            return name2class(name);
        } catch (ClassNotFoundException var2) {
            throw new IllegalStateException("Not found class " + name + ", cause: " + var2.getMessage(), var2);
        }
    }

    public static Class<?> name2class(String name) throws ClassNotFoundException {
        return name2class(getCurrentClassLoader(), name);
    }

    private static Class<?> name2class(ClassLoader cl, String name) throws ClassNotFoundException {
        int c = 0;
        int index = name.indexOf(91);
        if (index > 0) {
            c = (name.length() - index) / 2;
            name = name.substring(0, index);
        }

        if (c > 0) {
            StringBuilder clazz = new StringBuilder();

            while (c-- > 0) {
                clazz.append("[");
            }

            if ("void".equals(name)) {
                clazz.append('V');
            } else if ("boolean".equals(name)) {
                clazz.append('Z');
            } else if ("byte".equals(name)) {
                clazz.append('B');
            } else if ("char".equals(name)) {
                clazz.append('C');
            } else if ("double".equals(name)) {
                clazz.append('D');
            } else if ("float".equals(name)) {
                clazz.append('F');
            } else if ("int".equals(name)) {
                clazz.append('I');
            } else if ("long".equals(name)) {
                clazz.append('J');
            } else if ("short".equals(name)) {
                clazz.append('S');
            } else {
                clazz.append('L').append(name).append(';');
            }

            name = clazz.toString();
        } else {
            if ("void".equals(name)) {
                return Void.TYPE;
            }

            if ("boolean".equals(name)) {
                return Boolean.TYPE;
            }

            if ("byte".equals(name)) {
                return Byte.TYPE;
            }

            if ("char".equals(name)) {
                return Character.TYPE;
            }

            if ("double".equals(name)) {
                return Double.TYPE;
            }

            if ("float".equals(name)) {
                return Float.TYPE;
            }

            if ("int".equals(name)) {
                return Integer.TYPE;
            }

            if ("long".equals(name)) {
                return Long.TYPE;
            }

            if ("short".equals(name)) {
                return Short.TYPE;
            }
        }

        if (cl == null) {
            cl = getCurrentClassLoader();
        }

        Class var5 = (Class) NAME_CLASS_CACHE.get(name);
        if (var5 == null) {
            var5 = Class.forName(name, true, cl);
            NAME_CLASS_CACHE.put(name, var5);
        }

        return var5;
    }

    private static Class<?> desc2class(ClassLoader cl, String desc) throws ClassNotFoundException {
        switch (desc.charAt(0)) {
            case 'B':
                return Byte.TYPE;
            case 'C':
                return Character.TYPE;
            case 'D':
                return Double.TYPE;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new ClassNotFoundException("Class not found: " + desc);
            case 'F':
                return Float.TYPE;
            case 'I':
                return Integer.TYPE;
            case 'J':
                return Long.TYPE;
            case 'L':
                desc = desc.substring(1, desc.length() - 1).replace('/', '.');
                break;
            case 'S':
                return Short.TYPE;
            case 'V':
                return Void.TYPE;
            case 'Z':
                return Boolean.TYPE;
            case '[':
                desc = desc.replace('/', '.');
        }

        if (cl == null) {
            cl = getCurrentClassLoader();
        }

        Class clazz = (Class) DESC_CLASS_CACHE.get(desc);
        if (clazz == null) {
            clazz = Class.forName(desc, true, cl);
            DESC_CLASS_CACHE.put(desc, clazz);
        }

        return clazz;
    }

    public static Class<?>[] desc2classArray(String desc) throws ClassNotFoundException {
        Class[] ret = desc2classArray(getCurrentClassLoader(), desc);
        return ret;
    }

    private static Class<?>[] desc2classArray(ClassLoader cl, String desc) throws ClassNotFoundException {
        if (desc.length() == 0) {
            return EMPTY_CLASS_ARRAY;
        } else {
            ArrayList cs = new ArrayList();
            Matcher m = DESC_PATTERN.matcher(desc);

            while (m.find()) {
                cs.add(desc2class(cl, m.group()));
            }

            return (Class[]) cs.toArray(EMPTY_CLASS_ARRAY);
        }
    }

    public static ClassLoader getCurrentClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ReflectionUtils.class.getClassLoader();
        }

        return cl == null ? ClassLoader.getSystemClassLoader() : cl;
    }
}

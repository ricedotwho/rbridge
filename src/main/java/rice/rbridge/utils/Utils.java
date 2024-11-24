package rice.rbridge.utils;

import java.lang.reflect.Method;
import java.util.Objects;

public class Utils {
    public static boolean equalsOneOf(Object object, Object... others) {
        for (Object obj : others) {
            if (Objects.equals(object, obj)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isFloat(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isInt(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // MY SAVIOUR https://mcp.thiakil.com/#/search
    public static void printAvailableMethods(Class<?> clazz) {
        // Get all declared methods
        Method[] methods = clazz.getDeclaredMethods();

        System.out.println("Available methods in class " + clazz.getName() + ":");
        for (Method method : methods) {
            // Print method name and parameter types
            StringBuilder sb = new StringBuilder();
            sb.append(method.getName()).append("(");

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                sb.append(parameterTypes[i].getSimpleName());
                if (i < parameterTypes.length - 1) {
                    sb.append(",");
                }
            }
            sb.append(") : ").append(method.getReturnType().getSimpleName());
            System.out.println(sb);
        }
    }
}

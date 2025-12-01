package it.fair.McCommands;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.List;

public record Command(String pkg, Class<?> clazz, List<Method> method, List<String> subPackages) {

    public String getCommandName() {
        return clazz.getSimpleName();
    }

    public TypeVariable<Method>[] getCommandParameters(int idx) {
        return this.method.get(idx).getTypeParameters();
    }

    public Method getMethod(int idx) {
        return this.method.get(idx);
    }
}

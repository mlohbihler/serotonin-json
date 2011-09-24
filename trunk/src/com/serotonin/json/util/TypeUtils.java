package com.serotonin.json.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Utilities for determining generic type information.
 * 
 * @author Matthew Lohbihler
 */
public class TypeUtils {
    public static Class<?> getRawClass(Type type) {
        if (type instanceof Class)
            return (Class<?>) type;
        if (type instanceof ParameterizedType)
            return getRawClass(((ParameterizedType) type).getRawType());
        throw new RuntimeException("Unknown type: " + type);
    }

    public static Type getActualTypeArgument(Type type, int index) {
        if (type instanceof Class)
            return null;
        if (type instanceof ParameterizedType)
            return ((ParameterizedType) type).getActualTypeArguments()[index];
        throw new RuntimeException("Unknown type: " + type);
    }

    public static Type resolveTypeVariable(Type type, Type propertyType) {
        if (propertyType instanceof TypeVariable) {
            TypeVariable<?> typeVar = (TypeVariable<?>) propertyType;

            Class<?> typeClass = getRawClass(type);
            TypeVariable<?>[] typeVars = typeClass.getTypeParameters();

            int index = ArrayUtils.indexOf(typeVars, typeVar);

            return ((ParameterizedType) type).getActualTypeArguments()[index];
        }

        return propertyType;
    }
}

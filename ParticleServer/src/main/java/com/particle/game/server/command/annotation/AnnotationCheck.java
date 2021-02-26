package com.particle.game.server.command.annotation;

import com.particle.model.utils.ProphetPatterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.Map;

public class AnnotationCheck {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationCheck.class);

    private final static Map<Class<? extends Annotation>, Method> valueMethods = new IdentityHashMap<>();

    private final static Map<Class<? extends Annotation>, Void> noValueAnnotations = new IdentityHashMap<>();

    /**
     * 判断是否存在注解
     *
     * @param object
     * @param annoClass
     * @return
     */
    public static boolean hasAnnotation(AnnotatedElement object, Class<? extends Annotation> annoClass) {
        String value = getAnnotationValue(object, annoClass);
        return value == null ? false : true;
    }

    /**
     * 解析注解中的值，并以'|'分割
     *
     * @param object
     * @param annoClass
     * @return
     */
    public static String[] getAnnotationValues(AnnotatedElement object, Class<? extends Annotation> annoClass) {
        String value = getAnnotationValue(object, annoClass);
        if (value == null) {
            return null;
        }
        return ProphetPatterns.PIPE.split(value);
    }

    /**
     * 解析注解中的value的值
     * 当返回null，表示不存在该注解
     * 返回""，表示value值为空
     *
     * @param object
     * @param annoClass
     * @return
     */
    public static String getAnnotationValue(AnnotatedElement object, Class<? extends Annotation> annoClass) {
        Annotation annotation = object.getAnnotation(annoClass);
        String value = null;

        if (annotation != null) {
            Method valueMethod = valueMethods.get(annoClass);
            if (noValueAnnotations.containsKey(annoClass)) {
                value = "";
            } else {
                try {
                    if (valueMethod == null) {
                        valueMethod = annoClass.getMethod("value");
                        valueMethod.setAccessible(true);
                        valueMethods.put(annoClass, valueMethod);
                    }
                    value = (String) valueMethod.invoke(annotation);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    if (!(e instanceof NoSuchMethodException)) {
                        logger.error("Error getting annotation value", e);
                    }
                    noValueAnnotations.put(annoClass, null);
                    value = "";
                }
            }
        }

        return value;
    }
}

package com.particle.util.loader.service;

import com.alibaba.fastjson.JSON;
import com.particle.util.loader.model.Config;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ConfigInjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigInjectService.class);
    private ConfigManager configManager = ConfigManager.getInstance();
    private static final ConfigInjectService INSTANCE = new ConfigInjectService();

    public ConfigInjectService() {
    }

    public void injectConfig(String packet, ClassLoader classLoader) {
        try {
            Reflections reflections = new Reflections(new Object[]{classLoader, new FieldAnnotationsScanner()});
            Set<Field> fieldsList = reflections.getFieldsAnnotatedWith(Config.class);
            Iterator var5 = fieldsList.iterator();

            while (true) {
                while (var5.hasNext()) {
                    Field field = (Field) var5.next();
                    if (!Modifier.isStatic(field.getModifiers())) {
                        LOGGER.error("Illegal config bean of {}, should be static.", field.toString());
                    } else {
                        Config configAnnotation = (Config) field.getAnnotation(Config.class);
                        if (StringUtils.isBlank(configAnnotation.key())) {
                            LOGGER.error("Illegal config bean of {}, key not exist", field.toString());
                        } else {
                            Object configData = this.configManager.getConfig(configAnnotation.key(), field);
                            if (configData != null) {
                                if (field.isAccessible()) {
                                    field.set(field.getDeclaringClass(), configData);
                                } else {
                                    field.setAccessible(true);
                                    field.set(field.getDeclaringClass(), configData);
                                    field.setAccessible(false);
                                }
                            } else {
                                Map<String, Object> schema = new HashMap();
                                schema.put("key", "");
                                if (StringUtils.isNotBlank(configAnnotation.alias())) {
                                    schema.put("alias", configAnnotation.alias());
                                }

                                if (!field.getType().equals(Integer.class) && !field.getType().equals(Integer.TYPE)) {
                                    if (!field.getType().equals(Long.class) && !field.getType().equals(Long.TYPE)) {
                                        if (!field.getType().equals(Double.class) && !field.getType().equals(Double.TYPE)) {
                                            if (!field.getType().equals(Float.class) && !field.getType().equals(Float.TYPE)) {
                                                if (!field.getType().equals(Boolean.class) && !field.getType().equals(Boolean.TYPE)) {
                                                    if (field.getType().equals(String.class)) {
                                                        schema.put("type", "string");
                                                        schema.put("minLength", configAnnotation.minLength());
                                                        schema.put("maxLength", configAnnotation.maxLength());
                                                        schema.put("pattern", configAnnotation.pattern());
                                                    } else if (List.class.isAssignableFrom(field.getType())) {
                                                        schema.put("type", "array");
                                                        schema.put("minItems", configAnnotation.minItems());
                                                        schema.put("maxItems", configAnnotation.maxItems());
                                                    } else {
                                                        schema.put("type", "object");
                                                    }
                                                } else {
                                                    schema.put("type", "boolean");
                                                }
                                            } else {
                                                schema.put("type", "float");
                                                schema.put("minimum", configAnnotation.minimum());
                                                schema.put("maximum", configAnnotation.maximum());
                                            }
                                        } else {
                                            schema.put("type", "double");
                                            schema.put("minimum", configAnnotation.minimum());
                                            schema.put("maximum", configAnnotation.maximum());
                                        }
                                    } else {
                                        schema.put("type", "long");
                                        schema.put("minimum", configAnnotation.minimum());
                                        schema.put("maximum", configAnnotation.maximum());
                                    }
                                } else {
                                    schema.put("type", "integer");
                                    schema.put("minimum", configAnnotation.minimum());
                                    schema.put("maximum", configAnnotation.maximum());
                                }

                                List<Map<String, Object>> schemas = new LinkedList();
                                schemas.add(schema);
                                this.configManager.importConfig(configAnnotation.key(), JSON.parse(JSON.toJSONString(this.getFieldData(field))), false, schemas);
                            }
                        }
                    }
                }

                return;
            }
        } catch (Exception var11) {
            LOGGER.error("Fail to inject config.", var11);
        }
    }

    private Object getFieldData(Field field) {
        try {
            if (field.isAccessible()) {
                return field.get(field.getDeclaringClass());
            } else {
                field.setAccessible(true);
                Object data = field.get(field.getDeclaringClass());
                field.setAccessible(false);
                return data;
            }
        } catch (IllegalAccessException var3) {
            LOGGER.error("fail to get field {} data.", field);
            return null;
        }
    }

    public static ConfigInjectService getInstance() {
        return INSTANCE;
    }
}

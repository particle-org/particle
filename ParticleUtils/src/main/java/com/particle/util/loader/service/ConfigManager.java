package com.particle.util.loader.service;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import com.particle.util.loader.PathEncodeHelper;
import com.particle.util.loader.model.Schema;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
    private Map<String, Object> configDataCache = new HashMap();
    private Map<String, Schema> schemas = new HashMap();
    private Map<String, String> alias2Key = new HashMap();
    private Map<String, String> key2Alias = new HashMap();
    private static final ConfigManager INSTANCE = new ConfigManager();

    public Map<String, Object> getAllConfig() {
        return this.configDataCache;
    }

    public Object getConfig(String space, Field field) {
        if (space.equals("")) {
            return field.getType() == Map.class ? TypeUtils.cast(this.configDataCache, field.getType(), ParserConfig.getGlobalInstance()) : null;
        } else {
            Map operateNode;
            if (space.contains(".")) {
                operateNode = this.getOperateNode(space.substring(0, space.lastIndexOf(".")), false);
            } else {
                operateNode = this.configDataCache;
            }

            String key = space.substring(space.lastIndexOf(".") + 1);
            if (operateNode != null) {
                Object object = operateNode.get(key);
                if (object == null) {
                    return null;
                }

                if (field.getType() == Integer.TYPE || field.getType() == Integer.class || field.getType() == Long.TYPE || field.getType() == Long.class || field.getType() == Double.TYPE || field.getType() == Double.class || field.getType() == Float.TYPE || field.getType() == Float.class || field.getType() == Boolean.TYPE || field.getType() == Boolean.class || field.getType() == BigDecimal.class || field.getType() == String.class) {
                    return TypeUtils.cast(object, field.getType(), ParserConfig.getGlobalInstance());
                }

                if (field.getType().isAssignableFrom(object.getClass())) {
                    if (!List.class.isAssignableFrom(field.getType())) {
                        return TypeUtils.cast(object, field.getType(), ParserConfig.getGlobalInstance());
                    }

                    Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    List list = new ArrayList();
                    Iterator var8 = ((List) object).iterator();

                    while (var8.hasNext()) {
                        Object datum = var8.next();
                        list.add(TypeUtils.cast(datum, type, ParserConfig.getGlobalInstance()));
                    }

                    return list;
                }
            }

            return null;
        }
    }

    public void importConfig(String space, Object configData, boolean override, List<Map<String, Object>> schemas) {
        this.importSchemas(space, schemas);
        this.importConfig(space, configData, override);
    }

    public Object exportConfig(String space) {
        if (space.equals("")) {
            return this.exportConfig0(space, this.configDataCache);
        } else {
            Map operateNode;
            if (space.contains(".")) {
                operateNode = this.getOperateNode(space.substring(0, space.lastIndexOf(".")), false);
            } else {
                operateNode = this.configDataCache;
            }

            String key = space.substring(space.lastIndexOf(".") + 1);
            return operateNode != null ? this.exportConfig0(space, operateNode.get(key)) : null;
        }
    }

    private void importConfig(String space, Object configData, boolean override) {
        Object configDataCache = this.importConfig0(space, configData);
        if (this.logicalCheck(space, configDataCache)) {
            if (space.equals("")) {
                this.configDataCache = (Map) configDataCache;
            } else {
                Map operateNode;
                if (space.contains(".")) {
                    operateNode = this.getOperateNode(space.substring(0, space.lastIndexOf(".")), override);
                } else {
                    operateNode = this.configDataCache;
                }

                if (operateNode != null) {
                    if (this.alias2Key.containsKey(space)) {
                        operateNode.put(this.alias2Key.get(space), configDataCache);
                    } else {
                        operateNode.put(space.substring(space.lastIndexOf(".") + 1), configDataCache);
                    }
                }

            }
        }
    }

    private void importSchemas(String space, List<Map<String, Object>> schemas) {
        if (schemas != null) {
            Schema schema;
            for (Iterator var3 = schemas.iterator(); var3.hasNext(); this.schemas.put(schema.getKey(), schema)) {
                Map<String, Object> schemaMap = (Map) var3.next();
                schema = new Schema();
                String key = TypeUtils.castToString(schemaMap.getOrDefault("key", ""));
                schema.setKey(PathEncodeHelper.buildKey(space, key, "."));

                if (schemaMap.containsKey("type")) {
                    String value = TypeUtils.castToString(schemaMap.get("type"));
                    if (value != null) {
                        schema.setType(value);
                    }
                }

                if (schemaMap.containsKey("alias")) {
                    String value = TypeUtils.castToString(schemaMap.get("alias"));
                    if (value != null) {
                        schema.setAlias(value);
                    }
                }

                if (schemaMap.containsKey("minItems")) {
                    Integer value = TypeUtils.castToInt(schemaMap.get("minItems"));
                    if (value != null) {
                        schema.setMinItems(value);
                    }
                }

                if (schemaMap.containsKey("maxItems")) {
                    Integer value = TypeUtils.castToInt(schemaMap.get("maxItems"));
                    if (value != null) {
                        schema.setMaxItems(value);
                    }
                }

                if (schemaMap.containsKey("minLength")) {
                    Integer value = TypeUtils.castToInt(schemaMap.get("minLength"));
                    if (value != null) {
                        schema.setMinLength(value);
                    }
                }

                if (schemaMap.containsKey("maxLength")) {
                    Integer value = TypeUtils.castToInt(schemaMap.get("maxLength"));
                    if (value != null) {
                        schema.setMaxLength(value);
                    }
                }

                if (schemaMap.containsKey("pattern")) {
                    String value = TypeUtils.castToString(schemaMap.get("pattern"));
                    if (value != null) {
                        schema.setPattern(value);
                    }
                }

                if (schemaMap.containsKey("minimum")) {
                    Long value = TypeUtils.castToLong(schemaMap.get("minimum"));
                    if (value != null) {
                        schema.setMinimum(value);
                    }
                }

                if (schemaMap.containsKey("maximum")) {
                    Long value = TypeUtils.castToLong(schemaMap.get("maximum"));
                    if (value != null) {
                        schema.setMaximum(value);
                    }
                }

                if (StringUtils.isNotBlank(schema.getAlias())) {
                    this.alias2Key.put(schema.getAlias(), schema.getKey());
                    this.key2Alias.put(schema.getKey(), schema.getAlias());
                }
            }

        }
    }

    public List<Map<String, Object>> getSchema(String space) {
        List<Map<String, Object>> returnSchema = new ArrayList(this.schemas.size());

        HashMap schemaMap;
        for (Iterator var3 = this.schemas.values().iterator(); var3.hasNext(); returnSchema.add(schemaMap)) {
            Schema schema = (Schema) var3.next();
            schemaMap = new HashMap();
            if (schema.getKey().equals(space)) {
                schemaMap.put("key", "");
            } else if (space.equals("")) {
                schemaMap.put("key", schema.getKey());
            } else if (schema.getKey().startsWith(space + ".")) {
                schemaMap.put("key", schema.getKey().substring(space.length() + 1));
            } else {
                schemaMap.put("key", schema.getKey());
            }

            if (StringUtils.isNotBlank(schema.getAlias())) {
                schemaMap.put("alias", schema.getAlias());
            }

            if (StringUtils.isNotBlank(schema.getType())) {
                schemaMap.put("type", schema.getType());
            }

            if (schema.getMinItems() != 0) {
                schemaMap.put("minItems", schema.getMinItems());
            }

            if (schema.getMaxItems() != 2147483647) {
                schemaMap.put("maxItems", schema.getMaxItems());
            }

            if (schema.getMinLength() != -2147483648) {
                schemaMap.put("minLength", schema.getMinLength());
            }

            if (schema.getMaxLength() != 2147483647) {
                schemaMap.put("maxLength", schema.getMaxLength());
            }

            if (StringUtils.isNotBlank(schema.getPattern())) {
                schemaMap.put("pattern", schema.getPattern());
            }

            if (schema.getMinimum() != -9223372036854775808L) {
                schemaMap.put("minimum", schema.getMinimum());
            }

            if (schema.getMaximum() != 9223372036854775807L) {
                schemaMap.put("maximum", schema.getMaximum());
            }
        }

        return returnSchema;
    }

    private Object exportConfig0(String space, Object configData) {
        Iterator var4;
        if (configData instanceof Map) {
            Map<String, Object> exportData = new HashMap();
            var4 = ((Map) configData).entrySet().iterator();

            while (var4.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) var4.next();
                String key = PathEncodeHelper.buildKey(space, (String) entry.getKey(), ".");
                if (this.key2Alias.containsKey(key)) {
                    exportData.put(this.key2Alias.get(key), this.exportConfig0(key, entry.getValue()));
                } else {
                    exportData.put(entry.getKey(), this.exportConfig0(key, entry.getValue()));
                }
            }

            return exportData;
        } else if (!(configData instanceof List)) {
            return configData;
        } else {
            List<Map<String, Object>> exportData = new LinkedList();
            var4 = ((List) configData).iterator();

            while (var4.hasNext()) {
                Map<String, Object> configDatum = (Map) var4.next();
                Map<String, Object> exportDataItem = new HashMap();
                Iterator var7 = configDatum.entrySet().iterator();

                while (var7.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry) var7.next();
                    String key = PathEncodeHelper.buildKey(space, (String) entry.getKey(), ".[].");
                    if (this.key2Alias.containsKey(key)) {
                        exportDataItem.put(this.key2Alias.get(key), this.exportConfig0(key, entry.getValue()));
                    } else {
                        exportDataItem.put(entry.getKey(), this.exportConfig0(key, entry.getValue()));
                    }
                }

                exportData.add(exportDataItem);
            }

            return exportData;
        }
    }

    private Object importConfig0(String space, Object configData) {
        Iterator var4;
        if (configData instanceof Map) {
            Map<String, Object> importData = new HashMap();
            var4 = ((Map) configData).entrySet().iterator();

            while (var4.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry) var4.next();
                String configKey = (String) entry.getKey();
                Object configContent = entry.getValue();
                String realKey = (String) this.alias2Key.get(configKey);
                if (realKey != null) {
                    importData.put(PathEncodeHelper.decodeKey(realKey), this.importConfig0(realKey, configContent));
                } else {
                    importData.put(configKey, this.importConfig0(PathEncodeHelper.buildKey(space, configKey, "."), configContent));
                }
            }

            return importData;
        } else if (!(configData instanceof List)) {
            return configData;
        } else {
            List<Map<String, Object>> importData = new LinkedList();
            var4 = ((List) configData).iterator();

            while (var4.hasNext()) {
                Map<String, Object> configDatum = (Map) var4.next();
                Map<String, Object> exportDataItem = new HashMap();
                Iterator var7 = configDatum.entrySet().iterator();

                while (var7.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry) var7.next();
                    String configKey = (String) entry.getKey();
                    Object configContent = entry.getValue();
                    String realKey = (String) this.alias2Key.get(configKey);
                    if (realKey != null) {
                        exportDataItem.put(PathEncodeHelper.decodeKey(realKey), this.importConfig0(realKey, configContent));
                    } else {
                        exportDataItem.put(configKey, this.importConfig0(PathEncodeHelper.buildKey(space, configKey, ".[]."), configContent));
                    }
                }

                importData.add(exportDataItem);
            }

            return importData;
        }
    }

    private Map<String, Object> getOperateNode(String space, boolean override) {
        if (StringUtils.isBlank(space)) {
            LOGGER.error("Get operate node fail because space is blank");
            return null;
        } else {
            Map<String, Object> currentNode = this.configDataCache;
            String[] splitSpace = space.split("\\.");

            for (int i = 0; i < splitSpace.length; ++i) {
                String key = splitSpace[i];
                if (!((Map) currentNode).containsKey(key)) {
                    Map<String, Object> child = new HashMap();
                    ((Map) currentNode).put(key, child);
                    currentNode = child;
                } else {
                    Object data = ((Map) currentNode).get(splitSpace[i]);
                    if (data instanceof Map) {
                        currentNode = (Map) data;
                    } else {
                        StringBuilder missMatchNode = new StringBuilder();

                        for (int j = 0; j <= i; ++j) {
                            missMatchNode.append(key);
                            missMatchNode.append(".");
                        }

                        LOGGER.warn("Type should be map at {} where is {}", missMatchNode.substring(0, missMatchNode.length() - 1), data);
                        if (!override) {
                            return null;
                        }

                        Map<String, Object> child = new HashMap();
                        ((Map) currentNode).put(key, child);
                        currentNode = child;
                    }
                }
            }

            return (Map) currentNode;
        }
    }

    private boolean logicalCheck(String path, Object data) {
        Schema schema = (Schema) this.schemas.get(path);
        if (schema == null) {
            schema = new Schema();
            schema.setKey(path);
            if (data instanceof Integer) {
                schema.setType("integer");
            } else if (data instanceof Long) {
                schema.setType("long");
            } else if (data instanceof Double) {
                schema.setType("double");
            } else if (data instanceof BigDecimal) {
                schema.setType("bigDecimal");
            } else if (data instanceof Float) {
                schema.setType("float");
            } else if (data instanceof Boolean) {
                schema.setType("boolean");
            } else if (data instanceof String) {
                schema.setType("string");
            } else if (data instanceof List) {
                schema.setType("array");
            } else {
                if (!(data instanceof Map)) {
                    LOGGER.error("{} Data type illegal", path);
                    return false;
                }

                schema.setType("object");
            }

            this.schemas.put(path, schema);
        }

        return this.checkSchema(path, schema, data);
    }

    private boolean checkSchema(String path, Schema schema, Object data) {
        if (data == null) {
            return true;
        } else {
            String var4 = schema.getType();
            byte var5 = -1;
            switch (var4.hashCode()) {
                case -1325958191:
                    if (var4.equals("double")) {
                        var5 = 3;
                    }
                    break;
                case -1023368385:
                    if (var4.equals("object")) {
                        var5 = 8;
                    }
                    break;
                case -891985903:
                    if (var4.equals("string")) {
                        var5 = 0;
                    }
                    break;
                case -554856911:
                    if (var4.equals("bigDecimal")) {
                        var5 = 4;
                    }
                    break;
                case 3327612:
                    if (var4.equals("long")) {
                        var5 = 2;
                    }
                    break;
                case 64711720:
                    if (var4.equals("boolean")) {
                        var5 = 6;
                    }
                    break;
                case 93090393:
                    if (var4.equals("array")) {
                        var5 = 7;
                    }
                    break;
                case 97526364:
                    if (var4.equals("float")) {
                        var5 = 5;
                    }
                    break;
                case 1958052158:
                    if (var4.equals("integer")) {
                        var5 = 1;
                    }
            }

            switch (var5) {
                case 0:
                    String castStringData = TypeUtils.castToString(data);
                    if (castStringData == null) {
                        LOGGER.error("{} Data type illegal", path);
                        return false;
                    } else {
                        if (StringUtils.isNotBlank(schema.getPattern())) {
                            return castStringData.matches(schema.getPattern());
                        }

                        return true;
                    }
                case 1:
                    Integer integerVal = TypeUtils.castToInt(data);
                    if (integerVal == null) {
                        LOGGER.error("{} Data type illegal", path);
                        return false;
                    } else {
                        if ((long) integerVal >= schema.getMinimum() && (long) integerVal <= schema.getMaximum()) {
                            return true;
                        }

                        LOGGER.error("{} integer val range illegal", path);
                        return false;
                    }
                case 2:
                    Long longVal = TypeUtils.castToLong(data);
                    if (longVal == null) {
                        LOGGER.error("{} Data type illegal", path);
                        return false;
                    } else {
                        if (longVal >= schema.getMinimum() && longVal <= schema.getMaximum()) {
                            return true;
                        }

                        LOGGER.error("{} long val range illegal", path);
                        return false;
                    }
                case 3:
                    Double doubleVal = TypeUtils.castToDouble(data);
                    if (doubleVal == null) {
                        LOGGER.error("{} Data type illegal", path);
                        return false;
                    } else {
                        if (doubleVal >= (double) schema.getMinimum() && doubleVal <= (double) schema.getMaximum()) {
                            return true;
                        }

                        LOGGER.error("{} double val range illegal", path);
                        return false;
                    }
                case 4:
                    BigDecimal decimalVal = TypeUtils.castToBigDecimal(data);
                    if (decimalVal == null) {
                        LOGGER.error("{} Data type illegal", path);
                        return false;
                    } else {
                        if (decimalVal.doubleValue() >= (double) schema.getMinimum() && decimalVal.doubleValue() <= (double) schema.getMaximum()) {
                            return true;
                        }

                        LOGGER.error("{} bigDecimal val range illegal", path);
                        return false;
                    }
                case 5:
                    Float floatVal = TypeUtils.castToFloat(data);
                    if (floatVal == null) {
                        LOGGER.error("{} Data type illegal", path);
                        return false;
                    } else {
                        if (floatVal >= (float) schema.getMinimum() && floatVal <= (float) schema.getMaximum()) {
                            return true;
                        }

                        LOGGER.error("{} float val range illegal", path);
                        return false;
                    }
                case 6:
                    Boolean booleanValue = TypeUtils.castToBoolean(data);
                    if (booleanValue == null) {
                        LOGGER.error("{} Data type illegal", path);
                        return false;
                    }

                    return true;
                case 7:
                    List<Object> dataList = (List) data;
                    if (dataList.size() >= schema.getMinItems() && dataList.size() <= schema.getMaxItems()) {
                        Iterator var20 = dataList.iterator();

                        while (var20.hasNext()) {
                            Object dataItem = var20.next();
                            if (!(dataItem instanceof Map)) {
                                LOGGER.error("List data only support map");
                                return false;
                            }

                            Map<String, Object> dataMap = (Map) dataItem;
                            Iterator var17 = dataMap.entrySet().iterator();

                            while (var17.hasNext()) {
                                Map.Entry<String, Object> entry = (Map.Entry) var17.next();
                                Object dataVal = entry.getValue();
                                if (dataVal != null && !(dataVal instanceof Integer) && !(dataVal instanceof Long) && !(dataVal instanceof Float) && !(dataVal instanceof Double) && !(dataVal instanceof Boolean) && !(dataVal instanceof String) && !(dataVal instanceof BigDecimal)) {
                                    LOGGER.error("{}} child data only support basic type", path);
                                    return false;
                                }

                                if (path.equals("")) {
                                    if (!this.logicalCheck("[]." + (String) entry.getKey(), dataVal)) {
                                        return false;
                                    }
                                } else if (!this.logicalCheck(path + ".[]." + (String) entry.getKey(), dataVal)) {
                                    return false;
                                }
                            }
                        }

                        return true;
                    } else {
                        LOGGER.error("{} Data length illegal", path);
                        return false;
                    }
                case 8:
                    if (!(data instanceof Map)) {
                        LOGGER.error("{} Data type illegal", path);
                        return false;
                    } else {
                        Map<String, Object> dataMap = (Map) data;
                        Iterator var15 = dataMap.entrySet().iterator();

                        while (var15.hasNext()) {
                            Map.Entry<String, Object> entry = (Map.Entry) var15.next();
                            if (path.equals("")) {
                                if (!this.logicalCheck((String) entry.getKey(), entry.getValue())) {
                                    return false;
                                }
                            } else if (!this.logicalCheck(path + "." + (String) entry.getKey(), entry.getValue())) {
                                return false;
                            }
                        }

                        return true;
                    }
                default:
                    LOGGER.error("{} Data type illegal", path);
                    return false;
            }
        }
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    private ConfigManager() {
    }
}

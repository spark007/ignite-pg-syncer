package org.ruphile.bigdata.util;

import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;

import java.lang.reflect.Field;
import java.util.*;

public class CacheMapper {
    public static <K, V> QueryEntity createTableEntity(Class<K> key, Class<V> value) {
        QueryEntity queryEntity = new QueryEntity();
        queryEntity.setTableName(value.getSimpleName().toUpperCase());
        queryEntity.setKeyType(key.getName());
        queryEntity.setValueType(value.getName());

        // 1. 设置 Key 字段列表
        Set<String> keyFields = new HashSet<>();
        for (Field field : key.getDeclaredFields()) {
            keyFields.add(field.getName());
        }
        queryEntity.setKeyFields(keyFields);

        // 2. 设置所有可查询字段 (Key 字段 + Value 字段)
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();

        // 添加 Key 字段
        for (Field field : key.getDeclaredFields()) {
            fieldMap.put(field.getName(), field.getType().getName());
        }
        // 添加 Value 字段
        for (Field field : value.getDeclaredFields()) {
            fieldMap.put(field.getName(), field.getType().getName());
        }
        queryEntity.setFields(fieldMap);

        return queryEntity;
    }

    public static <K, V> JdbcType createJDBCType(Class<K> key, Class<V> value, String schema, String table) {
        JdbcType jdbcType = new JdbcType();
        //cacheName equals to name of value
        jdbcType.setCacheName(value.getSimpleName().toUpperCase());
        jdbcType.setKeyType(key.getName());
        jdbcType.setValueType(value.getName());
        jdbcType.setDatabaseSchema(schema);
        jdbcType.setDatabaseTable(table);

        // 核心修复 #4：配置 Value 字段映射
        JdbcTypeField[] keyFields = addJdbcTypeFields(key);
        JdbcTypeField[] valueFields = addJdbcTypeFields(value);
        // 设置字段映射
        jdbcType.setKeyFields(keyFields);
        jdbcType.setValueFields(valueFields);

        return jdbcType;
    }

    public static JdbcTypeField[] addJdbcTypeFields(Class<?> pojoClass) {
        List<JdbcTypeField> fieldsList = new ArrayList<>();

        // 遍历 POJO 类的所有字段
        for (Field field : pojoClass.getDeclaredFields()) {

            // 1. 忽略静态或瞬时字段，这些字段通常不需要持久化
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
                    java.lang.reflect.Modifier.isTransient(field.getModifiers())) {
                continue;
            }

            // 2. 读取 @ColumnMapping 注解
            ColumnMapping mapping = field.getAnnotation(ColumnMapping.class);

            if (mapping == null) {
                // 如果字段没有配置注解，通常应该抛出错误或跳过
                // 这里选择跳过并打印警告
                System.err.println("Warning: Field '" + field.getName() +
                        "' in class '" + pojoClass.getSimpleName() +
                        "' is missing @ColumnMapping and will be skipped.");
                continue;
            }

            // 3. 从注解中获取配置值
            String columnName = mapping.columnName();
            int sqlType = mapping.sqlType();

            // 4. 创建 JdbcTypeField 对象
            JdbcTypeField jdbcField = new JdbcTypeField(
                    sqlType,                // SQL 类型 (来自注解)
                    columnName,             // 数据库列名 (来自注解)
                    field.getType(),          // Java 类型全名 (POJO 类名)
                    field.getName()           // Java 字段名 (POJO 字段名)
            );
            fieldsList.add(jdbcField);
        }

        // 5. 将 List 转换为 JdbcTypeField 数组并返回
        return fieldsList.toArray(new JdbcTypeField[0]);
    }

    // --- 3. 核心测试方法：使用 Cache API 写入 ---

   /* *//**
     * 使用标准的 cache.put() 写入数据，从而触发 Write-Through。
     * @param cache 关联了 CacheJdbcPojoStore 的 IgniteCache
     *//*
    public void testPut(IgniteCache<K, V> cache, K key, V value) {
        cache.put(key, value);
        System.out.println("写入完成。请检查 PostgreSQL 的 public.risklog 表。");
    }*/
}

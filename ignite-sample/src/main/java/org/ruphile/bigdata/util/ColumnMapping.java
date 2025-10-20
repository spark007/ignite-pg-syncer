package org.ruphile.bigdata.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Types;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnMapping {
    /**
     * 数据库中的列名 (如: "s_no")
     */
    String columnName();

    /**
     * 对应的 JDBC SQL 类型 (使用 java.sql.Types 常量, 如: Types.VARCHAR)
     */
    int sqlType() default Types.VARCHAR;
}
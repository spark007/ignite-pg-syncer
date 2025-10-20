package org.ruphile.bigdata.entity;

import lombok.Data;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.ruphile.bigdata.util.ColumnMapping;

import java.io.Serializable;
import java.sql.Types;

@Data
public class User implements Serializable {
    @QuerySqlField
    @ColumnMapping(columnName = "name")
    private String name;

    @QuerySqlField
    @ColumnMapping(columnName = "age", sqlType = Types.INTEGER)
    private int age;

    @QuerySqlField
    @ColumnMapping(columnName = "email")
    private String email;

    @QuerySqlField
    @ColumnMapping(columnName = "phone")
    private String phone;

    @QuerySqlField
    @ColumnMapping(columnName = "address")
    private String address;

    @QuerySqlField
    @ColumnMapping(columnName = "gender", sqlType = Types.INTEGER)
    private int gender;

    public User() {
    }

    public User(String name, int age, String email, String phone, String address, int gender) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
    }
}

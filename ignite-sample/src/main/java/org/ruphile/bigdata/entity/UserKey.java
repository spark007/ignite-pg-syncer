package org.ruphile.bigdata.entity;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.ruphile.bigdata.util.ColumnMapping;

import java.io.Serializable;

public class UserKey implements Serializable {

    @QuerySqlField(index = true)
    @ColumnMapping(columnName = "id")
    private String id;

    public UserKey(String id) {
        this.id = id;
    }
}

package org.ruphile.bigdata.entity;

import lombok.Data;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.ruphile.bigdata.util.ColumnMapping;

import java.io.Serializable;

@Data
public class RiskLogKey implements Serializable {

    @QuerySqlField(index = true)
    @ColumnMapping(columnName = "sno")
    public String sno; // 主键字段 1
    @QuerySqlField(index = true)
    @ColumnMapping(columnName = "uno")
    public String uno; // 主键字段 2

    public RiskLogKey(String sno, String uno) {
        this.sno = sno;
        this.uno = uno;
    }
}
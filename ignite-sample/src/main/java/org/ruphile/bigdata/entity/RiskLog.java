package org.ruphile.bigdata.entity;

import lombok.Data;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.ruphile.bigdata.util.ColumnMapping;

import java.io.Serializable;
import java.sql.Types;

@Data
public class RiskLog implements Serializable {
    @QuerySqlField
    @ColumnMapping(columnName = "business_date")
    public String businessDate;
    @QuerySqlField
    @ColumnMapping(columnName = "business_time")
    public String businessTime;
    @QuerySqlField
    @ColumnMapping(columnName = "object_id", sqlType = Types.BIGINT)
    public Long objectId;
    @QuerySqlField
    @ColumnMapping(columnName = "object_name")
    public String objectName;
    @QuerySqlField
    @ColumnMapping(columnName = "risk_name")
    public String riskName;
    @QuerySqlField
    @ColumnMapping(columnName = "risk_type")
    public String riskType;
}

package org.solq.fm.common.db.config;

/***
 * 数据库配置
 * 
 * @author solq
 */
public interface DBConfig {
    // json 存储编码
    final static public String TYPE_JSON = "org.solq.fm.common.db.coder.JsonType";
    // 数据来源
    final static public String DATA_SOURCE_SQL_MANAGER = "data_source_sql_manager";

    final static public String DATA_SOURCE_1 = "dbSource1";
    final static public String DATA_SOURCE_2 = "dbSource2";

    final static public String OPERATION_CACHEDB_OPERATION = "CacheDbOperation";
    final static public String OPERATION_HIBERNATE_DBOPERATION = "HibernateDbOperation";
}

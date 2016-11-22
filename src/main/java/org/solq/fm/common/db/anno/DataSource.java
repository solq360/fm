package org.solq.fm.common.db.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.solq.fm.common.db.config.DBConfig;
import org.solq.fm.common.db.model.IDbOperation;
import org.solq.fm.common.db.service.HibernateDbOperation;

/***
 * 数据库来源
 * 
 * @author solq
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

    String value() default DBConfig.DATA_SOURCE_1;

    // 操作代理
    Class<? extends IDbOperation> proxy() default HibernateDbOperation.class;

    boolean openCache() default true;
    // CacheStrategy cache() default null;
}

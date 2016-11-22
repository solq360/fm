package org.solq.fm.common.db.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.solq.fm.common.db.config.DBConfig;

/***
 * 数据库来源
 * @author solq
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

    public String value() default DBConfig.DATA_SOURCE_1;
}

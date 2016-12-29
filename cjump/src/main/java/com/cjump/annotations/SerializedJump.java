package com.cjump.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Cuckoo
 * @date 2016-12-14
 * @description
 *      用于标记用于页面跳转的相关字段以及方法参数
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SerializedJump {
    /**
     * Activity中需要和跳转过来的参数进行绑定的字段名称
     */
    String value() default "";

    /**
     * 往Activity跳转创建Intent时所需参数名称
     */
    String[] args() default {};

}

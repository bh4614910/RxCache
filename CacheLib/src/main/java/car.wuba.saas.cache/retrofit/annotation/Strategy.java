package car.wuba.saas.cache.retrofit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import car.wuba.saas.cache.bean.ExpirationPolicies;

/**
 * Created by liubohua on 2018/7/27.
 * 超时策略注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Strategy {
    ExpirationPolicies key() default ExpirationPolicies.ReturnNull;
}

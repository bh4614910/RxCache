package com.example.liubohua.cacheapplication;

import java.util.concurrent.TimeUnit;

import car.wuba.saas.cache.bean.CacheType;
import car.wuba.saas.cache.bean.ExpirationPolicies;
import car.wuba.saas.cache.retrofit.annotation.CacheClass;
import car.wuba.saas.cache.retrofit.annotation.CacheKey;
import car.wuba.saas.cache.retrofit.annotation.CacheValue;
import car.wuba.saas.cache.retrofit.annotation.Lifecycle;
import car.wuba.saas.cache.retrofit.annotation.Method;
import car.wuba.saas.cache.retrofit.annotation.ShareName;
import car.wuba.saas.cache.retrofit.annotation.Strategy;
import car.wuba.saas.cache.retrofit.contant.MethodType;
import rx.Observable;

/**
 * Created by liubohua on 2018/7/27.
 */

public interface TestInerface {

    @Method(methodType = MethodType.GET,cacheType = CacheType.DISK)
    @Strategy(key = ExpirationPolicies.ReturnNull)
    <T> Observable<T> getData(@CacheKey String key, @CacheClass Class<T> clazz);

    @Method(methodType = MethodType.PUT,cacheType = CacheType.DISK)
    @Lifecycle(time = 1,unit = TimeUnit.SECONDS)
    <T> Observable<Boolean> putData( @CacheKey String key,@CacheValue T value, @CacheClass Class<T> clazz);

}

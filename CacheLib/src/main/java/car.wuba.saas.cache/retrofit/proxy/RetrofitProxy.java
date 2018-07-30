package car.wuba.saas.cache.retrofit.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import car.wuba.saas.cache.RxCache;
import car.wuba.saas.cache.bean.CacheType;
import car.wuba.saas.cache.bean.ExpirationPolicies;
import car.wuba.saas.cache.retrofit.annotation.*;
import car.wuba.saas.cache.retrofit.contant.MethodType;
import rx.Observable;

/**
 * Created by liubohua on 2018/7/27.
 * Retrofit动态代理类
 */

public class RetrofitProxy implements InvocationHandler {
    RxCache rxCache;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        car.wuba.saas.cache.retrofit.annotation.Method method1 = method.getAnnotation(car.wuba.saas.cache.retrofit.annotation.Method.class);
        Strategy strategy = method.getAnnotation(Strategy.class);
        Lifecycle lifecycle = method.getAnnotation(Lifecycle.class);
        ShareName shareName = method.getAnnotation(ShareName.class);
        Class CacheClazz = null;
        Object CacheValue = null;
        String CacheKey = null;

        Annotation[][] allParamsAnnotations = method.getParameterAnnotations();

        //获取key、value等注解对应的参数
        if (allParamsAnnotations != null) {
            for (int i = 0; i < allParamsAnnotations.length; i++) {
                Annotation[] paramAnnotations = allParamsAnnotations[i];
                if (paramAnnotations != null) {
                    for (Annotation annotation : paramAnnotations) {
                        if (annotation instanceof CacheClass) {
                            CacheClazz = (Class) args[i];
                        }

                        if (annotation instanceof CacheKey) {
                            CacheKey = (String) args[i];
                        }

                        if (annotation instanceof CacheValue) {
                            CacheValue = args[i];
                        }
                    }
                }
            }
        }
        //初始化各项参数
        if (method1 != null) {
            MethodType methodKey = method1.methodType();
            CacheType typeValue = method1.cacheType();
            long time = 0;
            TimeUnit unit = null;
            if (lifecycle != null) {
                time = lifecycle.time();
                unit = lifecycle.unit();
            }
            ExpirationPolicies policies = ExpirationPolicies.ReturnNull;
            if (strategy != null) {
                policies = strategy.key();
            }
            String name = "";
            if (shareName != null) {
                 name = shareName.name();
            }
            rxCache = RxCache.get();
            if (methodKey == MethodType.PUT) {
                return putMethod(typeValue, time, unit, CacheKey, CacheValue, name);
            } else if (methodKey == MethodType.GET) {
                return getMethod(typeValue, policies, CacheKey, CacheClazz, name);
            } else if (methodKey == MethodType.REMOVE) {
                return removeMethod(typeValue, CacheKey, name);
            } else if (methodKey == MethodType.CLEAR) {
                clearMethod(typeValue, name);
            }

        }
        return null;
    }


    /**
     * 调用put方法
     * @param type
     * @param time
     * @param unit
     * @param key
     * @param value
     * @param shareName
     * @param <T>
     * @return
     */
    public <T> Observable<Boolean> putMethod(CacheType type, long time, TimeUnit unit, String key, Object value, String shareName) {
        Observable<Boolean> observable = null;
        if (type != null && key != null && value != null) {
            observable = rxCache.putData2Cache(shareName, key, value, time, unit, type);
        }
        return observable;
    }


    /**
     * 调用get方法
     * @param type
     * @param policies
     * @param key
     * @param clazz
     * @param shareName
     * @param <T>
     * @return
     */
    public <T> Observable<T> getMethod(CacheType type, ExpirationPolicies policies, String key, Class<T> clazz, String shareName) {
        Observable<T> observable = null;
        if (type != null && key != null && clazz != null) {
            observable = rxCache.getDataForCache(shareName, key, clazz, type, policies);
        }
        return observable;
    }

    /**
     * 调用remove方法
     * @param type
     * @param key
     * @param shareName
     * @return
     */
    public Observable<Boolean> removeMethod(CacheType type, String key, String shareName) {
        Observable<Boolean> observable = null;
        if (type != null && key != null) {
            observable = rxCache.delCacheData(shareName, key, type);
        }
        return observable;
    }

    /**
     * 调用clear方法
     * @param type
     * @param shareName
     */
    public void clearMethod(CacheType type, String shareName) {
        if (type != null) {
            rxCache.clearSingleCache(shareName, type);
        }
    }
}

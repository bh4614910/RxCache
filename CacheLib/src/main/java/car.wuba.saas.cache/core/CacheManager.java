package car.wuba.saas.cache.core;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import car.wuba.saas.cache.CacheInstaller;
import car.wuba.saas.cache.bean.CacheResource;
import car.wuba.saas.cache.bean.CacheType;
import car.wuba.saas.cache.bean.ExpirationPolicies;
import car.wuba.saas.cache.encrypt.Encrypt;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liubohua on 2018/7/18.
 * Cache管理类
 */

public class CacheManager {
    private CacheWrapper wrapper;
    private Encrypt encrypt;

    public CacheManager(CacheWrapper wrapper, Encrypt encrypt) {
        this.wrapper = wrapper;
        this.encrypt = encrypt;
    }

    /**
     * 获取缓存内容
     *
     * @param key
     * @param type
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Observable<T> get(final String key, final CacheType type, final Class<T> clazz, final ExpirationPolicies policies) {
        Observable<T> observable = Observable.create(new Observable.OnSubscribe<CacheResource<T>>() {
            @Override
            public void call(Subscriber<? super CacheResource<T>> subscriber) {
                String cacheKey = encrypt.getEncryptKey(key);
                CacheResource<T> cacheResource = null;
                if(wrapper!=null){
                    cacheResource = wrapper.get(cacheKey,clazz);
                }
                subscriber.onNext(cacheResource);
                subscriber.onCompleted();
            }
        }).filter(new Func1<CacheResource<T>, Boolean>() {
            @Override
            public Boolean call(CacheResource<T> resource) {
                if (resource != null) {
                    if (resource.isExpired()) {
                        if (policies == ExpirationPolicies.ReturnNull) {
                            resource.setData(null);
                        }
                        remove(key).subscribe();
                    }
                }
                return true;
            }
        }).map(new Func1<CacheResource<T>, T>() {
            public T call(CacheResource<T> resource) {
                if (resource != null) {
                    return resource.getData();
                } else {
                    return null;
                }
            }
        }).subscribeOn(Schedulers.io());


        return observable;
    }

    /**
     * 添加缓存
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> Observable<Boolean> put(final String key, final T value, final long timeout, final TimeUnit unit) {
        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                String cacheKey = encrypt.getEncryptKey(key);
                CacheResource<T> cacheResource = new CacheResource<>(value, System.currentTimeMillis(), timeout, unit);
                boolean result = false;
                if (wrapper != null) {
                    result = wrapper.put(cacheKey, cacheResource);
                }
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());

        return observable;
    }

    /**
     * 移除缓存内容
     *
     * @param key
     * @return
     */
    public Observable<Boolean> remove(final String key) {
        Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean result = false;
                String cacheKey = encrypt.getEncryptKey(key);
                if (wrapper != null) {
                    wrapper.remove(cacheKey);
                }
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());

        return observable;
    }

    /**
     * 清空缓存
     */
    public void clear() {
        if (wrapper != null) {
            wrapper.clear();
        }
    }

    public static class CacheWrapperFactory implements CacheWrapper.Factory2 {
        @Override
        public CacheWrapper create(Context context, CacheType type, String shareName) {
            if (type == CacheType.DISK) {
                return new DiskCacheWrapper();
            } else if (type == CacheType.MEMORY) {
                return MemoryCacheWrapper.get();
            } else if (type == CacheType.SHARED) {
                return new ShareCacheWrapper(context, shareName);
            } else if (type == CacheType.TWO_LAYER) {
                return new TwoLayerWrapper();
            }
            return null;
        }
    }

}

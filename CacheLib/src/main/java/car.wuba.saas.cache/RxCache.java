package car.wuba.saas.cache;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import car.wuba.saas.cache.bean.CacheType;
import car.wuba.saas.cache.bean.ExpirationPolicies;
import car.wuba.saas.cache.core.CacheManager;
import car.wuba.saas.cache.core.CacheWrapper;
import car.wuba.saas.cache.encrypt.Encrypt;
import rx.Observable;

/**
 * Created by liubohua on 2018/7/16.
 * Cache对外提供的交互类
 */

public class RxCache implements Cache {

    private int timeout;
    private TimeUnit unit;
    private Context context;
    private Encrypt keyEncrypt;
    private ExpirationPolicies expirationPolicies = ExpirationPolicies.ReturnNull;


    private RxCache() {
        context = CacheInstaller.get().getContext();
        keyEncrypt = CacheInstaller.get().getKeyEncrypt();
    }

    public static RxCache get() {
        return new RxCache();
    }

    public RxCache setExpirationPolicies(ExpirationPolicies expirationPolicies) {
        this.expirationPolicies = expirationPolicies;
        return this;
    }

    /**
     * 设置超时时间
     *
     * @param timeout 超时时间
     * @param unit    单位
     * @return
     */
    public RxCache setTimeout(int timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
        return this;
    }

    /**
     * 配置本次数据缓存的加密方式
     * @param factory
     * @return
     */
    public RxCache keyEncryptFactory(Encrypt.Factory factory){
        if(factory!=null){
            Encrypt encrypt = factory.create();
            if(encrypt!=null){
                this.keyEncrypt = encrypt;
            }
        }
        return this;
    }


    /**
     * 清除缓存策略记录
     */
    public void clearTimeout() {
        expirationPolicies = ExpirationPolicies.ReturnNull;
        unit = null;
    }

    /**
     * 获取CacheManager对象
     *
     * @param name
     * @param type
     * @return
     */
    private CacheManager getCacheManager(String name, CacheType type) {
        CacheWrapper wrapper = new CacheManager
                .CacheWrapperFactory()
                .create(context, type, name);
        return new CacheManager(wrapper, keyEncrypt);
    }


    /**
     * 将数据存入缓存中
     *
     * @param key
     * @param tClass
     * @param type
     * @param expirationPolicies
     * @param <T>
     * @return
     */
    public <T> Observable<T> getDataForCache(String name, String key, Class<T> tClass, CacheType type, ExpirationPolicies expirationPolicies) {
        CacheManager manager = getCacheManager(name, type);
        try {
            Observable<T> observable = manager.get(key, type, tClass, expirationPolicies);
            return observable;
        } finally {
            clearTimeout();
        }
    }

    /**
     * 获取缓存数据
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    @Override
    public <T> Observable<T> getMemoryData(String key, Class<T> tClass) {
        return getDataForCache("", key, tClass, CacheType.MEMORY, expirationPolicies);
    }

    /**
     * 获取磁盘数据
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    @Override
    public <T> Observable<T> getDiskData(String key, Class<T> tClass) {
        return getDataForCache("", key, tClass, CacheType.DISK, expirationPolicies);
    }


    /**
     * 获取SharePreference中的数据
     * @param name
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    @Override
    public <T> Observable<T> getShareData(String name, String key, Class<T> tClass) {
        return getDataForCache(name, key, tClass, CacheType.SHARED, expirationPolicies);
    }

    /**
     * 获取二级缓存中的数据
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    @Override
    public <T> Observable<T> getDataTwoLayer(String key, Class<T> tClass) {
        return getDataForCache("",key,tClass,CacheType.TWO_LAYER,expirationPolicies);
    }

    /**
     * 从缓存中获取数据
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @param type
     * @param <T>
     * @return
     */
    public <T> Observable<Boolean> putData2Cache(String name, final String key, final T value, long timeout, TimeUnit unit, CacheType type) {
        CacheManager manager = getCacheManager(name, type);
        return manager.put(key, value, timeout, unit);
    }

    /**
     * 将数据存入缓存中
     * @param key
     * @param data
     * @param <T>
     * @return
     */
    @Override
    public <T> Observable<Boolean> putData2Memory(String key, T data) {
        return putData2Cache("", key, data, timeout, unit, CacheType.MEMORY);
    }

    /**
     * 将数据存入磁盘中
     * @param key
     * @param data
     * @param <T>
     * @return
     */
    @Override
    public <T> Observable<Boolean> putData2Disk(String key, T data) {
        return putData2Cache("", key, data, timeout, unit, CacheType.DISK);
    }

    /**
     * 将数据存入SharePreference中
     * @param name
     * @param key
     * @param data
     * @param <T>
     * @return
     */
    @Override
    public <T> Observable<Boolean> putData2Share(String name, String key, T data) {
        return putData2Cache(name, key, data, timeout, unit, CacheType.SHARED);
    }

    /**
     * 使用二级缓存存储数据
     * @param key
     * @param data
     * @param <T>
     * @return
     */
    @Override
    public <T> Observable<Boolean> putData2TwoLayer(String key, T data) {
        return putData2Cache("",key,data,timeout,unit,CacheType.TWO_LAYER);
    }

    /**
     * 从缓存中删除某个key对应的值
     *
     * @param key
     * @return
     */
    public Observable<Boolean> delCacheData(String name, String key, CacheType type) {
        CacheManager manager = getCacheManager(name, type);
        return manager.remove(key);
    }

    /**
     * 删除磁盘中对应的数据
     * @param key
     * @return
     */
    @Override
    public Observable<Boolean> delDiskData(String key) {
        return delCacheData("",key, CacheType.DISK);
    }

    /**
     * 删除缓存中对应的数据
     * @param key
     * @return
     */
    @Override
    public Observable<Boolean> delMemoryData(String key) {
        return delCacheData("",key, CacheType.MEMORY);
    }

    /**
     * 删除SharePreference中对应的数据
     * @param name
     * @param key
     * @return
     */
    @Override
    public Observable<Boolean> delShareData(String name,String key) {
        return delCacheData(name,key, CacheType.SHARED);
    }

    /**
     * 清除二级缓存的数据
     * @param key
     * @return
     */
    @Override
    public Observable<Boolean> delTwoLayerData(String key) {
        return delCacheData("",key,CacheType.TWO_LAYER);
    }

    /**
     * 清除某一类缓存
     *
     * @param type
     */
    public void clearSingleCache(String name,CacheType type) {
        CacheManager manager = getCacheManager(name,type);
        manager.clear();
    }

    /**
     * 清空磁盘
     */
    @Override
    public void clearDisk() {
        clearSingleCache("",CacheType.DISK);
    }

    /**
     * 清空缓存
     */
    @Override
    public void clearMemory() {
        clearSingleCache("",CacheType.MEMORY);
    }

    /**
     * 清空SharePreference对应文件中的缓存数据
     * @param name
     */
    @Override
    public void clearShare(String name) {
        clearSingleCache("",CacheType.SHARED);
    }

    /**
     * 清除磁盘及内存缓存
     */
    @Override
    public void clearAll() {
        clearDisk();
        clearMemory();
    }
}

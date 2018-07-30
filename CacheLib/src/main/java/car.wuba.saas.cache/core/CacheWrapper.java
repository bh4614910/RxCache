package car.wuba.saas.cache.core;

import android.content.Context;

import car.wuba.saas.cache.bean.CacheResource;
import car.wuba.saas.cache.bean.CacheType;

/**
 * Created by liubohua on 2018/7/16.
 * 缓存控制类接口
 */

public interface CacheWrapper {

    /**
     * 读取缓存类
     *
     * @param <T> 缓存值类型，需要实现Parcelable接口
     * @param key 缓存的key值
     * @return 返回CacheResult<T>类型
     */
    <T> CacheResource<T> get(String key, Class<T> clazz);

    /**
     * 存储缓存类
     *
     * @param key   缓存的key值
     * @param value 缓存值
     * @param <T>   缓存值类型，需要实现Parcelable接口
     * @return 返回true或者false表示缓存是否成功
     */
    <T> boolean put(String key, CacheResource<T> value);

    /**
     * 清空缓存
     */
    void clear();

    /**
     * 删除某个值
     *
     * @param key 需要删除的缓存值对应key
     * @return 返回true或者false表示删除是否成功
     */
    boolean remove(String key);


    /**
     * 构造用工厂接口
     */
    interface Factory {
    }

    interface Factory2 {
        CacheWrapper create(Context context, CacheType type, String shareName);
    }
}

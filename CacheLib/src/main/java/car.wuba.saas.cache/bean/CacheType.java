package car.wuba.saas.cache.bean;

/**
 * Created by liubohua on 2018/7/16.
 * Cache类型
 * DISK 缓存到本地
 * MEMORY 缓存到内存
 * SHARED 缓存到SharedPreference
 */

public enum CacheType {
    DISK,MEMORY,SHARED,TWO_LAYER
}

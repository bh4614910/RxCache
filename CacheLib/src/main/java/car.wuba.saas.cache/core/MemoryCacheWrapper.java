package car.wuba.saas.cache.core;

import android.graphics.Bitmap;
import android.util.LruCache;

import car.wuba.saas.cache.CacheInstaller;
import car.wuba.saas.cache.bean.CacheResource;
import car.wuba.saas.cache.util.LogUtil;
import car.wuba.saas.cache.util.SizeUtil;

/**
 * Created by liubohua on 2018/7/17.
 * 内存缓存控制类
 */

public class MemoryCacheWrapper implements CacheWrapper {
    private LruCache<String, Object> memoryCache;
    private static final int DEFAULT_MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);

    public static MemoryCacheWrapper get(){
        return MemoryCacheHolder.mInstance;
    }

    private MemoryCacheWrapper() {
        memoryCache = new LruCache<String, Object>(getCacheSize()) {
            @Override
            protected int sizeOf(String key, Object value) {
                if (Bitmap.class.isAssignableFrom(value.getClass())) {
                    return (int)SizeUtil.getBitmapSize((Bitmap) value);
                } else {
                    return (int) SizeUtil.getValueSize(value);
                }
            }
        };
    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    private int getCacheSize() {
        int cacheSize = CacheInstaller.get().getMemorySize();
        if (cacheSize <= 0) {
            cacheSize = DEFAULT_MEMORY_CACHE_SIZE;
        }
        return cacheSize;
    }


    @Override
    public <T> CacheResource<T> get(String key, Class<T> clazz) {
        CacheResource<T> value = (CacheResource<T>) memoryCache.get(key);
        if (value != null) {
            return value;
        }
        return null;
    }

    @Override
    public <T> boolean put(String key, CacheResource<T> value) {
        if (value != null && memoryCache.get(key) == null) {
            memoryCache.put(key, value);
            return true;
        } else {
            LogUtil.log("value值为空或key值以及存在");
        }
        return false;
    }

    @Override
    public void clear() {
        memoryCache.evictAll();
    }

    @Override
    public boolean remove(String key) {
        Object object = memoryCache.remove(key);
        if (object == null) {
            return false;
        } else {
            return true;
        }
    }

    private static class MemoryCacheHolder {

        public static MemoryCacheWrapper mInstance = new MemoryCacheWrapper();

        private MemoryCacheHolder() {
        }
    }
}

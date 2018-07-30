package car.wuba.saas.cache.core;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;

import car.wuba.saas.cache.CacheInstaller;
import car.wuba.saas.cache.bean.CacheResource;
import car.wuba.saas.cache.util.LogUtil;

/**
 * Created by liubohua on 2018/7/18.
 */

public class DiskCacheWrapper implements CacheWrapper {
    private DiskCacheProvider diskCacheProvider;

    public DiskCacheWrapper(File directory, int appVersion, long maxSize) {
        diskCacheProvider = new DiskCacheProvider(directory, appVersion, maxSize);
    }

    public DiskCacheWrapper() {
        this(new File(CacheInstaller.get().getDiskPath()), CacheInstaller.get().getDiskVersion(), CacheInstaller.get().getDiskSize());
    }


    @Override
    public <T> CacheResource<T> get(String key, Class<T> clazz) {
        CacheResource<T> value = null;
        if (clazz == byte[].class) {
            value = (CacheResource<T>) diskCacheProvider.getBytes(key);
        } else if (clazz == Bitmap.class) {
            value = (CacheResource<T>) diskCacheProvider.getBitmap(key);
        } else if (Serializable.class.isAssignableFrom(clazz)) {
            value = (CacheResource<T>) diskCacheProvider.getObject(key);
        }
        if (value != null) {
            return value;
        }
        return null;
    }

    @Override
    public <T> boolean put(String key, CacheResource<T> value) {
        if (value == null || value.getData() == null) {
            LogUtil.log("value值不能为null");
            return false;
        }
        Class clazz = value.getData().getClass();
        boolean result = false;
        if (clazz == byte[].class) {
            result = diskCacheProvider.putBytes(key, (CacheResource<byte[]>) value);
        } else if (clazz == Bitmap.class) {
            result = diskCacheProvider.putBitmap(key, (CacheResource<Bitmap>) value);
        } else if (Serializable.class.isAssignableFrom(clazz)) {
            result = diskCacheProvider.putObject(key, (CacheResource<Object>) value);
        }
        return result;
    }

    @Override
    public void clear() {
        diskCacheProvider.clear();
    }

    @Override
    public boolean remove(String key) {
        return diskCacheProvider.remove(key);
    }
}

package car.wuba.saas.cache.core;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import car.wuba.saas.cache.bean.CacheResource;
import car.wuba.saas.cache.converter.BitmapConverter;
import car.wuba.saas.cache.converter.ByteArrayConverter;
import car.wuba.saas.cache.converter.Converter;
import car.wuba.saas.cache.converter.ObjectConverter;
import car.wuba.saas.cache.util.LogUtil;
import rx.internal.operators.CachedObservable;

/**
 * Created by liubohua on 2018/7/24.
 * 提供本地缓存基础操作。
 */

public class DiskCacheProvider {
    private DiskLruCache diskLruCache;
    private Converter objectConverter;
    private Converter bitmapConverter;
    private Converter byteArrayConverter;

    public DiskCacheProvider(File directory, int appVersion, long maxSize) {
        objectConverter = new ObjectConverter();
        bitmapConverter = new BitmapConverter();
        byteArrayConverter = new ByteArrayConverter();
        try {
            diskLruCache = DiskLruCache.open(directory, appVersion, 1, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地缓存获取图片
     *
     * @param key
     * @return
     */
    public CacheResource<Bitmap> getBitmap(String key) {
        DiskLruCache.Snapshot snapShot = null;
        try {
            snapShot = diskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                CacheResource<Bitmap> value = null;
                value = bitmapConverter.read(is);
                if (value != null) {
                    return value;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            snapShot.close();
        }
        return null;
    }

    /**
     * 本地缓存获取实体类，需已经实现序列化接口
     *
     * @param key
     * @return
     */
    public CacheResource<Object> getObject(String key) {
        DiskLruCache.Snapshot snapShot = null;
        try {
            snapShot = diskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                CacheResource<Object> value = null;
                value = objectConverter.read(is);
                if (value != null) {
                    return value;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(snapShot!=null){
                snapShot.close();
            }
        }
        return null;

    }

    /**
     * 本地缓存获取字节数组
     *
     * @param key
     * @return
     */
    public CacheResource<byte[]> getBytes(String key) {
        DiskLruCache.Snapshot snapShot = null;
        try {
            snapShot = diskLruCache.get(key);

            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                CacheResource<byte[]> value = null;
                value = byteArrayConverter.read(is);
                if (value != null) {
                    return value;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapShot != null) {
                snapShot.close();
            }
        }
        return null;
    }

    /**
     * 本地缓存录入实体类，需实现序列化接口
     *
     * @param key
     * @param value
     * @return
     */
    public boolean putObject(String key, CacheResource<Object> value) {
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(0);
            boolean result = false;
            result = objectConverter.write(value, outputStream);
            if (result) {
                editor.commit();
            } else {
                editor.abort();
            }
            return result;
        } catch (IOException e) {
            LogUtil.error("存储报错", e);
        }
        return false;
    }

    /**
     * 本地缓存录入bitmap
     *
     * @param key
     * @param value
     * @return
     */
    public boolean putBitmap(String key, CacheResource<Bitmap> value) {
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(0);
            boolean result = false;
            result = bitmapConverter.write(value, outputStream);
            if (result) {
                editor.commit();
            } else {
                editor.abort();
            }
            return result;
        } catch (IOException e) {
            LogUtil.error("存储报错", e);
        }
        return false;
    }

    /**
     * 本地缓存录入字节数组
     *
     * @param key
     * @param value
     * @return
     */
    public boolean putBytes(String key, CacheResource<byte[]> value) {
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(0);
            boolean result = false;
            result = byteArrayConverter.write(value, outputStream);
            if (result) {
                editor.commit();
            } else {
                editor.abort();
            }
            return result;
        } catch (IOException e) {
            LogUtil.error("存储报错", e);
        }
        return false;
    }

    /**
     * 清除key值所对应的本地缓存
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        try {
            return diskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 清空所有本地缓存
     */
    public void clear() {
        try {
            diskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

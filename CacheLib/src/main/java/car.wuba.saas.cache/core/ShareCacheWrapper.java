package car.wuba.saas.cache.core;

import android.content.Context;

import java.util.Set;

import car.wuba.saas.cache.bean.CacheResource;

/**
 * 作者:  wangweiqiang
 * 时间:  2018/7/18
 * 说明:  SharePreference 操作API
 */
public class ShareCacheWrapper implements CacheWrapper {

    private PreferenceProvider provider;

    public ShareCacheWrapper(Context context, String name) {
        this.provider = new PreferenceProvider(context, name);
    }

    /**
     * 存储数据
     *
     * @param key
     * @param data
     * @param <T>
     */
    @Override
    public <T> boolean put(String key, CacheResource<T> data) {
        if (data.getData() instanceof String) {
            provider.putString(key, (String) data.getData());
        } else if (data.getData() instanceof Integer) {
            provider.putInt(key, (Integer) data.getData());
        } else if (data.getData() instanceof Boolean) {
            provider.putBoolean(key, (Boolean) data.getData());
        } else if (data.getData() instanceof Long) {
            provider.putLong(key, (Long) data.getData());
        } else if (data.getData() instanceof Set) {
            provider.putStringSet(key, (Set<String>) data);
        } else if (data.getData() instanceof Float) {
            provider.putFloat(key, (Float) data.getData());
        } else {
            throw new IllegalStateException("don't support this class of data=[" + data.getClass()
                    + "]");
        }
        return true;

    }


    /**
     * 获取数据
     *
     * @param <T>
     * @param key
     * @param tClass
     * @return
     */
    @Override
    public <T> CacheResource<T> get(String key, Class<T> tClass) {
        CacheResource<T> cacheResource = new CacheResource<>();
        if (tClass.isAssignableFrom(String.class)) {
            cacheResource.setData((T) provider.getString(key));
        } else if (tClass.isAssignableFrom(Integer.class)) {
            cacheResource.setData((T) provider.getInt(key));
        } else if (tClass.isAssignableFrom(Float.class)) {
            cacheResource.setData((T) provider.getFloat(key));
        } else if (tClass.isAssignableFrom(Long.class)) {
            cacheResource.setData((T) provider.getLong(key));
        } else if (tClass.isAssignableFrom(Boolean.class)) {
            cacheResource.setData((T) provider.getBoolean(key));
        } else if (tClass.isAssignableFrom(Set.class)) {
            cacheResource.setData((T) provider.getStringSet(key));
        } else {
            throw new IllegalStateException("don't final this class of data=[" + tClass
                    + "]");
        }
        return cacheResource;
    }

    @Override
    public boolean remove(String key) {
        provider.remove(key);
        return true;
    }


    @Override
    public void clear() {
        provider.removeAll();
    }

}

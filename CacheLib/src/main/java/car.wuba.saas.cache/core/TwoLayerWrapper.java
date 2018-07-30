package car.wuba.saas.cache.core;


import car.wuba.saas.cache.bean.CacheResource;

/**
 * 作者:  wangweiqiang
 * 时间:  2018/7/26
 * 说明:  TODO
 */
public class TwoLayerWrapper implements CacheWrapper {

    private CacheWrapper mMemoryWrapper, mDiskWrapper;

    public TwoLayerWrapper() {
        mDiskWrapper = new DiskCacheWrapper();
        mMemoryWrapper = MemoryCacheWrapper.get();
    }

    @Override
    public <T> CacheResource<T> get(String key, Class<T> clazz) {
        CacheResource<T> result = mMemoryWrapper.get(key, clazz);
        if (result != null) {
            return result;
        }
        result = mDiskWrapper.get(key, clazz);
        return result;
    }

    @Override
    public <T> boolean put(String key, CacheResource<T> value) {
        try {
            boolean mMBol = mMemoryWrapper.put(key, value);
            boolean mDBol = mDiskWrapper.put(key, value);
            return mMBol || mDBol;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean remove(String key) {
        try {
            mMemoryWrapper.remove(key);
            mDiskWrapper.remove(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

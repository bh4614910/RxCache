package car.wuba.saas.cache;

import rx.Observable;

/**
 * 作者:  wangweiqiang
 * 时间:  2018/7/26
 * 说明:  缓存API接口类
 */
interface Cache {

    <T> Observable<T> getMemoryData(String key, Class<T> tClass);

    <T> Observable<T> getDiskData(String key, Class<T> tClass);

    <T> Observable<T> getShareData(String name,String key, Class<T> tClass);

    <T> Observable<T> getDataTwoLayer(String key, Class<T> tClass);

    <T> Observable<Boolean> putData2Memory(String key, T data);

    <T> Observable<Boolean> putData2Disk(String key, T data);

    <T> Observable<Boolean> putData2Share(String name,String key, T data);

    <T> Observable<Boolean> putData2TwoLayer(String key,T data);

    Observable<Boolean> delDiskData(String key);

    Observable<Boolean> delMemoryData(String key);

    Observable<Boolean> delShareData(String name,String key);

    Observable<Boolean> delTwoLayerData(String key);

    void clearDisk();

    void clearMemory();

    void clearShare(String name);

    void clearAll();

}

package car.wuba.saas.cache;

import java.lang.reflect.Proxy;
import car.wuba.saas.cache.retrofit.proxy.RetrofitProxy;

/**
 * Created by liubohua on 2018/7/27.
 */

public class RetrofitCache {

    public static <T> T create(Class<T> clazz) {
        RetrofitProxy proxy = new RetrofitProxy();
        try {
            return (T) Proxy.newProxyInstance(RetrofitCache.class.getClassLoader(), new Class[]{clazz}, proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}

package car.wuba.saas.cache.util;

import android.util.Log;

/**
 * Created by liubohua on 2018/7/18.
 * Log帮助类
 */

public class LogUtil {
    private final static String TAG = "Cache";

    public static void log(String message){
        Log.d(TAG,message);
    }

    public static void error(String message,Throwable throwable){
        Log.e(TAG,message,throwable);
    }
}

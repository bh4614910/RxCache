package car.wuba.saas.cache.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 作者:  wangweiqiang
 * 时间:  2018/7/25
 * 说明:  计算工具类
 */
public class CalculateUtils {


    /**
     * 获取系统可用的内存大小
     *
     * @return
     */
    public static long getSystemAvaialbeMemorySize(Context context) {
        //获得ActivityManager服务的对象
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo);
        long memSize = memoryInfo.availMem;

        //字符类型转换
        return memSize;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static long getSDAvailableSize(File dir) {
        StatFs stat = new StatFs(dir.getAbsolutePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static long getSDAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 获取内部存储或SD卡存储的目录
     *
     * @param context
     * @return
     */
    public static String getDiskPath(Context context) {
        String path;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            path = context.getExternalCacheDir().getPath() + File.separator;
        } else {
            path = context.getCacheDir().getPath() + File.separator;
        }
        return path;
    }

}

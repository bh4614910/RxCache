package car.wuba.saas.cache;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

import car.wuba.saas.cache.encrypt.Encrypt;
import car.wuba.saas.cache.encrypt.MD5Encrypt;
import car.wuba.saas.cache.util.CalculateUtils;

/**
 * 作者:  wangweiqiang
 * 时间:  2018/7/25
 * 说明:  通用配置信息放在程序启动的时候进行装填，
 * 因为这部分信息的可变性较小，不需要每次都进行装填。
 * 为了程序的健壮性，在调用API上提供特殊情况配置的api
 * 此配置只允许设置一次
 */
public class CacheInstaller {
    private static final long MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final int DEFAULT_VERSION = 1;
    private static final String DEFAULT_PATH = "cache";
    private String diskPath;
    private int memorySize;
    private long diskSize =0l;
    private int diskVersion = -1;
    private boolean isInstall = false;
    private Context  context;
    private Encrypt encrypt;

    private CacheInstaller() {
    }

    private static class SingleTon {
        private static CacheInstaller INSTANCE = new CacheInstaller();
    }

    public static CacheInstaller get() {
        return SingleTon.INSTANCE;
    }

    /**
     * 配置磁盘缓存配置
     *
     * @param diskPath
     * @param diskSize
     * @param diskVersion
     * @return
     */
    public CacheInstaller configDiskCache(String diskPath, long diskSize, int diskVersion) {
        if (isInstall) {
            return this;
        }
        this.diskPath = diskPath;
        this.diskSize = diskSize;
        this.diskVersion = diskVersion;
        return this;
    }

    /**
     * 配置内存缓存配置
     *
     * @param memorySize
     * @return
     */
    public CacheInstaller configMemoryCache(int memorySize) {
        if (isInstall) {
            return this;
        }
        this.memorySize = memorySize;
        return this;
    }

    /**
     * 配置全局加密方式
     *
     * @param factory
     * @return
     */
    public CacheInstaller encryptFactory(Encrypt.Factory factory) {
        if (isInstall) {
            return this;
        }
        if(factory!=null){
            encrypt = factory.create();
        }
        return this;
    }

    /**
     * 完成装填工作
     */
    public void install(Context context) {
        this.isInstall = true;
        this.diskPath = getDirectory(context);
        this.diskVersion = getVersion();
        this.diskSize = getCacheSize(context);
        this.encrypt = createEncrypt();
        this.context = context.getApplicationContext();
    }

    /**
     * 重置装填状态
     * 慎用
     */
    public void resume() {
        this.isInstall = false;
    }

    public String getDiskPath() {
        return diskPath;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public long getDiskSize() {
        return diskSize;
    }

    public int getDiskVersion() {
        return diskVersion;
    }

    public Context getContext() {
        return context;
    }

    public Encrypt getKeyEncrypt() {
        return encrypt;
    }

    private Encrypt createEncrypt(){
        if(encrypt==null){
            encrypt = new MD5Encrypt.MD5EncryptFactory().create();
        }
        return encrypt;
    }

    /**
     * 获取存储路径
     *
     * @param context
     * @return
     */
    private String getDirectory(Context context) {
        if (TextUtils.isEmpty(diskPath)) {
            diskPath = DEFAULT_PATH;
        }
        return CalculateUtils.getDiskPath(context) + File.separator + diskPath;
    }

    /**
     * 获取缓存空间大小
     *
     * @param context
     * @return
     */
    private long getCacheSize(Context context) {
        if(diskSize==0l){
            diskSize = MAX_DISK_CACHE_SIZE;
        }
        return Math.min(CalculateUtils.getSDAvailableSize(context), diskSize);
    }

    /**
     * 获取存储版本号
     * @return
     */
    private int getVersion(){
        if(diskVersion==-1){
            diskVersion = DEFAULT_VERSION;
        }
        return diskVersion;
    }
}

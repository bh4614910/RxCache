package car.wuba.saas.cache.encrypt;

/**
 * Created by liubohua on 2018/7/18.
 * 加密策略接口
 */

public interface Encrypt {
    String getEncryptKey(String key);

    /**
     * 使用工厂类生成加密方式，提高可扩展性
     */
    interface Factory {
        Encrypt create();
    }
}

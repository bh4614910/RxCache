package car.wuba.saas.cache.bean;

/**
 * Created by liubohua on 2018/7/24.
 * ReturnNull 过期清除数据，并返回null
 * ReturnExpiredData 过期清除数据，并将过期返回数据
 */

public enum ExpirationPolicies {
    ReturnNull, ReturnExpiredData
}

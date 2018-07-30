package car.wuba.saas.cache.bean;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by liubohua on 2018/7/24.
 */

public class CacheResource<T> implements Serializable {

    private int NEVER = 1;
    private int DAYS = NEVER << 1;
    private int HOURS = DAYS << 1;
    private int MINUTES = HOURS << 1;
    private int NANOSECONDS = MINUTES << 1;
    private int MICROSECONDS = NANOSECONDS << 1;
    private int MILLISECONDS = MICROSECONDS << 1;
    private int SECONDS = MILLISECONDS << 1;


    private long timestamp;
    private long timeout;
    private int unit;
    private T data;

    public CacheResource() {

    }

    public CacheResource(T data, long timestamp, long timeout, TimeUnit unit) {
        this.data = data;
        this.timestamp = timestamp;
        this.timeout = timeout;
        setUnit(unit);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getUnit() {
        return this.unit;
    }

    public void setUnit(TimeUnit unit) {
        if (unit == TimeUnit.DAYS) {
            this.unit = DAYS;
        } else if (unit == TimeUnit.HOURS) {
            this.unit = HOURS;
        } else if (unit == TimeUnit.MINUTES) {
            this.unit = MINUTES;
        } else if (unit == TimeUnit.NANOSECONDS) {
            this.unit = NANOSECONDS;
        } else if (unit == TimeUnit.MICROSECONDS) {
            this.unit = MICROSECONDS;
        } else if (unit == TimeUnit.MILLISECONDS) {
            this.unit = MILLISECONDS;
        } else if (unit == TimeUnit.SECONDS) {
            this.unit = SECONDS;
        } else {
            this.unit = NEVER;
        }
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public TimeUnit getTimeUnit() {
        if (unit == DAYS) {
            return TimeUnit.DAYS;
        } else if (unit == HOURS) {
            return TimeUnit.HOURS;
        } else if (unit == MINUTES) {
            return TimeUnit.MINUTES;
        } else if (unit == NANOSECONDS) {
            return TimeUnit.NANOSECONDS;
        } else if (unit == MICROSECONDS) {
            return TimeUnit.MICROSECONDS;
        } else if (unit == MILLISECONDS) {
            return TimeUnit.MILLISECONDS;
        } else if (unit == SECONDS) {
            return TimeUnit.SECONDS;
        }
        return null;
    }

    /**
     * 判断数据是否超时
     *
     * @return
     */
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        if (getTimeUnit() == null || (this.timestamp + getTimeUnit().toMillis(this.timeout) > currentTime)) {
            return false;
        } else {
            return true;
        }
    }

}

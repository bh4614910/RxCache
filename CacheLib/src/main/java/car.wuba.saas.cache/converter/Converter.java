package car.wuba.saas.cache.converter;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import car.wuba.saas.cache.bean.CacheResource;
import car.wuba.saas.cache.util.ExtraIOUtil;

/**
 * Created by liubohua on 2018/7/20.
 * 转换器通用超类
 */


public abstract class Converter<T> {

    public abstract boolean write(CacheResource<T> value, OutputStream outputStream);

    public abstract CacheResource<T> read(InputStream inputStream);

    /**
     * 读取文件头信息
     * @param is
     * @param value
     */
    protected void readHeader(InputStream is,CacheResource<T> value){
        try {
            value.setTimeout(ExtraIOUtil.readLong(is));
            value.setUnit(ExtraIOUtil.readInt(is));
            value.setTimestamp(ExtraIOUtil.readLong(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 录入文件头信息
     * @param os
     * @param value
     */
    protected void writeHeader(OutputStream os,CacheResource<T> value){
        try {
            ExtraIOUtil.writeLong(os,value.getTimeout());
            ExtraIOUtil.writeInt(os,value.getUnit());
            ExtraIOUtil.writeLong(os,value.getTimestamp());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

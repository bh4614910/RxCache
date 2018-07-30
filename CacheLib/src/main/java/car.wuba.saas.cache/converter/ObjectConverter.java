package car.wuba.saas.cache.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import car.wuba.saas.cache.bean.CacheResource;
import car.wuba.saas.cache.util.LogUtil;

/**
 * Created by liubohua on 2018/7/18.
 * 类与流的转换器,需要实现序列化的对象
 */

public class ObjectConverter extends Converter<Object> {

    /**
     * 将信息写入流
     *
     * @param value
     * @param outputStream
     * @return
     */
    public boolean write(CacheResource<Object> value, OutputStream outputStream) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(outputStream);
            writeHeader(outputStream,value);
            oos.writeObject(value.getData());
            oos.flush();
            return true;
        } catch (IOException e) {
            LogUtil.error("ObjectConverter数据解析出错", e);
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }



    /**
     * 读取流中的数据
     *
     * @param inputStream
     * @return
     */
    public CacheResource<Object> read(InputStream inputStream) {
        ObjectInputStream ois = null;
        CacheResource<Object> cacheObject = new CacheResource<>();
        try {
            ois = new ObjectInputStream(inputStream);
            readHeader(inputStream,cacheObject);
            cacheObject.setData(ois.readObject());
            return cacheObject;
        } catch (IOException e) {
            LogUtil.error("ObjectConverter数据解析出错", e);
        } catch (ClassNotFoundException e) {
            LogUtil.error("ObjectConverter数据解析出错", e);
        } finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

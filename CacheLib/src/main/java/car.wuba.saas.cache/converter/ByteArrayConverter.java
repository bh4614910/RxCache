package car.wuba.saas.cache.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import car.wuba.saas.cache.bean.CacheResource;

/**
 * Created by liubohua on 2018/7/20.
 * 字节数组转换器
 */

public class ByteArrayConverter extends Converter<byte[]> {

    @Override
    public boolean write(CacheResource<byte[]> value, OutputStream outputStream) {
        try {
            writeHeader(outputStream,value);
            outputStream.write(value.getData());
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public CacheResource<byte[]> read(InputStream inputStream) {
        ByteArrayOutputStream outputStream = null;
        CacheResource<byte[]> cacheBytes = new CacheResource<>();
        try {
            byte[] buffer = new byte[1024];
            outputStream = new ByteArrayOutputStream();
            int len = 0;
            readHeader(inputStream,cacheBytes);
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            cacheBytes.setData(outputStream.toByteArray());
            return cacheBytes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

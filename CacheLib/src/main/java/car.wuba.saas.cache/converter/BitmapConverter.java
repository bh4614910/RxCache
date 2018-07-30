package car.wuba.saas.cache.converter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.io.OutputStream;

import car.wuba.saas.cache.bean.CacheResource;

/**
 * Created by liubohua on 2018/7/20.
 * 图片转换器
 */

public class BitmapConverter extends Converter<Bitmap> {
    @Override
    public boolean write(CacheResource<Bitmap> value, OutputStream outputStream) {
        writeHeader(outputStream, value);
        value.getData().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return true;
    }

    @Override
    public CacheResource<Bitmap> read(InputStream inputStream) {
        CacheResource<Bitmap> cacheBitmap = new CacheResource<>();
        readHeader(inputStream, cacheBitmap);
        cacheBitmap.setData(BitmapFactory.decodeStream(inputStream));
        return cacheBitmap;
    }
}

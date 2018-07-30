package car.wuba.saas.cache.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by liubohua on 2018/7/17.
 * 测量实体类的大小
 */

public class SizeUtil {
    /**
     * 测量普通类型的实际大小
     * @param object
     * @return
     */
    public static long getValueSize(Object object) {
        if (object == null) {
            return 1;
        }
        long size = 1;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();  //缓冲流
            size = baos.size();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 测量Bitmap的大小
     * @param bitmap
     * @return
     */
    public static long getBitmapSize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        long size = -1;
        ByteArrayOutputStream baos = null;
        baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        size = baos.size();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return size;
    }

}

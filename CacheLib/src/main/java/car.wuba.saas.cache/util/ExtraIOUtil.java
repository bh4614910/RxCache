package car.wuba.saas.cache.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by liubohua on 2018/7/24.
 * 用于存储文件头的魔法数字工具类
 */

public class ExtraIOUtil {

    public static void writeInt(OutputStream os, int n) throws IOException {
        os.write(n >> 0 & 255);
        os.write(n >> 8 & 255);
        os.write(n >> 16 & 255);
        os.write(n >> 24 & 255);
    }

    public static int readInt(InputStream is) throws IOException {
        int n = 0;
        n = n | read(is) << 0;
        n |= read(is) << 8;
        n |= read(is) << 16;
        n |= read(is) << 24;
        return n;
    }

    public static void writeLong(OutputStream os, long n) throws IOException {
        os.write((byte)((int)(n >>> 0)));
        os.write((byte)((int)(n >>> 8)));
        os.write((byte)((int)(n >>> 16)));
        os.write((byte)((int)(n >>> 24)));
        os.write((byte)((int)(n >>> 32)));
        os.write((byte)((int)(n >>> 40)));
        os.write((byte)((int)(n >>> 48)));
        os.write((byte)((int)(n >>> 56)));
    }

    public static long readLong(InputStream is) throws IOException {
        long n = 0L;
        n |= ((long)read(is) & 255L) << 0;
        n |= ((long)read(is) & 255L) << 8;
        n |= ((long)read(is) & 255L) << 16;
        n |= ((long)read(is) & 255L) << 24;
        n |= ((long)read(is) & 255L) << 32;
        n |= ((long)read(is) & 255L) << 40;
        n |= ((long)read(is) & 255L) << 48;
        n |= ((long)read(is) & 255L) << 56;
        return n;
    }

    public static void writeString(OutputStream os, String s) throws IOException {
        byte[] b = s.getBytes("UTF-8");
        writeLong(os, (long)b.length);
        os.write(b, 0, b.length);
    }

    public static String readString(InputStream is) throws IOException {
        int n = (int)readLong(is);
        byte[] b = streamToBytes(is, n);
        return new String(b, "UTF-8");
    }

    private static int read(InputStream is) throws IOException {
        int b = is.read();
        if(b == -1) {
            throw new EOFException();
        } else {
            return b;
        }
    }

    private static byte[] streamToBytes(InputStream in, int length) throws IOException {
        byte[] bytes = new byte[length];

        int count;
        int pos;
        for(pos = 0; pos < length && (count = in.read(bytes, pos, length - pos)) != -1; pos += count) {
            ;
        }

        if(pos != length) {
            throw new IOException("Expected " + length + " bytes, read " + pos + " bytes");
        } else {
            return bytes;
        }
    }
}

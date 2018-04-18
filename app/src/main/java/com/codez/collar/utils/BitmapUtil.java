package com.codez.collar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * 图片工具类
 * Created by 酸奶 on 2016/4/20.
 */
public class BitmapUtil {
    private static final String TAG = "BitmapUtil";
    public static Bitmap getBitmap(Context context, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    public static Bitmap reduce(Bitmap bitmap, int width, int height, boolean isAdjust) {
        // 如果想要的宽度和高度都比源图片小，就不压缩了，直接返回原图
        if (bitmap.getWidth() < width && bitmap.getHeight() < height) {
            return bitmap;
        }
        // 根据想要的尺寸精确计算压缩比例, 方法详解：public BigDecimal divide(BigDecimal divisor, int scale, int roundingMode);
        // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃
        float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();
        float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();
        if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸
            sx = (sx < sy ? sx : sy);
            sy = sx;// 哪个比例小一点，就用哪个比例
        }
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);// 调用api中的方法进行压缩，就大功告成了
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 600) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap result = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return result;
    }

    public static String saveHead(Bitmap bitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return null;
        } else {
            Log.i(TAG, "sd可用");
        }
        File dirFile = new File(SDCardUtil.getAppPackagePath());
        if (!dirFile.exists()) {
            Log.w(TAG, "path isn't exist");
            dirFile.mkdirs();
        }
        String fileName = SDCardUtil.getAppImagePath() + System.getProperty(File.separator)
                + System.getProperty(File.separator) + "head.jpg";//图片名字
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //关闭流
            if (b != null) {
                b.flush();
                b.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static String savePic(Bitmap bitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return null;
        } else {
            Log.i(TAG, "sd可用");
        }
        File dirFile = new File(SDCardUtil.getAppImagePath());
        if (!dirFile.exists()) {
            Log.w(TAG, "path isn't exist");
            dirFile.mkdirs();
        }
        String fileName = SDCardUtil.getAppImagePath() + System.getProperty("file.separator")
                + TimeUtil.getCurTimeToName() + ".jpg";//图片名字(未定义图片名，使用毫秒数)
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //关闭流
            if (b != null) {
                b.flush();
                b.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 保存图片到本地
     * @param bitmap
     * @param name name.jpg包含格式的图片名
     * @return
     */
    public static String savePic(Bitmap bitmap, String name) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return null;
        } else {
            Log.i(TAG, "sd可用");
        }
        File dirFile = new File(SDCardUtil.getAppImagePath());
        if (!dirFile.exists()) {
            Log.w(TAG, "path isn't exist");
            dirFile.mkdirs();
        }
        String fileName = SDCardUtil.getAppImagePath() + System.getProperty("file.separator")
                + name;//图片名字
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //关闭流
            if (b != null) {
                b.flush();
                b.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
}

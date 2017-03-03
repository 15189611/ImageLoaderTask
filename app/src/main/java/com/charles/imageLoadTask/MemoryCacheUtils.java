package com.charles.imageLoadTask;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Charles on 2017/3/3.
 */

public class MemoryCacheUtils {

    private LruCache<String, Bitmap> lruCache;

    public MemoryCacheUtils(){
        // lruCache最大允许内存一般为Android系统分给每个应用程序内存大小（默认Android系统给每个应用程序分配16兆内存）的八分之一（推荐）
        // 获得当前应用程序运行的内存大小
        long mCurrentMemory = Runtime.getRuntime().maxMemory();
        int maxSize = (int) (mCurrentMemory / 8);
        // 给LruCache设置最大的内存
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 获取每张图片所占内存的大小
                // 计算方法是：图片显示的宽度的像素点乘以高度的像素点
                int byteCount = value.getRowBytes() * value.getHeight();// 获取图片占用内存大小
                return byteCount;
            }
        };
    }

    public Bitmap getBitmapFromMemory(String url){
        Bitmap bitmap = lruCache.get(url);
        return bitmap;
    }

    public void setBitmapToMemory(String url,Bitmap bitmap){
        if(getBitmapFromMemory(url) == null && bitmap != null){
            lruCache.put(url,bitmap);
        }
    }

}


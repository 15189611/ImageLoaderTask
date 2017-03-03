package com.charles.imageLoadTask;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Charles on 2017/3/3.
 */

public class ImageLoadUtils {

    private NetCacheUtils netCacheUtils;
    private LocalCacheUtils localCacheUtils;
    private MemoryCacheUtils memoryCacheUtils;

    private  ImageLoadUtils(){
        netCacheUtils = new NetCacheUtils();
        localCacheUtils = new LocalCacheUtils();
        memoryCacheUtils = new MemoryCacheUtils();
    }


    public static ImageLoadUtils newInstance(){
        return SingletonHolder.instance;
    }

    private static class SingletonHolder{
        private final static ImageLoadUtils instance = new ImageLoadUtils();
    }

    public void displayImageView(ImageView imageView,String url){
        //1.内存
        Bitmap  bitmapFromMemory = memoryCacheUtils.getBitmapFromMemory(url);
        if(bitmapFromMemory != null){
            imageView.setImageBitmap(bitmapFromMemory);
            System.out.println("从内存缓存中加载图片");
            return;
        }

        //2.sd卡
        Bitmap  bitmapFromLocal = localCacheUtils.getBitmapFromLocal(url);
        if(bitmapFromLocal != null){
            imageView.setImageBitmap(bitmapFromLocal);
            memoryCacheUtils.setBitmapToMemory(url, bitmapFromLocal);// 将图片保存到内存
            System.out.println("从本地SD卡加载的图片");
            return;
        }

        //3.net
        System.out.println("从网络加载的图片");
        netCacheUtils.getBitmapFromNet(imageView, url, new NetCacheUtils.SaveBitmapListener() {
            @Override
            public void saveLocal(String url, Bitmap bitmap) {
                localCacheUtils.setBitmapToLocal(url,bitmap);
            }

            @Override
            public void saveMemory(String url, Bitmap bitmap) {
                memoryCacheUtils.setBitmapToMemory(url, bitmap);// 将图片保存到内存
            }
        });
    }

}

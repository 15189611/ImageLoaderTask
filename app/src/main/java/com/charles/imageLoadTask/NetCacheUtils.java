package com.charles.imageLoadTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Charles on 2017/3/3.
 */

public class NetCacheUtils {
    private SaveBitmapListener saveBitmapListener;

    public interface SaveBitmapListener{
        void saveLocal(String url,Bitmap bitmap);
        void saveMemory(String url,Bitmap bitmap);
    }

    public void getBitmapFromNet(ImageView imageView,String url,SaveBitmapListener saveBitmapListener){
        this.saveBitmapListener = saveBitmapListener;
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(imageView,url);
    }

    private class MyAsyncTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView ivPic;
        private String url;
        private Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
                    ivPic.setTag(url);
                }
            }
        };

        // 耗时任务执行之前 --主线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // 后台执行的任务
        @Override
        protected Bitmap doInBackground(Object... params) {
            // 执行异步任务的时候，将URL传过来
            ivPic = (ImageView) params[0];
            url = (String) params[1];
            handler.sendEmptyMessage(1);
            Bitmap bitmap = downloadBitmap(url);
            // 为了保证ImageView控件和URL一一对应，给ImageView设定一个标记
            return bitmap;
        }

        // 更新进度 --主线程
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        // 耗时任务执行之后--主线程
        @Override
        protected void onPostExecute(Bitmap result) {
            String mCurrentUrl = (String) ivPic.getTag();
            if (url.equals(mCurrentUrl)) {
                ivPic.setImageBitmap(result);
                // 从网络加载完之后，将图片保存到本地SD卡一份，保存到内存中一份
                // 从网络加载完之后，将图片保存到本地SD卡一份，保存到内存中一份
                if(saveBitmapListener != null){
                    saveBitmapListener.saveLocal(url,result);
                    saveBitmapListener.saveMemory(url,result);
                }
            }
        }
    }

    /**
     * 下载网络图片
     */
    private Bitmap downloadBitmap(String url) {
        HttpURLConnection conn = null;
        try {
            URL mURL = new URL(url);
            // 打开HttpURLConnection连接
            conn = (HttpURLConnection) mURL.openConnection();
            // 设置参数
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            // 开启连接
            conn.connect();

            // 获得响应码
            int code = conn.getResponseCode();
            if (code == 200) {
                // 相应成功,获得网络返回来的输入流
                InputStream is = conn.getInputStream();

                // 图片的输入流获取成功之后，设置图片的压缩参数,将图片进行压缩
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;// 将图片的宽高都压缩为原来的一半,在开发中此参数需要根据图片展示的大小来确定,否则可能展示的不正常
                options.inPreferredConfig = Bitmap.Config.RGB_565;// 这个压缩的最小

                // Bitmap bitmap = BitmapFactory.decodeStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);// 经过压缩的图片

                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 断开连接
            conn.disconnect();
        }

        return null;
    }

}

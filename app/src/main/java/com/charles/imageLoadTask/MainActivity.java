package com.charles.imageLoadTask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView) findViewById(R.id.imageMy);
        ImageLoadUtils.newInstance().displayImageView(imageView,"https://yppphoto.yupaopao.cn/upload/assets/20170223_loading_6p.jpg");

    }
}

package com.example.liubohua.cacheapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import car.wuba.saas.cache.CacheInstaller;
import car.wuba.saas.cache.RetrofitCache;
import car.wuba.saas.cache.RxCache;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by liubohua on 2018/7/19.
 */

public class TestActivity extends AppCompatActivity {

    private TestBean testBean;
    private Bitmap bitmap;
    private ImageView testImage;
    private byte[] bytes = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testBean = new TestBean();
        TestBean.A testA = new TestBean.A();
        testA.setString("aaaa");
        testBean.setAa(testA);
        testBean.setA(1);
        testBean.setStr("xiugai");

        Drawable drawable = getResources().getDrawable(R.drawable.test);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        bitmap = bitmapDrawable.getBitmap();
        final Button saveBtn = (Button) findViewById(R.id.memory_save_btn);
        Button loadBtn = (Button) findViewById(R.id.memory_get_btn);
        Button saveBtn2 = (Button) findViewById(R.id.memory_save_btn2);
        Button loadBttn2 = (Button) findViewById(R.id.memory_get_btn2);
        testImage = (ImageView) findViewById(R.id.memory_img);

        CacheInstaller.get()
                .configDiskCache("TestCache", 50 * 1024 * 1024, 1)
                .install(this);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestInerface testInerface = RetrofitCache.create(TestInerface.class);
                testInerface.putData("testKey", bitmap, Bitmap.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Toast.makeText(TestActivity.this, aBoolean + "", Toast.LENGTH_SHORT).show();
                    }
                });

//                RxCache.get().putData2TwoLayer("diskKey", testBean).subscribe(new Action1<Boolean>() {
//                    @Override
//                    public void call(Boolean aBoolean) {
//                        Toast.makeText(TestActivity.this, aBoolean + "", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestInerface testInerface = RetrofitCache.create(TestInerface.class);
                testInerface.getData("testKey",Bitmap.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap s) {
                        if (s != null) {
//                            Toast.makeText(TestActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
                            testImage.setImageBitmap(s);
                        } else {
                            Toast.makeText(TestActivity.this, "数据为null", Toast.LENGTH_SHORT).show();
                        }
//                        testImage.setImageBitmap(s);
                    }
                });


//                RxCache.get().getDataTwoLayer("diskKey", TestBean.class).subscribe(new Action1<TestBean>() {
//                    @Override
//                    public void call(TestBean s) {
//                        if (s != null) {
//                            Toast.makeText(TestActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(TestActivity.this, "数据为null", Toast.LENGTH_SHORT).show();
//                        }
////                        testImage.setImageBitmap(s);
//                    }
//                });
            }
        });


        saveBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        loadBttn2.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

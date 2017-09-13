package com.bwie.photoview;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class DescActivity extends AppCompatActivity {

    private ViewPager vp;

    private ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);
        initview();
    }

    private void initview() {
        vp = (ViewPager) findViewById(R.id.vp);
        Intent intent=getIntent();
        if(intent.getExtras()!= null){
            System.out.println("xxxxxxxx"+"判断传值不为空");
            list= (ArrayList<String>) intent.getExtras().getSerializable("imgs");
        }

        vp.setAdapter(new MyAda());
    //    System.out.println("xxxx另一个页面的"+list.toString());
    }

    class MyAda extends PagerAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view=View.inflate(DescActivity.this,R.layout.item,null);
            PhotoView pv=view.findViewById(R.id.pv);
            Glide.with(DescActivity.this).load(list.get(position)).into(pv);
            pv.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {//给图片设置点击事件，点击退出
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    DescActivity.this.finish();
                }
            });

            container.addView(view);//添加父类元素
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

package com.bwie.photoview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String url="http://mini.eastday.com/mobile/170913032156560.html";
    private WebView wv;
    private ArrayList<String> list;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();

        initview();
        paraseImgs();
        wv.loadUrl(url);
        wv.addJavascriptInterface(new Clicks(),"img");



    }

    private void paraseImgs() {

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Document   doc = Jsoup.connect(url).get(); //强制转换为文档类型

                    Elements all = doc.getElementsByTag("img");  //获取img标签

                    for (Element img : all) {
                     //   System.out.println("xxx"+img.attr("src"));
                       list.add(img.attr("src"));//读取img标签中的src属性，获取图片

                    }
                //    System.out.println("xxxx"+list.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        }).start();
    }

    private void initview() {
        wv = (WebView) findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);

        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                wv.loadUrl("javascript:(function(){" +//给html文件添加图片点击事件
                        "var objs = document.getElementsByTagName(\"img\"); " +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{"
                        + "    objs[i].onclick=function()  " +
                        "    {  "
                        + "        window.img.imgsClick(this.src);  " +
                        "    }  " +
                        "}" +
                        "})()");
            }
        });
    }

    class Clicks{
       @JavascriptInterface
        public void imgsClick(String imgurl){

            Intent in=new Intent(MainActivity.this,DescActivity.class);
            Bundle bundle=new Bundle();
         //  System.out.println("xxxx跳转过去的数据"+list.toString());
            bundle.putSerializable("imgs", list);
            in.putExtras(bundle);
            startActivity(in);
        }
    }
}

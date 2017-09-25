package com.example.administrator.down6footgpx;

import android.content.Context;
import android.content.Intent;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.UiScrollable;
//import android.widget.ActionMenuView;
import android.support.v7.widget.ActionMenuView;

import android.util.Log;
//import android.widget.ActionMenuView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import static org.junit.Assert.*;
import android.os.RemoteException;

import jp.jun_nama.test.utf7ime.helper.Utf7ImeHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private UiDevice mUIDevice = null;
    private Context mContext = null;
    String APP = "com.topgether.sixfoot";

    @Before
    public void setUp() throws RemoteException{
        mUIDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());  //获得device对象
        mContext = InstrumentationRegistry.getContext();

        if(!mUIDevice.isScreenOn()){  //唤醒屏幕
            mUIDevice.wakeUp();
        }
        mUIDevice.pressHome();  //按home键
    }

    @Test
    public void test1(){
        /*
        System.out.println("读取文件内容出错");
        File file=new File("c://source.txt");
        try {
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String target = bufferedReader.readLine();
                String province = bufferedReader.readLine();

                read.close();
            }else{
                file.createNewFile();
                System.out.println("file"+file.getAbsolutePath());
                return ;
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        */

        Intent myIntent = mContext.getPackageManager().getLaunchIntentForPackage(APP);  //启动app

        mContext.startActivity(myIntent);
        mUIDevice.waitForWindowUpdate(APP, 5 * 2000);

        try {
            //先用用看
            UiObject trybutton = mUIDevice.findObject(new UiSelector().text("先用用看"));
            trybutton.click();

            UiObject sender = mUIDevice.findObject(new UiSelector().text("发现"));  //定位text内容为Send的控键
            sender.click();  //点击按键
            UiObject searchbar = mUIDevice.findObject(new UiSelector().resourceId("com.topgether.sixfoot:id/tv_recommend_explorer"));  //定位text内容为Send的控键
            searchbar.click();//点击

            //指明具体线路
            UiObject distancebar = mUIDevice.findObject(new UiSelector().resourceId("com.topgether.sixfoot:id/et_search"));
            //distancebar.click();
            distancebar.setText(Utf7ImeHelper.e("望京楼"));

            UiObject provincebar = mUIDevice.findObject(new UiSelector().resourceId("com.topgether.sixfoot:id/tv_search_province"));

            provincebar.click();//点击



            Log.i(APP,"选择省份");
            String province="北京";
            //省份的列表分为2页
            try {
                //在第一页上，可以直接找到，否则需要翻页
                UiObject citybar = mUIDevice.findObject(new UiSelector().text(province));
                citybar.click();//自动退回
            }
            catch (android.support.test.uiautomator.UiObjectNotFoundException e) {
                //在第二页上，需要翻页
                UiScrollable provinceuis = new UiScrollable(new UiSelector().resourceId("com.topgether.sixfoot:id/contentListView"));
                Log.i(APP, "是否可滚动:" + provinceuis.isScrollable());
                Log.i(APP, "是否可滚动:" + provinceuis.isLongClickable());
                //provinceuis.setAsHorizontalList();--->调用后会出现bug
                //翻到第二页
                provinceuis.scrollForward();
                //provinceuis.swipeLeft(50);
                UiObject citybar = mUIDevice.findObject(new UiSelector().text(province));
                //UiObject citybar = provinceuis.getChildByText(new UiSelector().resourceId("com.topgether.sixfoot:id/title"),"北京",false );
                citybar.click();//自动退回
            }

            UiObject typebar = mUIDevice.findObject(new UiSelector().resourceId("com.topgether.sixfoot:id/tv_search_type"));
            typebar.click();
            UiObject typeSelect = mUIDevice.findObject(new UiSelector().text("徒步"));
            typeSelect.click();

            //不限距离
            //UiObject distancebar = mUIDevice.findObject(new UiSelector().resourceId("com.topgether.sixfoot:id/tv_search_distance"));
            //distancebar.setText("不限");

            //是否选择推荐
            //UiObject recommendbar = mUIDevice.findObject(new UiSelector().resourceId("com.topgether.sixfoot:id/sb_recommend"));
            //recommendbar.click();

            UiObject searchbutton = mUIDevice.findObject(new UiSelector().resourceId("com.topgether.sixfoot:id/btn_search"));
            searchbutton.click();

            mUIDevice.waitForWindowUpdate(APP, 5 * 2000);

            //下面是处理可以滚动的列表

            UiScrollable uis = new UiScrollable(new UiSelector().resourceId("com.topgether.sixfoot:id/recycler_view"));
            System.out.println("列表子元素的个数:" + uis.getChildCount());

            processPage(uis);

            //如果当前页面处理完毕，向下翻页，继续
            while(uis.scrollForward(50)){
                Log.i(APP,"向下翻屏");
                System.out.println("列表子元素的个数:" + uis.getChildCount());
                boolean isTimeOut= mUIDevice.waitForWindowUpdate(APP, 10 * 2000);
                if(!isTimeOut)
                    Log.i(APP,"翻页超时");
                processPage(uis);

            }

            //将最后一屏的列表都处理完
            //因为没办法明确得到索引和屏幕显示的对应关系
            processPage(uis);


        }catch (Exception e){
            e.printStackTrace();
        }
        mUIDevice.pressBack();
        mUIDevice.pressBack();
        mUIDevice.pressBack();
        mUIDevice.pressBack();
        assertTrue(true); //断言，随便乱写的，此处未起到断言作用
    }

    //当前屏幕处理。
    //问题是并不是从0到9开始编号的。
    //比如处理到9，但是只是中间部分，然后出发下翻动作，然后又从0开始处理。
    //需要一个方法，准确的获得从上到下的索引。
    //在app中的处理，是监听点击函数，这样能准确的知道是那个位置。
    //在黑盒测试中，是模拟点击操作，我去点击，而不是被动监听。没法准确知道多少个条目，除非catch 找不到元素的exception
    //单页处理中需要以找不到元素的exception才能结束。
    public void processPage(UiScrollable uis) throws  Exception{
        boolean isTimeOut;
        try {
            int num = uis.getChildCount();
            //如果个数不足，那么按照个数来。
            if (num > 10)
                num = 10;
            for (int i = 0; i < num; i++) {
                Log.i(APP, Integer.toString(i) + "条");
                UiObject abc = uis.getChildByInstance(new UiSelector().resourceId("com.topgether.sixfoot:id/tv_track_name"), i);
                UiObject comment =uis.getChildByInstance(new UiSelector().resourceId("com.topgether.sixfoot:id/tv_comment_count"), i);
                Log.i(APP, "下载"+abc.getText()+"评价"+comment.getText());

                abc.click();

                //等待下载地图
                isTimeOut=mUIDevice.waitForWindowUpdate(APP, 4 * 2000);
                if(!isTimeOut)
                    Log.i(APP,"等待下载地图超时");

                 //UiObject menubar = mUIDevice.findObject(new UiSelector().className("android.support.v7.widget.ActionMenuView"));
                UiObject menubar = mUIDevice.findObject(new UiSelector().description("更多选项"));

                menubar.click();


                UiObject gpxbar = mUIDevice.findObject(new UiSelector().text("导出GPX"));
                gpxbar.click();

                isTimeOut=mUIDevice.waitForWindowUpdate(APP, 3 * 2000);
                if(!isTimeOut)
                    Log.i(APP,"导出GPX超时");

                //如果已存在
                UiObject duplicatebar = mUIDevice.findObject(new UiSelector().text("文件已存在"));
                if (duplicatebar.exists()) {
                    UiObject cancelbar = mUIDevice.findObject(new UiSelector().text("取消"));
                    cancelbar.click();
                } else
                //确认下载
                {
                    UiObject comfirmbar = mUIDevice.findObject(new UiSelector().text("确定"));
                    comfirmbar.click();
                }


                //返回
                mUIDevice.pressBack();

                //处理下一个
            }
        }
        catch (android.support.test.uiautomator.UiObjectNotFoundException e){
            Log.i(APP,"索引超过显示界面，需要向下翻页");
        }
    }
}
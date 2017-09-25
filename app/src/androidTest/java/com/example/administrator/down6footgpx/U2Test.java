package com.example.administrator.down6footgpx;

import org.junit.AfterClass;
import org.junit.*;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import  android.util.Log;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

/**
 * Created by Administrator on 2017/7/19.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class U2Test {
    public static final String TAG = "U2Test";

    @BeforeClass
    public static void beforeClass(){
        Log.d(TAG, "BeforeClass");
    }

    @AfterClass
    public static void afterClass(){
        Log.d(TAG, "AfterClass");
    }

    @Before
    public void before(){
        Log.d(TAG, "Before");
    }

    @After
    public void after(){
        Log.d(TAG, "After");
    }

    @Test
    public void test001() {
        Log.d(TAG, "test001");
    }

    @Test
    public void test002() {
        Log.d(TAG, "test002");
    }
}
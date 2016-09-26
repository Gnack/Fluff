package com.ru.gacklash.fluff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.OnObbStateChangeListener;
import android.os.storage.StorageManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.util.Vector;

/**
 * Created by gnack_000 on 05.12.2015.
 */
public class ChoseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }




    public void onIgButtonClick(View view) {
        ImageButton pressedBtn=(ImageButton) view;
       // getResources().getDrawable(R.drawable.menu_chaos, null));
        Drawable animImg, oldImg;
        if(android.os.Build.VERSION.SDK_INT >= 21){
            oldImg = getResources().getDrawable(R.drawable.menu_ig, getTheme());
            animImg = getResources().getDrawable(R.drawable.access, getTheme());
        } else {
            oldImg = getResources().getDrawable(R.drawable.menu_ig);
            animImg = getResources().getDrawable(R.drawable.access);
        }
//        pressedBtn.setBackground(newImg);
        StateListDrawable states = new StateListDrawable();
        states.setExitFadeDuration(500);
        states.addState(new int[]{android.R.attr.state_pressed},animImg);
        states.addState(new int[]{}, oldImg);
        if(android.os.Build.VERSION.SDK_INT < 16) {
            pressedBtn.setBackgroundDrawable(states);
        } else {
            pressedBtn.setBackground(states);
        }


        Intent intent=new Intent(this, WebActivity.class);
        Uri uri = Uri.parse("/ig/index.html");
        intent.setData(uri);
        startActivity(intent);
    }

    public void onSmButtonClick(View view) {
        ImageButton pressedBtn=(ImageButton) view;
        // getResources().getDrawable(R.drawable.menu_chaos, null));
        Drawable animImg, oldImg;
        if(android.os.Build.VERSION.SDK_INT >= 21){
            oldImg = getResources().getDrawable(R.drawable.menu_kosmodes, getTheme());
            animImg = getResources().getDrawable(R.drawable.access, getTheme());
        } else {
            oldImg = getResources().getDrawable(R.drawable.menu_kosmodes);
            animImg = getResources().getDrawable(R.drawable.access);
        }
//        pressedBtn.setBackground(newImg);
        StateListDrawable states = new StateListDrawable();
        states.setExitFadeDuration(500);
        states.addState(new int[]{android.R.attr.state_pressed},animImg);
        states.addState(new int[]{}, oldImg);
        if(android.os.Build.VERSION.SDK_INT < 16) {
            pressedBtn.setBackgroundDrawable(states);
        } else {
            pressedBtn.setBackground(states);
        }


        Intent intent=new Intent(this, WebActivity.class);
        Uri uri = Uri.parse("/sm/index.html");
        intent.setData(uri);
        startActivity(intent);
    }

    public void onChButtonClick(View view) {
        ImageButton pressedBtn=(ImageButton) view;
        // getResources().getDrawable(R.drawable.menu_chaos, null));
        Drawable animImg, oldImg;
        if(android.os.Build.VERSION.SDK_INT >= 21){
            oldImg = getResources().getDrawable(R.drawable.menu_chaos, getTheme());
            animImg = getResources().getDrawable(R.drawable.access, getTheme());
        } else {
            oldImg = getResources().getDrawable(R.drawable.menu_chaos);
            animImg = getResources().getDrawable(R.drawable.access);
        }
//        pressedBtn.setBackground(newImg);
        StateListDrawable states = new StateListDrawable();
        states.setExitFadeDuration(500);
        states.addState(new int[]{android.R.attr.state_pressed},animImg);
        states.addState(new int[]{}, oldImg);
        if(android.os.Build.VERSION.SDK_INT < 16) {
            pressedBtn.setBackgroundDrawable(states);
        } else {
            pressedBtn.setBackground(states);
        }


        Intent intent=new Intent(this, WebActivity.class);
        Uri uri = Uri.parse("/ch/index.html");
        intent.setData(uri);
        startActivity(intent);
    }

    public void onCopyrightButtonClick(View view) {
//        ImageButton pressedBtn=(ImageButton) view;
////         getResources().getDrawable(R.drawable.menu_chaos, null);
//        Drawable animImg, oldImg;
//        if(android.os.Build.VERSION.SDK_INT >= 21){
//            oldImg = getResources().getDrawable(R.drawable.copyrigth, getTheme());
//            animImg = getResources().getDrawable(R.drawable.access, getTheme());
//        } else {
//            oldImg = getResources().getDrawable(R.drawable.copyrigth);
//            animImg = getResources().getDrawable(R.drawable.access);
//        }
////        pressedBtn.setBackground(newImg);
//        StateListDrawable states = new StateListDrawable();
//        states.setExitFadeDuration(500);
//        states.addState(new int[]{android.R.attr.state_pressed},animImg);
//        states.addState(new int[] { },oldImg);
//        pressedBtn.setBackground(states);

        Intent intent=new Intent(this, WebActivity.class);
        Uri uri = Uri.parse("file:///android_asset/webFiles/copyright.html");
        intent.setData(uri);
        startActivity(intent);

    }



}

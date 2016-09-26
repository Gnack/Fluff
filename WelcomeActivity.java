package com.ru.gacklash.fluff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.storage.OnObbStateChangeListener;
import android.os.storage.StorageManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.ru.gacklash.fluff.support.XmlHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class WelcomeActivity extends AppCompatActivity {

    private final static String EXP_PATH = "/Android/obb/";
    private static String MOUNT_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        TextView randQuote=(TextView) findViewById(R.id.randQuote);

        List quotesList;
        try {
            InputStream str;
            str=getAssets().open("quotes.xml");
            quotesList= XmlHelper.readQuotes(str);
            Random randgen= new Random();
            randQuote.setText((CharSequence) quotesList.get(randgen.nextInt(quotesList.size()-1)));
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        String appKey = "19ce2bca7349eeac5940d179f21692febde1208fb1134d4e";
        Appodeal.disableLocationPermissionCheck();
        Appodeal.initialize(this, appKey, Appodeal.BANNER);
        Appodeal.initialize(this, appKey, Appodeal.REWARDED_VIDEO);

        StorageManager sm= (StorageManager) this.getSystemService(STORAGE_SERVICE);
        OnObbStateChangeListener listener=new OnObbStateChangeListener() {
            @Override
            public void onObbStateChange(String path, int state) {
                super.onObbStateChange(path, state);
            }
        };
        String expFilePath=getAPKExpansionFiles(this, getResources().getInteger(R.integer.versionCustom));
//        while (expFilePath==null){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Ошибка!")
//                    .setMessage("Файлы приложения недоступны. Возможно устройство подключено к ПК, либо извлечена sd карта.")
//                    .setCancelable(false)
//                    .setNegativeButton("Повторить попытку",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//            AlertDialog alert = builder.create();
//            alert.show();
//            expFilePath=getAPKExpansionFiles(this,getResources().getInteger(R.integer.versionCustom));
//        }
        sm.mountObb(expFilePath,null,listener);

    }

    static String getAPKExpansionFiles(Context ctx, int mainVersion) {
        String packageName = ctx.getPackageName();
        // Vector<String> ret = new Vector<String>();
        if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {
            // Build the full path to the app's expansion files
            File root = Environment.getExternalStorageDirectory();
            File expPath = new File(root.toString() + EXP_PATH + packageName);

            // Check that expansion file path exists
            if (expPath.exists()) {
                if ( mainVersion > 0 ) {
                    String strMainPath = expPath + File.separator + "main." +
                            mainVersion + "." + packageName + ".obb";
                    File main = new File(strMainPath);
                    if ( main.isFile() ) {
                        return strMainPath;
                    }
                }
//                if ( patchVersion > 0 ) {
//                    String strPatchPath = expPath + File.separator + "patch." +
//                            mainVersion + "." + packageName + ".obb";
//                    File main = new File(strPatchPath);
//                    if ( main.isFile() ) {
//                        ret.add(strPatchPath);
//                    }
//                }
            }
        }
//        String[] retArray = new String[ret.size()];
//        ret.toArray(retArray);
        return null;
    }


    public void onGoButtonClick(View view) {
        Intent intent=new Intent(this, ChoseActivity.class);
        startActivity(intent);
    }

    public void onPartnersButtonClick(View view) {
        Intent intent=new Intent(this, PartnersActivity.class);
        startActivity(intent);
    }
}
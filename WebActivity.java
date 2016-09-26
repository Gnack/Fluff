package com.ru.gacklash.fluff;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.os.storage.OnObbStateChangeListener;
import android.os.storage.StorageManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.ru.gacklash.fluff.support.BookmarkContract;
import android.content.CursorLoader;
//import com.ru.gacklash.fluff.support.CustomAdapter;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by gnack_000 on 05.12.2015.
 * last update: 01.05.2016
 */
public class WebActivity  extends AppCompatActivity {
    private WebView mWebView;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int mFontSize=0;
    private boolean mHasToRestoreState = false;
    private float mProgressToRestore;
    private String mPageToRestore;
    public static final String PREFS_NAME = "WebActivitySettings";
    public static final int TEXT_SIZE_POS = 2;
    static final int PICK_BOOKMARK = 1;
    private long mAdOffStarted=0;

    private final static String EXP_PATH = "/Android/obb/";
    private static String MOUNT_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        StorageManager sm= (StorageManager) this.getSystemService(STORAGE_SERVICE);
        OnObbStateChangeListener listener=new OnObbStateChangeListener() {
            @Override
            public void onObbStateChange(String path, int state) {
                super.onObbStateChange(path, state);
            }
        };
        String expFilePath=getAPKExpansionFiles(this, getResources().getInteger(R.integer.versionCustom));
        if (!sm.isObbMounted(expFilePath)){
            sm.mountObb(expFilePath,null,listener);
            try {
                wait(1000*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        MOUNT_PATH=sm.getMountedObbPath(expFilePath);


        setContentView(R.layout.activity_web);

        //Appodeal.show(this, Appodeal.BANNER_BOTTOM);
        adController();
        mWebView=(WebView) findViewById(R.id.mainView);
        mWebView.setWebViewClient(new CustomWebViewClient());
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //mainView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //"file:///android_asset/webFiles/index.html"
        Uri fractionUri=getIntent().getData();
        mWebView.loadUrl("file://"+MOUNT_PATH+fractionUri.toString());
        mWebView.setBackgroundColor(Color.TRANSPARENT);

        String[] mDrawerTitles = getResources().getStringArray(R.array.drawer_array);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new CustomAdapter(this,
        R.layout.drawer_list_item, mDrawerTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
       // mDrawerLayout.openDrawer(mDrawerList);
        mDrawerList.bringToFront();


        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {

            @Override
            public void onRewardedVideoLoaded() {

            }
            @Override
            public void onRewardedVideoFailedToLoad() {

            }
            @Override
            public void onRewardedVideoShown() {

            }
            @Override
            public void onRewardedVideoFinished(int amount, String name) {
                mAdOffStarted=SystemClock.elapsedRealtime();
                adController();
            }

            @Override
            public void onRewardedVideoClosed() {

            }


        });



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

    @Override
    public void onStop() {

        super.onStop();
        Appodeal.hide(this, Appodeal.BANNER_BOTTOM);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME ,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("textSize", mFontSize);
        editor.putLong("adOffStarted", mAdOffStarted);
        // Commit the edits!
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        Appodeal.onResume(this, Appodeal.BANNER_BOTTOM);
        adController();
        setFontSize();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

        }
    }

    public void onPlusButtonClick(View view) {
        mFontSize++;
        setFontSize();
        updateFontSizeText();
//        WebSettings webSettings = mWebView.getSettings();
//        Resources res = getResources();
//        int fontSize=webSettings.getDefaultFontSize();
//        webSettings.setDefaultFontSize(++fontSize);
//        LinearLayout vParentRow = (LinearLayout)view.getParent();
//        TextView sizeText=(TextView) vParentRow.getChildAt(1);
//        sizeText.setText(Integer.toString(fontSize));
    }

    public void onMinusButtonClick(View view) {
        mFontSize--;
        setFontSize();
        updateFontSizeText();
    }

    public void adController(){
        Long adOffDuration = new Long(1000  * 60 * 60 * 3); //* 60 * 3
        if (mAdOffStarted==0){
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            mAdOffStarted = settings.getLong("adOffStarted", SystemClock.elapsedRealtime()-(adOffDuration+1));
            if (SystemClock.elapsedRealtime()<mAdOffStarted){
                mAdOffStarted=0;
            }
        }

        if (mAdOffStarted==0){
            Appodeal.show(this, Appodeal.BANNER_BOTTOM);
            return;
        }

        Long adOffTime = SystemClock.elapsedRealtime() - mAdOffStarted;
        if (adOffTime > adOffDuration) {
            Appodeal.show(this, Appodeal.BANNER_BOTTOM);
        } else {
            Appodeal.hide(this, Appodeal.BANNER_BOTTOM);
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            if (mHasToRestoreState) {
                mHasToRestoreState = false;
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        float webviewsize = mWebView.getContentHeight() - mWebView.getTop();
                        float positionInWV = webviewsize * mProgressToRestore;
                        int positionY = Math.round(mWebView.getTop() + positionInWV);
                        mWebView.scrollTo(0, positionY);
                    }
                    // Delay the scrollTo to make it work
                }, 300);
            }
            
            adController();

            super.onPageFinished(view, url);
        }
    }

    public void onSaveButtonClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Название:");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Text = input.getText().toString();
                ContentValues bookmarkValues = new ContentValues();

                bookmarkValues.put(BookmarkContract.BookmarkEntry.COLUMN_BOOKMARK_NAME, Text);
                bookmarkValues.put(BookmarkContract.BookmarkEntry.COLUMN_BOOKMARK_ADDRESS, mPageToRestore);
                bookmarkValues.put(BookmarkContract.BookmarkEntry.COLUMN_BOOKMARK_PROGRESS, mProgressToRestore);
                getContentResolver().insert(BookmarkContract.BookmarkEntry.CONTENT_URI, bookmarkValues);
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        mProgressToRestore = calculateProgression();
        mPageToRestore = mWebView.getUrl().replaceAll("file://"+MOUNT_PATH, "");

    }

    private float calculateProgression() {
        float positionTopView = mWebView.getTop();
        float contentHeight = mWebView.getContentHeight();
        float currentScrollPosition = mWebView.getScrollY();
        return (currentScrollPosition - positionTopView) / contentHeight;
    }

    public void onLoadButtonClick(View view) {

        Intent bookmarkIntent = new Intent(this, BookmarkActivity.class);
        mDrawerLayout.closeDrawers();
        startActivityForResult(bookmarkIntent, PICK_BOOKMARK);

//        mWebView.loadUrl(mPageToRestore);
//        mHasToRestoreState=true;
    }

    public void onVideoButtonClick(View view) {

//        Intent bookmarkIntent = new Intent(this, BookmarkActivity.class);
//        mDrawerLayout.closeDrawers();
//        startActivityForResult(bookmarkIntent, PICK_BOOKMARK);
        Appodeal.show(this, Appodeal.REWARDED_VIDEO);


//        mWebView.loadUrl(mPageToRestore);
//        mHasToRestoreState=true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            String sortOrder = BookmarkContract.BookmarkEntry._ID + " ASC";
            Uri Uri = BookmarkContract.BookmarkEntry.CONTENT_URI;
            CursorLoader loader=new CursorLoader(this, Uri,
                    null,
                    null,
                    null,
                    sortOrder);

            Cursor cursor = loader.loadInBackground();
            cursor.moveToPosition(Integer.parseInt(data.getData().toString()));
            mPageToRestore=cursor.getString(cursor.getColumnIndex(BookmarkContract.BookmarkEntry.COLUMN_BOOKMARK_ADDRESS));
            mProgressToRestore=cursor.getFloat(cursor.getColumnIndex(BookmarkContract.BookmarkEntry.COLUMN_BOOKMARK_PROGRESS));
            mHasToRestoreState=true;
            mWebView.loadUrl("file://"+MOUNT_PATH+mPageToRestore);

        }

    }

    private void setFontSize(){


        WebSettings webSettings = mWebView.getSettings();

        if (mFontSize==0){
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            mFontSize = settings.getInt("textSize", webSettings.getDefaultFontSize());
        }

        webSettings.setDefaultFontSize(mFontSize);

       // LinearLayout vParentRow = (LinearLayout)view.getParent();
       // TextView sizeTextView=(TextView) mDrawerLayout.findViewById(R.id.drawerFontSizeTextView);
        //TextView) vParentRow.getChildAt(1);

    }

    private void updateFontSizeText (){
        LinearLayout sizeHorizontalView=(LinearLayout) getViewByPosition(TEXT_SIZE_POS, mDrawerList);
//        TextView sizeTextView=(TextView)sizeView.getChildAt(1);
        LinearLayout sizeView=(LinearLayout) sizeHorizontalView.getChildAt(1);
        TextView sizeTextView=(TextView) sizeView.getChildAt(1);
        sizeTextView.setText(String.format(Locale.ENGLISH,"%d", mFontSize));
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private static class ViewHolder {
        public TextView textView;
    }

    private class CustomAdapter extends ArrayAdapter {

        private static final int VIEW_TYPE_SIZE = 0;
        private static final int VIEW_TYPE_MENU = 1;
        private static final int VIEW_TYPE_BOOKMARK = 2;
        private ArrayList mData = new ArrayList();
        private LayoutInflater mInflater;

        public CustomAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);

            mInflater = LayoutInflater.from(context);
        }

//    public static class ViewHolder {
//        public final TextView drawerFontSizeTextView;
//        public final TextView listTextView;
//
//        public ViewHolder(View view) {
//            drawerFontSizeTextView = (TextView) view.findViewById(R.id.drawerFontSizeTextView);
//            listTextView = (TextView) view.findViewById(R.id.listTextView);
//        }
//    }




        @Override
        public int getItemViewType(int position) {
            String viewName=getItem(position).toString();

            if (viewName.equals(getContext().getString(R.string.resize_view))){
                return VIEW_TYPE_SIZE;
            }
            if (viewName.equals(getContext().getString(R.string.bookmarks_view))){
                return VIEW_TYPE_BOOKMARK;
            }

            return VIEW_TYPE_MENU;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = new ViewHolder();
            int viewType = getItemViewType(position);
            int layoutId = -1;
            if (convertView == null) {

                switch (viewType) {
                    case VIEW_TYPE_SIZE: {
                        layoutId = R.layout.drawer_font_size_view;
                        convertView = mInflater.inflate(layoutId, null);

                        holder.textView = (TextView)convertView.findViewById(R.id.drawerFontSizeTextView);
                        holder.textView.setText(String.format(Locale.ENGLISH,"%d", mFontSize));

                        convertView.setTag(holder);

                        break;
                    }
                    case VIEW_TYPE_BOOKMARK: {
                        layoutId = R.layout.drawer_bookmark;
                        convertView = mInflater.inflate(layoutId, null);
                        convertView.setTag(holder);

                        break;
                    }
                    case VIEW_TYPE_MENU: {
                        layoutId = R.layout.drawer_list_item;
                        convertView = mInflater.inflate(layoutId, null);

                       // holder.textView = (TextView)convertView.findViewById(R.id.listTextView);
                       // holder.textView.setText(getItem(position).toString());
                        convertView.setTag(holder);
                        break;
                    }
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
                switch (viewType) {
                    case VIEW_TYPE_SIZE: {
                        // holder.textView.setText("22");
                        break;
                    }
                    case VIEW_TYPE_BOOKMARK: {

                        break;
                    }
                    case VIEW_TYPE_MENU: {
                        //holder.textView.setText(getItem(position).toString());
                        break;
                    }

                }
            }

            return convertView;
        }
    }


}

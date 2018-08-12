package com.example.czd.kotlinone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.czd.bean.TitleList;
import com.example.czd.kotlinone.adapter.TabFragmentAdapter;
import com.example.czd.kotlinone.view.TabContainerView;
import com.example.czd.util.HttpUtil;
import com.example.czd.util.SQLiteDbHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * Activity主页
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{


    public static void startMainActivity (Activity activity, int tab) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("tab", tab);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);

    }

    /**
     * tab图标集合
     */
    private final int ICONS_RES[][] = {
            {
                    R.mipmap.ic_home_normal,
                    R.mipmap.ic_home_focus
            },
            {
                    R.mipmap.ic_message_normal,
                    R.mipmap.ic_message_focus
            },
            {
                    R.mipmap.ic_message_normal,
                    R.mipmap.ic_message_focus
            },
            {
                    R.mipmap.ic_mine_normal,
                    R.mipmap.ic_mine_focus
            }
    };

    /** tab 颜色值*/
    private final int[] TAB_COLORS = new int []{
            R.color.main_bottom_tab_textcolor_normal,
            R.color.main_bottom_tab_textcolor_selected};

    private Fragment[] fragments = {
            new HomeFragment(),
            new TechFragment(),
            new MessageFragment(),
            new MineFragment()
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initViews();
        ImageView imageview = (ImageView)findViewById(R.id.searching);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SearchingActivity.class);
                startActivity(intent);
            }
        });


    }







    private void initViews() {
        TabFragmentAdapter mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragments);
        ViewPager mPager = (ViewPager) findViewById(R.id.tab_pager);
        //设置当前可见Item左右可见page数，次范围内不会被销毁
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(mAdapter);

        TabContainerView mTabLayout = (TabContainerView) findViewById(R.id.ll_tab_container);
        mTabLayout.setOnPageChangeListener(this);

        mTabLayout.initContainer(getResources().getStringArray(R.array.tab_main_title), ICONS_RES, TAB_COLORS, true);

        int width = getResources().getDimensionPixelSize(R.dimen.tab_icon_width);
        int height = getResources().getDimensionPixelSize(R.dimen.tab_icon_height);
        mTabLayout.setContainerLayout(R.layout.tab_container_view, R.id.iv_tab_icon, R.id.tv_tab_text, width, height);
//        mTabLayout.setSingleTextLayout(R.layout.tab_container_view, R.id.tv_tab_text);
//        mTabLayout.setSingleIconLayout(R.layout.tab_container_view, R.id.iv_tab_icon);

        mTabLayout.setViewPager(mPager);
        mPager.setCurrentItem(getIntent().getIntExtra("tab", 0));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int index = 0, len = fragments.length; index < len; index ++) {
            fragments[index].onHiddenChanged(index != position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
}

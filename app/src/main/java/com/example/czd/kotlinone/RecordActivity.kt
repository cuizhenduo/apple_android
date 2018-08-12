package com.example.czd.kotlinone

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

import com.example.czd.bean.UrlList
import com.example.czd.kotlinone.adapter.UrlListAdapter
import com.example.czd.util.SQLiteDbHelper
import kotlinx.android.synthetic.main.activity_record.*
import org.jetbrains.anko.startActivity

class RecordActivity : AppCompatActivity() {
    internal var urlLists: MutableList<UrlList> = java.util.ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        tl_head.setNavigationIcon(R.drawable.back_32)
        tl_head.setBackgroundResource(R.color.colorPrimaryDark)
        tl_head.title = "浏览记录"
        val dbHelper = SQLiteDbHelper(this@RecordActivity)
        val db = dbHelper.writableDatabase
        var res = db.rawQuery("select * from ourl order by id desc",null)
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                val name = res.getString(res.getColumnIndex("title"))
                val author = res.getString(res.getColumnIndex("url"))
                val titleList = UrlList(name, author)
                urlLists.add(titleList)
            }
            val urlListAdapter = UrlListAdapter(this, R.layout.title_item, urlLists)
            list_view.setAdapter(urlListAdapter)
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = urlLists[position]
                startActivity<RecordShowActivity>(
                        Pair("surl",titleList.getUrl().toString())
                )
            }
        }
        tl_head.setNavigationOnClickListener{finish()}
    }
}

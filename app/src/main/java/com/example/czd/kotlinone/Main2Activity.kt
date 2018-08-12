package com.example.czd.kotlinone


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.view.WindowManager

import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.czd.bean.TitleList
import com.example.czd.kotlinone.adapter.TitleListAdapter
import kotlinx.android.synthetic.main.activity_main2.*

import org.jetbrains.anko.startActivity

import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener




class Main2Activity : AppCompatActivity() {
    internal var titleLists: MutableList<TitleList> = ArrayList()

    fun parseJson(jsdata: String,kind: String) {
        try {
            if (kind == "re"){
                titleLists.clear()
            }

            val jsonObject = JSONObject(jsdata)
            val jsonArray = jsonObject.getJSONArray("tit")
            for (i in 0 until jsonArray.length()) {
                val jsonObject1 = jsonArray.getJSONObject(i)
                val pid = jsonObject1.getInt("id")
                val tit = jsonObject1.getString("title")
                val titleList = TitleList(pid, tit)
                titleLists.add(titleList)
            }
            //适配显示listview
            val titleListAdapter = TitleListAdapter(this, R.layout.title_item, titleLists)
            list_view.setAdapter(titleListAdapter)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun sendrequesturl(ourl:String,kind:String) {
        Thread(Runnable {
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val url = URL(ourl)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 8000
                connection.readTimeout = 8000

                val `in` = connection.inputStream
                reader = BufferedReader(InputStreamReader(`in`))
                val response = StringBuilder()
                var line: String
                while (true) {
                    val line = reader.readLine() ?: break
                    response.append(line)
                }
                if(response.toString()==""){
                    page = page - 1
                }else{
                showResponse(response.toString(),kind)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                connection?.disconnect()
            }
        }).start()
    }

    fun showResponse(response: String,kind: String) {
        runOnUiThread {
            parseJson(response,kind)
        }
    }

    var kinddata = arrayOf("早熟品种介绍", "中熟品种介绍", "晚熟品种介绍")
    var earthdata = arrayOf("土壤", "施肥", "灌水")
    var collectdata = arrayOf("花期", "套袋", "解袋","采收")
    var pestdata = arrayOf("病害", "虫害")
    var mark:String = ""
    var page = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }

        //接收上个页面传过来的数据
        val intent = intent
        mark = intent.getStringExtra("mark")

        tl_head.setNavigationIcon(R.drawable.back_32)
        tl_head.setBackgroundResource(R.color.colorPrimaryDark)



        //根据上个页面传过来的数据，判断这个页面加载哪个listview
        if (mark=="主要品种"){
            val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, kinddata)
            list_view.setAdapter(adapter)
            tl_head.title = "主要品种"
            list_view.setOnItemClickListener { parent, view, position, id ->
                startActivity<SubPageActivity>(
                        Pair("kind",kinddata[position])
                )
            }
        }

        if (mark=="建园"){
            sendrequesturl("http://39.104.27.39:8995/buildydata?page=1","re")
            tl_head.title = "建园"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","建园")
                )
            }
        }

        if (mark=="土壤肥水管理"){
            val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, earthdata)
            list_view.setAdapter(adapter)
            tl_head.title = "土壤肥水管理"
            list_view.setOnItemClickListener { parent, view, position, id ->
                startActivity<SubPageActivity>(
                        Pair("kind",earthdata[position])
                )
            }
        }

        if (mark=="整型修剪"){
            sendrequesturl("http://39.104.27.39:8995/winjiandata?page=1","re")
            tl_head.title = "整型修剪"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","整型修剪")
                )
            }
        }

        if (mark=="花果采收"){val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, collectdata)
            list_view.setAdapter(adapter)
            tl_head.title = "花果采收"
            list_view.setOnItemClickListener { parent, view, position, id ->
                startActivity<SubPageActivity>(
                        Pair("kind",collectdata[position])
                )
            }
        }

        if (mark=="病虫害"){val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, pestdata)
            list_view.setAdapter(adapter)
            tl_head.title = "病虫害"
            list_view.setOnItemClickListener { parent, view, position, id ->
                startActivity<SubPageActivity>(
                        Pair("kind",pestdata[position])
                )
            }
        }

        if (mark=="机械操作"){
            sendrequesturl("http://39.104.27.39:8995/machinedata?page=1","re")
            tl_head.title = "机械操作"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","机械操作")
                )
            }
        }

        if (mark=="其他"){
            sendrequesturl("http://39.104.27.39:8995/otherdata?page=1","re")
            tl_head.title = "其他"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","其他")
                )
            }
        }

        refreshLayout.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                if(mark=="建园") {
                    sendrequesturl("http://39.104.27.39:8995/buildydata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "建园")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }
                if(mark=="整型修剪") {
                    sendrequesturl("http://39.104.27.39:8995/winjiandata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "整型修剪")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }
                if(mark=="机械操作") {
                    sendrequesturl("http://39.104.27.39:8995/machinedata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "机械操作")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }
                if(mark=="其他") {
                    sendrequesturl("http://39.104.27.39:8995/otherdata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "其他")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }

                refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败


            }
        })

        refreshLayout.setOnLoadmoreListener(object : OnLoadmoreListener {

            override fun onLoadmore(refreshlayout: RefreshLayout) {

                if(mark=="建园") {
                    sendrequesturl("http://39.104.27.39:8995/buildydata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "建园")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }
                if(mark=="整型修剪") {
                    sendrequesturl("http://39.104.27.39:8995/winjiandata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "整型修剪")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }
                if(mark=="机械操作") {
                    sendrequesturl("http://39.104.27.39:8995/machinedata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "机械操作")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }
                if(mark=="其他") {
                    sendrequesturl("http://39.104.27.39:8995/otherdata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "其他")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }







            }
        })


        tl_head.setNavigationOnClickListener{finish()}
    }


}

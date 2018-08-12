package com.example.czd.kotlinone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.czd.bean.TitleList
import com.example.czd.kotlinone.adapter.TitleListAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_sub_page.*
import org.jetbrains.anko.startActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class SubPageActivity : AppCompatActivity() {

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

    var mark:String = ""
    var page = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_page)

        tl_head.setNavigationIcon(R.drawable.back_32)
        tl_head.setBackgroundResource(R.color.colorPrimaryDark)

        var bundle = intent.extras
        mark = bundle.getString("kind");

        if (mark=="早熟品种介绍"){
            sendrequesturl("http://39.104.27.39:8995/zsdata?page=1","re")
            tl_head.title = "早熟品种介绍"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","早熟品种介绍")
                )
            }
        }
        if (mark=="中熟品种介绍"){
            sendrequesturl("http://39.104.27.39:8995/zhongshudata?page=1","re")
            tl_head.title = "中熟品种介绍"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","中熟品种介绍")
                )
            }
        }
        if (mark=="晚熟品种介绍"){
            sendrequesturl("http://39.104.27.39:8995/wsdata?page=1","re")
            tl_head.title = "晚熟品种介绍"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","晚熟品种介绍")
                )
            }
        }
        //土壤
        if (mark=="土壤"){
            sendrequesturl("http://39.104.27.39:8995/earthdata?page=1","re")
            tl_head.title = "土壤"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","土壤")
                )
            }
        }

        //施肥
        if (mark=="施肥"){
            sendrequesturl("http://39.104.27.39:8995/shifeidata?page=1","re")
            tl_head.title = "施肥"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","施肥")
                )
            }
        }

        //灌水
        if (mark=="灌水"){
            sendrequesturl("http://39.104.27.39:8995/waterdata?page=1","re")
            tl_head.title = "灌水"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","灌水")
                )
            }
        }

        //花期
        if (mark=="花期"){
            sendrequesturl("http://39.104.27.39:8995/blotimedata?page=1","re")
            tl_head.title = "花期"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","花期")
                )
            }
        }
        //套袋
        if (mark=="套袋"){
            sendrequesturl("http://39.104.27.39:8995/tiedata?page=1","re")
            tl_head.title = "套袋"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","套袋")
                )
            }
        }
        //解袋
        if (mark=="解袋"){
            sendrequesturl("http://39.104.27.39:8995/untiedata?page=1","re")
            tl_head.title = "解袋"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","解袋")
                )
            }
        }
        //采收
        if (mark=="采收"){
            sendrequesturl("http://39.104.27.39:8995/collectdata?page=1","re")
            tl_head.title = "采收"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","采收")
                )
            }
        }
        //病害
        if (mark=="病害"){
            sendrequesturl("http://39.104.27.39:8995/bingdata?page=1","re")
            tl_head.title = "病害"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","病害")
                )
            }
        }
        //虫害
        if (mark=="虫害"){
            sendrequesturl("http://39.104.27.39:8995/chongdata?page=1","re")
            tl_head.title = "虫害"
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                startActivity<ShowActivity>(
                        Pair("pid",titleList.pid.toString()),
                        Pair("kind","虫害")
                )
            }
        }


        refreshLayout.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                if(mark=="早熟品种介绍") {
                    sendrequesturl("http://39.104.27.39:8995/zsdata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "早熟品种介绍")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }

                if(mark=="中熟品种介绍") {
                    sendrequesturl("http://39.104.27.39:8995/zhongshudata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "中熟品种介绍")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }

                if(mark=="晚熟品种介绍") {
                    sendrequesturl("http://39.104.27.39:8995/wsdata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "晚熟品种介绍")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }

                //土壤
                if(mark=="土壤") {
                    sendrequesturl("http://39.104.27.39:8995/earthdata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "土壤")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }
                //施肥
                if(mark=="施肥") {
                    sendrequesturl("http://39.104.27.39:8995/shifeidata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "施肥")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }
                //灌水
                if(mark=="灌水") {
                    sendrequesturl("http://39.104.27.39:8995/waterdata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "灌水")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }
                //花期
                if(mark=="花期") {
                    sendrequesturl("http://39.104.27.39:8995/blotimedata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "花期")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }
                //套袋
                if(mark=="套袋") {
                    sendrequesturl("http://39.104.27.39:8995/tiedata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "套袋")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }
                //解袋
                if(mark=="解袋") {
                    sendrequesturl("http://39.104.27.39:8995/untiedata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "解袋")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }
                //采收
                if(mark=="采收") {
                    sendrequesturl("http://39.104.27.39:8995/collectdata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "采收")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }
                //病害
                if(mark=="病害") {
                    sendrequesturl("http://39.104.27.39:8995/bingdata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "病害")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }
                //虫害
                if(mark=="虫害") {
                    sendrequesturl("http://39.104.27.39:8995/chongdata?page=1", "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "虫害")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
                    page = 2
                }



            }
        })

        refreshLayout.setOnLoadmoreListener(object : OnLoadmoreListener {

            override fun onLoadmore(refreshlayout: RefreshLayout) {

                if(mark=="早熟品种介绍") {
                    sendrequesturl("http://39.104.27.39:8995/zsdata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "早熟品种介绍")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }

                if(mark=="中熟品种介绍") {
                    sendrequesturl("http://39.104.27.39:8995/zhongshudata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "中熟品种介绍")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }

                if(mark=="晚熟品种介绍") {
                    sendrequesturl("http://39.104.27.39:8995/wsdata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "晚熟品种介绍")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }

                //土壤
                if(mark=="土壤") {
                    sendrequesturl("http://39.104.27.39:8995/earthdata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "土壤")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }
                //施肥
                if(mark=="施肥") {
                    sendrequesturl("http://39.104.27.39:8995/shifeidata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "施肥")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }
                //灌水
                if(mark=="灌水") {
                    sendrequesturl("http://39.104.27.39:8995/waterdata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "灌水")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }
                //花期
                if(mark=="花期") {
                    sendrequesturl("http://39.104.27.39:8995/blotimedata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "花期")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }
                //套袋
                if(mark=="套袋") {
                    sendrequesturl("http://39.104.27.39:8995/tiedata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "套袋")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }
                //解袋
                if(mark=="解袋") {
                    sendrequesturl("http://39.104.27.39:8995/untiedata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "解袋")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }
                //采收
                if(mark=="采收") {
                    sendrequesturl("http://39.104.27.39:8995/collectdata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "采收")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }
                //病害
                if(mark=="病害") {
                    sendrequesturl("http://39.104.27.39:8995/bingdata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "病害")
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(true);
                    refreshlayout.finishLoadmore()
                    page += 1
                    //refreshlayout.finishLoadmore(1000/*,false*/)//传入false表示加载失败
                }
                //虫害
                if(mark=="虫害") {
                    sendrequesturl("http://39.104.27.39:8995/chongdata?page="+page, "mo")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowActivity>(
                                Pair("pid", titleList.pid.toString()),
                                Pair("kind", "虫害")
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

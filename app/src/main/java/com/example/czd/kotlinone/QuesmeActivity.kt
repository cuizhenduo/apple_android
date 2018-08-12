package com.example.czd.kotlinone

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.czd.bean.TitleList
import com.example.czd.kotlinone.adapter.TitleListAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_quesme.*
import org.jetbrains.anko.startActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class QuesmeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quesme)

        tl_head.setNavigationIcon(R.drawable.back_32)
        tl_head.setBackgroundResource(R.color.colorPrimaryDark)

        //读取sharedpreferencce
        val preferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val name = preferences.getString("username", "moren")
        val shenfen = preferences.getInt("shenfen", 0)
        sendrequesturl("http://39.104.27.39:8995/profqnum/?pname="+name,"re")
        list_view.setOnItemClickListener { parent, view, position, id ->
            val titleList = titleLists[position]
            startActivity<ShowQuesmeActivity>(
                    Pair("pid",titleList.pid.toString())
            )
        }
        refreshLayout.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {

                    sendrequesturl("http://39.104.27.39:8995/profqnum/?pname="+name, "re")
                    list_view.setOnItemClickListener { parent, view, position, id ->
                        val titleList = titleLists[position]
                        startActivity<ShowQuesmeActivity>(
                                Pair("pid",titleList.pid.toString())
                        )
                    }
                    list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    list_view.setStackFromBottom(false);
                    refreshlayout.finishRefresh(1000/*,false*/)//传入false表示刷新失败
            }
        })

        tl_head.setNavigationOnClickListener{finish()}
    }
    internal var titleLists: MutableList<TitleList> = ArrayList()

    fun parseJson(jsdata: String,kind: String) {
        try {

            titleLists.clear()


            val jsonObject = JSONObject(jsdata)
            val jsonArray = jsonObject.getJSONArray("qn")
            for (i in 0 until jsonArray.length()) {
                val jsonObject1 = jsonArray.getJSONObject(i)
                val pid = jsonObject1.getInt("num")
                val tit = jsonObject1.getString("tit")
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
                showResponse(response.toString(),kind)

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
}

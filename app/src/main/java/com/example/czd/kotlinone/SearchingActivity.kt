package com.example.czd.kotlinone


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.czd.bean.TitleList
import com.example.czd.kotlinone.adapter.TitleListAdapter
import kotlinx.android.synthetic.main.activity_searching.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.util.ArrayList

class SearchingActivity : AppCompatActivity() {
    internal var titleLists: MutableList<TitleList> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searching)

        tl_head.setNavigationIcon(R.drawable.back_32)
        tl_head.setBackgroundResource(R.color.colorPrimaryDark)
        tl_head.title = "智慧苹果"

        stsea.setOnClickListener {
            var seacon = seaobj.text
            var requestBody: RequestBody = FormBody.Builder().add(
                    "kwd",
                    seacon.toString()
            ).build()
            postdata(requestBody,"http://39.104.27.39:8995/sea/")
            list_view.setOnItemClickListener { parent, view, position, id ->
                val titleList = titleLists[position]
                var mo:Int = titleList.title.toString().indexOf('-')
                var objcon:String = titleList.title.toString().substring(0, mo)
                var str2 = ""
                if (objcon != null && "" != objcon) {
                    for (i in 0 until objcon.length) {
                        if (objcon[i] >= '0' && objcon[i] <= '9') {
                            str2 += objcon[i]
                        }
                    }

                }
                var kind = objcon.substring(str2.length,mo)
                startActivity<ShowActivity>(
                        Pair("pid",str2.toInt().toString()),
                        Pair("kind",kind)
                )
            }
        }

        tl_head.setNavigationOnClickListener{finish()}
    }

    fun postdata(requestBody: RequestBody, ourl:String) {

        Thread(Runnable {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(ourl).post(requestBody).build()
                val response = client.newCall(request).execute()
                val respdata = response.body().string()
                showResponse(respdata)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }
    fun showResponse(response: String) {
        runOnUiThread {

            parseJson(response)
        }
    }

    fun parseJson(jsdata: String) {
        try {
            val jsonObject = JSONObject(jsdata)
            val jsonArray = jsonObject.getJSONArray("seares")
            println(jsonArray)
            for (i in 0 until jsonArray.length()) {
                /*val jsonObject1 = jsonArray.getJSONObject(i)
                println(jsonObject1)
                val tit = jsonObject1.toString()*/
                println(jsonArray[i])
                val titleList = TitleList(i, jsonArray[i].toString())
                titleLists.add(titleList)
            }
            //适配显示listview
            val titleListAdapter = TitleListAdapter(this, R.layout.title_item, titleLists)
            list_view.setAdapter(titleListAdapter)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

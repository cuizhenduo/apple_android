package com.example.czd.kotlinone


import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebViewClient
import com.example.czd.util.SQLiteDbHelper
import kotlinx.android.synthetic.main.activity_show.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
//import javax.swing.UIManager.put
import android.content.ContentValues



class ShowActivity : AppCompatActivity() {

    var surl:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)


        tl_head.setNavigationIcon(R.drawable.back_32)
        tl_head.setBackgroundResource(R.color.gray)
        tl_head.title = "智慧苹果"

        var bundle = intent.extras
        val id = bundle.getString("pid");
        val kind = bundle.getString("kind");
        when(kind){
            "建园" -> surl = "http://39.104.27.39:8995/buildyread/?num=" + id
            "整型修剪" -> surl = "http://39.104.27.39:8995/winjianread/?num=" + id
            "机械操作" -> surl = "http://39.104.27.39:8995/machineread/?num=" + id
            "早熟品种介绍" -> surl = "http://39.104.27.39:8995/zsread/?num=" + id
            "中熟品种介绍" -> surl = "http://39.104.27.39:8995/zhongshuread/?num=" + id
            "晚熟品种介绍" -> surl = "http://39.104.27.39:8995/wsread/?num=" + id
            "土壤" -> surl = "http://39.104.27.39:8995/earthread/?num=" + id
            "施肥" -> surl = "http://39.104.27.39:8995/shifeiread/?num=" + id
            "灌水" -> surl = "http://39.104.27.39:8995/waterread/?num=" + id
            "花期" -> surl = "http://39.104.27.39:8995/blotimeread/?num=" + id
            "套袋" -> surl = "http://39.104.27.39:8995/tieread/?num=" + id
            "解袋" -> surl = "http://39.104.27.39:8995/untieread/?num=" + id
            "采收" -> surl = "http://39.104.27.39:8995/collectread/?num=" + id
            "病害" -> surl = "http://39.104.27.39:8995/bingread/?num=" + id
            "虫害" -> surl = "http://39.104.27.39:8995/chongread/?num=" + id
            "新闻" -> surl = "http://39.104.27.39:8995/readnews/?num=" + id


        }
        h5.getSettings().setBuiltInZoomControls(false)
        h5.settings.javaScriptEnabled = true
        h5.setWebViewClient(WebViewClient())
        h5.loadUrl("file:///android_asset/test.html")

        h5.addJavascriptInterface(JavaScriptObject(),"kotjs")
        tl_head.setNavigationOnClickListener{finish()}
    }

    inner class JavaScriptObject {
        @JavascriptInterface
        fun sendrequesturl() {
            Thread(Runnable {
                var connection: HttpURLConnection? = null
                var reader: BufferedReader? = null
                try {
                    val url = URL(surl)
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
                    showResponse(response.toString())
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

        fun showResponse(response: String) {
            runOnUiThread {
                val jsonObject = JSONObject(response)
                val retit = jsonObject.getString("title")

                val dbHelper = SQLiteDbHelper(this@ShowActivity)
                val db = dbHelper.writableDatabase
                val values = ContentValues()
                values.put("title", retit)
                values.put("url", surl)
                db.insert("ourl", null, values)
                h5.evaluateJavascript("s('"+surl+"')", ValueCallback {})
            }
        }


    }


}

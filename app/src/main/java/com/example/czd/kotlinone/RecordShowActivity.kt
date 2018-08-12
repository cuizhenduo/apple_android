package com.example.czd.kotlinone

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebViewClient
import com.example.czd.util.SQLiteDbHelper
import kotlinx.android.synthetic.main.activity_record_show.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class RecordShowActivity : AppCompatActivity() {
    var surl:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_show)

        tl_head.setNavigationIcon(R.drawable.back_32)
        tl_head.setBackgroundResource(R.color.gray)
        tl_head.title = "智慧苹果"

        var bundle = intent.extras
        surl = bundle.getString("surl");
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

                h5.evaluateJavascript("s('"+surl+"')", ValueCallback {})
            }
        }


    }
}

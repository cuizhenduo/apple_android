package com.example.czd.kotlinone

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_show_tie.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ShowTieActivity : AppCompatActivity() {
    var surl:String = ""
    var hurl:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_tie)

        tl_head.setNavigationIcon(R.drawable.back_32)
        tl_head.setBackgroundResource(R.color.gray)
        tl_head.title = "苹果矮密栽培技术"

        val preferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val name = preferences.getString("username", "moren")
        var bundle = intent.extras
        val id = bundle.getString("pid");
        surl = "http://39.104.27.39:8995/ltread/?num="+id
        hurl = "http://39.104.27.39:8995/huifuread/?num="+id
        h5.getSettings().setBuiltInZoomControls(false)
        h5.settings.javaScriptEnabled = true
        h5.setWebViewClient(WebViewClient())


        h5.addJavascriptInterface(JavaScriptObject(),"kotjs")

        sthf.setOnClickListener {
            if(name!="moren") {
                startActivity<HuiFuActivity>(Pair("id", id))
            }else{
                toast("您还未登录")
            }

        }

        tl_head.setNavigationOnClickListener{finish()}
    }

    override fun onResume() {
        super.onResume()
        h5.loadUrl("file:///android_asset/tie.html")
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
                h5.evaluateJavascript("d('"+hurl+"')", ValueCallback {})
            }
        }


    }
}

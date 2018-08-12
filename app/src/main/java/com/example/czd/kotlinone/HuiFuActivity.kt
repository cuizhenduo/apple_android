package com.example.czd.kotlinone

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_hui_fu.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast



class HuiFuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hui_fu)

        tl_head.setNavigationIcon(R.drawable.back_32)
        tl_head.setBackgroundResource(R.color.gray)
        tl_head.title = "智慧苹果"

        var bundle = intent.extras
        val id = bundle.getString("id");
        val preferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val name = preferences.getString("username", "moren")

        h5.getSettings().setBuiltInZoomControls(false)
        h5.settings.javaScriptEnabled = true
        h5.setWebViewClient(WebViewClient())
        h5.loadUrl("file:///android_asset/huifu.html")
        h5.addJavascriptInterface(JavaScriptObject(),"kotjs")

        fbhf.setOnClickListener {
            h5.evaluateJavascript("s('"+id+"','"+name+"')", ValueCallback {})
            toast("发表成功")
            finish()
        }

        tl_head.setNavigationOnClickListener{finish()}
    }
    inner class JavaScriptObject {
        @JavascriptInterface
        fun tishi() {
            toast("发表成功")
        }




    }

}

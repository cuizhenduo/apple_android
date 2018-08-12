package com.example.czd.kotlinone

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_fatie.*
import org.jetbrains.anko.toast

class FatieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fatie)

        val preferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val name = preferences.getString("username", "moren")

        h5.getSettings().setBuiltInZoomControls(false)
        h5.settings.javaScriptEnabled = true
        h5.setWebViewClient(WebViewClient())
        h5.loadUrl("file:///android_asset/fatie.html")

        fbt.setOnClickListener {
            h5.evaluateJavascript("submittz('"+name+"')", ValueCallback {})
            toast("发表成功")
            finish()
        }
    }
}

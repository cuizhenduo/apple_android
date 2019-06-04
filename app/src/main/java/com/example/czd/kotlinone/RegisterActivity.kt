package com.example.czd.kotlinone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tl_head.setNavigationIcon(R.drawable.back_32)
        tl_head.setBackgroundResource(R.color.gray)
        tl_head.title = "苹果矮密栽培技术"

        submit_reg.setOnClickListener {
            submit_reg.setEnabled(false)
            val username = username.text
            val passwd = passwd.text

            var requestBody: RequestBody = FormBody.Builder().add("username", username.toString()).add("password", encode(passwd.toString())).build()
            postdata(requestBody,"http://39.104.27.39:8995/getuser/",username.toString())
        }
        tl_head.setNavigationOnClickListener{finish()}
    }
    fun postdata(requestBody: RequestBody,ourl:String,username:String) {

        Thread(Runnable {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(ourl).post(requestBody).build()
                val response = client.newCall(request).execute()
                val respdata = response.body().string()
                showResponse(respdata,username)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }
    fun showResponse(response: String,username: String) {
        runOnUiThread {

            if (response == "ok"){
                var editor: SharedPreferences.Editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
                editor.putString("username",username)
                editor.putInt("shenfen",0)
                editor.apply()
                toast("注册成功")
                /*//startActivity<MainActivity>()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this.finish()
                this.overridePendingTransition(0, 0)*/
                this.finish();

            }
            if (response == "had"){
                toast("用户名已存在")
                submit_reg.setEnabled(true)
            }
        }
    }
    fun encode(text: String): String {
        try {
            //获取md5加密对象
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            //对字符串加密，返回字节数组
            val digest:ByteArray = instance.digest(text.toByteArray())
            var sb : StringBuffer = StringBuffer()
            for (b in digest) {
                //获取低八位有效值
                var i :Int = b.toInt() and 0xff
                //将整数转化为16进制
                var hexString = Integer.toHexString(i)
                if (hexString.length < 2) {
                    //如果是一位的话，补0
                    hexString = "0" + hexString
                }
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }
}

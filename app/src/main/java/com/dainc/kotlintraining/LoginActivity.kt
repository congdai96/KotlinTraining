package com.dainc.kotlintraining

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dainc.kotlintraining.constant.SystemConstant
import com.dainc.kotlintraining.ipconfig.IPConfig

import org.json.JSONException
import org.json.JSONObject

import java.util.HashMap

class LoginActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var edtUserId: EditText
    private lateinit var edtPassword: EditText
    private lateinit var queue: RequestQueue

    private var share: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.title = "ログイン"

        btnLogin = findViewById<View>(R.id.btnLogin) as Button
        edtUserId = findViewById<View>(R.id.etUserId) as EditText
        edtPassword = findViewById<View>(R.id.etPassword) as EditText

        share = getSharedPreferences("login", Context.MODE_PRIVATE)

        btnLogin.setOnClickListener {
            editor = share!!.edit()
            editor!!.putString(SystemConstant.SHARE_USER_ID, edtUserId.text.toString())
            editor!!.putString(SystemConstant.SHARE_PASSWORD, edtPassword.text.toString())
            checkLogin(IPConfig.CHECK_LOGIN)
        }

    }

    private fun checkLogin(url: String) {
        queue = Volley.newRequestQueue(this)

        val getRequest = object : StringRequest(Request.Method.POST, url,
            object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    var `object`: JSONObject? = null
                    try {
                        `object` = JSONObject(response)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    if (`object` == null) return
                    try {
                        val status = `object`.getString("status")
                        if (status == "login_false") {
                            makeToask("ログイン失敗", Color.RED)
                            return
                        }
                        editor!!.putString(SystemConstant.SHARE_TOKEN, `object`.getString("token"))
                        editor!!.putString(SystemConstant.SHARE_USER_NAME, `object`.getString("userName"))
                        editor!!.commit()
                        makeToask("ログインできた、よろこそ" + share?.getString(SystemConstant.SHARE_USER_NAME, "") + "さん",Color.BLUE)

                        val i = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(i)
                        finish()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            },
            Response.ErrorListener { error ->
                Log.d("ERROR", "Error => $error")
                Toast.makeText(this@LoginActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["userId"] = edtUserId.text.toString()
                params["password"] = edtPassword.text.toString()

                return params
            }

        }

        queue.add(getRequest)
    }

    private fun makeToask(text: String, color: Int) {
        val toast: Toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        toast.setText(text)
        val v: TextView  = toast.view.findViewById(android.R.id.message)
        v.setTextColor(color)
        v.textSize = 16F
        toast.show()
    }
}

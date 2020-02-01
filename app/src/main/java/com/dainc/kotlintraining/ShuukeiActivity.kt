package com.dainc.kotlintraining

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dainc.kotlintraining.adapter.ListShuukeiAdapter
import com.dainc.kotlintraining.constant.SystemConstant
import com.dainc.kotlintraining.ipconfig.IPConfig
import com.dainc.kotlintraining.model.ShuukeiModel
import org.json.JSONArray
import org.json.JSONException
import java.util.*


class ShuukeiActivity : AppCompatActivity() {
    private var share: SharedPreferences? = null
    private var adapter: ListShuukeiAdapter? = null
    private var lview: ListView? = null
    private var shuukeiList: ArrayList<ShuukeiModel>? = null

    private var btnBack: Button? = null
    private var btnReset: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shuukei)
        supportActionBar!!.title = "役職別集計"

        btnBack = findViewById(R.id.btnBack)
        btnReset = findViewById(R.id.btnReset)

        share = getSharedPreferences("login", Context.MODE_PRIVATE)

        shuukeiList = ArrayList()
        lview = findViewById(R.id.listviewShuukei)
        adapter = ListShuukeiAdapter(this, shuukeiList!!)
        lview!!.adapter = adapter
        getListShuukei(IPConfig.GET_LIST_SHUUKEI)

        btnReset!!.setOnClickListener {
            shuukeiList!!.clear()
            getListShuukei(IPConfig.GET_LIST_SHUUKEI)
            makeToask("最新データが更新されました。", Color.BLUE)
        }

        btnBack!!.setOnClickListener {
            val i = Intent(this@ShuukeiActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }

    }

    private fun getListShuukei(url: String) {

        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                var `object`: JSONArray? = null
                try {
                    `object` = JSONArray(response)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    makeToask("セッションの有効期限が切れている。",Color.RED)
                    val i = Intent(this@ShuukeiActivity, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }

                if (`object` == null) {
                    adapter!!.notifyDataSetChanged()
                    return@Listener
                }
                for (i in 0 until `object`.length()) {
                    try {
                        val obj = `object`.getJSONObject(i)
                        val roleName = obj.getString("roleName")
                        val male = obj.getInt("male")
                        val female = obj.getInt("female")
                        val ageMax19 = obj.getInt("ageMax19")
                        val ageMin20 = obj.getInt("ageMin20")
                        val notFull = obj.getInt("notFull")
                        val notAge = obj.getInt("notAge")

                        val shuukeiModel = ShuukeiModel(roleName, male, female, ageMax19, ageMin20, notFull, notAge)

                        shuukeiList!!.add(shuukeiModel)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
                adapter!!.notifyDataSetChanged()
            },
            Response.ErrorListener { Toast.makeText(this@ShuukeiActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show() }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + share!!.getString(SystemConstant.SHARE_TOKEN, "")!!
                return headers
            }

        }
        requestQueue.add(stringRequest)
    }


    private fun makeToask(text: String, color: Int) {
        val inflater = layoutInflater
        val layout = inflater.inflate(
            R.layout.custom_toast,
            findViewById(R.id.custom_toast_container)
        )

        val textView = layout.findViewById(R.id.text) as TextView
        textView.setTextColor(color)
        textView.text = text

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

}


package com.dainc.kotlintraining

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dainc.kotlintraining.constant.SystemConstant
import com.dainc.kotlintraining.ipconfig.IPConfig
import com.dainc.kotlintraining.model.UserModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class DeleteActivity : AppCompatActivity() {

    private var tvUserId: TextView? = null
    private var tvUserName: TextView? = null
    private var btnBack: Button? = null
    private var btnDelete: Button? = null

    private var share: SharedPreferences? = null
    private var selectedUser: UserModel? = null

    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)
        supportActionBar!!.title = "削除"

        val intent = intent
        selectedUser = intent.getSerializableExtra("selectedUser") as UserModel
        share = getSharedPreferences("login", Context.MODE_PRIVATE)

        tvUserId = findViewById<View>(R.id.tvUserId) as TextView
        tvUserName = findViewById<View>(R.id.tvUserName) as TextView
        btnBack = findViewById<View>(R.id.btnBack) as Button
        btnDelete = findViewById<View>(R.id.btnDelete) as Button

        tvUserId!!.text = selectedUser!!.getUserId()
        tvUserName!!.text = selectedUser!!.getFamilyName().toString() + " " + selectedUser!!.getFirstName().toString()

        btnBack!!.setOnClickListener { finish() }

        btnDelete!!.setOnClickListener {
            AlertDialog.Builder(this@DeleteActivity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(Html.fromHtml("<font color='#F70008'>確認</font>"))
                .setMessage("削除してよろしいですか?")
                .setPositiveButton(
                    "OK"
                ) { dialog, which -> delete(IPConfig.DELETE_USER) }
                .setNegativeButton("キャンセル", null)
                .show()
        }
    }

    private fun delete(url: String) {
        queue = Volley.newRequestQueue(this)
        val getRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                var `object`: JSONObject? = null
                try {
                    `object` = JSONObject(response)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    makeToask("セッションの有効期限が切れている。",Color.RED)
                    val i = Intent(this@DeleteActivity, LoginActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                    finish()
                }

                if (`object` == null) return@Listener
                try {
                    val status = `object`.getString("status")
                    if (status == "not_haved") {
                        makeToask("指定したユーザーが存在しません。", Color.RED)
                        val intent = Intent(this@DeleteActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else if (status == "delete_success") {
                        makeToask("削除できた。", Color.BLUE)
                        var intent = Intent(this@DeleteActivity, DeleteSuccessActivity::class.java)
                        if (selectedUser!!.getUserId().equals(share!!.getString(SystemConstant.SHARE_USER_ID, ""))) {
                            intent = Intent(this@DeleteActivity, LoginActivity::class.java)
                        }
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.d("ERROR", "Error => $error")
                Toast.makeText(this@DeleteActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["userId"] = selectedUser!!.getUserId().toString()
                return params
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + share!!.getString(SystemConstant.SHARE_TOKEN, "")!!
                return headers
            }

        }

        queue.add(getRequest)
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

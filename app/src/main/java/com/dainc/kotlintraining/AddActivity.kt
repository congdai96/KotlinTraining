package com.dainc.kotlintraining

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dainc.kotlintraining.constant.SystemConstant
import com.dainc.kotlintraining.ipconfig.IPConfig
import com.dainc.kotlintraining.model.GenderModel
import com.dainc.kotlintraining.model.RoleModel
import com.dainc.kotlintraining.model.UserModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern

class AddActivity : AppCompatActivity() {

    private var edtUserId: EditText? = null
    private var edtPassword: EditText? = null
    private var edtFamilyName: EditText? = null
    private var edtFirstName: EditText? = null
    private var edtAge: EditText? = null
    private var spGender: Spinner? = null
    private var spRole: Spinner? = null
    private var cbAdmin: CheckBox? = null

    private var btnAdd: Button? = null
    private var btnBack: Button? = null

    private var roleList: ArrayList<RoleModel>? = null
    private var genderList: ArrayList<GenderModel>? = null
    private val selectedUser: UserModel? = null
    private var share: SharedPreferences? = null
    internal var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar!!.title = "登録"

        edtUserId = findViewById(R.id.edtUserId)
        edtPassword = findViewById(R.id.edtPassword)
        edtFamilyName = findViewById(R.id.edtFamilyName)
        edtFirstName = findViewById(R.id.edtFirstName)
        edtAge = findViewById(R.id.edtAge)
        spGender = findViewById(R.id.spGender)
        spRole = findViewById(R.id.spRole)
        cbAdmin = findViewById(R.id.cbAdmin)

        btnAdd = findViewById(R.id.btnAdd)
        btnBack = findViewById(R.id.btnBack)

        val intent = intent
        roleList = intent.getSerializableExtra("roleList") as ArrayList<RoleModel>
        val dataAdapter = ArrayAdapter<RoleModel>(
            this,
            android.R.layout.simple_spinner_item, roleList!!
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spRole!!.adapter = dataAdapter

        share = getSharedPreferences("login", Context.MODE_PRIVATE)
        genderList = ArrayList()
        getListGender(IPConfig.GET_LIST_GENDER)

        btnBack!!.setOnClickListener {
            val i = Intent(this@AddActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        btnAdd!!.setOnClickListener(View.OnClickListener {
            if (!checkValidate()) {
                return@OnClickListener
            }
            AlertDialog.Builder(this@AddActivity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(Html.fromHtml("<font color='#3B3BFD'>確認</font>"))
                .setMessage("登録してよろしいですか?")
                .setPositiveButton(
                    "OK"
                ) { dialog, which -> addUser(IPConfig.GET_INF_USER) }
                .setNegativeButton("キャンセル", null)
                .show()
        })


    }

    private fun getListGender(url: String) {

        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                var `object`: JSONArray? = null
                try {
                    `object` = JSONArray(response)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    makeToask("セッションの有効期限が切れている。",Color.RED)
                    val i = Intent(this@AddActivity, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }

                if (`object` == null) return@Listener
                for (i in 0 until `object`.length()) {
                    try {
                        val obj = `object`.getJSONObject(i)
                        val genderId = obj.getInt("genderId")
                        val genderName = obj.getString("genderName")
                        val genderModel = GenderModel(genderId, genderName)
                        genderList!!.add(genderModel)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            },
            Response.ErrorListener { Toast.makeText(this@AddActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show() }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + share!!.getString(SystemConstant.SHARE_TOKEN, "")!!
                return headers
            }
        }
        requestQueue.add(stringRequest)
        genderList!!.add(GenderModel(0, ""))
        val dataAdapter2 = ArrayAdapter<GenderModel>(
            this,
            android.R.layout.simple_spinner_item, genderList!!
        )
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spGender!!.adapter = dataAdapter2

    }

    private fun addUser(url: String) {

        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                var `object`: JSONObject? = null
                try {
                    `object` = JSONObject(response)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    makeToask("セッションの有効期限が切れている。",Color.RED)
                    val i = Intent(this@AddActivity, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }

                if (`object` == null) return@Listener
                try {
                    val status = `object`.getString("status")
                    if (status == "add_success") {
                        makeToask("登録できた。", Color.BLUE)
                        val intent = Intent(this@AddActivity, AddSuccessActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (status == "user_haved") {
                        makeToask("ユーザーIDが保存している。", Color.RED)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { Toast.makeText(this@AddActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show() }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + share!!.getString(SystemConstant.SHARE_TOKEN, "")!!
                return headers
            }

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["userId"] = edtUserId!!.text.toString()
                params["familyName"] = edtFamilyName!!.text.toString()
                params["firstName"] = edtFirstName!!.text.toString()
                params["age"] = edtAge!!.text.toString()
                params["password"] = edtPassword!!.text.toString()
                val roleModel = spRole!!.selectedItem as RoleModel
                val genderModel = spGender!!.selectedItem as GenderModel
                params["genderId"] = genderModel.getGenderId().toString()
                params["authorityId"] = roleModel.getAuthorityId().toString()
                if (cbAdmin!!.isChecked) {
                    params["admin"] = 1.toString()
                } else
                    params["admin"] = 0.toString()
                return params
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

    private fun checkValidate(): Boolean {
        val pHiragana = "[\\u3041-\\u3096\\u309D-\\u309F]|\\uD82C\\uDC01|\\uD83C\\uDE00"
        val pKatakana =
            "[\\u30A1-\\u30FA\\u30FD-\\u30FF\\u31F0-\\u31FF\\u32D0-\\u32FE\\u3300-\\u3357\\uFF66-\\uFF6F\\uFF71-\\uFF9D]|\\uD82C\\uDC00"
        val pHan =
            "[\\u2E80-\\u2E99\\u2E9B-\\u2EF3\\u2F00-\\u2FD5\\u3005\\u3007\\u3021-\\u3029\\u3038-\\u303B\\u3400-\\u4DB5\\u4E00-\\u9FD5\\uF900-\\uFA6D\\uFA70-\\uFAD9]|[\\uD840-\\uD868\\uD86A-\\uD86C\\uD86F-\\uD872][\\uDC00-\\uDFFF]|\\uD869[\\uDC00-\\uDED6\\uDF00-\\uDFFF]|\\uD86D[\\uDC00-\\uDF34\\uDF40-\\uDFFF]|\\uD86E[\\uDC00-\\uDC1D\\uDC20-\\uDFFF]|\\uD873[\\uDC00-\\uDEA1]|\\uD87E[\\uDC00-\\uDE1D]"
        val NAME_PATTERN = "^(([a-zA-Z0-9]|$pHiragana|$pKatakana|$pHan){1,10})$"
        val USERPASS_PATTERN = "^([a-zA-Z0-9]{1,8})$"
        val AGE_PATTERN = "^([0-9]{0,})$"
        if (edtUserId!!.text.isEmpty()) {
            makeToask("ユーザーIDが未入力です。", Color.RED)
            return false
        } else if (edtUserId!!.text.length > 8) {
            makeToask("ユーザーIDは8文字以下。", Color.RED)
            return false
        } else if (!Pattern.matches(USERPASS_PATTERN, edtUserId!!.text)) {
            makeToask("IDはalphabet文字と数字だけ。", Color.RED)
            return false
        } else if (edtPassword!!.text.isEmpty()) {
            makeToask("パスワードが未入力です。", Color.RED)
            return false
        } else if (edtPassword!!.text.length > 8) {
            makeToask("パスワードは8文字以下。", Color.RED)
            return false
        } else if (!Pattern.matches(USERPASS_PATTERN, edtPassword!!.text)) {
            makeToask("パスワードはalphabet文字と数字だけ。", Color.RED)
            return false
        } else if (edtFamilyName!!.text.isEmpty()) {
            makeToask("姓がが未入力です。", Color.RED)
            return false
        } else if (edtFamilyName!!.text.length > 10) {
            makeToask("姓は10文字以下。", Color.RED)
            return false
        } else if (!Pattern.matches(NAME_PATTERN, edtFamilyName!!.text)) {
            makeToask("姓は文字と数字だけ。", Color.RED)
            return false
        } else if (edtFirstName!!.text.isEmpty()) {
            makeToask("名がが未入力です。", Color.RED)
            return false
        } else if (edtFirstName!!.text.length > 10) {
            makeToask("名は10文字以下。", Color.RED)
            return false
        } else if (!Pattern.matches(NAME_PATTERN, edtFirstName!!.text)) {
            makeToask("名は文字と数字だけ。", Color.RED)
            return false
        } else if (!Pattern.matches(AGE_PATTERN, edtAge!!.text)) {
            makeToask("年齢は整数", Color.RED)
            return false
        }
        return true
    }
}

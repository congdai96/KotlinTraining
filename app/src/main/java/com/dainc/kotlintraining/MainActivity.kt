package com.dainc.kotlintraining

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dainc.kotlintraining.adapter.ListUserAdapter
import com.dainc.kotlintraining.constant.SystemConstant
import com.dainc.kotlintraining.ipconfig.IPConfig
import com.dainc.kotlintraining.model.RoleModel
import com.dainc.kotlintraining.model.UserModel
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var share: SharedPreferences? = null

    private var adapter: ListUserAdapter? = null
    private var lview: ListView? = null
    private var userList: ArrayList<UserModel>? = null
    private var roleList: ArrayList<RoleModel>? = null
    private var spAuthority: Spinner? = null
    private var edtFamilyName: EditText? = null
    private var edtFirstName: EditText? = null
    private var btnSearch: Button? = null
    private var selectedUser: UserModel? = null
    private lateinit var queue: RequestQueue


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        share = getSharedPreferences("login", Context.MODE_PRIVATE)
        setMenuInf()

        spAuthority = findViewById(R.id.spAuthority)
        edtFamilyName = findViewById(R.id.edtFamilyName)
        edtFirstName = findViewById(R.id.edtFirstName)
        btnSearch = findViewById(R.id.btnSearch)

        roleList = ArrayList()
        userList = ArrayList()
        lview = findViewById(R.id.listview)
        adapter = ListUserAdapter(this, userList!!)
        lview!!.adapter = adapter

        getListUser(IPConfig.GET_LIST_USER_BY_SEARCH, "", "", 0)
        getListRole(IPConfig.GET_LIST_ROLE)

        lview!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val userId = (view.findViewById(R.id.product) as TextView).text.toString()
            getUserInf(IPConfig.GET_INF_USER, userId)
        }

        btnSearch!!.setOnClickListener {
            userList!!.clear()
            val roleModel = spAuthority!!.selectedItem as RoleModel
            getListUser(
                IPConfig.GET_LIST_USER_BY_SEARCH,
                edtFamilyName!!.text.toString(),
                edtFirstName!!.text.toString(),
                roleModel.getAuthorityId()
            )
            makeToask("最新データが更新されました。", Color.BLUE)
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {
                val i = Intent(this@MainActivity, AddActivity::class.java)
                i.putExtra("roleList", roleList)
                startActivity(i)
                finish()
            }
            R.id.nav_slideshow -> {
                val i = Intent(this@MainActivity, ShuukeiActivity::class.java)
                startActivity(i)
                finish()

            }
            R.id.nav_logout -> {
                share?.edit()?.clear()?.commit()
                val i = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(i)
                makeToask("ログアウトできた", Color.BLUE)
                finish()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setMenuInf() {
        val inflater = layoutInflater
        val layout = inflater.inflate(
            R.layout.nav_header_main2,
            findViewById(R.id.nav_view)
        )
        val tvUserName = layout.findViewById(R.id.userName) as TextView
        val tvUserId = layout.findViewById(R.id.userId) as TextView
        tvUserName.text = "よろこそ、" + share!!.getString(SystemConstant.SHARE_USER_NAME, "") + "さん"
        tvUserId.text = "ユーザーID: " + share!!.getString(SystemConstant.SHARE_USER_ID, "")

    }

    private fun getListUser(url: String, familyName: String, firstName: String, authorityId: Int) {

        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                var `object`: JSONArray? = null
                try {
                    `object` = JSONArray(response)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    makeToask("セッションの有効期限が切れている。",Color.RED)
                    val i = Intent(this@MainActivity, LoginActivity::class.java)
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
                        val sNo = (i + 1).toString()
                        val userId = obj.getString("userId")
                        val familyName = obj.getString("familyName")
                        val firstName = obj.getString("firstName")
                        val admin = obj.getInt("admin")
                        var authorityName = obj.getJSONObject("mstRoleModel").getString("authorityName")
                        if (authorityName == "null") authorityName = ""

                        val userModel = UserModel(sNo, userId, familyName, firstName, authorityName, admin)
                        userList!!.add(userModel)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
                adapter!!.notifyDataSetChanged()
            },
            Response.ErrorListener { Toast.makeText(this@MainActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show() }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + share!!.getString(SystemConstant.SHARE_TOKEN, "")!!
                return headers
            }

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["familyName"] = familyName
                params["firstName"] = firstName
                params["authorityId"] = authorityId.toString()

                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    private fun getListRole(url: String) {

        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                var `object`: JSONArray? = null
                try {
                    `object` = JSONArray(response)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    makeToask("セッションの有効期限が切れている。",Color.RED)
                    val i = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }

                if (`object` == null) return@Listener
                for (i in 0 until `object`.length()) {
                    try {
                        val obj = `object`.getJSONObject(i)
                        val authorityId = obj.getInt("authorityId")
                        val authorityName = obj.getString("authorityName")

                        val roleModel = RoleModel(authorityId, authorityName)
                        roleList!!.add(roleModel)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            },
            Response.ErrorListener { Toast.makeText(this@MainActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show() }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + share!!.getString(SystemConstant.SHARE_TOKEN, "")!!
                return headers
            }
        }
        requestQueue.add(stringRequest)
        roleList!!.add(RoleModel(0, ""))

        val dataAdapter = ArrayAdapter<RoleModel>(
            this,
            android.R.layout.simple_spinner_item, roleList!!
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spAuthority!!.adapter = dataAdapter

    }

    private fun getUserInf(url: String, userId: String) {
        queue = Volley.newRequestQueue(this)
        val finalUrl = "$url?userId=$userId"
        val getRequest = object : StringRequest(
            Request.Method.GET, finalUrl,
            Response.Listener { response ->
                var `object`: JSONObject? = null
                try {
                    `object` = JSONObject(response)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    makeToask("セッションの有効期限が切れている。",Color.RED)
                    val i = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }

                if (`object` == null) {
                    makeToask("指定したユーザーが存在しません。", Color.RED)
                    btnSearch!!.callOnClick()
                    return@Listener
                }
                try {
                    val userId = `object`.getString("userId")
                    val familyName = `object`.getString("familyName")
                    val firstName = `object`.getString("firstName")
                    val password = `object`.getString("password")
                    var authorityName = ""
                    var genderName = ""
                    try {
                        authorityName = `object`.getJSONObject("mstRoleModel").getString("authorityName")
                    } catch (e: JSONException) {
                    }

                    try {
                        genderName = `object`.getJSONObject("mstGenderModel").getString("genderName")
                    } catch (e: JSONException) {
                    }

                    val authorityId = `object`.getInt("authorityId")
                    val genderId = `object`.getInt("genderId")
                    val admin = `object`.getInt("admin")
                    val age = `object`.getInt("age")

                    selectedUser = UserModel(
                        userId,
                        familyName,
                        firstName,
                        age,
                        genderId,
                        authorityId,
                        admin,
                        password,
                        authorityName,
                        genderName
                    )
                    val i = Intent(this@MainActivity, FullInfActivity::class.java)
                    i.putExtra("selectedUser", selectedUser)
                    i.putExtra("roleList", roleList)
                    startActivity(i)


                } catch (e: JSONException) {
                    e.printStackTrace()
                    makeToask("null", Color.RED)
                }
            },
            Response.ErrorListener { error ->
                Log.d("ERROR", "Error => $error")
                Toast.makeText(this@MainActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show()
            }
        ) {
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
        val toast: Toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
        toast.setText(text)
        val v: TextView = toast.view.findViewById(android.R.id.message)
        v.setTextColor(color)
        v.textSize = 16F
        toast.show()
    }
}

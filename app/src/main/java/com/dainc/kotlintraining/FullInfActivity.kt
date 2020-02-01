package com.dainc.kotlintraining

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.dainc.kotlintraining.model.RoleModel
import com.dainc.kotlintraining.model.UserModel

import java.util.ArrayList

class FullInfActivity : AppCompatActivity() {

    private var tvUserId: TextView? = null
    private var tvPassword: TextView? = null
    private var tvFamilyName: TextView? = null
    private var tvFirstName: TextView? = null
    private var tvAge: TextView? = null
    private var tvGender: TextView? = null
    private var tvRole: TextView? = null
    private var tvAdmin: TextView? = null

    private var btnEdit: Button? = null
    private var btnDelete: Button? = null
    private var btnBack: Button? = null

    private val intent = null
    private var selectedUser: UserModel? = null
    private var roleList: ArrayList<RoleModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_inf)
        supportActionBar!!.title = "詳細"

        val intent = getIntent()
        selectedUser = intent.getSerializableExtra("selectedUser") as UserModel
        roleList = intent.getSerializableExtra("roleList") as ArrayList<RoleModel>

        tvUserId = findViewById(R.id.tvUserId)
        tvPassword = findViewById(R.id.tvPassword)
        tvFamilyName = findViewById(R.id.tvFamilyName)
        tvFirstName = findViewById(R.id.tvFirstName)
        tvAge = findViewById(R.id.tvAge)
        tvGender = findViewById(R.id.tvGender)
        tvRole = findViewById(R.id.tvRole)
        tvAdmin = findViewById(R.id.tvAdmin)

        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)
        btnBack = findViewById(R.id.btnBack)

        tvUserId!!.text = selectedUser!!.getUserId()
        tvPassword!!.text = selectedUser!!.getPassword()
        tvFamilyName!!.text = selectedUser!!.getFamilyName()
        tvFirstName!!.text = selectedUser!!.getFirstName()
        if (selectedUser!!.getAge() === 0)
            tvAge!!.text = ""
        else
            tvAge!!.text = (selectedUser!!.getAge()).toString()
        tvGender!!.text = selectedUser!!.getGenderName()
        tvRole!!.text = selectedUser!!.getAuthorityName()
        if (selectedUser!!.getAdmin() === 1)
            tvAdmin!!.text = "o"
        else
            tvAdmin!!.text = "x"


        btnBack!!.setOnClickListener { finish() }

        btnDelete!!.setOnClickListener {
            val i = Intent(this@FullInfActivity, DeleteActivity::class.java)
            i.putExtra("selectedUser", selectedUser)
            startActivity(i)
            finish()
        }

        btnEdit!!.setOnClickListener {
            val i = Intent(this@FullInfActivity, EditActivity::class.java)
            i.putExtra("selectedUser", selectedUser)
            i.putExtra("roleList", roleList)
            startActivity(i)
            finish()
        }
    }
}

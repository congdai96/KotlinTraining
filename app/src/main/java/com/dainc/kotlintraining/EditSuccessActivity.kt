package com.dainc.kotlintraining

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button


class EditSuccessActivity : AppCompatActivity() {
    private var btnBack: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_success)
        supportActionBar!!.title = "更新完了"

        btnBack = findViewById(R.id.btnBack)

        btnBack!!.setOnClickListener {
            val intent = Intent(this@EditSuccessActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}


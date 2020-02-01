package com.dainc.kotlintraining

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button


class AddSuccessActivity : AppCompatActivity() {
    private var btnBack: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_success)
        supportActionBar!!.title = "登録完了"

        btnBack = findViewById(R.id.btnBack)

        btnBack!!.setOnClickListener {
            val intent = Intent(this@AddSuccessActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}


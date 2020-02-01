package com.dainc.kotlintraining

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button


class DeleteSuccessActivity : AppCompatActivity() {
    private var btnBack: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_success)
        supportActionBar!!.setTitle("削除完了")

        btnBack = findViewById<View>(R.id.btnBack) as Button

        btnBack!!.setOnClickListener {
            val intent = Intent(this@DeleteSuccessActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}


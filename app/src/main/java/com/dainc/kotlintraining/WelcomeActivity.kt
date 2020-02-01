package com.dainc.kotlintraining

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity


class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        Handler().postDelayed({
            val i = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }, 1000)
    }

}


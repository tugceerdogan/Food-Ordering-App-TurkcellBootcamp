package com.example.yemeksiparisuygulamasi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_acitivity)
        val timerThread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(4000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        timerThread.start()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}



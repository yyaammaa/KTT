package com.yyaammaa.ktt

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Button

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    override fun onResume() {
        super.onResume()

        val pm: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, TAG)
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)
    }

    override fun onStop() {
        super.onStop()

        wakeLock.release()
    }

    private fun initView() {
        val button30: Button = findViewById(R.id.act_main_button_30)
        button30.setOnClickListener {
            startActivity(TimerActivity.createIntent(this, 30))
        }

        val button45: Button = findViewById(R.id.act_main_button_45)
        button45.setOnClickListener {
            startActivity(TimerActivity.createIntent(this, 45))
        }

        val button60: Button = findViewById(R.id.act_main_button_60)
        button60.setOnClickListener {
            startActivity(TimerActivity.createIntent(this, 60))
        }

        val button90: Button = findViewById(R.id.act_main_button_90)
        button90.setOnClickListener {
            startActivity(TimerActivity.createIntent(this, 90))
        }

        val button120: Button = findViewById(R.id.act_main_button_120)
        button120.setOnClickListener {
            startActivity(TimerActivity.createIntent(this, 120))
        }
    }
}

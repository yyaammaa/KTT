package com.yyaammaa.ktt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TimerActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "TimerActivity"
        private const val EXTRA_TIME = "time"

        fun createIntent(context: Context, time: Int): Intent {
            val intent = Intent(context, TimerActivity::class.java)
            intent.putExtra(EXTRA_TIME, time)
            return intent
        }
    }

    private lateinit var textView: TextView
    private lateinit var wakeLock: PowerManager.WakeLock
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        val pm: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, TAG)
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)

        val time = intent.getIntExtra(EXTRA_TIME, 0)
        Log.d(TAG, time.toString())

        initView()
        startTimer(time)
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
        wakeLock.release()
    }

    private fun initView() {
        textView = findViewById(R.id.act_timer_text)

        val button: Button = findViewById(R.id.act_timer_button)
        button.setOnClickListener { finish() }
    }

    private fun startTimer(time: Int) {
        textView.text = time.toString()

        compositeDisposable.add(
            Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { v ->
                        Log.d(TAG, v.toString())
                        textView.text = (time - v).toString()
                        if (time <= v) {
                            finish()
                        }
                    },
                    { Log.e(TAG, it.toString()) })
        )
    }
}

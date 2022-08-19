package com.Player2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.Player2.test.SoundPoolTool
import kotlinx.android.synthetic.main.activity_main.*

class TestActivity : AppCompatActivity() {


    private var soundPool: SoundPoolTool? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        soundPool = SoundPoolTool(
            soundIds = listOf(
                R.raw.select_button,
                R.raw.confirm
            )
        )
        btn1.setOnClickListener { soundPool?.playSound(R.raw.select_button) }
        btn2.setOnClickListener { soundPool?.playSound(R.raw.confirm) }
    }

    override fun onDestroy() {
        soundPool?.release()
        super.onDestroy()
    }
}
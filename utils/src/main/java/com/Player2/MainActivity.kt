package com.Player2

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSoundPool()
    }

    private var soundPool: SoundPool? = null
    private var resourcesMap: HashMap<Int, Int>? = null

    /**
    * soundID       – load() 函数返回的 soundID
    * leftVolume    – 左音量值（范围 = 0.0 到 1.0）
    * rightVolume   – 右音量值（范围 = 0.0 到 1.0）
    * priority      – 流优先级（0 = 最低优先级）
    * loop          – 循环模式（0 = 无循环，-1 = 永远循环）
    * rate          – 播放速率（1.0 = 正常播放，范围 0.5 到 2.0）
     */
    fun btn1(view: View) {
        resourcesMap?.apply {
            soundPool?.play(get(1)!!, 1f, 1f, 0, 0, 1f);
        }
    }

    /**
     *
     */
    fun btn2(view: View) {
        resourcesMap?.apply {
            soundPool?.play(get(2)!!, 1f, 1f, 0, 0, 1f);
        }
    }

    private fun initSoundPool() {
        SoundPool.Builder()
            .setMaxStreams(100)   //设置允许同时播放的流的最大值
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .build().also {
                resourcesMap = it.loadResources()
                //设置资源加载监听
                it.setOnLoadCompleteListener(MLoadCompleteListener {
                    soundPool = it
                })
            }
    }

    private fun SoundPool.loadResources(): HashMap<Int, Int> {
        //定义一个HashMap用于存放音频流的ID
        return HashMap<Int, Int>().let {
            //通过load方法加载指定音频流，并将返回的音频ID放入 map 中
            it[1] = load(this@MainActivity, R.raw.select_button, 1)
            it[2] = load(this@MainActivity, R.raw.confirm, 1)
            it
        }
    }

    class MLoadCompleteListener(private val successListene: () -> Unit) :
        SoundPool.OnLoadCompleteListener {
        /**
         *
         * @param soundPool – 来自 load() 方法的 SoundPool 对象
         * @param sampleId  – 加载的声音的样本 ID
         * @param status    – 加载操作的状态 (0 = success)
         */
        override fun onLoadComplete(sp: SoundPool?, sampleId: Int, status: Int) {
            if (status == 0) successListene.invoke()
        }
    }
}
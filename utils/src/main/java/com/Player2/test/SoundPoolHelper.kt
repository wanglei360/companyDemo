package com.Player2.test

import android.content.ContentResolver
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.media.SoundPool
import android.net.Uri
import android.provider.MediaStore
import com.Player2.App

/** * 创建者：leiwu
 * * 时间：2022/8/19 11:14
 * * 类描述：
 * * 修改人：
 * * 修改时间：
 * * 备注：因为有 load 这样的方法，所以该类不能使用延迟初始化
https://www.jianshu.com/p/526bc0d80afe?u_atoken=9a28da63-2232-4075-8532-ee1f0d559dd0&u_asession=01ZkhfyXUd9gE-AfrUNBLiXYkFzqLDXaI3lbsXZmj3LsOSlIgXnzX_OQfRUamaFnVhX0KNBwm7Lovlpxjd_P_q4JsKWYrT3W_NKPr8w6oU7K8YpAc-ssE6TsgG_Hl1keAk3TYvls7v_Epik-OyKXq1TWBkFo3NEHBv0PZUm6pbxQU&u_asig=05y-GwE0MUeUMh9hPe_EyEzZDH78_GKTeyuv1r6tbwMvPioYjE5ON4y03UuTm_Po1IcMC2rtNbuE5kInypHYAqBUkYtVyCDbUkJCgv4Gq-QoSYSgUIvYuFQpWOIn7X01bhAV_9AXrUFrwRIJNCV_06EN7aEkLgZtuLUCWu5bJUsIT9JS7q8ZD7Xtz2Ly-b0kmuyAKRFSVJkkdwVUnyHAIJzUYxA415n5g5pLS0LrQCl4RiRXJwE0nIa5lulkT81lQPyZo170oZzfjmGNwwqqFezu3h9VXwMyh6PgyDIVSG1W93NaawmUOo6PDeLxuvwd_Oj7fGLiguhfvrRmLpZQrmWmzZzA6Le7rRhcHAUzOPq1712hhzCM2QyEgcchbE0dx3mWspDxyAEEo4kbsryBKb9Q&u_aref=grqVTVfEF84NNChCa0aw8rHjW3M%3D
https://blog.csdn.net/qq_36017059/article/details/114367925
 */
class SoundPoolTool(
    //要播放的音效 id 集合
    soundIds: List<Int>,
    //设置可以同时播放的最大同时流数,因为是为音效设置的所以默认为1个
    maxStreams: Int? = 1,
    //播放速率（1.0 = 正常播放，范围 0.5 到 2.0）
    private val rate: Float? = null,
    //错误时，是否播放默认音效,手机不同，音效比较杂，最好不用
    private val isPlayDefaultSound: Boolean? = false,
    //默认音效id
    private val defaultSoundId: Int? = null
) {

    private var soundPool: SoundPool? = null
    private val soundMap by lazy { HashMap<String, Int>() }
    private val defaultName = "defaultSoundIdName"

    init {
        SoundPool.Builder()
            .setMaxStreams(maxStreams!!)// 设置允许同时播放的流的最大值
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            ).build().also {
                soundIds.forEach { id ->
                    soundMap["$id"] = it.load(App.context, id, 1)
                }
                if (isPlayDefaultSound == true) {
                    getSystemDefaultRingtoneUri().also { uri ->
                        when {
                            defaultSoundId != null -> {
                                soundMap[defaultName] = it.load(App.context, defaultSoundId, 1)
                            }
                            uri != null -> {
                                soundMap[defaultName] =
                                    it.load(uriToPath(uri), 1)
                            }
                        }
                    }
                }
                /**
                 * 设置资源加载监听
                 * @param soundPool – 来自 load() 方法的 SoundPool 对象
                 * @param sampleId  – 加载的声音的样本 ID
                 * @param status    – 加载操作的状态 (0 = success)
                 */
                it.setOnLoadCompleteListener { sp, _, status ->
                    if (status == 0) soundPool = sp
                }
            }
    }

    fun playSound(soundId: Int) {
        soundPool?.apply {
            soundMap["$soundId"].let { id ->
                id ?: if (isPlayDefaultSound == true) soundMap[defaultName]
                else null
            }?.also {
                /**
                 * soundID      – load() 函数返回的 soundID
                 * leftVolume   – 左音量值（范围 = 0.0 到 1.0）
                 * rightVolume  – 右音量值（范围 = 0.0 到 1.0）
                 * priority     – 流优先级（0 = 最低优先级）
                 * loop         – 循环模式（0 = 无循环，-1 = 永远循环）
                 * rate         – 播放速率（1.0 = 正常播放，范围 0.5 到 2.0）
                 */
                play(it, 1f, 1f, 0, 0, rate ?: 1f)
            }
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        soundPool?.apply { release() }
    }

    /**
     * 获取系统默认铃声的Uri
     * 手机不同，音效比较杂，最好不用
     * @return uri
     */
    private fun getSystemDefaultRingtoneUri(): Uri? {
        return try {
            RingtoneManager.getActualDefaultRingtoneUri(
                App.context,
                RingtoneManager.TYPE_NOTIFICATION
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 把 Uri 转变 为 真实的 String 路径
     */
    private fun uriToPath(uri: Uri): String? {
        return when (uri.scheme) {
            null -> uri.path
            ContentResolver.SCHEME_FILE -> uri.path
            ContentResolver.SCHEME_CONTENT -> {
                var path: String? = null
                App.context.contentResolver.query(
                    uri,
                    arrayOf(MediaStore.Images.ImageColumns.DATA),
                    null,
                    null,
                    null
                )?.also {
                    if (it.moveToFirst()) {
                        it.getColumnIndex(MediaStore.Images.ImageColumns.DATA).also { index ->
                            if (index > -1) path = it.getString(index)
                        }
                    }
                    it.close()
                }
                path
            }
            else -> null
        }
    }
}
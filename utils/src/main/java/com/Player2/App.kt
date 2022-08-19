package com.Player2

import android.app.Application

/** * 创建者：leiwu
 * * 时间：2022/8/19 11:25
 * * 类描述：
 * * 修改人：
 * * 修改时间：
 * * 修改备注：
 */
class App : Application() {

    companion object {
        lateinit var context: App
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}
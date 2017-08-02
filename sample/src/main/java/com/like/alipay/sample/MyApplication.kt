package com.like.alipay.sample

import com.like.base.context.BaseApplication
import com.like.retrofit.request.BaseInterceptor
import com.like.retrofit.request.IRetrofitApplication
import com.like.retrofit.request.RetrofitUtils
import com.like.retrofit.request.di.component.RetrofitAppComponent
import com.like.retrofit.request.security.aesEncrypt
import org.json.JSONObject
import java.util.*

class MyApplication : BaseApplication(), IRetrofitApplication {

    override val retrofitAppComponent: RetrofitAppComponent by lazy {
        getComponent()
    }

    override fun getApplication() = this

    override fun getScheme() = RetrofitUtils.SCHEME_HTTP // 可以去掉，默认就是它

    override fun getIp() = "www.gdep.com.cn"

    override fun getPort() = 8081

    override fun getApiClass() = Api::class.java

    override fun getInterceptor() = object : BaseInterceptor() { // 可以去掉，默认为null
        override fun getPublicParamsMap(): HashMap<String, Any>? {
            val publicParamsMap = HashMap<String, Any>()
            publicParamsMap.put("device", "0")
            publicParamsMap.put("timestamp", System.currentTimeMillis().toString() + "")
            publicParamsMap.put("token", "SpTgPUCJOMgtRVsmg9usYLXh8SbD3pxkxZ3lsLRywSc=")// 测试=是否被转义成\u003d
            return publicParamsMap
        }

        override fun getHeaderMap(): HashMap<String, String>? {
            val headerMap = HashMap<String, String>()
            headerMap.put("Cookie", "JSESSIONID=")
            return headerMap
        }

        override fun handlePostContent(content: String): String {
            val obj = JSONObject()
            try {
                obj.put("data", content.aesEncrypt())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return obj.toString()
        }
    }

}
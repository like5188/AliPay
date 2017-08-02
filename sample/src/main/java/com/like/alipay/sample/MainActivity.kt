package com.like.alipay.sample

import android.view.View
import com.like.alipay.AliPayUtils
import com.like.base.context.BaseActivity
import com.like.base.viewmodel.BaseViewModel
import com.like.logger.Logger
import com.like.retrofit.request.RetrofitUtils
import okhttp3.ResponseBody
import org.json.JSONObject
import java.util.*

class MainActivity : BaseActivity() {
    val retrofitUtils: RetrofitUtils  by lazy { RetrofitUtils(host) }

    override fun getViewModel(): BaseViewModel? {
        setContentView(R.layout.activity_main)
        return null
    }

    fun alipay(view: View) {
        val params = HashMap<String, Any>()
        params.put("payway", 0)// 0：支付宝，1：微信
        params.put("username", "13399857800")
        params.put("amount", "0.01")// 充值金额
        retrofitUtils.request<ResponseBody>("rechargePersonCenter", params, onSuccess = {
            try {
                // 这里解析会处理错误。
                val responseParser = ResponseParser(host.applicationContext, it)
                Logger.i(responseParser.data)
                val payInfo = JSONObject(responseParser.data).getString("payInfo")
                AliPayUtils.getInstance(this@MainActivity).pay(payInfo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }
}
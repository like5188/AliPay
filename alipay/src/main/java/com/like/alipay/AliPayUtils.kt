package com.like.alipay

import android.app.Activity
import android.text.TextUtils
import com.alipay.sdk.app.PayTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AliPayUtils {

    /**
     * 支付
     * @param orderInfo 订单信息
     * @return 同步通知结果
     */
    suspend fun pay(activity: Activity, orderInfo: String): Boolean = withContext(Dispatchers.IO) {
        // 用户在商户 app 内部点击付款，是否需要一个 loading 做为在钱包唤起之前的过渡，这个值设置为 true，将会在调用 pay 接口的时候直接唤起一个 loading，直到唤起 H5 支付页面或者唤起外部的钱包付款页面 loading 才消失。（建议将该值设置为 true，优化点击付款到支付唤起支付页面的过渡过程。）
        val result = PayTask(activity).payV2(orderInfo, true)
        val payResult = PayResult(result)

        // 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
        val resultInfo = payResult.result// 同步返回需要验证的信息
        val resultStatus = payResult.resultStatus
        // 判断resultStatus 为9000则代表支付成功。该笔订单是否真实支付成功，需要依赖服务端的异步通知。
        TextUtils.equals(resultStatus, "9000")
    }
}
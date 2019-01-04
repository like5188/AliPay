package com.like.alipay

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.alipay.sdk.app.PayTask
import com.like.livedatabus.LiveDataBus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.jvm.functions.FunctionN

class AliPayUtils private constructor(private val mActivity: Activity) {
    companion object : SingletonHolder<AliPayUtils>(object : FunctionN<AliPayUtils> {
        override val arity: Int = 1 // number of arguments that must be passed to constructor

        override fun invoke(vararg args: Any?): AliPayUtils {
            return AliPayUtils(args[0] as Activity)
        }
    }) {
        private val TAG = AliPayUtils::class.java.simpleName
        /**
         * 支付成功
         */
        const val TAG_PAY_SUCCESS = "TAG_PAY_SUCCESS"
        /**
         * 支付失败
         */
        const val TAG_PAY_FAILURE = "TAG_PAY_FAILURE"
        /**
         * 支付结果确认中
         */
        const val TAG_PAY_RESULT_CONFIRMING = "TAG_PAY_RESULT_CONFIRMING"
    }

    /**
     * 支付
     *
     * @param orderInfo 订单信息
     */
    fun pay(orderInfo: String) {
        GlobalScope.launch {
            // 用户在商户app内部点击付款，是否需要一个loading做为在钱包唤起之前的过渡，这个值设置为true。
            val result = PayTask(mActivity).payV2(orderInfo, true)
            launch(Dispatchers.Main) {
                val payResult = PayResult(result)
                /**
                 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                 * docType=1) 建议商户依赖异步通知
                 */
                val resultInfo = payResult.result// 同步返回需要验证的信息
                Log.i(TAG, "resultInfo=$resultInfo")
                val resultStatus = payResult.resultStatus
                Log.i(TAG, "resultStatus=$resultStatus")
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    LiveDataBus.post(TAG_PAY_SUCCESS)
                    Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show()
                } else {
                    // 判断resultStatus 为非"9000"则代表可能支付失败
                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        LiveDataBus.post(TAG_PAY_RESULT_CONFIRMING)
                        Toast.makeText(mActivity, "支付结果确认中...", Toast.LENGTH_SHORT).show()
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        LiveDataBus.post(TAG_PAY_FAILURE)
                        Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * get the sdk version.
     */
    fun getSDKVersion(): String {
        return PayTask(mActivity).version
    }
}
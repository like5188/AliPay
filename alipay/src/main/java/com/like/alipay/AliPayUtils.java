package com.like.alipay;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.like.logger.Logger;
import com.like.rxbus.RxBus;
import com.like.toast.ToastUtils;

import java.util.Map;

public class AliPayUtils {
    private Activity mActivity;
    private volatile static AliPayUtils sInstance = null;

    public static AliPayUtils getInstance(Activity activity) {
        if (sInstance == null) {
            synchronized (AliPayUtils.class) {
                if (sInstance == null) {
                    sInstance = new AliPayUtils(activity);
                }
            }
        }
        return sInstance;
    }

    private AliPayUtils(Activity activity) {
        mActivity = activity;

    }

    /**
     * 支付
     *
     * @param orderInfo 订单信息
     */
    public void pay(String orderInfo) {
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(mActivity);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            PayResult payResult = new PayResult(result);
            /**
             * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
             * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
             * docType=1) 建议商户依赖异步通知
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            Logger.i("resultInfo=" + resultInfo);
            String resultStatus = payResult.getResultStatus();
            Logger.i("resultStatus=" + resultStatus);
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                RxBus.postByTag(RxBusTag.TAG_PAY_SUCCESS);
                ToastUtils.showShortCenter(mActivity, "支付成功");
            } else {
                // 判断resultStatus 为非"9000"则代表可能支付失败
                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    RxBus.postByTag(RxBusTag.TAG_PAY_RESULT_CONFIRMING);
                    ToastUtils.showShortCenter(mActivity, "支付结果确认中...");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    RxBus.postByTag(RxBusTag.TAG_PAY_FAILURE);
                    ToastUtils.showShortCenter(mActivity, "支付失败");
                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sdk version.
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(mActivity);
        String version = payTask.getVersion();
        ToastUtils.showShortCenter(mActivity, version);
    }


}

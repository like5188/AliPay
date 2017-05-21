package com.like.alipay.sample;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/21.
 */

public interface Api {
    // 直接充值时获取支付宝支付信息
    @FormUrlEncoded
    @POST("/user/addOrder")
    Observable<ResponseBody> rechargePersonCenter(@FieldMap Map<String, Object> paramsMap);
}

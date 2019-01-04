#### 最新版本

模块|AliPay
---|---
最新版本|[![Download](https://jitpack.io/v/like5188/AliPay.svg)](https://jitpack.io/#like5188/AliPay)

## 功能介绍

1、支付宝支付封装

## 使用方法：

1、引用

在Project的gradle中加入：
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
在Module的gradle中加入：
```groovy
    android {
        repositories {
            flatDir {
                dirs 'libs', '../alipay/libs'
            }
        }
    }
    dependencies {
        implementation 'com.github.like5188:AliPay:版本号'
        // 引用LiveDataBus库，用于接收返回结果
        implementation 'com.github.like5188.LiveDataBus:livedatabus:1.2.2'
        kapt 'com.github.like5188.LiveDataBus:livedatabus_compiler:1.2.2'
    }
```

2、支付
```java
    AliPayUtils.getInstance(activity).pay(orderInfoString);
```

3、获取支付宝SDK版本号
```java
    AliPayUtils.getInstance(activity).getSDKVersion();
```

4、接收返回结果
```java
    在任意一个类中注册
    LiveDataBus.register(this, this);
```
        然后用下面三个方法接收支付宝返回的结果
```java
    // 支付成功
    @RxBusSubscribe(AliPayUtils.TAG_PAY_SUCCESS)
    public void onPaySuccess() {
    }
```
```java
    // 支付结果确认中
    @RxBusSubscribe(AliPayUtils.TAG_PAY_RESULT_CONFIRMING)
    public void onPayResultConfirming() {
    }
```
```java
    // 支付失败
    @RxBusSubscribe(AliPayUtils.TAG_PAY_FAILURE)
    public void onPayFailure() {
    }
```

5、Proguard
```java
    -dontwarn android.net.**

    # LiveDataBus
    -keep class * extends com.like.livedatabus.Bridge
    -keep class com.like.livedatabus_annotations.**{*;}
```

#### 最新版本

模块|AliPay
---|---
最新版本|[![Download](https://jitpack.io/v/like5188/AliPay.svg)](https://jitpack.io/#like5188/AliPay)

## 功能介绍

1、支付宝支付封装

## 使用方法：

1、引用

在Module的gradle中加入：

```groovy
    dependencies {
    implementation 'com.github.like5188:AliPay:版本号'
}
```

2、支付

```java
    AliPayUtils.pay(activity, orderInfoString)
```
# AliPay

支付宝使用工具

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
    dependencies {
        compile 'com.github.like5188:AliPay:1.0.0'
        compile 'com.github.like5188.RxBus:rxbus:1.0.0'
        annotationProcessor 'com.github.like5188.RxBus:rxbus_compiler:1.0.0'
    }
```
2、支付
```java
    AliPayUtils.getInstance(context).pay(orderInfoString);
```
3、获取支付宝SDK版本号
```java
    AliPayUtils.getInstance(context).getSDKVersion();
```
4、接收返回结果
```java
    在任意一个类中注册
    RxBus.register(this);
    在这个类销毁时反注册
    RxBus.unregister(this);
```
        然后用下面三个方法接收支付宝返回的结果
```java
    // 支付成功
    @RxBusSubscribe(RxBusTag.TAG_PAY_SUCCESS)
    public void TAG_PAY_SUCCESS() {
    }
```
```java
    // 支付结果确认中
    @RxBusSubscribe(RxBusTag.TAG_PAY_RESULT_CONFIRMING)
    public void TAG_PAY_RESULT_CONFIRMING() {
    }
```
```java
    // 支付失败
    @RxBusSubscribe(RxBusTag.TAG_PAY_FAILURE)
    public void TAG_PAY_FAILURE() {
    }
```
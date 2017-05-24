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
    public void onPaySuccess() {
    }
```
```java
    // 支付结果确认中
    @RxBusSubscribe(RxBusTag.TAG_PAY_RESULT_CONFIRMING)
    public void onPayResultConfirming() {
    }
```
```java
    // 支付失败
    @RxBusSubscribe(RxBusTag.TAG_PAY_FAILURE)
    public void onPayFailure() {
    }
```
5、引入的库
```java
    compile rootProject.ext.deps.rxbus
    compile rootProject.ext.deps.Logger
    compile rootProject.ext.deps.Toast
```
# License
```xml
    Copyright 2017 like5188
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

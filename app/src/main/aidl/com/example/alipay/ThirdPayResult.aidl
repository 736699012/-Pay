// ThirdPayResult.aidl
package com.example.alipay;

// Declare any non-default types here with import statements

interface ThirdPayResult {
    //支付成功
        void paySuccess ();

        //支付失败
        void payError(in int errorCode,in String msg);
}

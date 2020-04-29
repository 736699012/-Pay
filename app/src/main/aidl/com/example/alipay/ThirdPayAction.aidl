// ThirdPayAction.aidl
package com.example.alipay;

// Declare any non-default types here with import statements
import com.example.alipay.ThirdPayResult;

interface ThirdPayAction {

   void requestPay(in String orderInfo,in float money,in ThirdPayResult call);
}

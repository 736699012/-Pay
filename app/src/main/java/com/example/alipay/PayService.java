package com.example.alipay;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.alipay.utils.Constant;

public class PayService extends Service {

    private static final String TAG = "PayService";
    private ThirdPayImpl thirdPay;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String action = intent.getAction();
        if(Constant.ACTION_THREEPAY.equals(action)){
            thirdPay = new ThirdPayImpl();
            return thirdPay;
        }
        return new PayAction();
    }

    public class PayAction extends Binder{

        public void play (float money){
            //TODO:支付的方法
            Log.d(TAG,"pay money is --"+money);
            thirdPay.paySuccess();
        }
        public void userCancel(){
            //TODO:取消支付
            thirdPay.payError(1,"user cancel pay");
        }
    }


    public class ThirdPayImpl extends ThirdPayAction.Stub{

        private ThirdPayResult mcallback;

        @Override
        public void requestPay(String orderInfo, float money, ThirdPayResult call) throws RemoteException {

            //第三方支付请求，然后打开一个支付界面。

            mcallback =call;
            if(call==null){
                Log.d(TAG,"传递的回调函数是空");
            }
            Log.d(TAG,orderInfo+"    11    "+money );
            Intent intent =new Intent();
            intent.setClass(PayService.this,PayActivity.class);
            intent.putExtra(Constant.KEY_BILL_INFO,orderInfo);
            intent.putExtra(Constant.KEY_PAY_MONEY,money);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }

        public void paySuccess(){
            try {
                if(mcallback!=null){
                    mcallback.paySuccess();
                    Log.d(TAG,"支付成功");
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        public void payError(int errorCode,String msg){
            try {
                if(mcallback!=null){
                    mcallback.payError(errorCode,msg);
                    Log.d(TAG,"错误代码"+errorCode+"  错误信息"+msg);
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}

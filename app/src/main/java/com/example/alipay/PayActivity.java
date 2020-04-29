package com.example.alipay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alipay.utils.Constant;


public class PayActivity extends AppCompatActivity {

    private static final String TAG = "PayActivity";
    private boolean isBindService;
    private PayService.PayAction mpayAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        doBindService();
        initView();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mpayAction!=null){
            mpayAction.userCancel();
        }

    }

    private void initView() {
        Intent intent = getIntent();
        String orderInfo =intent.getStringExtra(Constant.KEY_BILL_INFO);
        final float money = intent.getFloatExtra(Constant.KEY_PAY_MONEY,0);
        TextView order_info_tv = findViewById(R.id.order_info_tv);
        TextView pay_money = findViewById(R.id.pay_money);
        order_info_tv.setText("支付信息"+orderInfo);
        pay_money.setText("支付金额:"+money);
        Button button = findViewById(R.id.commit);
        final EditText editText =findViewById(R.id.password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:提交信息 判断密码
                if ("123456".equals(editText.getText().toString())&&mpayAction!=null) {
                    mpayAction.play(money);
//                    Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"pay is finished");
                    finish();

                }else{
                    Toast.makeText(PayActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 绑定服务
     */
    private void doBindService() {
        Intent intent = new Intent(this,PayService.class);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        isBindService = bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection  =new ServiceConnection() {

        /**
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mpayAction = (PayService.PayAction) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mpayAction =null;
        }
    };


    /**
     * 解除服务绑定
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBindService&&serviceConnection !=null){
            unbindService(serviceConnection);
            serviceConnection=null;
            isBindService =false;
        }
    }
}

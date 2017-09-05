package com.goixeomdriver.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.models.VerifyCode;
import com.goixeomdriver.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class AccuracyActivity extends BaseAuthActivity {
    @BindView(R.id.code_0)
    EditText code0;
    @BindView(R.id.code_1)
    EditText code1;
    @BindView(R.id.code_2)
    EditText code2;
    @BindView(R.id.code_3)
    EditText code3;
    @BindView(R.id.txt4)
    CustomTextView txt4;
    private CustomTextView mAccuracy;
    private LinearLayout mAccuracyLayout;
    private String strCode;
    private String codeVerify;
    private String mPhone;
    private int typeService;
    private boolean isFirstTime = true;
    public static final String SMS_BUNDLE = "pdus";
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle intentExtras = intent.getExtras();
            if (intentExtras != null) {
                Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
                String smsMessageStr = "";
                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                    String smsBody = smsMessage.getMessageBody();
                    String address = smsMessage.getOriginatingAddress();
                    smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += smsBody + "\n";

                    if (smsBody.contains("Mật khẩu Gọi Xe Ôm của bạn là")) {
                        String numberCode = smsBody.substring(smsBody.indexOf(":") +2, smsBody.length());
                        if (numberCode.length() == 4) {
                            code0.setText(numberCode.substring(0, 1));
                            code1.setText(numberCode.substring(1, 2));
                            code2.setText(numberCode.substring(2, 3));
                            code3.setText(numberCode.substring(3, 4));
                        }
                        break;
                    }
                }
//                ToastUtils.showShort(smsMessageStr);
LogUtils.e(smsMessageStr);
                //this will update the UI with message

            }
        }
    };
    private CountDownTimer countDownTimer = new CountDownTimer(Constants.COUNT_DOWN_MILIS, 1000) {
        @Override
        public void onTick(long l) {
            txt4.setText(Html.fromHtml(String.format(getString(R.string.dem_nguoc), l / 1000 + "s")));
            txt4.setClickable(false);

        }

        @Override
        public void onFinish() {
            txt4.setClickable(true);
            txt4.setText(Html.fromHtml(getString(R.string.guilaima)));
        }
    };
    private String name;
    private String refcode;
    private String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accuracy);
        ButterKnife.bind(this);

        mAccuracy = (CustomTextView) findViewById(R.id.accuracy_txt);
        if (getIntent() != null) {
            mPhone = getIntent().getStringExtra(Constants.PHONE);
            email = getIntent().getStringExtra(Constants.EMAIL);
            name = getIntent().getStringExtra(Constants.NAME);
            refcode = getIntent().getStringExtra(Constants.REF_CODE);
            typeService = getIntent().getIntExtra(Constants.TYPE_SERVICE,0);
            sendSms(mPhone);
            mAccuracy.setText(Html.fromHtml(String.format(getString(R.string.txt_accuracy), mPhone)));

        } else {
            finish();
        }
        mAccuracyLayout = (LinearLayout) findViewById(R.id.accuracy_layout);
        mAccuracyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCodeNow();
            }
        });

        addWatcherToEditext();
    }


    private void sendSms(String phone) {
        Call<ApiResponse<VerifyCode>> sms = getmApi().sendSMS(ApiConstants.API_KEY, phone);
        if (!isFirstTime) {
            getDialogProgress().showDialog();
        }
        isFirstTime = false;
        sms.enqueue(new CallBackCustom<ApiResponse<VerifyCode>>(this, getDialogProgress(), new OnResponse<ApiResponse<VerifyCode>>() {
            @Override
            public void onResponse(ApiResponse<VerifyCode> object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    LogUtils.d("Sent code to device");
                    codeVerify = object.getData().getCode();
                    countDownTimer.start();
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    private void addWatcherToEditext() {
        code0.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    code1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    code2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    code3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    strCode = code0.getText().toString() + code1.getText().toString() + code2.getText().toString() + code3.getText().toString();
                    if (strCode.length() == 4) {
                        checkCodeNow();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean verifyPass() {
        strCode = code0.getText().toString() + code1.getText().toString() + code2.getText().toString() + code3.getText().toString();
        if (strCode.length() != 4 || !strCode.equals(codeVerify)) {
            showDialogErrorContent("Mã code xác thực không hợp lệ ! Vui lòng kiểm tra lại...");
            return false;
        }
        return true;
    }

    private void checkCodeNow() {
        if (verifyPass()) {
            getDialogProgress().showDialog();
            Call<ApiResponse<String>> register = getmApi().register(ApiConstants.API_KEY, mPhone, name, email, refcode, codeVerify, PhoneUtils.getIMEI(),typeService);
            register.enqueue(new CallBackCustom<ApiResponse<String>>(this, getDialogProgress(), new OnResponse<ApiResponse<String>>() {
                @Override
                public void onResponse(ApiResponse<String> object) {
                    if (object.getStatus() == ApiConstants.CODE_SUCESS && object.getData() != null) {
                        getmSetting().put(Constants.IS_SLIDE, true);
                        getmSetting().put(Constants.ID, Integer.parseInt(object.getData()));
                        Intent intent = new Intent(AccuracyActivity.this, UpdateActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        finishAffinity();
                    } else {
                        showDialogErrorContent(object.getMessage());
                    }
                }
            }));
        }
    }

    @OnClick(R.id.txt4)
    public void onViewClicked() {
        sendSms(mPhone);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.RECEIVE_SMS,
                            android.Manifest.permission.READ_SMS},
                    10000);
        } else {
            Log.e("DB", "PERMISSION GRANTED");
            IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            intentFilter.setPriority(1000);
            registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10000 ){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                showDialogPermission();
            }else{
                Log.e("DB", "PERMISSION GRANTED");
                IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                intentFilter.setPriority(1000);
                registerReceiver(broadcastReceiver, intentFilter);
            }
        }
    }
    private void showDialogPermission() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this).title(getString(R.string.error))
                .content("Bạn cần cấp quyền truy cập ứng dụng để tiếp tục sử dụng dịch vụ")
                .positiveColor(Color.GRAY)
                .positiveText("Đồng ý")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ActivityCompat.requestPermissions(AccuracyActivity.this,
                                new String[]{android.Manifest.permission.RECEIVE_SMS,
                                        android.Manifest.permission.READ_SMS},
                                10000);
                    }
                }).show();
    }
}

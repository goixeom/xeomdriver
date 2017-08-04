package com.goixeomdriver.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

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
    private boolean isFirstTime = true;
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
            Call<ApiResponse<String>> register = getmApi().register(ApiConstants.API_KEY, mPhone, name, email, refcode, codeVerify, PhoneUtils.getIMEI());
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
}

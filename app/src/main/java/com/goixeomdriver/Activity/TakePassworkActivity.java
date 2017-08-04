package com.goixeomdriver.Activity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.goixeom.R;
import com.goixeomdriver.View.CustomEditText;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.application.MainApplication;
import com.goixeomdriver.interfaces.OnResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class TakePassworkActivity extends BaseAuthActivity {
    @BindView(R.id.edt_email)
    CustomEditText edtEmail;
    private LinearLayout mSendPasswork;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_passwork);
        ButterKnife.bind(this);
        mSendPasswork = (LinearLayout) findViewById(R.id.sendpasswork_txt);
        mSendPasswork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainApplication.getInstance().isResent()) {
                    showDialogErrorContent("Vui lòng đợi 5 phút tiếp theo để gửi lại mật khẩu");
                    return;
                }

                if(edtEmail.getText().toString().isEmpty()){
                    showDialogErrorContent(getString(R.string.please_enter_email_2));
                    return;
                }
                if (!isValidEmail(edtEmail.getText().toString())){
                    showDialogErrorContent(getString(R.string.please_enter_email));
                    return;
                }
                getDialogProgress().showDialog();
                Call<ApiResponse> forgot = getmApi().forgotPassword(ApiConstants.API_KEY, edtEmail.getText().toString());
                forgot.enqueue(new CallBackCustom<ApiResponse>(TakePassworkActivity.this, getDialogProgress(), new OnResponse<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse object) {
                        if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                            MainApplication.getInstance().setResent(true);
                            MainApplication.getInstance().getCountDownTimer().start();
                            Toast.makeText(TakePassworkActivity.this, getString(R.string.forgot_password_msg), Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            showDialogErrorContent(object.getMessage());
                        }
                    }
                }));
            }
        });
    }


    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}

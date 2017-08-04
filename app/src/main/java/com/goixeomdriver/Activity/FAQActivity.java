package com.goixeomdriver.Activity;

import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.utils.CommonUtils;
import com.goixeomdriver.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class FAQActivity extends BaseActivity {


    @BindView(R.id.edt_faq)
    EditText edtFaq;
    @BindView(R.id.send_help)
    Button sendHelp;
    Unbinder unbinder;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_toolbar)
    CustomTextView txtToolbar;
    @BindView(R.id.tv_conlai)
    CustomTextView tvConlai;
    @BindView(R.id.phone)
    ImageView phone;


    @Override
    public void pingNotification(String title, String content) {

    }

    @Override
    public void onSocketReady() {

    }

    @Override
    public void onLocationChange(Location location) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_help);
        unbinder = ButterKnife.bind(this);
        txtToolbar.setText(Html.fromHtml(getString(R.string.txt_toolbar_faq)));
        tvConlai.setText(String.format(getString(R.string.c_n_l_i_200_k_t), 200));
        edtFaq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvConlai.setText(String.format(getString(R.string.c_n_l_i_200_k_t), 200 - charSequence.length()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @OnClick(R.id.send_help)
    public void onViewClicked() {
        vibrate();

        getDialogProgress().showDialog();
        Call<ApiResponse> feedback = getmApi().feedback(ApiConstants.API_KEY, getmSetting().getInt(Constants.ID), edtFaq.getText().toString());
        feedback.enqueue(new CallBackCustom<ApiResponse>(this, getDialogProgress(), new OnResponse<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    Toast.makeText(getApplicationContext(), "Ý kiến đã  được gửi tới người quản trị.Cám ơn bạn !", Toast.LENGTH_LONG).show();
                    getmSocket().feedback(edtFaq.getText().toString());
                    finish();
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    @OnClick(R.id.img_back)
    public void onViewCancelClicked() {
        vibrate();

        finish();
    }

    @OnClick(R.id.phone)
    public void onViewCallClicked() {
        vibrate();

        CommonUtils.intentToCall("02462931011",this);
    }
}

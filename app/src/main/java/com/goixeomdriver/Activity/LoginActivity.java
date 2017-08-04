package com.goixeomdriver.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.goixeom.R;
import com.goixeomdriver.View.CustomEditText;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.models.User;
import com.goixeomdriver.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class LoginActivity extends BaseAuthActivity {
    @BindView(R.id.send_imv)
    ImageView sendImv;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.customTextView)
    CustomTextView customTextView;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.edt_phone)
    CustomEditText edtPhone;
    @BindView(R.id.edt_password)
    CustomEditText edtPassword;
    @BindView(R.id.tv_forgot_password)
    CustomTextView tvForgotPassword;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    private LinearLayout mSendpasswork;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
        }
        mSendpasswork = (LinearLayout) findViewById(R.id.sendpasswork_layout);
        mSendpasswork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edtPassword.getText().toString();
                if (edtPhone.getText().toString().matches("[0-9]+")) {
                    if (!verifyPhoneSucess()) {
                        return;
                    }
                } else {
                    if (edtPhone.getText().toString().isEmpty()) {
                        showDialogErrorContent(getString(R.string.please_enter_email_2));
                        return;
                    }
                    if (!isValidEmail(edtPhone.getText().toString())) {
                        showDialogErrorContent(getString(R.string.please_enter_email));
                        return;
                    }

                }
                if (password.length() < 4) {
                    showDialogErrorContent("Mật khẩu phải từ 4 ký tự trở lên");
                    return;
                }
                getDialogProgress().showDialog();
                Call<ApiResponse<User>> login = getmApi().login(ApiConstants.API_KEY, edtPhone.getText().toString(), password, PhoneUtils.getIMEI());
                login.enqueue(new CallBackCustom<ApiResponse<User>>(LoginActivity.this, getDialogProgress(), new OnResponse<ApiResponse<User>>() {
                    @Override
                    public void onResponse(ApiResponse<User> response) {
                        if (response.getData() != null) {
                            getmSetting().put(Constants.IS_SLIDE, true);
                            getmSetting().put(Constants.ID, response.getData().getId());
                            if(response.getData().getClicked() ==1 ){
                                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                finishAffinity();
                                return;
                            }
                            Intent intent = new Intent(LoginActivity.this, UpdateActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            finishAffinity();
                        } else {
                            showDialogErrorContent(response.getMessage());
                        }
                    }
                }));


            }
        });
        llRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = llRoot.getRootView().getHeight() - llRoot.getHeight();
                if (heightDiff > dpToPx(LoginActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    scrollView.scrollTo(0,200);
                }
            }
        });

        KeyboardUtils.hideSoftInput(LoginActivity.this);
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            showDialogPermission();
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
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                    }
                }).show();
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean verifyPhoneSucess() {
        String phone = edtPhone.getText().toString();
        if (phone.isEmpty()) {
            new MaterialDialog.Builder(this).title(R.string.error)
                    .content(getString(R.string.empty_phone))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        if ((phone.length() < 10) || phone.length() > 11) {
            new MaterialDialog.Builder(this).title(R.string.error)
                    .content(getString(R.string.please_enter_phone))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        if (phone.length() == 10) {
            if (!phone.substring(0, 2).equals("09")) {
                new MaterialDialog.Builder(this).title(R.string.error)
                        .content(getString(R.string.please_enter_phone))
                        .positiveText(R.string.dismis)
                        .positiveColor(Color.GRAY)
                        .show();
                return false;
            }
        } else {
            if (!phone.substring(0, 2).equals("01")) {
                new MaterialDialog.Builder(this).title(R.string.error)
                        .content(getString(R.string.please_enter_phone))
                        .positiveText(R.string.dismis)
                        .positiveColor(Color.GRAY)
                        .show();
                return false;
            }
        }


        return true;
    }

    @OnClick(R.id.tv_forgot_password)
    public void onViewClicked() {
        Intent intent = new Intent(LoginActivity.this, TakePassworkActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}

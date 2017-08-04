package com.goixeomdriver.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.bumptech.glide.Glide;
import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.application.MainApplication;
import com.goixeomdriver.models.User;
import com.goixeomdriver.utils.Constants;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Huy on 6/9/2017.
 */

public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.img_avt)
    CircleImageView imgAvt;
    @BindView(R.id.tv_name)
    CustomTextView tvName;
    @BindView(R.id.tv_phone)
    CustomTextView tvPhone;
    //    @BindView(R.id.edt_name)
//    EditText edtName;
//    @BindView(R.id.edt_email)
//    EditText edtEmail;
//    @BindView(R.id.edt_phone)
//    EditText edtPhone;
//    @BindView(R.id.ll_root_change_pass)
//    LinearLayout llRootChangePass;
//    @BindView(R.id.notification)
//    ImageView notification;
//    @BindView(R.id.sw_noti)
//    Switch swNoti;
//    @BindView(R.id.ll_root_sw)
//    LinearLayout llRootSw;

    Unbinder unbinder;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_toolbar)
    CustomTextView txtToolbar;

    @BindView(R.id.rating_bar)
    MaterialRatingBar ratingBar;
    @BindView(R.id.sw_direct)
    com.kyleduo.switchbutton.SwitchButton swDirect;
    @BindView(R.id.ll_ring)
    LinearLayout llRing;
    @BindView(R.id.sw_notification)
    SwitchButton swNotification;
    @BindView(R.id.sw_vibarate)
    SwitchButton swVibarate;
    @BindView(R.id.tv_imei)
    CustomTextView tvImei;
    @BindView(R.id.tv_version)
    CustomTextView tvVersion;
    @BindView(R.id.tv_build)
    CustomTextView tvBuild;
    @BindView(R.id.btn_dang_xuat)
    Button btnDangXuat;
    @BindView(R.id.ll_root)
    RelativeLayout llRoot;
    @BindView(R.id.tv_noti)
    CustomTextView tvNoti;
    private User user;

    public SettingActivity() {
    }

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
        setContentView(R.layout.content_personal);
        unbinder = ButterKnife.bind(this);
        user = MainApplication.getInstance().getmUser();
        tvName.setText(user.getName());
        tvPhone.setText(user.getPhone());
        ratingBar.setRating((float) user.getVote());
        view = llRoot;
//        edtEmail.setText(user.getEmail());
//        edtName.setText(user.getName());
//        edtPhone.setText(user.getPhone());
//        edtPhone.setEnabled(false);
//        edtEmail.setEnabled(false);
//        edtName.setEnabled(false);
        Glide.with(this).load("https://" + user.getAvatar()).into(imgAvt);
//        swNoti.setChecked(getmSetting().getBoolean(Constants.RECEIVE_NOTI));
//        swNoti.setOnCheckedChangeListener(this);
        swDirect.setChecked(getmSetting().getBoolean(Constants.SETTING_DIRECT, true));
        swNotification.setChecked(getmSetting().getBoolean(Constants.SETTING_NOTIFICATION, true));
        swVibarate.setChecked(getmSetting().getBoolean(Constants.SETTING_VIBARATE, true));
        txtToolbar.setText(Html.fromHtml(getString(R.string.txt_toolbar_setting)));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
        } else {
            tvImei.setText(String.format("IMEI %s", PhoneUtils.getIMEI()));
        }
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            tvVersion.setText(String.format("VERSION %s", version));
            tvBuild.setText(String.format("BUILD %d", verCode));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvNoti.setSelected(true);
        addListenSwitch();
        KeyboardUtils.hideSoftInput(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void addListenSwitch() {
        swVibarate.setOnCheckedChangeListener(this);
        swNotification.setOnCheckedChangeListener(this);
        swDirect.setOnCheckedChangeListener(this);
    }

    //
    @OnClick({R.id.btn_dang_xuat})
    public void onViewClicked(View view) {
        vibrate();

        switch (view.getId()) {
//            case R.id.ll_root_change_pass:
//                startActivity(new Intent(SettingActivity.this, ChangePasswordActivity.class));
//                break;
//            case R.id.ll_root_sw:
//                swNoti.setChecked(!swNoti.isChecked());
//                break;
            case R.id.btn_dang_xuat:
                setResult(Constants.RESULT_LOGOUT);
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        vibrate();

        if (b) {

            switch (compoundButton.getId()) {
                case R.id.sw_direct: {
                    getmSetting().put(Constants.SETTING_DIRECT, b);
                    showSnackBar("Đã bật chỉ đường", "#25b45b");
                    break;
                }
                case R.id.sw_notification: {
                    getmSetting().put(Constants.SETTING_NOTIFICATION, b);
                    showSnackBar("Đã bật thông báo", "#25b45b");

                    break;
                }
                case R.id.sw_vibarate: {
                    getmSetting().put(Constants.SETTING_VIBARATE, b);
                    showSnackBar("Đã bật rung khi chạm", "#25b45b");

                    break;
                }
            }
        } else {
            switch (compoundButton.getId()) {
                case R.id.sw_direct: {
                    getmSetting().put(Constants.SETTING_DIRECT, b);
                    showSnackBar("Đã tắt chỉ đường", "#FFBE08");
                    break;
                }
                case R.id.sw_notification: {
                    getmSetting().put(Constants.SETTING_NOTIFICATION, b);
                    showSnackBar("Đã tắt thông báo", "#FFBE08");
                    break;
                }
                case R.id.sw_vibarate: {
                    getmSetting().put(Constants.SETTING_VIBARATE, b);
                    showSnackBar("Đã tắt rung khi chạm", "#FFBE08");
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            showDialogPermission();
            return;
        }
        tvImei.setText(String.format("IMEI %s", PhoneUtils.getIMEI()));
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
                        ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                    }
                }).show();
    }

    private void updateInformation(String phone, String firstName, String email) {
//        getDialogProgress().showDialog();
//        Call<ApiResponse<String>> updateProfile = getmApi().updateInformation(ApiConstants.API_KEY, phone, getmSetting().getString(Constants.ID), firstName, email);
//        updateProfile.enqueue(new CallBackCustom<ApiResponse<String>>(this, getDialogProgress(), new OnResponse<ApiResponse<String>>() {
//            @Override
//            public void onResponse(ApiResponse<String> object) {
//                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
//                    Toast.makeText(getApplicationContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_LONG).show();
//                } else {
//                    showDialogErrorContent(object.getMessage());
//                }
//            }
//        }));
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        vibrate();

        finish();
    }

    public void showSnackBar(String content, String color) {
        Crouton.cancelAllCroutons();
        Style croutonStyle = new Style.Builder().setBackgroundColorValue(Color.parseColor(color)).build();
//        Crouton crouton = Crouton.makeText(this, content, croutonStyle)
//                .setConfiguration(new Configuration.Builder().setDuration(Configuration.DURATION_INFINITE).build());
//        crouton.show();

        Crouton.makeText(this, content, croutonStyle, llRoot).show();
    }

    @OnClick(R.id.ll_ring)
    public void onViewRingClicked() {
        vibrate();

        startActivity(new Intent(SettingActivity.this, ChoseRingtoneActivity.class));
    }
}

package com.goixeomdriver.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.PhoneUtils;
import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.ApiUtils;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.application.MainApplication;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.models.User;
import com.goixeomdriver.socket.SocketConstants;
import com.goixeomdriver.socket.SocketResponse;
import com.goixeomdriver.utils.CommonUtils;
import com.goixeomdriver.utils.Constants;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class UpdateActivity extends BaseActivity {
    @BindView(R.id.ll_root_getting_start)
    LinearLayout llRootGettingStart;
    @BindView(R.id.ll_root_missing_data)
    LinearLayout llRootMissingData;
    @BindView(R.id.update3_txt)
    CustomTextView update3Txt;
    @BindView(R.id.ll_root_success_update)
    LinearLayout llRootSuccessUpdate;
    @BindView(R.id.img_result_bangLai)
    ImageView imgResultBangLai;
    @BindView(R.id.img_next_bangLai)
    ImageView imgNextBangLai;
    @BindView(R.id.progress_bangLai)
    ProgressBar progressBangLai;
    @BindView(R.id.ll_root_bangLai)
    LinearLayout llRootBangLai;

    @BindView(R.id.img_result_dangkyxe)
    ImageView imgResultDangkyxe;
    @BindView(R.id.img_next_dangkyxe)
    ImageView imgNextDangkyxe;
    @BindView(R.id.progress_dangkyxe)
    ProgressBar progressDangkyxe;
    @BindView(R.id.ll_root_dangkyxe)
    LinearLayout llRootDangkyxe;

    @BindView(R.id.img_result_baohiem)
    ImageView imgResultBaohiem;
    @BindView(R.id.img_next_baohiem)
    ImageView imgNextBaohiem;
    @BindView(R.id.progress_baohiem)
    ProgressBar progressBaohiem;
    @BindView(R.id.ll_root_baohiem)
    LinearLayout llRootBaohiem;

    @BindView(R.id.img_result_cmnd)
    ImageView imgResultCmnd;
    @BindView(R.id.img_next_cmnd)
    ImageView imgNextCmnd;
    @BindView(R.id.progress_cmnd)
    ProgressBar progressCmnd;
    @BindView(R.id.ll_root_cmnd)
    LinearLayout llRootCmnd;


    @BindView(R.id.update_layout)
    LinearLayout updateLayout;
    @BindView(R.id.tv_go_main)
    CustomTextView tvGoMain;
    @BindView(R.id.tv_getting)
    CustomTextView tvGetting;
    @BindView(R.id.tv_missing)
    CustomTextView tvMissing;
    private CustomTextView mUpdateTextView;
    private LinearLayout mUpdate;
    int id = getmSetting().getInt(Constants.ID);
    final int PENDDING = 0;
    final int VERIFIED = 1;
    final int NEED_VERIFY_AGAIN = 2;
    final int DENY = 3;
    final int NOT_UPLOAD = 4;
    private BroadcastReceiver receiverProfile = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String profile = intent.getStringExtra(SocketConstants.KEY_UPDATE_PROFILE);
                if (profile != null) {
                    SocketResponse response = new Gson().fromJson(profile, SocketResponse.class);
                    if (response != null && response.getIdDriver() == getmSetting().getInt(Constants.ID)) {
                        tvMissing.setText(response.getContent());
                        tvGetting.setText(response.getContent());
                        switch (response.getStatus()) {
                            case PENDDING: {
                                llRootMissingData.setVisibility(View.VISIBLE);
                                llRootGettingStart.setVisibility(View.GONE);
                                llRootSuccessUpdate.setVisibility(View.GONE);
                                changeStateToVerify(response.getType());
                                break;
                            }
                            case VERIFIED: {
                                changeStateToSuccess(response.getType());
                                int id = getmSetting().getInt(Constants.ID);
                                if (id != -1) {
                                    getInfor(id);
                                }
                                break;
                            }
                            case NEED_VERIFY_AGAIN: {
                                llRootMissingData.setVisibility(View.VISIBLE);
                                llRootGettingStart.setVisibility(View.GONE);
                                llRootSuccessUpdate.setVisibility(View.GONE);
                                changeStateToVerify(response.getType());
                                break;
                            }
                            case DENY: {
                                llRootMissingData.setVisibility(View.VISIBLE);
                                llRootGettingStart.setVisibility(View.GONE);
                                llRootSuccessUpdate.setVisibility(View.GONE);
                                changeStateToDeny(response.getType());
                                break;
                            }
                            case NOT_UPLOAD: {
                                llRootMissingData.setVisibility(View.GONE);
                                llRootGettingStart.setVisibility(View.VISIBLE);
                                llRootSuccessUpdate.setVisibility(View.GONE);
                                break;
                            }
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(getmSocket()!=null)
        getmSocket().updateNewImei(PhoneUtils.getIMEI(), getCurrentIdUser());

    }

    @Override
    public void pingNotification(String title, String content) {

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
                        ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                    }
                }).show();
    }

    @Override
    public void onSocketReady() {
        if (id != -1 ) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                return;
            }
            if( getmSocket() != null)
            getmSocket().updateNewImei(PhoneUtils.getIMEI(), id);
//            getmSocket().listenProfileUpdateVerify();
        } else
            logout();
    }

    @Override
    public void onLocationChange(Location location) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getmSetting().getBoolean(Constants.CLICK_GOMAP, false)) {
//            startActivity(new Intent(UpdateActivity.this, MapsActivity.class));
//            finish();
//        }
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
        mUpdateTextView = (CustomTextView) findViewById(R.id.update3_txt);
        mUpdateTextView.setText(Html.fromHtml(getString(R.string.update_3)));
        mUpdate = (LinearLayout) findViewById(R.id.update_layout);
//        mUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UpdateActivity.this, LoginActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//            }
//        });
        registerReceiver(receiverProfile, new IntentFilter(SocketConstants.EVENT_PROFILE));
        if (getIntent() != null) {
            String content = getIntent().getStringExtra(Constants.CONTENT);
            if (content != null) {
                tvGetting.setText(content);
                tvMissing.setText(content);
            }
        }
        if (id != -1) {
            getInfor(id);
        } else {
            logout();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( getmSocket() != null)
            getmSocket().updateNewImei(PhoneUtils.getIMEI(), id);
    }

    @Override
    public void onBackPressed() {
        goHome();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonUtils.showNotifyOpenApp(this);
        unregisterReceiver(receiverProfile);

    }

    private void getInfor(final int id) {
        Call<ApiResponse<User>> getInfo = getmApi().getInfor(ApiConstants.API_KEY, id);
//        getDialogProgress().showDialog();
        getInfo.enqueue(new CallBackCustom<ApiResponse<User>>(this, new OnResponse<ApiResponse<User>>() {
            @Override
            public void onResponse(ApiResponse<User> object) {
                if (object.getData() != null) {
                    if (object.getData().getActive() == 2) {
                        BLOCK();
                        return;
                    }
                    if(object.getData().getClicked()==1 && object.getData().getActive() == 1){
                        getmSetting().put(Constants.CLICK_GOMAP, true);
                        startActivity(new Intent(UpdateActivity.this, MapsActivity.class));
                        finish();
                        return;
                    }
                    getmSetting().put(Constants.CLICK_GOMAP, false);
                    update3Txt.setText(Html.fromHtml(String.format(getString(R.string.update_3), object.getData().getName())));
                    MainApplication.getInstance().setmUser(object.getData());
                    updateStateItem(ApiConstants.TYPE_BANG_LAI, object.getData().getLicense());
                    updateStateItem(ApiConstants.TYPE_BAO_HIEM, object.getData().getInsurance());
                    updateStateItem(ApiConstants.TYPE_CMT, object.getData().getCmnd());
                    updateStateItem(ApiConstants.TYPE_DANGKY, object.getData().getRegister());
                    updateStateItem(ApiConstants.TYPE_HOCHIEU, object.getData().getFamily());
                    if (object.getData().getActive() == Constants.ACTIVE) {
                        llRootMissingData.setVisibility(View.GONE);
                        llRootGettingStart.setVisibility(View.GONE);
                        llRootSuccessUpdate.setVisibility(View.VISIBLE);
                    }
                } else {
                    logout();
                }
            }
        }));
    }

    private void updateStateItem(int type, int state) {
        switch (state) {
            case PENDDING: {
                llRootMissingData.setVisibility(View.VISIBLE);
                llRootGettingStart.setVisibility(View.GONE);
                llRootSuccessUpdate.setVisibility(View.GONE);
                changeStateToVerify(type);
                break;
            }
            case VERIFIED: {
                changeStateToSuccess(type);
                break;
            }
            case NEED_VERIFY_AGAIN: {
                llRootMissingData.setVisibility(View.VISIBLE);
                llRootGettingStart.setVisibility(View.GONE);
                llRootSuccessUpdate.setVisibility(View.GONE);
                changeStateToVerify(type);
                break;
            }
            case DENY: {
                llRootMissingData.setVisibility(View.VISIBLE);
                llRootGettingStart.setVisibility(View.GONE);
                llRootSuccessUpdate.setVisibility(View.GONE);
                changeStateToDeny(type);
                break;
            }
            case NOT_UPLOAD: {
                llRootMissingData.setVisibility(View.GONE);
                llRootGettingStart.setVisibility(View.VISIBLE);
                llRootSuccessUpdate.setVisibility(View.GONE);
                break;
            }
        }
    }

    ArrayList<String> listPermissions = new ArrayList<>();
    View view;

    @OnClick({R.id.ll_root_bangLai, R.id.ll_root_dangkyxe, R.id.ll_root_baohiem, R.id.ll_root_cmnd})
    public void onViewClicked(View view) {
        this.view = view;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            listPermissions.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            listPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            listPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (listPermissions.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(listPermissions.toArray(new String[listPermissions.size()]), 100);
                return;
            }
        }
        clickView(view);
    }

    private void clickView(View view) {
        switch (view.getId()) {
            case R.id.ll_root_bangLai:
                PhotoPicker.builder().setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .start(this, 1);
                break;
            case R.id.ll_root_dangkyxe:
                PhotoPicker.builder().setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .start(this, 2);
                break;
            case R.id.ll_root_baohiem:
                PhotoPicker.builder().setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .start(this, 3);
                break;
            case R.id.ll_root_cmnd:
                PhotoPicker.builder().setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(true)
                        .start(this, 4);
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showDialogPermission();
            } else {
                getmSocket().updateNewImei(PhoneUtils.getIMEI(), id);
            }
            return;
        }
        if (grantResults.length == listPermissions.size()) {
            boolean isSuccessful = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    isSuccessful = false;
                    break;
                }
            }

            if (isSuccessful && view != null) {
                clickView(view);
            }
        } else {
//            showDialogErrorContent(getString(R.string.permission));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            File newFile = new File((String) data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS).get(0));
            if (newFile != null && newFile.exists()) {
                uploadProfile(newFile, requestCode);
            }
        }

    }

    private void uploadProfile(File newFile, final int type) {
        RequestBody nameRequest = ApiUtils.createPartFromString(type + "");
        RequestBody idRequest = ApiUtils.createPartFromString(getmSetting().getInt(Constants.ID) + "");
        RequestBody keyRequest = ApiUtils.createPartFromString(ApiConstants.API_KEY);
        Map<String, RequestBody> map = new HashMap<>();
        MultipartBody.Part body = ApiUtils.prepareFilePart("img", newFile);
        map.put("type", nameRequest);
        map.put("id", idRequest);
        map.put("key", keyRequest);
//        getDialogProgress().showDialog();
        changeStateToLoading(type);
        Call<ApiResponse> upload = getmApi().uploadProfile(map, body);
        upload.enqueue(new CallBackCustom<ApiResponse>(this, new OnResponse<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    changeStateToVerify(type);
                    getmSocket().uploadSuccessProfile(type, getmSetting().getInt(Constants.ID));
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    private void changeStateToLoading(int type) {
        switch (type) {
            case 1: {
                progressBangLai.setVisibility(View.VISIBLE);
                imgNextBangLai.setVisibility(View.GONE);
                llRootBangLai.setClickable(false);

                break;
            }
            case 2: {
                progressDangkyxe.setVisibility(View.VISIBLE);
                imgNextDangkyxe.setVisibility(View.GONE);
                llRootDangkyxe.setClickable(false);

                break;
            }
            case 3: {
                progressBaohiem.setVisibility(View.VISIBLE);
                imgNextBaohiem.setVisibility(View.GONE);
                llRootBaohiem.setClickable(false);

                break;
            }
            case 4: {
                progressCmnd.setVisibility(View.VISIBLE);
                imgNextCmnd.setVisibility(View.GONE);
                llRootCmnd.setClickable(false);

                break;
            }
            case 5: {

                break;
            }
        }
    }

    private void changeStateToVerify(int type) {
        switch (type) {
            case 1: {
                progressBangLai.setVisibility(View.GONE);
                imgNextBangLai.setVisibility(View.GONE);
                imgResultBangLai.setImageResource(R.drawable.ic_wait);
                imgResultBangLai.setVisibility(View.VISIBLE);
                llRootBangLai.setClickable(true);

                break;
            }
            case 2: {
                progressDangkyxe.setVisibility(View.GONE);
                imgNextDangkyxe.setVisibility(View.GONE);
                imgResultDangkyxe.setImageResource(R.drawable.ic_wait);
                imgResultDangkyxe.setVisibility(View.VISIBLE);
                llRootDangkyxe.setClickable(true);

                break;
            }
            case 3: {
                progressBaohiem.setVisibility(View.GONE);
                imgNextBaohiem.setVisibility(View.GONE);
                imgResultBaohiem.setImageResource(R.drawable.ic_wait);
                imgResultBaohiem.setVisibility(View.VISIBLE);
                llRootBaohiem.setClickable(true);

                break;
            }
            case 4: {
                progressCmnd.setVisibility(View.GONE);
                imgNextCmnd.setVisibility(View.GONE);
                imgResultCmnd.setImageResource(R.drawable.ic_wait);
                imgResultCmnd.setVisibility(View.VISIBLE);
                llRootCmnd.setClickable(true);

                break;
            }
            case 5: {

                break;
            }
        }
    }

    private void changeStateToDeny(int type) {
        switch (type) {
            case 1: {
                progressBangLai.setVisibility(View.GONE);
                imgNextBangLai.setVisibility(View.GONE);
                imgResultBangLai.setImageResource(R.drawable.ic_error);
                imgResultBangLai.setVisibility(View.VISIBLE);
                llRootBangLai.setClickable(true);

                break;
            }
            case 2: {
                progressDangkyxe.setVisibility(View.GONE);
                imgNextDangkyxe.setVisibility(View.GONE);
                imgResultDangkyxe.setImageResource(R.drawable.ic_error);
                imgResultDangkyxe.setVisibility(View.VISIBLE);
                llRootBangLai.setClickable(true);

                break;
            }
            case 3: {
                progressBaohiem.setVisibility(View.GONE);
                imgNextBaohiem.setVisibility(View.GONE);
                imgResultBaohiem.setImageResource(R.drawable.ic_error);
                imgResultBaohiem.setVisibility(View.VISIBLE);
                llRootBangLai.setClickable(true);

                break;
            }
            case 4: {
                progressCmnd.setVisibility(View.GONE);
                imgNextCmnd.setVisibility(View.GONE);
                imgResultCmnd.setImageResource(R.drawable.ic_error);
                imgResultCmnd.setVisibility(View.VISIBLE);
                llRootBangLai.setClickable(true);

                break;
            }
            case 5: {

                llRootBangLai.setClickable(true);

                break;
            }
        }
    }

    private void changeStateToSuccess(int type) {
        switch (type) {
            case 1: {
                progressBangLai.setVisibility(View.GONE);
                imgNextBangLai.setVisibility(View.GONE);
                imgResultBangLai.setImageResource(R.drawable.ic_success);
                imgResultBangLai.setVisibility(View.VISIBLE);
                llRootBangLai.setClickable(false);
                break;
            }
            case 2: {
                progressDangkyxe.setVisibility(View.GONE);
                imgNextDangkyxe.setVisibility(View.GONE);
                imgResultDangkyxe.setImageResource(R.drawable.ic_success);
                imgResultDangkyxe.setVisibility(View.VISIBLE);
                llRootDangkyxe.setClickable(false);

                break;
            }
            case 3: {
                progressBaohiem.setVisibility(View.GONE);
                imgNextBaohiem.setVisibility(View.GONE);
                imgResultBaohiem.setImageResource(R.drawable.ic_success);
                imgResultBaohiem.setVisibility(View.VISIBLE);
                llRootBaohiem.setClickable(false);

                break;
            }
            case 4: {
                progressCmnd.setVisibility(View.GONE);
                imgNextCmnd.setVisibility(View.GONE);
                imgResultCmnd.setImageResource(R.drawable.ic_success);
                imgResultCmnd.setVisibility(View.VISIBLE);
                llRootCmnd.setClickable(false);

                break;
            }
            case 5: {


                break;
            }
        }
    }

    @OnClick(R.id.tv_go_main)
    public void onViewClicked() {
        getDialogProgress().showDialog();
        Call<ApiResponse> click = getmApi().click(ApiConstants.API_KEY,getCurrentIdUser());
        click.enqueue(new CallBackCustom<ApiResponse>(UpdateActivity.this, getDialogProgress(), new OnResponse<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse object) {
                getmSetting().put(Constants.CLICK_GOMAP, true);
                startActivity(new Intent(UpdateActivity.this, MapsActivity.class));
                finish();
            }
        }));

    }
}

package com.goixeomdriver.Activity;

import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;

import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.models.WalletModel;
import com.goixeomdriver.utils.Constants;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class WalletActivity extends BaseActivity {

    @BindView(R.id.tv_taikhoan)
    CustomTextView tvTaikhoan;
    @BindView(R.id.tv_date)
    CustomTextView tvDate;
    @BindView(R.id.tv_diduong)
    CustomTextView tvDiduong;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_toolbar)
    CustomTextView txtToolbar;

    @Override
    public void pingNotification(String title, String content) {
        getWallet();
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
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        txtToolbar.setText(Html.fromHtml(getString(R.string.txt_toolbar_wallet)));
        getWallet();
    }

    private void getWallet() {
        getDialogProgress().showDialog();
        Call<ApiResponse<WalletModel>> getNoti = getmApi().getWallet(ApiConstants.API_KEY, getmSetting().getInt(Constants.ID));
        getNoti.enqueue(new CallBackCustom<ApiResponse<WalletModel>>(this, getDialogProgress(), new OnResponse<ApiResponse<WalletModel>>() {
            @Override
            public void onResponse(ApiResponse<WalletModel> object) {
                if (object.getData() != null) {
                    tvDate.setText(String.format("Lần nạp cuối %s", object.getData().getDate()));
                    tvDiduong.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(object.getData().getRoad()) + " VNĐ");
                    tvTaikhoan.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(object.getData().getAccount()) + " VNĐ");
                }
            }
        }));
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        vibrate();

        finish();
    }
}

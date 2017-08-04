package com.goixeomdriver.Activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.adapters.AdapterNotification;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.models.NotificationData;
import com.goixeomdriver.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class NotificationActivity extends BaseActivity {


    Unbinder unbinder;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_toolbar)
    CustomTextView txtToolbar;
    @BindView(R.id.rcv)
    RecyclerView rcv;
    AdapterNotification adapterNotification;
    List<NotificationData> list;
    @BindView(R.id.img_empty)
    ImageView imgEmpty;

    public NotificationActivity() {
    }

    @Override
    public void pingNotification(String title, String content) {
        getNotification();
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
        setContentView(R.layout.content_notification);
        unbinder = ButterKnife.bind(this);
        list = new ArrayList<>();
        adapterNotification = new AdapterNotification(this, list);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setHasFixedSize(true);
        rcv.setAdapter(adapterNotification);
        getNotification();
        txtToolbar.setText(Html.fromHtml(getString(R.string.txt_toolbar_notification)));
    }

    private void getNotification() {
        getDialogProgress().showDialog();
        Call<ApiResponse<List<NotificationData>>> getNoti = getmApi().getNotification(ApiConstants.API_KEY, getmSetting().getInt(Constants.ID));
        getNoti.enqueue(new CallBackCustom<ApiResponse<List<NotificationData>>>(this, getDialogProgress(), new OnResponse<ApiResponse<List<NotificationData>>>() {
            @Override
            public void onResponse(ApiResponse<List<NotificationData>> object) {
                if (object.getData() != null) {
                    list.clear();
                    list.addAll(object.getData());
                    adapterNotification.notifyDataSetChanged();
                    if (object.getData().size() > 0) {
                        imgEmpty.setVisibility(View.GONE);

                    } else {
                        imgEmpty.setVisibility(View.VISIBLE);
                    }
                }else{
                    imgEmpty.setVisibility(View.VISIBLE);
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

package com.goixeomdriver.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.goixeom.R;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.application.MainApplication;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.models.User;
import com.goixeomdriver.utils.Constants;

import retrofit2.Call;


public class SplashScreenActivity extends BaseAuthActivity implements Runnable {
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showNotifyOpenApp();
        if (getIntent() != null) {
            if (getIntent().getBooleanExtra(Constants.MSG, false)) {
                run();
                return;
            }
        }

        setContentView(R.layout.activity_splash_screen);
        if (getmSetting().getInt(Constants.ID) != -1) {
            getInfor(getmSetting().getInt(Constants.ID));
        } else {
            handler = new Handler();
            handler.postDelayed(this, 2000);
        }
    }

    private void getInfor(final int id) {
        Call<ApiResponse<User>> getInfo = getmApi().getInfor(ApiConstants.API_KEY, id);
//        getDialogProgress().showDialog();
        getInfo.enqueue(new CallBackCustom<ApiResponse<User>>(this, new OnResponse<ApiResponse<User>>() {
            @Override
            public void onResponse(ApiResponse<User> object) {
                if (object.getData() != null) {
                    MainApplication.getInstance().setmUser(object.getData());

                    if (object.getData().getActive() == 2) {
                        Intent intent;
                        intent = new Intent(SplashScreenActivity.this, SliderInfoActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        return;
                    }
                    if (object.getData().getClicked() == 1 && object.getData().getActive() == 1) {
                        getmSetting().put(Constants.CLICK_GOMAP, true);
                        startActivity(new Intent(SplashScreenActivity.this, MapsActivity.class));
                        finish();
                        return;
                    } else {
                        getmSetting().put(Constants.CLICK_GOMAP, false);
                        startActivity(new Intent(SplashScreenActivity.this, UpdateActivity.class));
                        finish();
                    }


                } else {
                    Intent intent;
                    intent = new Intent(SplashScreenActivity.this, SliderInfoActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    return;
                }
            }
        }));
    }

    private void showNotifyOpenApp() {
        Intent notificationIntent = new Intent(this, SplashScreenActivity.class);
        notificationIntent.putExtra(Constants.MSG, true);
//        notificationIntent.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int)System.currentTimeMillis(), notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Ứng dụng đang chạy")
                .setContentIntent(pendingIntent).build();
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(1337, notification);
    }

    @Override
    public void run() {
        Intent intent;
        if (getmSetting().getBoolean(Constants.IS_SLIDE, false) && getmSetting().getInt(Constants.ID) != -1) {
            getInfor(getmSetting().getInt(Constants.ID));
        } else {
            intent = new Intent(SplashScreenActivity.this, SliderInfoActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

package com.goixeomdriver.Activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.goixeom.R;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.application.MainApplication;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.models.AppConfig;
import com.goixeomdriver.models.User;
import com.goixeomdriver.utils.Constants;
import com.goixeomdriver.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashScreenActivity extends BaseAuthActivity  {
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
          run();
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
                       run();
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

    public void run() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    10000);
        } else {
            getAppConfig();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10000 ){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED){
                showDialogPermission();
            }else{
                getAppConfig();
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
                        ActivityCompat.requestPermissions(SplashScreenActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                10000);
                    }
                }).show();
    }
    private void getAppConfig() {
        Call<ApiResponse<AppConfig>> getApp = getmApi().getAppConfig(ApiConstants.API_KEY);
        getApp.enqueue(new CallBackCustom<ApiResponse<AppConfig>>(this, new OnResponse<ApiResponse<AppConfig>>() {
            @Override
            public void onResponse(ApiResponse<AppConfig> object) {
                getmSetting().put(Constants.TIME_UPDATE, object.getData().getTimeUpdate());
                getmSetting().put(Constants.TIME_WAIT, object.getData().getTimeWait());
                downloadImage(object.getData());
            }
        }));
    }
    private void downloadImage(final AppConfig app) {
        Call<ResponseBody> download = getmApi().downloadFileWithDynamicUrlAsync(app.getBike());
        download.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            File writtenToDisk = writeResponseBodyToDisk(response.body(), getString(R.string.bike));

                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            downloadImageCar(app.getCar());
                        }
                    }.execute();
                } else {

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }
        });
    }

    private void downloadImageCar(String Url) {
        Call<ResponseBody> download = getmApi().downloadFileWithDynamicUrlAsync(Url);
        download.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            File writtenToDisk = writeResponseBodyToDisk(response.body(),  getString(R.string.car));
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent;
                                intent = new Intent(SplashScreenActivity.this, MapsActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                    }.execute();
                } else {
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }
        });
    }
    private File writeResponseBodyToDisk(ResponseBody body, String name) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(FileUtils.getFolder(this)  + name);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    LogUtils.e("file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return futureStudioIconFile;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }
}

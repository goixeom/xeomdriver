package com.goixeomdriver.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.goixeom.R;
import com.goixeomdriver.apis.ApiUtils;
import com.goixeomdriver.apis.GoiXeOmAPI;
import com.goixeomdriver.application.MainApplication;
import com.goixeomdriver.socket.GPSService;
import com.goixeomdriver.socket.SocketClient;
import com.goixeomdriver.socket.SocketConstants;
import com.goixeomdriver.socket.SocketResponse;
import com.goixeomdriver.utils.CommonUtils;
import com.goixeomdriver.utils.Constants;
import com.goixeomdriver.views.AlertDialogCustom;
import com.goixeomdriver.views.ProgressDialogCustom;
import com.google.gson.Gson;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


/**
 * Created by DuongKK on 6/16/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public abstract void pingNotification(String title, String content);

    public abstract void onSocketReady();

    public abstract void onLocationChange(Location location);

    protected int getCurrentIdUser() {
        int id = getmSetting().getInt(Constants.ID);
        if (id == -1) {
            logout();
            return -1;
        }
        return id;
    }

    private ProgressDialogCustom dialogProgress;
    private GoiXeOmAPI mGoiXeOmApi;
    private SPUtils mSetting = new SPUtils(Constants.SETTING);
    private boolean mBounded;
    protected SocketClient mSocket;
    ViewGroup view;
    protected Location mLocation;
    protected Location oldLocation;
    Vibrator vibe;
    ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.e("disconnect socket");
            BaseActivity.this.mBounded = false;
            BaseActivity.this.mSocket = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
//            LogUtils.e("connected umbersk");
            BaseActivity.this.mBounded = true;
            SocketClient.LocalBinder mLocalBinder = (SocketClient.LocalBinder) service;
            BaseActivity.this.mSocket = mLocalBinder.getServerInstance();
            String imei = PhoneUtils.getIMEI();
            getmSocket().updateNewImei(imei, mSetting.getInt(Constants.ID));
            onSocketReady();
        }
    };
    BroadcastReceiver receiverConnectionNetwork = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isConnected()) {
//                if(getmSocket()!=null) getmSocket().disconnect();
                if (dialogNetwork == null)
                    dialogNetwork = AlertDialogCustom.dialogMessage(BaseActivity.this);
                dialogNetwork.show();
                if (view != null)
                    CommonUtils.disable(view);

            } else {
                if(getmSocket()!=null) getmSocket().reconection();
                if (dialogNetwork != null) dialogNetwork.dismiss();
                if (view != null)
                    CommonUtils.enable(view);
            }

        }
    };

    BroadcastReceiver receiverGPS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                mLocation = (Location) intent.getParcelableExtra(Constants.LOCATION);
                onLocationChange(mLocation);
                float bearing = intent.getFloatExtra(Constants.ANGLE_TO, 0);
//                getmSocket().updateLatlngAngle(mLocation.getLatitude(), mLocation.getLongitude(), 45, bearing, getCurrentIdUser());
                oldLocation = mLocation;
            }
        }
    };

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            LogUtils.e("Reciever Recieve data");
            if (intent != null) {
                //Connection
                String connection = intent.getStringExtra(SocketConstants.KEY_STATUS_CONNECTION);
                if (connection != null && !connection.isEmpty()) {
                    LogUtils.e("connection : " + connection);
                    if (connection.equals(getString(R.string.connected))) {
                        onSocketReady();
                    }
                    if (view != null) {
                        if (connection.equals(getString(R.string.disconnect))) {
                            Crouton.makeText(BaseActivity.this, connection, Style.ALERT, view).show();
                            CommonUtils.disable(view);
                        } else if (connection.equals(getString(R.string.connected))) {
                            showSnackBar(connection);
                            CommonUtils.enable(view);
                            if (ActivityCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                                return;
                            }


                        } else {
                            Crouton.makeText(BaseActivity.this, connection, Style.INFO, view).show();
                            CommonUtils.disable(view);
                        }
                    }
                    return;
                }
                //Profile
                String jsonResponse = intent.getStringExtra(Constants.PROFILE);
                SocketResponse response = new Gson().fromJson(jsonResponse, SocketResponse.class);
                if (response != null && getmSetting().getInt(Constants.ID) != -1 && response.getIdDriver() == getmSetting().getInt(Constants.ID)) {
                    LogUtils.e("Profile - " + jsonResponse);
                    if (response.getImei() != null && !response.getImei().equals(PhoneUtils.getIMEI())) {
                        logout();
                        return;
                    }
                    if (response.getStatus() == 2) {
                        //BLOCK
                        BLOCK();

                    }
                    return;
                }

                //Notification
                String jsonResponseNotification = intent.getStringExtra(Constants.NOTIFICATION_SOCKET);
                SocketResponse responseNotifcation = new Gson().fromJson(jsonResponseNotification, SocketResponse.class);
                if (responseNotifcation != null) {
                    LogUtils.e("responseNotifcation - " + responseNotifcation);
                    if (responseNotifcation.getType() == Constants.TYPE_ALL_SYSTEM || responseNotifcation.getType() == Constants.TYPE_DRIVER) {
                        if (responseNotifcation.getTypeDetail() == Constants.TYPE_ALL_DRIVER || responseNotifcation.getTypeDetail() == Constants.TYPE_ALL || responseNotifcation.getTypeDetail() == MainApplication.getInstance().getmUser().getType()) {
                            if (responseNotifcation.getCategory() == Constants.PROMOTION_TYPE) {
                                //     CommonUtils.createNotificationWithMsg(getApplicationContext(), responseNotifcation.getTitle(), responseNotifcation.getContent(), Constants.PROMOTION_TYPE);
                                pingNotification(responseNotifcation.getTitle(), responseNotifcation.getContent());
                            } else {
                                pingNotification(responseNotifcation.getTitle(), responseNotifcation.getContent());
                                //CommonUtils.createNotificationWithMsg(getApplicationContext(), responseNotifcation.getTitle(), responseNotifcation.getContent(), Constants.NOTIFICATION_NORMAL_TYPE);
                            }
                        }
                    }
                    return;
                }

                String jsonNotifyWallet = intent.getStringExtra(Constants.NOTIFICATION_WALLET);
                SocketResponse responseNotifyWallet = new Gson().fromJson(jsonNotifyWallet, SocketResponse.class);
                if (responseNotifyWallet != null && getCurrentIdUser() != -1) {
                    pingNotification("Ví tiền", "");
                    return;
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String imei = PhoneUtils.getIMEI();
            getmSocket().updateNewImei(imei, MainApplication.getInstance().getmUser().getId());
        }
    }

    protected void BLOCK() {
        getmSocket().updateStatusDriver(0, getmSetting().getInt(Constants.ID));
        getmSetting().put(Constants.PHONE, -1);
        getmSetting().put(Constants.ID, -1);
        getmSetting().put(Constants.IS_SLIDE, false);
        getmSetting().put(Constants.TYPE_PASSWORD_MAP, false);
        getmSetting().put(Constants.CLICK_GOMAP, false);
        MainApplication.getInstance().setmUser(null);
        Intent i = new Intent(BaseActivity.this, SliderInfoActivity.class);
        i.putExtra(Constants.STATUS, 2);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);
        startActivity(i);
        finishAffinity();
    }

    protected void goHome() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public void logout() {
//        getmSocket().updateStatusDriver(0, getmSetting().getInt(Constants.ID));
        getmSetting().put(Constants.PHONE, -1);
        getmSetting().put(Constants.ID, -1);
        getmSetting().put(Constants.IS_SLIDE, false);
        getmSetting().put(Constants.TYPE_PASSWORD_MAP, false);
        getmSetting().put(Constants.CLICK_GOMAP, false);
        getmSetting().put(Constants.TYPE_USER_STR, -1);
        MainApplication.getInstance().setmUser(null);
        Intent i = new Intent(BaseActivity.this, SliderInfoActivity.class);
        i.putExtra(Constants.STATUS, 1);
        startActivity(i);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);
        finishAffinity();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogProgress = new ProgressDialogCustom(this);
        mGoiXeOmApi = ApiUtils.getRootApi().create(GoiXeOmAPI.class);
        dialogError = new MaterialDialog.Builder(this).title(R.string.error)
                .content(getString(R.string.unknow_error))
                .positiveText(R.string.dismis)
                .positiveColor(Color.GRAY)
                .build();

        IntentFilter intentFilter = new IntentFilter(GPSService.ACTION_LOCATION_UPDATE);
        registerReceiver(receiverGPS, intentFilter);
        IntentFilter iConnection = new IntentFilter();
        iConnection.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.receiverConnectionNetwork, iConnection);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    protected void vibrate() {
        if (getmSetting().getBoolean(Constants.SETTING_VIBARATE, true))
            vibe.vibrate(100);
    }

    public void showDialogErrorContent(String content) {
        if (dialogError != null) {
            dialogError.setContent(content);
            dialogError.show();
        } else {
            dialogError = new MaterialDialog.Builder(this).title(R.string.error)
                    .content(content)
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
        }
    }

    public void showDialog(String title, String content) {
        if (dialogError != null) {
            dialogError.setTitle(title);
            dialogError.setContent(content);
            dialogError.show();
        } else {
            dialogError = new MaterialDialog.Builder(this).title(title)
                    .content(content)
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
        }
    }

    private MaterialDialog dialogError;

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtils.e("onLowMemory OS may kill my service");
        getmSetting().getBoolean(Constants.OS_KILLAPP,true);
    }


    @Override
    public void onDestroy() {
//        getmSocket().updateStatusDriver(1,getCurrentIdUser());
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);

        super.onDestroy();
        if (getDialogProgress() != null && getDialogProgress().isShowing())
            getDialogProgress().hideDialog();
        if (dialogError != null && dialogError.isShowing()) dialogError.dismiss();
        if (this.mBounded) {
            unbindService(this.mConnection);
            this.mBounded = false;
        }
//        Intent intentToGPS = new Intent(this, GPSService.class);

//        stopService(intentToGPS);
        unregisterReceiver(receiverGPS);
        unregisterReceiver(this.receiverConnectionNetwork);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialogNetwork != null) dialogNetwork.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getmSetting().getBoolean(Constants.OS_KILLAPP,false);
        Intent mIntent = new Intent(this, SocketClient.class);
//        startService(mIntent);
        bindService(mIntent, this.mConnection, BIND_AUTO_CREATE);
//        Intent intentToGPS = new Intent(this, GPSService.class);
//        startService(intentToGPS);
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter i = new IntentFilter();
        i.addAction(SocketConstants.EVENT_CONNECTION);
        registerReceiver(this.receiver, i);

        if (getCurrentIdUser() == -1) {
            logout();
        } else if (getCurrentIdUser() == -2) {
            BLOCK();
        }

        if (getmSetting().getBoolean(Constants.SETTING_VIBARATE, true) && view != null) {
            CommonUtils.vibrate(view);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.receiver);
    }

    public void showSnackBar(String content) {
        Crouton.cancelAllCroutons();
        Style croutonStyle = new Style.Builder().setBackgroundColorValue(Color.parseColor("#25b45b")).build();
//        Crouton.makeText(this, content, croutonStyle, view).setConfiguration(new Configuration.Builder().setDuration(Configuration.DURATION_INFINITE).build()).show();
        Crouton.makeText(this, content, croutonStyle, view).show();
    }

    AlertDialog dialogNetwork;



    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    public SPUtils getmSetting() {
        if (mSetting == null) mSetting = new SPUtils(Constants.SETTING);
        return mSetting;
    }

    public void setmSetting(SPUtils mSetting) {
        this.mSetting = mSetting;
    }

    public ProgressDialogCustom getDialogProgress() {
        return dialogProgress;
    }

    public void setDialogProgress(ProgressDialogCustom dialogProgress) {
        this.dialogProgress = dialogProgress;
    }

    public GoiXeOmAPI getmApi() {
        return mGoiXeOmApi;
    }

    public void setmViCoApi(GoiXeOmAPI mCanetsService) {
        this.mGoiXeOmApi = mCanetsService;
    }

    public SocketClient getmSocket() {
        return mSocket;
    }

    public void setmSocket(SocketClient mSocket) {
        this.mSocket = mSocket;
    }

}

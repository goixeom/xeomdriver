package com.goixeomdriver.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.goixeom.R;
import com.goixeomdriver.apis.ApiUtils;
import com.goixeomdriver.apis.GoiXeOmAPI;
import com.goixeomdriver.socket.SocketClient;
import com.goixeomdriver.utils.CommonUtils;
import com.goixeomdriver.utils.Constants;
import com.goixeomdriver.views.AlertDialogCustom;
import com.goixeomdriver.views.ProgressDialogCustom;


/**
 * Created by DuongKK on 6/16/2017.
 */

public class BaseAuthActivity extends AppCompatActivity {
    private ProgressDialogCustom dialogProgress;
    private GoiXeOmAPI mGoiXeOmApi;
    private SPUtils mSetting = new SPUtils(Constants.SETTING);



    AlertDialog dialogNetwork;
    protected ViewGroup view;
    BroadcastReceiver receiverConnectionNetwork = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isConnected()) {
                if (dialogNetwork == null)
                    dialogNetwork = AlertDialogCustom.dialogMessage(BaseAuthActivity.this);
                dialogNetwork.show();
                if(view != null)
                CommonUtils.disable(view);

            } else {
                if (dialogNetwork != null) dialogNetwork.dismiss();
                if(view != null)

                    CommonUtils.enable(view);

            }

        }
    };

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
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
    public void onDestroy() {
        super.onDestroy();
        if (getDialogProgress() != null && getDialogProgress().isShowing())
            getDialogProgress().hideDialog();
        if (dialogError != null && dialogError.isShowing()) dialogError.dismiss();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (this.mBounded) {
//            unbindService(this.mConnection);
//            this.mBounded = false;
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Intent mIntent = new Intent(this, SocketClient.class);
////        startService(mIntent);
//        bindService(mIntent, this.mConnection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        IntentFilter i = new IntentFilter();
//        i.addAction(SocketConstants.EVENT_CONNECTION);
        IntentFilter iConnection = new IntentFilter();
        iConnection.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.receiverConnectionNetwork, iConnection);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.receiverConnectionNetwork);

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


}

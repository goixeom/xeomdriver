package com.goixeomdriver.apis;

import android.content.Context;
import android.graphics.Color;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.goixeom.R;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.views.ProgressDialogCustom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MyPC on 11/6/2016.
 */

public class CallBackCustomNoDelay<T> implements Callback<T> {
    OnResponse<T> t;
    ProgressDialogCustom dialogCustom;
    Context context;

    public CallBackCustomNoDelay(Context context, ProgressDialogCustom dialogCustom, OnResponse<T> t) {
        this.dialogCustom = dialogCustom;
        this.t = t;
        this.context = context;
    }

    public CallBackCustomNoDelay(Context context, OnResponse<T> t) {
        this.context = context;
        this.t = t;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {
            if (dialogCustom != null)
                dialogCustom.hideDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response.isSuccessful()) {
            t.onResponse(response.body());


        } else {
            new MaterialDialog.Builder(context).title(R.string.error)
                    .content(context.getString(R.string.unknow_error))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            LogUtils.e("Error onRespone " + response.code());
//            RLog.e(call.request().body().contentType().toString());
//            RLog.e(call.request().headers().toString());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        try {
            if (dialogCustom != null)
                dialogCustom.hideDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        RLog.e(call.request().body().contentType().toString());
//        RLog.e(call.request().headers().toString());
        new MaterialDialog.Builder(context).title(R.string.error)
                .content(context.getString(R.string.unknow_error))
                .positiveText(R.string.dismis)
                .positiveColor(Color.GRAY)
                .show();
        LogUtils.e("error - callback " + t.getMessage());
    }
}

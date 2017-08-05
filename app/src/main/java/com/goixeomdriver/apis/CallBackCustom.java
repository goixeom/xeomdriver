package com.goixeomdriver.apis;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.goixeom.R;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.views.ProgressDialogCustom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MyPC on 11/6/2016.
 */

public class CallBackCustom<T> implements Callback<T> {
    OnResponse<T> t;
    ProgressDialogCustom dialogCustom;
    Context context;

    public CallBackCustom(Context context, ProgressDialogCustom dialogCustom, OnResponse<T> t) {
        this.dialogCustom = dialogCustom;
        this.t = t;
        this.context = context;
    }

    public CallBackCustom(Context context, OnResponse<T> t) {
        this.context = context;
        this.t = t;
    }

    @Override
    public void onResponse(Call<T> call, final Response<T> response) {
        if (dialogCustom != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    try {
                        dialogCustom.hideDialog();
//                    if (dialogCustom != null) {
//                        new AsynDialog(context, dialogCustom, response, t).execute();
//                    }
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
                    }
                }
            }.execute();
        } else {
            if (response.isSuccessful()) {
                t.onResponse(response.body());
            } else {
                new MaterialDialog.Builder(context).title(R.string.error)
                        .content(context.getString(R.string.unknow_error))
                        .positiveText(R.string.dismis)
                        .positiveColor(Color.GRAY)
                        .show();
                LogUtils.e("Error onRespone " + response.code());
            }
        }

    }

    @Override
    public void onFailure(Call<T> call, final Throwable t) {
        if (dialogCustom != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    try {

                        dialogCustom.hideDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(!NetworkUtils.isConnected()) return;

//        RLog.e(call.request().body().contentType().toString());
//        RLog.e(call.request().headers().toString());
                    new MaterialDialog.Builder(context).title(R.string.error)
                            .content(context.getString(R.string.unknow_error))
                            .positiveText(R.string.dismis)
                            .positiveColor(Color.GRAY)
                            .show();
                    LogUtils.e("error - callback " + t.getMessage());
                }
            }.execute();
        } else {
            if(!NetworkUtils.isConnected()) return;
            new MaterialDialog.Builder(context).title(R.string.error)
                    .content(context.getString(R.string.unknow_error))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            LogUtils.e("error - callback " + t.getMessage());
        }
    }


}

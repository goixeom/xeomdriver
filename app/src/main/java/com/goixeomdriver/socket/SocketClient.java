package com.goixeomdriver.socket;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.parser.Packet;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.goixeom.R;
import com.goixeomdriver.Activity.SliderInfoActivity;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.ApiUtils;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.apis.GoiXeOmAPI;
import com.goixeomdriver.application.MainApplication;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.models.User;
import com.goixeomdriver.utils.CommonUtils;
import com.goixeomdriver.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Call;


public class SocketClient extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    IBinder mBinder;
    Socket socket;
    public boolean isContinuteBooking = false;
    SPUtils mSetting = new SPUtils(Constants.SETTING);
    public static final String TAG = "socket.driver";
    Notification notification;
    CountDownTimer countDownTimer;
    int idBooking;
    int idUser;
    private boolean isCancelable = true;
//    public void disconnect() {
//        LogUtils.e("disconnect now");
//        this.socket.disconnect();
//    }
    public void reconection() {
        if(socket!=null && !socket.connected()) {
            LogUtils.e("connectinggggggg..");
            socket.connect();
            return;
        }
        initSocket();
    }
    public class LocalBinder extends Binder {
        public SocketClient getServerInstance() {
            return SocketClient.this;
        }
    }
    public void onDestroy() {
        stopLocationUpdate();
        super.onDestroy();
        LogUtils.e("onDestroy");
        this.socket.disconnect();
        Intent broadcastIntent = new Intent("chayngam.restart2");
        sendBroadcast(broadcastIntent);
    }
    public void onCreate() {
        super.onCreate();
        LogUtils.e("OnCreate");
        //creatNotify();
        mBinder = new LocalBinder();
        if (this._gac == null) {
            this._gac = new GoogleApiClient.Builder(getApplicationContext()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
        this._gac.connect();
        initSocket();
        countDownTimer = new CountDownTimer(21000, 1000) {
            @Override
            public void onTick(long l) {
                l -= 1000;
                LogUtils.e("Count down cancle from service : " + l / 1000);
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(Constants.UPDATE_TIME_COUNTDOWN, l);
                SocketClient.this.sendBroadcast(intent);
            }

            @Override
            public void onFinish() {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(2);
                if (isCancelable) {
                    updateStatusBooking(SocketConstants.STATUS_DRIVER_CANCLE, idBooking, mSetting.getInt(Constants.ID), idUser);
                    mSetting.put(Constants.IDTRIP, -1);
                    mSetting.put(Constants.IDUSER, -1);
                    mSetting.put(Constants.ISSTART, false);
                    mSetting.put(Constants.ISGOIN, false);
                    Intent intentTimeout = new Intent();
                    intentTimeout.setAction(SocketConstants.EVENT_CONNECTION);
                    intentTimeout.putExtra(Constants.TIME_OUT_BOOKING, true);
                    SocketClient.this.sendBroadcast(intentTimeout);
                    showNotificationInStack();

                    //                LogUtils.e("Count down cancle from service : " + 0);
                    //                Intent intent = new Intent();
                    //                intent.setAction(SocketConstants.EVENT_CONNECTION);
                    //                intent.putExtra(Constants.UPDATE_TIME_COUNTDOWN, 0);
                    //                SocketClient.this.sendBroadcast(intent);
                    //                handler.postDelayed(new Runnable() {
                    //                    @Override
                    //                    public void run() {
                    //
                    //                        }
                    //                    }
                    //                },1000);
                }
            }
        };
    }
    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }
    //true to disable countdown
    public void setCancelable(boolean join) {
        isCancelable = join;
    }
    public void uploadSuccessProfile(int type, int id) {
        if (socket == null) return;

        JSONObject objJoin = new JSONObject();
        try {
            objJoin.put("type", type);
            objJoin.put("id", id);
            this.socket.emit("upload", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                    JSONObject object = (JSONObject) args[0];
                    LogUtils.d(object.toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void updateStatusDriver(int status, int id) {
        if (socket == null) return;
        JSONObject objJoin = new JSONObject();
        try {

            objJoin.put("status", status);
            objJoin.put("d_id", id);
            this.socket.emit("update-status", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                    JSONObject object = (JSONObject) args[0];
                    LogUtils.d(object.toString());
                }
            });
            LogUtils.e("Update status driver  to " + status);
            mSetting.put(Constants.STATUS_DRIVER,status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void updateNewImei(String imei, int id) {
//        LogUtils.e("Socket ready update imei --- " + imei);
        JSONObject objJoin = new JSONObject();
        try {
            objJoin.put("imei", imei);
            objJoin.put("d_id", id);
//            this.socket.emit("update_imei_driver", objJoin, new Ack() {
//                @Override
//                public void call(Object... args) {
//                }
//            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void updateStatusBooking(int status, int idTrip, int Driver, int uid, double lat, double lng) {
        JSONObject objJoin = new JSONObject();
        try {
            objJoin.put("status", status);
            objJoin.put("d_id", Driver);
            objJoin.put("t_id", idTrip);
            objJoin.put("u_id", uid);
            objJoin.put("lat", lat);
            objJoin.put("lng", lng);
            this.socket.emit("receive-trip", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                }
            });
            isCancelable = false;
            LogUtils.e("Update trip : " + idTrip + " - " + status);
            if (status == 9 || status == 3) isContinuteBooking = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateStatusBooking(int status, int idTrip, int Driver, int uid) {
        JSONObject objJoin = new JSONObject();
        countDownTimer.cancel();
        try {
            objJoin.put("status", status);
            objJoin.put("d_id", Driver);
            objJoin.put("t_id", idTrip);
            objJoin.put("u_id", uid);

            this.socket.emit("receive-trip", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                }
            });
            isCancelable = false;
            if (status == 9 || status == 3) isContinuteBooking = false;
            LogUtils.e("Update trip : " + idTrip + " - " + status);
            if (status == SocketConstants.STATUS_DRIVER_CANCLE || status == SocketConstants.STATUS_FINISH) {
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(Constants.STATUS, String.format("{d_id=%d,status=1}", Driver));
                SocketClient.this.sendBroadcast(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateDriverId(int id) {
        JSONObject objJoin = new JSONObject();
        try {
            objJoin.put("d_id", id);
            this.socket.emit("login", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                }
            });
            LogUtils.e("Update id driver : " + id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void listenProfileUpdateVerify() {
        socket.off("notify-upload");
        this.socket.on("notify-upload", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                LogUtils.e("notify-from server update profile " + object.toString());
                SocketResponse response = new Gson().fromJson(object.toString(), SocketResponse.class);
                if (response != null && response.getIdDriver() == mSetting.getInt(Constants.ID)) {
                    Notification notification = CommonUtils.createNotificationUpdateProfile(getApplicationContext(), getString(R.string.app_name), response.getContent());
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(2, notification);
                }
                final Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_PROFILE);
                intent.putExtra(SocketConstants.KEY_UPDATE_PROFILE, object.toString());
                SocketClient.this.sendBroadcast(intent);
            }
        });
    }


    public void feedback(String feedbackContent) {
        JSONObject objJoin = new JSONObject();
        try {
            objJoin.put("id", (mSetting.getInt(Constants.ID)));
            objJoin.put("content", feedbackContent);
            this.socket.emit("feedback", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                    JSONObject object = (JSONObject) args[0];
                    LogUtils.d(object.toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public IBinder onBind(Intent intent) {
        LogUtils.e("onBind");
        initSocket();
        //  creatNotify();
        return this.mBinder;

    }

    public void showNotificationInStack() {
        LogUtils.e("Noti from stack");
        if (notification != null && !isContinuteBooking) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(2, notification);
            notification = null;
            LogUtils.e("we Notified from stack");

        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mSetting.put(Constants.OS_KILLAPP, true);
    }
    private void initSocket() {
        if (mSetting.getInt(Constants.ID) == -1 || !NetworkUtils.isConnected() || (socket != null && socket.connected()))
            return;
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
            }};
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, null);
            IO.setDefaultSSLContext(sc);
            IO.Options opts = new IO.Options();
            opts.secure = true;
            opts.reconnection = true;
            opts.reconnectionDelay = 10000;
            opts.reconnectionAttempts=10;
            opts.sslContext = sc;
            this.socket = IO.socket("https://goixeom.com:3001/", opts);
            this.socket.connect();
            LogUtils.e("Set Socket IO", "Socket IO Seting");
        } catch (Exception e) {
            LogUtils.e("Socket Problem", "Try cath", e);
            ToastUtils.showLong(e.getMessage());
        }

        updateDriverId(mSetting.getInt(Constants.ID));
        updateNewImei(PhoneUtils.getIMEI(), mSetting.getInt(Constants.ID));
//        if (mSetting.getBoolean(Constants.CLICK_GOMAP,true)  ) {
        socket.off("send-booking");
        socket.on("send-booking", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                LogUtils.e("send-booking : " + object.toString());
                final SocketResponse responseBooking = new Gson().fromJson(object.toString(), SocketResponse.class);
                if (responseBooking != null && responseBooking.getIdDriver() == mSetting.getInt(Constants.ID)) {
                    isCancelable = true;
                    if (mSetting.getBoolean(Constants.SETTING_NOTIFICATION, true)) {
                        LogUtils.e("Noti send booking : " + object.toString());
                        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(2, CommonUtils.createNotification(getApplicationContext(), "Tài xế", "Bạn vừa nhận một thông báo chuyến đi mới", object.toString()));
                    }
                    idBooking = responseBooking.getIdTrip();
                    idUser = responseBooking.getIdUser();
                    countDownTimer.start();
                    isContinuteBooking = true;
                    Intent intent = new Intent();
                    intent.setAction(SocketConstants.EVENT_CONNECTION);
                    intent.putExtra(Constants.BOOKING, object.toString());
                    SocketClient.this.sendBroadcast(intent);
                }
            }
        });
        LogUtils.e("Listen send booking");
        socket.off("send-notify");
        socket.on("send-notify", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
//                if (!mSetting.getBoolean(Constants.CLICK_GOMAP, false))
//                    return;
                JSONObject object = (JSONObject) args[0];
                SocketResponse responseNotifcation = new Gson().fromJson(object.toString(), SocketResponse.class);
                if (responseNotifcation != null && mSetting.getInt(Constants.ID) != -1) {
                    if (responseNotifcation.getType() == Constants.TYPE_ALL_SYSTEM || responseNotifcation.getType() == Constants.TYPE_DRIVER) {
                        if (responseNotifcation.getTypeDetail() == Constants.TYPE_ALL_DRIVER || responseNotifcation.getTypeDetail() == Constants.TYPE_ALL || responseNotifcation.getTypeDetail() == mSetting.getInt(Constants.TYPE_USER_STR)) {
                            if (responseNotifcation.getCategory() == Constants.PROMOTION_TYPE) {
                                notification = CommonUtils.createNotificationWithMsg(getApplicationContext(), responseNotifcation.getTitle(), responseNotifcation.getContent(), Constants.PROMOTION_TYPE);
                                showNotificationInStack();
                                LogUtils.e("Create Notification");
                            } else {
                                notification = CommonUtils.createNotificationWithMsg(getApplicationContext(), responseNotifcation.getTitle(), responseNotifcation.getContent(), Constants.NOTIFICATION_NORMAL_TYPE);
                                showNotificationInStack();
                                LogUtils.e("Create Notification");
                            }
                        }
                    }
                }
                LogUtils.e(object.toString());
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(Constants.NOTIFICATION_SOCKET, object.toString());
                SocketClient.this.sendBroadcast(intent);
            }
        });
        socket.off("vote-driver");
        socket.on("vote-driver", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                SocketResponse responseNotifcation = new Gson().fromJson(object.toString(), SocketResponse.class);
                if (responseNotifcation != null && mSetting.getInt(Constants.ID) != -1 && mSetting.getInt(Constants.ID) == responseNotifcation.getIdDriver()) {
                    notification = CommonUtils.createNotificationRating(getApplicationContext(), responseNotifcation.getVote());
                    showNotificationInStack();
                }
                LogUtils.e(object.toString());
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(Constants.RATING, object.toString());
                SocketClient.this.sendBroadcast(intent);
            }
        });
        socket.off("event_profile_driver");
        socket.on("event_profile_driver", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                LogUtils.e(object.toString());
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(Constants.PROFILE, object.toString());
                SocketClient.this.sendBroadcast(intent);
                SocketResponse response = new Gson().fromJson(object.toString(), SocketResponse.class);
                if (response != null && mSetting.getInt(Constants.ID) != -1 && response.getIdDriver() == mSetting.getInt(Constants.ID)) {
                    if (response.getImei() != null && !response.getImei().equals(PhoneUtils.getIMEI())) {       // Kick out from other devices
                        Intent i = new Intent(SocketClient.this, SliderInfoActivity.class);
                        i.putExtra(Constants.MSG, true);
                        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);

                        Notification notification = CommonUtils.createNotification(getApplicationContext()
                                , getString(R.string.error)
                                , "Tài khoản đã đăng nhập trên một thiết bị khác"
                                , i
                        );
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(2, notification);
//                                logout(-1);
                        updateStatusDriver(0, mSetting.getInt(Constants.ID));

                        return;
                    }
                    if (response.getStatus() == 2) {
                        //BLOCK
//                                updateStatusDriver(0, mSetting.getInt(Constants.ID));
                        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);

                        Intent i = new Intent(SocketClient.this, SliderInfoActivity.class);
                        i.putExtra(Constants.STATUS, 2);
                        Notification notification = CommonUtils.createNotification(getApplicationContext()
                                , getString(R.string.error)
                                , "Tài khoản đã bị block. Vui lòng liên hệ với người quản trị để được hỗ trợ"
                                , i
                        );
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(2, notification);
//                                logout(-2);
                        updateStatusDriver(0, mSetting.getInt(Constants.ID));
                    }
                    return;
                }
            }
        });
        socket.off("block-driver");
        socket.on("block-driver", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                LogUtils.e(object.toString());
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(Constants.PROFILE, object.toString());
                SocketClient.this.sendBroadcast(intent);
            }
        });
        socket.off("status-driver");
        socket.on("status-driver", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(Constants.STATUS, object.toString());
                SocketClient.this.sendBroadcast(intent);
                SocketResponse response = new Gson().fromJson(object.toString(), SocketResponse.class);
                if (response != null && response.getIdDriver() == mSetting.getInt(Constants.ID)) {
                    mSetting.put(Constants.STATUS_DRIVER, response.getStatus());
                    LogUtils.e("status-driver : " + response.getStatus());
                }
            }
        });
        LogUtils.e("Status driver saved : " + mSetting.getInt(Constants.STATUS_DRIVER));
            if (mSetting.getInt(Constants.STATUS_DRIVER) != 3) {
                if (mSetting.getBoolean(Constants.USER_TAP_ON, true)) {
                    LogUtils.e("update status to 1 cause OS kill service");
                    updateStatusDriver(1, mSetting.getInt(Constants.ID));
                } else if (!mSetting.getBoolean(Constants.USER_TAP_ON, true)) {
                    LogUtils.e("User Off");
                    updateStatusDriver(0, mSetting.getInt(Constants.ID));
                }
            }
        socket.on("driver", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                LogUtils.e(object.toString());
            }
        });
        //Notify nap tien
        socket.off("notify-wallet");
        socket.on("notify-wallet", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                SocketResponse responseNotifcation = new Gson().fromJson(object.toString(), SocketResponse.class);
                if (responseNotifcation != null && mSetting.getInt(Constants.ID) != -1 && mSetting.getInt(Constants.TYPE_USER_STR) == responseNotifcation.getTypeDetail()) {
                    notification = CommonUtils.createNotificationWithMsg(getApplicationContext(), "Ví tài khoản", responseNotifcation.getContent(), Constants.WALLET);
                    showNotificationInStack();
                    LogUtils.e(object.toString());
                    Intent intent = new Intent();
                    intent.setAction(SocketConstants.EVENT_CONNECTION);
                    intent.putExtra(Constants.NOTIFICATION_WALLET, object.toString());
                    SocketClient.this.sendBroadcast(intent);
                }
            }
        });
        //Notify nap tien
        socket.off("notify-cash-driver");
        socket.on("notify-cash-driver", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                SocketResponse responseNotifcation = new Gson().fromJson(object.toString(), SocketResponse.class);
                if (responseNotifcation != null && mSetting.getInt(Constants.ID) == responseNotifcation.getIdDriver()) {
                    notification = CommonUtils.createNotificationWithMsg(getApplicationContext(), "Ví tài khoản", "Bạn đã nạp thêm " + responseNotifcation.getValue() + "đ vào tài khoản", Constants.WALLET);
                    showNotificationInStack();
                    LogUtils.e(object.toString());
                    Intent intent = new Intent();
                    intent.setAction(SocketConstants.EVENT_CONNECTION);
                    intent.putExtra(Constants.NOTIFICATION_WALLET, object.toString());
                    SocketClient.this.sendBroadcast(intent);
                }

            }
        });
        this.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.e("Connected");
                final Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(SocketConstants.KEY_STATUS_CONNECTION, getString(R.string.connected));
                SocketClient.this.sendBroadcast(intent);
                listenProfileUpdateVerify();

                getInfor(mSetting.getInt(Constants.ID));
                updateDriverId(mSetting.getInt(Constants.ID));

            }
        });

        this.socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                Log.i("desc", "error desc");
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(SocketConstants.KEY_STATUS_CONNECTION, getString(R.string.disconnect));
                SocketClient.this.sendBroadcast(intent);
            }
        });
        this.socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            public void call(Object... args) {
                LogUtils.e("Error", args[0].toString());
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(SocketConstants.KEY_STATUS_CONNECTION, getString(R.string.disconnect));
           //     SocketClient.this.sendBroadcast(intent);
            }
        });
        this.socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                LogUtils.e("Error", "Event Error");
                LogUtils.e(args[0].toString());
            }
        });
        this.socket.on(Packet.ERROR, new Emitter.Listener() {
            public void call(Object... args) {
                LogUtils.e("Error", "Event Error");
                LogUtils.e(args[0].toString());
            }
        });
        this.socket.on(Socket.EVENT_RECONNECTING, new Emitter.Listener() {
            public void call(Object... args) {
                Intent intent = new Intent();
                intent.setAction(SocketConstants.EVENT_CONNECTION);
                intent.putExtra(SocketConstants.KEY_STATUS_CONNECTION, SocketClient.this.getString(R.string.reconnect));
                SocketClient.this.sendBroadcast(intent);
                LogUtils.e(" reconecting ");
            }
        });
        this.socket.on("*", new Emitter.Listener() {
            public void call(Object... args) {
                LogUtils.e("alllll" + args[0].toString());
            }
        });
        this.socket.on(Packet.MESSAGE, new Emitter.Listener() {
            public void call(Object... args) {
                LogUtils.e(Packet.MESSAGE + args[0].toString());
            }
        });
    }
    public void updateLatlngAngle(double lat, double lng, double anlgeFrom, double angleTo, int id) {
        JSONObject objJoin = new JSONObject();
        try {
            objJoin.put("lat", lat);
            objJoin.put("lng", lng);
            objJoin.put("angle", anlgeFrom);
            objJoin.put("d_id", id);
            this.socket.emit("update-latlong", objJoin, new Ack() {
                @Override
                public void call(Object... args) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getInfor(final int id) {
        Call<ApiResponse<User>> getInfo = ApiUtils.getRootApi().create(GoiXeOmAPI.class).getInfor(ApiConstants.API_KEY, id);
        getInfo.enqueue(new CallBackCustom<ApiResponse<User>>(this, new OnResponse<ApiResponse<User>>() {
            @Override
            public void onResponse(ApiResponse<User> object) {
                if (object.getData() != null) {
                    LogUtils.e("Get infor from socket status driver now : "+ object.getData().getStatus() + "driver in booking : "+ object.getData().getIsInBooking());
                    if(object.getData().getStatus() == 2 && mSetting.getBoolean(Constants.USER_TAP_ON,true)) {
                        updateStatusDriver(1,object.getData().getId());
                    }
                    if(object.getData().getIsInBooking()==0 && mSetting.getBoolean(Constants.USER_TAP_ON,true)){
                        updateStatusDriver(1,object.getData().getId());
                    }
                } else {
                }
            }
        }));
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e("onStartCommand");
        reconection();
        return START_STICKY;
    }
    public Socket getSocket() {
        return this.socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(getApplicationContext(), SocketClient.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
        super.onTaskRemoved(rootIntent);
    }

    public void logout(int type) {
        mSetting.put(Constants.PHONE, -1);
        mSetting.put(Constants.ID, type);
        mSetting.put(Constants.IS_SLIDE, false);
        mSetting.put(Constants.TYPE_PASSWORD_MAP, false);
        mSetting.put(Constants.CLICK_GOMAP, false);
        mSetting.put(Constants.TYPE_USER_STR, -1);
        MainApplication.getInstance().setmUser(null);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);
    }
    //GPS
    public static final String ACTION_LOCATION_UPDATE = "1";
    private static Location _location;
    private GoogleApiClient _gac;
    private float mCurrentDegree = 0f;
    Random rand = new Random(System.currentTimeMillis());
    public void onLocationChanged(final Location arg0) {
        LatLng oldLatlng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
//        if (_location != null) {
//            oldLatlng = new LatLng(_location.getLatitude(), _location.getLongitude());
//
//        }
//        Call<ApiResponse<List<Routes>>> direction = ApiUtils.getAPIPLACE().create(GoiXeOmAPI.class)
//                .getDirection(oldLatlng.latitude + "," + oldLatlng.longitude
//                        , arg0.getLatitude() + "," + arg0.getLongitude(), getString(R.string.google_map_id));
//        direction.enqueue(new Callback<ApiResponse<List<Routes>>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<List<Routes>>> call, Response<ApiResponse<List<Routes>>> response) {
//                ApiResponse<List<Routes>> object = response.body();
//                if (object.getRoutes() != null && object.getRoutes().size() > 0 && object.getRoutes().get(0).getLegs() != null && object.getRoutes().get(0).getLegs().size() > 0) {
//                    LatLng latLng = new LatLng(object.getRoutes().get(0).getLegs().get(0).getStart_location().getLat(), object.getRoutes().get(0).getLegs().get(0).getStart_location().getLng());
                    LatLng latLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                    if (_location != null) {
                        //mCurrentDegree = MapUtils.bearingBetweenLocations(latLng, new LatLng(_location.getLatitude(), _location.getLongitude()));
                        mCurrentDegree= rand.nextInt(360) + 1;
                    }
//                    if (_location != null && _location.getLongitude() == latLng.longitude && _location.getLatitude() == latLng.latitude)
//                        return;
                    if (_location == null) _location = new Location("");
                    _location.setLatitude(latLng.latitude);
                    _location.setLongitude(latLng.longitude);
                    Intent i = new Intent(ACTION_LOCATION_UPDATE);
                    i.putExtra(Constants.LOCATION, _location);
                    i.putExtra(Constants.ANGLE_TO, mCurrentDegree);
                    LogUtils.e("Update location : " + _location.getLatitude() + " - " + _location.getLongitude() + "- bearing : " + mCurrentDegree);
                    mSetting.put(Constants.LAT, (float) _location.getLatitude());
                    mSetting.put(Constants.LNG, (float) _location.getLongitude());
                    MainApplication.getInstance().setmLocation(_location);
                    sendBroadcast(i);
                    int idDriver = mSetting.getInt(Constants.ID);
                    int idTrip = mSetting.getInt(Constants.IDTRIP);
                    int idUser = mSetting.getInt(Constants.IDUSER);
                    boolean statusStart = mSetting.getBoolean(Constants.ISSTART);
                    boolean statusGoin = mSetting.getBoolean(Constants.ISGOIN);
                    if (socket == null || idDriver == -1) return;
                    updateLatlngAngle(_location.getLatitude(), _location.getLongitude(), mCurrentDegree, mCurrentDegree, idDriver);

                    if (statusGoin && idTrip != -1 && idUser != -1) {
                        updateStatusBooking(SocketConstants.STATUS_ONGOING, idTrip, idDriver, idUser, _location.getLatitude(), _location.getLongitude());
                        return;
                    }
                    if (statusStart && idTrip != -1 && idUser != -1) {
                        updateStatusBooking(SocketConstants.STATUS_START, idTrip, idDriver, idUser, _location.getLatitude(), _location.getLongitude());
                        return;
//                    }
//                }
            }

//            @Override
//            public void onFailure(Call<ApiResponse<List<Routes>>> call, Throwable t) {
//
//            }
//        });


    }
    public void requestLocation() {
        LogUtils.e("request location now");
        LocationRequest lr = LocationRequest.create();
        lr.setPriority(100);
        lr.setInterval(mSetting.getLong(Constants.TIME_UPDATE,30000));
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationServices.FusedLocationApi.requestLocationUpdates(this._gac, lr, (LocationListener) this);
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocation();
    }
    public void onConnectionSuspended(int arg0) {
        LocationServices.FusedLocationApi.removeLocationUpdates(this._gac, (LocationListener) this);
    }
    private void stopLocationUpdate() {
        if (this._gac != null) {
            this._gac.disconnect();
        }
    }
    public void onConnectionFailed(ConnectionResult connectionResult) {
        switch (connectionResult.getErrorCode()) {
        }
    }


}

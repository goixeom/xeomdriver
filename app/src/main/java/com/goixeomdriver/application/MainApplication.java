package com.goixeomdriver.application;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.crashlytics.android.Crashlytics;
import com.goixeom.BuildConfig;
import com.goixeomdriver.models.User;
import com.goixeomdriver.socket.SocketClient;
import com.goixeomdriver.utils.Constants;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;


/**
 * Created by Huy on 6/13/2017.
 */

public class MainApplication extends MultiDexApplication {
    static MainApplication instance;
    private User mUser;
    private int mIdBooking;
    private int mIdDriver;
    private String mIdAuth;
    SocketClient socketClient;
    private boolean isResent = false;
    Location mLocation;
    CountDownTimer countDownTimer = new CountDownTimer(300000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            isResent =false;
        }
    };

    public static synchronized MainApplication getInstance() {
        if (instance == null) instance = new MainApplication();
        return instance;
    }

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public User getmUser() {
        if (mUser == null) {
            String json = new SPUtils(Constants.SETTING).getString("user_save");
            mUser = new Gson().fromJson(json, User.class);
        }
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
        String jsonUser;
        if (mUser != null) {
            jsonUser = new Gson().toJson(mUser);
        } else {
            jsonUser = "";
        }
        new SPUtils(Constants.SETTING).put("user_save", jsonUser);
    }

    public boolean isResent() {
        return isResent;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    public void setResent(boolean resent) {
        isResent = resent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    String phoneNumber;

    public String getmIdAuth() {
        return mIdAuth;
    }

    public void setmIdAuth(String mIdAuth) {
        this.mIdAuth = mIdAuth;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
//        instance = this;
        Utils.init(this);
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "OpenSans-Regular.ttf");

        getHashesKey(this);
//        fb
//        FacebookSdk.sdkInitialize(this);
//        getHashesKey(geA());
//        Permission[] permissions = new Permission[] {
//                Permission.USER_PHOTOS,
//                Permission.EMAIL,
//                Permission.PUBLISH_ACTION
//        };
//        SimpleFacebookConfiguration configurationFb = new SimpleFacebookConfiguration.Builder()
//                .setAppId(getString(R.string.fb_id))
//                .setNamespace("goixeom")
//                .setPermissions(permissions)
//                .build();
//        SimpleFacebook.setConfiguration(configurationFb);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initLog();
        sendBroadcast(new Intent("chayngam.restart.driver"));
        startService(new Intent(this,SocketClient.class));
    }
    public static void initLog() {
        LogUtils.Builder builder = new LogUtils.Builder()
                .setLogSwitch(BuildConfig.DEBUG)// 设置log总开关，包括输出到控制台和文件，默认开
                .setGlobalTag(null)// 设置log全局标签，默认为空
                // 当全局标签不为空时，我们输出的log全部为该tag，
                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
                .setLogHeadSwitch(true)// 设置log头信息开关，默认为开
                .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
                .setDir("")// 当自定义路径为空时，写入应用的/cache/log/目录中
                .setBorderSwitch(true);// 输出日志是否带边框开关，默认开
        LogUtils.d(builder.toString());
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getmIdBooking() {
        return mIdBooking;
    }

    public void setmIdBooking(int mIdBooking) {
        this.mIdBooking = mIdBooking;
    }

    public int getmIdDriver() {
        return mIdDriver;
    }

    public void setmIdDriver(int mIdDriver) {
        this.mIdDriver = mIdDriver;
    }

    void getHashesKey(Context context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

//            MyLog.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            LogUtils.e("Name not found" + e1.toString());
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e("No such an algorithm" + e.toString());
        } catch (Exception e) {
            LogUtils.e("Exception" + e.toString());
        }
    }
}
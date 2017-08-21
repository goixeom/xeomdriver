package com.goixeomdriver.socket;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.goixeom.R;
import com.goixeomdriver.Activity.SplashScreenActivity;
import com.goixeomdriver.utils.Constants;

/**
 * Created by DuongKK on 6/24/2017.
 */

public class RestartReceiver  extends BroadcastReceiver {
    public AlarmManager alarmManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (new SPUtils(Constants.SETTING).getInt(Constants.ID) == -1 ) {
            LogUtils.e("No internet or log out");
            return;

        }

        LogUtils.e("onReceive RestartReceiver");
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(SocketClient.TAG);
        PendingIntent pendingIntent = PendingIntent.getService(context, (int) (System.currentTimeMillis()%10000),i,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+100,5000,pendingIntent);
    }

    private void creatNotify(Context context) {
        Intent notificationIntent = new Intent(context, SplashScreenActivity.class);
        notificationIntent.putExtra(Constants.MSG, true);
//        notificationIntent.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int)System.currentTimeMillis(), notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setOngoing(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Ứng dụng đang chạy")
                .setContentIntent(pendingIntent).build();
    }
}
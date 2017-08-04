package com.goixeomdriver.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.goixeom.R;
import com.goixeomdriver.Activity.MapsActivity;
import com.goixeomdriver.Activity.RegisterActivity;
import com.goixeomdriver.Activity.SplashScreenActivity;
import com.goixeomdriver.Activity.UpdateActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;


/**
 * Created by DuongKK on 6/16/2017.
 */

public class CommonUtils {
    public static void shareSimpleText(String msg, Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", R.string.app_name);
        intent.putExtra("android.intent.extra.TEXT", msg);
        context.startActivity(Intent.createChooser(intent, "Send via"));
    }

    public static void disable(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disable((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

    public static void enable(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                enable((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }

    public static void vibrate(final ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                enable((ViewGroup) child);
            } else {
                child.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        Vibrator v = (Vibrator) layout.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(100);
                        return false;
                    }
                });
            }
        }
    }

    public static void launchMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    public static String getCompleteAddressNomal(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        try {
            List<Address> addresses = new Geocoder(context, Locale.getDefault()).getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = (Address) addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                LogUtils.e("My Current loction Nomal address" + strReturnedAddress.toString());
                return strAdd;
            }
            LogUtils.e("My Current loction address NomalNo Address returned!");
            return strAdd;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("My Current loction address NomalCanont get Address!");
            return strAdd;
        }
    }

    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        try {
            List<com.doctoror.geocoder.Address> addresses = new com.doctoror.geocoder.Geocoder(context, Locale.getDefault()).getFromLocation(LATITUDE, LONGITUDE, 20, true);
            if (addresses != null) {
                com.doctoror.geocoder.Address returnedAddress = (com.doctoror.geocoder.Address) addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                LogUtils.e(returnedAddress.toString());
                strAdd = returnedAddress.getFormattedAddress();
                LogUtils.e("My Current loction address" + strAdd.toString());
                return strAdd;
            }
            LogUtils.e("My Current loction addressNo Address returned!");
            return getCompleteAddressNomal(context, LATITUDE, LONGITUDE);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("My Current loction addressCanont get Address!");
            return getCompleteAddressNomal(context, LATITUDE, LONGITUDE);
        }
    }

    public static void focusAllMarkers(List<LatLng> places, GoogleMap mMap, Context context) {
        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
        for (LatLng place : places) {
            latLngBounds.include(place);
        }
        LatLngBounds bounds = latLngBounds.build();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(),14));
//        focusCurrentLocation(bounds.getCenter(),13,mMap);
        LogUtils.e(width + "----" + height);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height/3 , 16));


//        try {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
//        } catch (Exception e) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 900, 900, 50));
//            e.fillInStackTrace();
//        }
//        LatLng latLng  = new LatLng(        bounds.getCenter().latitude-0.0001,bounds.getCenter().longitude);
//        focusCurrentLocation(latLng,10,mMap);
    }

    public static void intentToCall(String phone, Context context) {
        Intent i = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + phone));
        context.startActivity(i);
    }

    public static void focusCurrentLocation(LatLng latLng, float sizeZoom, GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latLng).zoom(sizeZoom).build()));
    }

    public static Marker addMarker(Drawable resource, GoogleMap mMap, LatLng latLng) {
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(resource);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).anchor(0.5f, 0.5f).icon(markerIcon));
        return marker;
    }

    private static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public static Notification createNotificationWithMsg(Context context, String title, String msg, int isNomalNoti) {
        android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setAutoCancel(true).setOngoing(false);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(RegisterActivity.class);
        Intent intentMain = new Intent(context, MapsActivity.class);
        intentMain.putExtra(Constants.NOTIFICATION_SOCKET, isNomalNoti);
//        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Vicostone/");
//        intent.setData(uri);
//        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
//        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Vicostone/"); // a directory
//        intent.setDataAndType(uri, "*/*");
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        stackBuilder.addNextIntent(intentMain);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        return builder.build();

    }

    public static Notification createNotification(Context context, String title, String msg, String json) {
        android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setAutoCancel(true).setOngoing(false);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(RegisterActivity.class);
        Intent intentMain = new Intent(context, MapsActivity.class);
        intentMain.putExtra(Constants.BOOKING, json);
        builder.setSound(Uri.parse(switchRingtone(new SPUtils(Constants.SETTING).getInt(Constants.SETTING_SOUND))));
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        stackBuilder.addNextIntent(intentMain);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        return builder.build();
    }
    public static Notification createNotificationRating(Context context, int rating) {
        android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("Tài xế");
        builder.setContentText("Khách hàng đã đánh giá bạn "+ rating + "★");
        builder.setAutoCancel(true).setOngoing(false);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(RegisterActivity.class);
        Intent intentMain = new Intent(context, MapsActivity.class);
        intentMain.putExtra(Constants.RATING, rating);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        stackBuilder.addNextIntent(intentMain);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        return builder.build();
    }
    private static String switchRingtone(int type) {
        switch (type) {
            case 0: {
                return "android.resource://com.goixeomdriver/raw/banana_song";
            }
            case 1: {
                return "android.resource://com.goixeomdriver/raw/despacito";
            }
            case 2: {
                return "android.resource://com.goixeomdriver/raw/mr_taxi";
            }
            case 3: {
                return "android.resource://com.goixeomdriver/raw/shape_of_you";
            }
            case 4: {
                return "android.resource://com.goixeomdriver/raw/very_super_tone_ever";
            }
            default:
                return "android.resource://com.goixeomdriver/raw/very_super_tone_ever";
        }
    }

    public static void showNotifyOpenApp(Context context) {
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
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1337, notification);
    }

    public static Notification createNotificationUpdateProfile(Context context, String title, String msg) {
        android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setAutoCancel(true).setOngoing(false);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(UpdateActivity.class);
        Intent intentMain = new Intent(context, UpdateActivity.class);
        intentMain.putExtra(Constants.CONTENT, msg);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        stackBuilder.addNextIntent(intentMain);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        return builder.build();

    }

    public static Notification createNotification(Context context, String title, String msg, Intent intent) {
        android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setAutoCancel(true).setOngoing(false);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(UpdateActivity.class);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        return builder.build();

    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}

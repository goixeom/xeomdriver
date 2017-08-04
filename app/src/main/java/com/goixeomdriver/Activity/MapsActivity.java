package com.goixeomdriver.Activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.bumptech.glide.Glide;
import com.eralp.circleprogressview.CircleProgressView;
import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.View.CustomTypefaceSpan;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.ApiUtils;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.apis.CallBackCustomNoDelay;
import com.goixeomdriver.apis.GoiXeOmAPI;
import com.goixeomdriver.application.MainApplication;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.maputils.DirectionCustomModel;
import com.goixeomdriver.maputils.MapUtils;
import com.goixeomdriver.models.NextBooking;
import com.goixeomdriver.models.Routes;
import com.goixeomdriver.models.TripInforModel;
import com.goixeomdriver.models.User;
import com.goixeomdriver.socket.SocketConstants;
import com.goixeomdriver.socket.SocketResponse;
import com.goixeomdriver.utils.CommonUtils;
import com.goixeomdriver.utils.Constants;
import com.goixeomdriver.views.AlertDialogCustom;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.perf.metrics.Trace;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;


public class MapsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, OnMapReadyCallback {
    GoogleMap mMap;
    @BindView(R.id.ll_root)
    RelativeLayout llRoot;
    @BindView(R.id.img_direction)
    ImageView imgDirection;
    @BindView(R.id.tv_time_take_trip)
    CustomTextView tvTimeTakeTrip;
    @BindView(R.id.progressBar)
    CircleProgressView progressBar;
    @BindView(R.id.tv_address_from)
    CustomTextView tvAddressFrom;
    @BindView(R.id.tv_address_des)
    CustomTextView tvAddressDes;
    @BindView(R.id.tv_distance_take_trip)
    CustomTextView tvDistanceTakeTrip;
    @BindView(R.id.tv_cost_take_trip)
    CustomTextView tvCostTakeTrip;
    @BindView(R.id.ll_root_accept_take_trip)
    LinearLayout llRootAcceptTakeTrip;
    @BindView(R.id.tv_time_countdown_take_trip)
    TextView tvCountdown;
    @BindView(R.id.tv_call)
    CustomTextView tvCall;
    @BindView(R.id.img_avt_receive)
    CircleImageView imgAvtReceive;
    @BindView(R.id.tv_name_receive)
    CustomTextView tvNameReceive;
    @BindView(R.id.tv_type_receive)
    CustomTextView tvTypeReceive;
    @BindView(R.id.tv_address_from_receive)
    CustomTextView tvAddressFromReceive;
    @BindView(R.id.tv_address_des_receive)
    CustomTextView tvAddressDesReceive;
    @BindView(R.id.tv_duration_goin)
    CustomTextView tvDurationGoin;
    @BindView(R.id.tv_address_from_move)
    CustomTextView tvAddressFromMove;
    @BindView(R.id.tv_address_des_move)
    CustomTextView tvAddressDesMove;


    @BindView(R.id.tv_cost_pay)
    CustomTextView tvCostPay;
    @BindView(R.id.tv_distance_pay)
    CustomTextView tvDistancePay;
    @BindView(R.id.ll_root_promotion_pay)
    LinearLayout llRootPromotionPay;
    @BindView(R.id.pay_txt)
    CustomTextView payTxt;
    @BindView(R.id.tv_time_pay)
    CustomTextView tvTimePay;
    @BindView(R.id.agree_layout)
    LinearLayout agreeLayout;
    @BindView(R.id.pay_layout)
    LinearLayout payLayout;
    @BindView(R.id.tv_address_from_pay)
    CustomTextView tvAddressFromPay;
    @BindView(R.id.tv_address_des_pay)
    CustomTextView tvAddressDesPay;
    @BindView(R.id.tv_time_start_pay)
    CustomTextView tvTimeStartPay;
    @BindView(R.id.tv_time_end_pay)
    CustomTextView tvTimeEndPay;
    @BindView(R.id.btn_mylocation)
    FloatingActionButton btnMylocation;
    @BindView(R.id.img_type)
    ImageView imgType;
    @BindView(R.id.tv_type)
    CustomTextView tvType;
    @BindView(R.id.img_type_user)
    ImageView imgTypeUser;
    boolean isFocusDirection = false;
    @BindView(R.id.img_vote)
    ImageView imgVote;
    @BindView(R.id.tv_number_vote)
    CustomTextView tvNumberVote;
    //    @BindView(R.id.mapView)
//    MapView mapView;
    private Marker mMarkerDes;
    private Marker mMarkerFrom;
    private Polyline polyline;
    private TripInforModel tripInforModel;
    private SwitchButton mSwitch;
    private CustomTextView mReady, mPay, mReceiveText, mCancel, mMoveText;
    private LinearLayout mReceive, mMove, mTakeTrip, mPayLayout;
    private FragmentManager mFragmentManager;
    private Marker m;
    Trace myTrace = FirebasePerformance.getInstance().newTrace("Tai xe nhan chuyen -> mo dialog");

    //    private CountDownTimer timer = new CountDownTimer(20000, 100) {
//        @Override
//        public void onTick(long l) {
//            LogUtils.e("Coundown  " + l / 1000);
//            tvCountdown.setText(l / 1000 + "");
//            progressBar.setProgressWithAnimation(l * 100 / 20000, 300);
//        }
//
//        @Override
//        public void onFinish() {
//            clearInforTrip();
//            resetMap();
//            resetMap();
//            showSnackBar("Chuyến đi  được chuyển tới tài xế khác");
//            getmSocket().setJoin(true);
//            getmSocket().cancelBooking(tripInforModel.getTrip_info().getIdTrip(), getCurrentIdUser());
//            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);
//        }
//    };
    String TAG = "SENSOR";
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private boolean isMarkerRotating;
    private CircleImageView mAvt;
    private CustomTextView mTvName;
    private CustomTextView mTvNumber;
    private CustomTextView mTvVote;
    private MaterialRatingBar mRating;
    private boolean isUserCancel;
    private BroadcastReceiver receiverBooking = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String jsonResponseBooking = intent.getStringExtra(Constants.BOOKING);
                SocketResponse responseBooking = new Gson().fromJson(jsonResponseBooking, SocketResponse.class);
                if (responseBooking != null && getCurrentIdUser() != -1 && responseBooking.getIdDriver() == getCurrentIdUser() && responseBooking.getStatus() == 0) {
                    LogUtils.e("Booking = " + jsonResponseBooking);
                    isUserCancel = false;
                    showDetailBooking(responseBooking.getIdTrip(), responseBooking.getIdUser());
                    return;
                }

                boolean timeout = intent.getBooleanExtra(Constants.TIME_OUT_BOOKING, false);
                if (timeout) {
                    tvCountdown.setText(0 + "");
                    progressBar.setProgressWithAnimation(0, 300);
                    clearInforTrip();
                    resetMap();
                    showSnackBar("Chuyến đi được chuyển tới tài xế khác");
                    ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);
                    getmSocket().showNotificationInStack();
                    return;
                }

                //Update countdowntimer
                long timeCountdown = intent.getLongExtra(Constants.UPDATE_TIME_COUNTDOWN, 0);
                if (timeCountdown > 0 && !isUserCancel) {
                    tvCountdown.setText(timeCountdown / 1000 + "");
                    progressBar.setProgressWithAnimation(timeCountdown * 100 / 20000, 300);
                    if (timeCountdown / 1000 == 0) {
                        progressBar.setProgressWithAnimation(0, 300);
                    }
                    return;
                }

                //update status
                String statusResponse = intent.getStringExtra(Constants.STATUS);
                SocketResponse response = new Gson().fromJson(statusResponse, SocketResponse.class);
                if (response != null && response.getIdDriver() == getCurrentIdUser()) {
                    needToShow = false;
                    LogUtils.e("Map status receive : " + response.getStatus());
                    if (response.getStatus() == 1) {
                        mSwitch.setChecked(true);
                    } else if (response.getStatus() == 0) {
                        mSwitch.setChecked(false);
                    } else if (getmSetting().getBoolean(Constants.USER_TAP_ON, true) && response.getStatus() == 2) {
                        mSwitch.setChecked(true);
                    }
                    needToShow = true;
                }

                //Rating
                String rateResponse = intent.getStringExtra(Constants.RATING);
                SocketResponse responseRate = new Gson().fromJson(rateResponse, SocketResponse.class);
                if (responseRate != null && responseRate.getIdDriver() == getCurrentIdUser() && responseRate.getVote() != -1) {
                    showSnackBar("Khách hàng đã đánh giá bạn " + responseRate.getVote() + "★");
                    getInfor(getCurrentIdUser());
                }
            }
        }
    };
    private DirectionCustomModel directionCustomModel = new DirectionCustomModel();
    private boolean isJoinInToBooking;
    private boolean needToShow = true;
    private boolean dialogFinish = false;


    @Override
    public void pingNotification(String title, String content) {

    }


    @Override
    public void onSocketReady() {
        if (getmSocket() != null)
            getmSocket().updateNewImei(PhoneUtils.getIMEI(), getCurrentIdUser());
        reconnectBooking();
    }


    boolean isFirstTimeFocus = false;
    boolean isStarting = false;

    @Override
    public void onLocationChange(Location location) {
        if (mMap != null) {
            if (m != null) {
                m.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                m.setVisible(true);
            } else {
                m = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_motorcycle, null), mMap, new LatLng(location.getLatitude(), location.getLongitude()));
            }
            m.setVisible(true);
            if (!isFirstTimeFocus) {
                isFirstTimeFocus = true;
                CommonUtils.focusCurrentLocation(new LatLng(location.getLatitude(), location.getLongitude()), Constants.ZOOM, mMap);
            }
        }
        if (isJoinInToBooking) {
//            getmSocket().updateStatusBooking(SocketConstants.STATUS_ONGOING,tripInforModel.getTrip_info().getIdTrip(),getCurrentIdUser(),tripInforModel.getUser().getU_id(),location.getLatitude(),location.getLongitude());
//            if (polyline != null) polyline.remove();
            showDirection(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), new LatLng(tripInforModel.getTrip_info().getLatFrom(), tripInforModel.getTrip_info().getLngFrom())
                    , false);
            return;
        }

        if (isStarting && tripInforModel != null) {
//            mMap.clear();
            if (mMarkerFrom != null && mMarkerDes != null) {
                mMarkerDes.remove();
                mMarkerFrom.remove();
            }
            mMarkerFrom = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_motorcycle, null), mMap, new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
            mMarkerDes = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pin_green2, null), mMap, new LatLng(tripInforModel.getTrip_info().getLatTo(), tripInforModel.getTrip_info().getLngTo()));

            if (directionCustomModel != null && directionCustomModel.getPolyline() != null)
                directionCustomModel.getPolyline().remove();

            if (polyline != null) polyline.setVisible(true);
            showDirection(new LatLng(location.getLatitude(), location.getLongitude())
                    , new LatLng(tripInforModel.getTrip_info().getLatTo(), tripInforModel.getTrip_info().getLngTo()), true);
//            new DirectionAsynTaskBooking(MapsActivity.this
//                    , directionCustomModel
//                    , mMap
//                    , tvDurationGoin
//                    , polyline
//                    , mMoveText)
//                    .execute(MapUtils.getDirectionsUrl(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(tripInforModel.getTrip_info().getLatTo(), tripInforModel.getTrip_info().getLngTo())));
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "MyriadPro-Regular.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    @AddTrace(name = "Render View")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getmSetting().put(Constants.BOOKING,"");
//        Mapbox.getInstance(this, "pk.eyJ1IjoiZHVvbmdudjE5OTYiLCJhIjoiY2owaHRtcmVnMDFhcjJ3dWozOG9oZTNjNiJ9.rzDhWZz--vRm_Al5yacEEg");
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
//        mapView.onCreate(savedInstanceState);
        view = llRoot;
//        CommonUtils.vibrate(llRoot);
        registerReceiver(receiverBooking, new IntentFilter(SocketConstants.EVENT_CONNECTION));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
        View header = navigationView.getHeaderView(0);
        this.mTvName = (CustomTextView) header.findViewById(R.id.tv_name);
        this.mTvNumber = (CustomTextView) header.findViewById(R.id.tv_number);
        this.mAvt = (CircleImageView) header.findViewById(R.id.profile_image);
        mTvVote = (CustomTextView) header.findViewById(R.id.tv_vote);
        mRating = (MaterialRatingBar) header.findViewById(R.id.rating_bar);
        mSwitch = (SwitchButton) findViewById(R.id.Switch);
        mReady = (CustomTextView) findViewById(R.id.ready_txt);
        mReceiveText = (CustomTextView) findViewById(R.id.receive_txt);
        mCancel = (CustomTextView) findViewById(R.id.cancel_txt);
        mMoveText = (CustomTextView) findViewById(R.id.move_txt);
        mMoveText.setClickable(false);
        mReceiveText.setClickable(false);
        mReceiveText.setEnabled(false);

        tvAddressFromPay.setSelected(true);
        tvAddressFrom.setSelected(true);
        tvNameReceive.setSelected(true);
        tvAddressFromMove.setSelected(true);
        tvAddressDes.setSelected(true);
        tvAddressDesMove.setSelected(true);
        tvAddressDesPay.setSelected(true);
        tvAddressDesReceive.setSelected(true);
        tvAddressFromReceive.setSelected(true);
//        mReceiveText.setVisibility(View.GONE);
        mReceiveText.setText("DI CHUYỂN ĐỂ ĐÓN KHÁCH HÀNG");
        if (mSwitch.isChecked()) {
            mReady.setText("ĐANG SẴN SÀNG");
            mReady.setBackgroundColor(ContextCompat.getColor(MapsActivity.this, (R.color.colorGreen)));
        } else {
            mReady.setText("CHƯA SẴN SÀNG");
            mReady.setBackgroundColor(ContextCompat.getColor(MapsActivity.this, (R.color.colorRed)));
        }
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vibrate();

                if (isChecked) {

                    mReady.setText("ĐANG SẴN SÀNG");
                    mReady.setBackgroundColor(ContextCompat.getColor(MapsActivity.this, (R.color.colorGreen)));
                    if (getmSocket() != null)
                        getmSocket().updateStatusDriver(SocketConstants.STATUS_READY, getCurrentIdUser());
                    getmSetting().put(Constants.USER_TAP_ON, true);
                    if (needToShow) showSnackBar("Hiện tại bạn đã có thể nhận chuyến");
                    needToShow = true;
                } else {
                    mReady.setText("CHƯA SẴN SÀNG");
                    if (needToShow) showSnackBar("Hiện tại bạn không  thể nhận chuyến", "#FFBE08");

                    mReady.setBackgroundColor(ContextCompat.getColor(MapsActivity.this, (R.color.colorRed)));
                    if (getmSocket() != null)
                        getmSocket().updateStatusDriver(SocketConstants.STATUS_UN_READY, getCurrentIdUser());
                    getmSetting().put(Constants.USER_TAP_ON, false);

                }
            }
        });
        mPay = (CustomTextView) findViewById(R.id.pay_txt);
        mPay.setText(Html.fromHtml(getString(R.string.txt_pay)));
        mMove = (LinearLayout) findViewById(R.id.move_layout);
        mReceive = (LinearLayout) findViewById(R.id.receive_layout);
        mTakeTrip = (LinearLayout) findViewById(R.id.take_trip_layout);
        mPayLayout = (LinearLayout) findViewById(R.id.pay_layout);
        mReady.setOnClickListener(this);
        mReceiveText.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mMoveText.setOnClickListener(this);
        mMoveText.setClickable(false);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFragmentManager = getSupportFragmentManager();
        getInfor(getCurrentIdUser());
        imgDirection.setVisibility(View.INVISIBLE);
        resetMap();
        agreeLayout.setOnClickListener(this);
        progressBar.setTextEnabled(false);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);

        if (getIntent() != null) {
            int type = getIntent().getIntExtra(Constants.NOTIFICATION_SOCKET, -1);
            if (type == Constants.NOTIFICATION_NORMAL_TYPE) {
                startActivity(new Intent(MapsActivity.this, NotificationActivity.class));
                return;
            } else if (type != -1) {
                startActivity(new Intent(MapsActivity.this, WalletActivity.class));
                return;
            }
            String json = getIntent().getStringExtra(Constants.BOOKING);
            if (json != null && !json.isEmpty()) {
                SocketResponse responseBooking = new Gson().fromJson(json, SocketResponse.class);
                if (responseBooking != null && getCurrentIdUser() != -1 && responseBooking.getIdDriver() == getCurrentIdUser()) {
                    showDetailBooking(responseBooking.getIdTrip(), responseBooking.getIdUser());
                }
            }

            int rate = getIntent().getIntExtra(Constants.RATING, -1);
            if (rate != -1) {
                showSnackBar("Khách hàng đã đánh giá bạn " + rate + "★");
                getInfor(getCurrentIdUser());
            }
        }

        CommonUtils.showNotifyOpenApp(this);

    }

    private float getDistance(LatLng latLng1, LatLng latLng2) {
        float[] result = new float[2];
        Location.distanceBetween(latLng1.latitude, latLng1.longitude, latLng2.latitude, latLng2.longitude, result);
        return result[0];
    }

    private void showDirection(final LatLng latLngOrgin, final LatLng latLngDestination, final boolean ableToFinish) {
        Call<ApiResponse<List<Routes>>> direction = ApiUtils.getAPIPLACE().create(GoiXeOmAPI.class)
                .getDirection(latLngOrgin.latitude + "," + latLngOrgin.longitude
                        , latLngDestination.latitude + "," + latLngDestination.longitude, getString(R.string.google_map_id));
        direction.enqueue(new CallBackCustom<ApiResponse<List<Routes>>>(MapsActivity.this, new OnResponse<ApiResponse<List<Routes>>>() {
            @Override
            public void onResponse(ApiResponse<List<Routes>> object) {
                if (object.getRoutes() != null && object.getRoutes().size() > 0) {
                    LogUtils.e("distance = " + object.getRoutes().get(0).getLegs().get(0).getDistance().getValue());
                    if (ableToFinish && getDistance(latLngOrgin, latLngDestination) < 1000) {
                        mMoveText.setText("KẾT THÚC CHUYẾN");
                        mMoveText.setClickable(true);
                        mMoveText.setBackgroundColor(Color.parseColor("#f9ba48"));
                        getmSocket().updateStatusDriver(1, getCurrentIdUser());
                    } else {
                        mMoveText.setText("ĐANG DI CHUYỂN...");
                        mMoveText.setClickable(false);
                        mMoveText.setBackgroundColor(Color.parseColor("#2db45b"));
                    }
//                    tvDurationGoin.setText("Cách điểm đến " + object.getRoutes().get(0).getLegs().get(0).getDuration().getText().replace("min", "phút").replace("hour", "giờ").replace("s", ""));
                    tvDurationGoin.setText(Html.fromHtml(
                            String.format(
                                    getString(R.string.txt_duration)
                                    , "Cách điểm đến", object.getRoutes().get(0).getLegs().get(0).getDuration().getText().replace("day", "ngày").replace("min", "phút").replace("hour", "giờ").replace("s", ""))
                            )
                    );
                    if (getDistance(latLngOrgin, latLngDestination) < 500) {
                        mReceiveText.setClickable(true);
                        mReceiveText.setEnabled(true);
                        mReceiveText.setVisibility(View.VISIBLE);
                        mReceiveText.setText("KHÁCH ĐÃ LÊN XE");
                    } else {
                        mReceiveText.setEnabled(false);
                        mReceiveText.setText("DI CHUYỂN ĐỂ ĐÓN KHÁCH HÀNG");
                    }

                    PolylineOptions line = new PolylineOptions();
                    line.width(10).color(ContextCompat.getColor(MapsActivity.this, R.color.colorGreen));
                    int n = object.getRoutes().get(0).getLegs().get(0).getSteps().size();
                    line.add(latLngOrgin);
                    for (int i = 0; i < n; i++) {
                        line.add(new LatLng(
                                object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStart_location().getLat()
                                , object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStart_location().getLng()
                        ));
                        line.addAll(decodePoly(object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getPolyline().getPoints()));
                        line.add(new LatLng(
                                object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getEnd_location().getLat()
                                , object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getEnd_location().getLng()
                        ));
                    }
                    line.add(latLngDestination);
                    if (!isFocusDirection) {
                        if (polyline != null) polyline.remove();
                        polyline = mMap.addPolyline(line);
                        CameraPosition cameraPosition =
                                new CameraPosition.Builder()
                                        .target(MapUtils.convertLocationToLatlng(mLocation))
                                        .bearing(MapUtils.bearingBetweenLocations(polyline.getPoints().get(0), polyline.getPoints().get(1)))
                                        .zoom(mMap.getCameraPosition().zoom)
                                        .build();
                        mMap.animateCamera(
                                CameraUpdateFactory.newCameraPosition(cameraPosition),
                                20,
                                null
                        );
                    }
                    CommonUtils.focusAllMarkers(polyline.getPoints(), mMap, MapsActivity.this);
                } else {
                    LogUtils.e("direction null or no routes");
                }
            }
        }));

    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    public void showSnackBar(String content, String color) {
        Crouton.cancelAllCroutons();
        Style croutonStyle = new Style.Builder().setBackgroundColorValue(Color.parseColor(color)).build();
//        Crouton crouton = Crouton.makeText(this, content, croutonStyle)
//                .setConfiguration(new Configuration.Builder().setDuration(Configuration.DURATION_INFINITE).build());
//        crouton.show();

        Crouton.makeText(this, content, croutonStyle, llRoot).show();
    }

    private void getInfor(final int id) {
        Call<ApiResponse<User>> getInfo = getmApi().getInfor(ApiConstants.API_KEY, id);
//        getDialogProgress().showDialog();
        getInfo.enqueue(new CallBackCustom<ApiResponse<User>>(this, new OnResponse<ApiResponse<User>>() {
            @Override
            public void onResponse(ApiResponse<User> object) {
                if (object.getData() != null) {
                    MainApplication.getInstance().setmUser(object.getData());
                    getmSetting().put(Constants.TYPE_USER_STR, object.getData().getType());
                    mTvName.setText(object.getData().getName());
                    mTvNumber.setText(object.getData().getNumber());
                    Glide.with(MapsActivity.this).load("https://" + object.getData().getAvatar()).asBitmap().into(mAvt);
                    Glide.with(MapsActivity.this).load("https://" + object.getData().getLink()).asBitmap().into(imgType);
                    tvType.setText(getType(object.getData().getType()));
                    mTvVote.setText(object.getData().getVote() + "");
                    tvNumberVote.setText(object.getData().getNumber_vote() + " Bình chọn");
                    switch (object.getData().getType_vote()){
                        case 1:{
                            imgVote.setImageResource(R.drawable.ic_talking_funny);
                            break;
                        }
                        case 2:{
                            imgVote.setImageResource(R.drawable.ic_pro);
                            break;
                        }
                        case 3:{
                            imgVote.setImageResource(R.drawable.ic_right_rule);
                            break;
                        }
                        default:{
                            imgVote.setImageResource(R.drawable.ic_talking_funny);
                            break;
                        }
                    }
                    mRating.setRating((float) object.getData().getVote());
                    if (object.getData().getStatus() == 1) {
                        mSwitch.setChecked(true);
                    } else if (object.getData().getStatus() == 0) {
                        mSwitch.setChecked(false);
                    }

                } else {
                    Toast.makeText(MapsActivity.this, object.getMessage(), Toast.LENGTH_LONG).show();
                    getmSetting().put(Constants.PHONE, -1);
                    getmSetting().put(Constants.ID, -1);
                    getmSetting().put(Constants.IS_SLIDE, false);
                    getmSetting().put(Constants.CLICK_GOMAP, false);
                    getmSetting().put(Constants.TYPE_PASSWORD_MAP, false);
                    getmSetting().put(Constants.TYPE_USER_STR, -1);
                    getmSocket().updateStatusDriver(0, getmSetting().getInt(Constants.ID));
                    ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);

                    MainApplication.getInstance().setmUser(null);
                    Intent i = new Intent(MapsActivity.this, SliderInfoActivity.class);
                    startActivity(i);
                    finishAffinity();
                }
            }
        }));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            goHome();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        vibrate();
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(MapsActivity.this, NotificationActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MapsActivity.this, HistoryActivity.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(MapsActivity.this, FAQActivity.class));
        } else if (id == R.id.nav_manage) {
            startActivityForResult(new Intent(MapsActivity.this, SettingActivity.class), Constants.CODE_REQ_SETTING);
        } else if (id == R.id.nav_wallet) {
            startActivity(new Intent(MapsActivity.this, WalletActivity.class));
        } else if (id == R.id.nav_pocket) {
            startActivity(new Intent(MapsActivity.this, PocketActivity.class));
        } //else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mapView.onStart();
        if (getmSocket() != null) {
            getmSocket().requestLocation();
//            reconnectBooking();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mapView.onResume();
        if (getmSocket() != null) {
            getmSocket().updateNewImei(PhoneUtils.getIMEI(), getCurrentIdUser());
//            reconnectBooking();

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        mapView.onPause();
    }

    @Override
    public void onClick(View v) {
        vibrate();
        switch (v.getId()) {
            case R.id.ready_txt:
                break;
            case R.id.receive_txt:
                //Khach da len xe -> Dang di chuyen
                startBooking();
                if (getmSetting().getBoolean(Constants.SETTING_DIRECT, true))
                    imgDirection.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel_txt:
                new MaterialDialog.Builder(this).title(getString(R.string.error))
                        .content("Bạn có chắc chắn muốn hủy chuyến này")
                        .cancelable(false)
                        .positiveText("Trở lại")
                        .negativeText("Đồng ý")
                        .negativeColor(Color.RED)
                        .positiveColor(Color.GRAY)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancel(2);
                                showSnackBar("Bạn đã hủy chuyến đi này");
                                if (isStarting && tripInforModel != null && tripTempForStack != null) {
                                    getmSocket().updateStatusBooking(SocketConstants.STATUS_DRIVER_CANCLE, tripTempForStack.getTrip_info().getIdTrip(), getCurrentIdUser(), tripTempForStack.getUser().getU_id());
                                    changeStateToGoin();
                                    return;
                                } else {
                                    getmSocket().updateStatusBooking(SocketConstants.STATUS_DRIVER_CANCLE, tripInforModel.getTrip_info().getIdTrip(), getCurrentIdUser(), tripInforModel.getUser().getU_id());
                                }
                                tripTempForStack = null;
                                mTakeTrip.setVisibility(View.INVISIBLE);
                                isUserCancel = true;
                                getmSetting().put(Constants.BOOKING, "");

                                if (dialogFinish) {
                                    changeToFinish();
                                } else {
                                    clearInforTrip();
                                    resetMap();
                                }
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        }).show();
                break;
            case R.id.move_txt:
                //Ket thuc
                AlertDialogCustom.dialogConfirm(this, new OnResponse() {
                    @Override
                    public void onResponse(Object object) {
                        FinishBooking();
                    }
                }).show();
//                changeToStateTakeTrip();
                break;
            case R.id.agree_layout: {
                dialogFinish = false;
                resetMap();
                showSnackBar("Hãy sẵn sàng để nhận chuyến đi mới");
                clearInforTrip();
//                if (stackTrips != null && !stackTrips.isEmpty()) {
//                    tripInforModel = stackTrips.pop();
                //// TODO: 7/27/2017  get next trip in stack
                getDialogProgress().showDialog();
                Call<ApiResponse<NextBooking>> getDetailBooking = getmApi().getNextBookingDriver(ApiConstants.API_KEY, getCurrentIdUser());
                getDetailBooking.enqueue(new CallBackCustomNoDelay<ApiResponse<NextBooking>>(this, getDialogProgress(), new OnResponse<ApiResponse<NextBooking>>() {
                    @Override
                    public void onResponse(ApiResponse<NextBooking> object) {
                        if (object.getStatus() == ApiConstants.CODE_SUCESS && object.getData() != null) {
                            tripInforModel = new TripInforModel();
                            TripInforModel.Trip_info trip_info = new TripInforModel.Trip_info();
                            trip_info.setIdTrip(object.getData().getIdTrip());
                            TripInforModel.mUser user = new TripInforModel.mUser();
                            user.setU_id(object.getData().getIdUser());
                            tripInforModel.setTrip_info(trip_info);
                            tripInforModel.setUser(user);
                            getmSetting().put(Constants.BOOKING, new Gson().toJson(tripInforModel));
                            reconnectBooking();
                            return;
                        } else {
                            getmSetting().put(Constants.BOOKING, "");
                            getmSocket().showNotificationInStack();
                        }
                    }
                }));
//                }

                break;
            }
        }
    }

    private void FinishBooking() {
        isJoinInToBooking = false;
        isStarting = false;
        getmSocket().updateStatusBooking(SocketConstants.STATUS_FINISH, tripInforModel.getTrip_info().getIdTrip(), getCurrentIdUser(), tripInforModel.getUser().getU_id());
        resetMap();
        clearInforTrip();
        changeToFinish();
        tvAddressFromPay.setText(tripInforModel.getTrip_info().getStart());
        tvAddressDesPay.setText(tripInforModel.getTrip_info().getEnd());
        tvCostPay.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(tripInforModel.getTrip_info().getPrice()) + " vnđ");
        tvDistancePay.setText(CommonUtils.round(tripInforModel.getTrip_info().getDistance(), 1) + "km");
        tvTimePay.setText(tripInforModel.getTrip_info().getTime_start());
        String time = tripInforModel.getTrip_info().getTime_start();
        tvTimeStartPay.setText(" " + time.substring(time.length() - 5));
        int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        tvTimeEndPay.setText(" " + hours + ":" + min);
        if (tripInforModel.getTrip_info().getPromotionValue() != 0) {
            payTxt.setText(Html.fromHtml(String.format(getString(R.string.txt_pay), NumberFormat.getNumberInstance(Locale.GERMAN).format(tripInforModel.getTrip_info().getPromotionValue()))));
            tvCostPay.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(tripInforModel.getTrip_info().getPrice() - tripInforModel.getTrip_info().getPromotionValue()) + " vnđ");
        } else {
            payTxt.setVisibility(View.GONE);
            llRootPromotionPay.setVisibility(View.GONE);
        }
        getmSocket().showNotificationInStack();
        tripInforModel.getTrip_info().setStatus(SocketConstants.STATUS_FINISH);
        getmSetting().put(Constants.BOOKING, new Gson().toJson(tripInforModel));
        dialogFinish = true;

    }

    private void startBooking() {
        isJoinInToBooking = false;
        isStarting = true;
        getmSetting().put(Constants.ISGOIN, false);
        getmSetting().put(Constants.ISSTART, true);
        changeStateToGoin();
        tvAddressFromMove.setText(tripInforModel.getTrip_info().getStart());
        tvAddressDesMove.setText(tripInforModel.getTrip_info().getEnd());
        if (!isFocusDirection)
            mMap.clear();
        if (mMarkerFrom != null && mMarkerDes != null) {
            mMarkerDes.remove();
            mMarkerFrom.remove();
        }
        getmSocket().updateStatusBooking(SocketConstants.STATUS_START, tripInforModel.getTrip_info().getIdTrip(), getCurrentIdUser(), tripInforModel.getUser().getU_id(), mLocation.getLatitude(), mLocation.getLongitude());
        mMarkerFrom = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_motorcycle, null), mMap, new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
        mMarkerDes = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pin_green2, null), mMap, new LatLng(tripInforModel.getTrip_info().getLatTo(), tripInforModel.getTrip_info().getLngTo()));
//        if (polyline != null) polyline.remove();
        showDirection(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()),
                new LatLng(tripInforModel.getTrip_info().getLatTo(), tripInforModel.getTrip_info().getLngTo())
                , true);
        List<LatLng> list = new ArrayList<LatLng>();
        list.add(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
        list.add(new LatLng(tripInforModel.getTrip_info().getLatTo(), tripInforModel.getTrip_info().getLngTo()));
        CommonUtils.focusAllMarkers(list, mMap, MapsActivity.this);
        Glide.with(getApplicationContext()).load("https://" + tripInforModel.getUser().getAvatar()).into(imgAvtReceive);
        tripInforModel.getTrip_info().setStatus(SocketConstants.STATUS_START);
        getmSetting().put(Constants.BOOKING, new Gson().toJson(tripInforModel));
    }

    private void changeStateToGoin() {
        mReceive.setVisibility(View.GONE);
        mMove.setVisibility(View.VISIBLE);
        mPayLayout.setVisibility(View.GONE);
        mTakeTrip.setVisibility(View.GONE);
        btnMylocation.setVisibility(View.GONE);
        if (getmSetting().getBoolean(Constants.SETTING_DIRECT, true))

            imgDirection.setVisibility(View.VISIBLE);
    }

    private String getType(int type) {
        switch (type) {
            case 1: {
                return "Lái mới";
            }
            case 2: {
                return "Lái lụa";
            }
            case 3: {
                return "Tay lái vàng";
            }
            case 4: {
                return "Vua ôm";
            }
            default:
                return "Lái mới";

        }
    }

    @Override
    @AddTrace(name = "Map Render")

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {

            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
            mMap.setMapStyle(style);
            mMap.setTrafficEnabled(true);
            if (mLocation == null && getmSetting().getFloat(Constants.LAT) != 0 && getmSetting().getFloat(Constants.LNG) != 0) {
                mLocation = new Location("");
                mLocation.setLatitude(getmSetting().getFloat(Constants.LAT));
                mLocation.setLongitude(getmSetting().getFloat(Constants.LNG));
                CommonUtils.focusCurrentLocation(new LatLng(getmSetting().getFloat(Constants.LAT), getmSetting().getFloat(Constants.LNG)), Constants.ZOOM, mMap);
                m = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_motorcycle, null), mMap, new LatLng(getmSetting().getFloat(Constants.LAT), getmSetting().getFloat(Constants.LNG)));
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10000);
                return;
            }
            if (getmSocket() != null) {
                getmSocket().requestLocation();
            }
//            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    if (m != null) {
                        m.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                    } else {
                        m = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_motorcycle, null), mMap, new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                    if (!isFirstTimeFocus) {
                        isFirstTimeFocus = true;
                        CommonUtils.focusCurrentLocation(new LatLng(location.getLatitude(), location.getLongitude()), Constants.ZOOM, mMap);
                    }
                }
            });

            reconnectBooking();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10000 && grantResults[0] == PackageManager.PERMISSION_GRANTED && mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
//            mMap.setMyLocationEnabled(true);
            if (getmSocket() != null) {
                getmSocket().requestLocation();
            }
            return;
        }

        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_DENIED) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CODE_REQ_SETTING) {
            if (resultCode == Constants.RESULT_LOGOUT && !isJoinInToBooking && !isStarting) {
                getDialogProgress().showDialog();
                Call<ApiResponse> logout = getmApi().logout(ApiConstants.API_KEY, getCurrentIdUser());
                logout.enqueue(new CallBackCustom<ApiResponse>(MapsActivity.this, getDialogProgress(), new OnResponse<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse object) {
                        if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                            getmSocket().updateStatusDriver(0, getmSetting().getInt(Constants.ID));
                            getmSetting().put(Constants.PHONE, -1);
                            getmSetting().put(Constants.ID, -1);
                            getmSetting().put(Constants.IS_SLIDE, false);
                            getmSetting().put(Constants.TYPE_PASSWORD_MAP, false);
                            MainApplication.getInstance().setmUser(null);
                            getmSetting().put(Constants.CLICK_GOMAP, false);
                            Intent i = new Intent(MapsActivity.this, SliderInfoActivity.class);
                            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);
                            startActivity(i);
                            finishAffinity();
                        } else {
                            showDialogErrorContent(object.getMessage());
                        }
                    }
                }));

            }
        }
    }

    @AddTrace(name = "reconnectBooking")
    void reconnectBooking() {
        needToShow = false;
        String booking = getmSetting().getString(Constants.BOOKING);
        if (booking != null && !booking.isEmpty() && getmSocket() != null && mMap != null) {
            TripInforModel bookingModel = new Gson().fromJson(booking, TripInforModel.class);
            if (bookingModel != null && bookingModel.getTrip_info() != null && bookingModel.getUser() != null) {
                getDialogProgress().showDialog();
                Call<ApiResponse<TripInforModel>> getDetailBooking = getmApi().getDetailReconnectTrip(ApiConstants.API_KEY, bookingModel.getTrip_info().getIdTrip(), bookingModel.getUser().getU_id());
                getDetailBooking.enqueue(new CallBackCustomNoDelay<ApiResponse<TripInforModel>>(this, getDialogProgress(), new OnResponse<ApiResponse<TripInforModel>>() {
                    @Override
                    public void onResponse(ApiResponse<TripInforModel> object) {
                        if (object.getData() != null) {
                            mReady.setVisibility(View.GONE);
                            mSwitch.setVisibility(View.GONE);
                            tripInforModel = object.getData();
                            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(2);
                            switch (tripInforModel.getTrip_info().getStatus()) {
                                case SocketConstants.STATUS_JOIN: {
                                    JoinBooking(false);
                                    getmSocket().updateStatusDriver(3, getCurrentIdUser());
                                    break;
                                }
                                case SocketConstants.STATUS_ONGOING: {
                                    JoinBooking(false);
                                    getmSocket().updateStatusDriver(3, getCurrentIdUser());

                                    break;
                                }
                                case SocketConstants.STATUS_START: {
                                    saveInforTrip();
                                    startBooking();
                                    getmSocket().updateStatusDriver(3, getCurrentIdUser());

                                    break;
                                }
                                case SocketConstants.STATUS_FINISH: {
                                    FinishBooking();
                                    clearInforTrip();
                                    break;
                                }
                            }
                        }
                    }
                }));

            }
        }
    }

    Stack<TripInforModel> stackTrips = new Stack<>();
    TripInforModel tripTempForStack;

    private void showDetailBooking(final int idBooking, int idUser) {
        needToShow = false;
        myTrace.start();
        Call<ApiResponse<TripInforModel>> getDetailBooking = getmApi().getDetailTrip(ApiConstants.API_KEY, idBooking, idUser);
        getDialogProgress().showDialog();
        getDetailBooking.enqueue(new CallBackCustomNoDelay<ApiResponse<TripInforModel>>(this, getDialogProgress(), new OnResponse<ApiResponse<TripInforModel>>() {
            @Override
            public void onResponse(ApiResponse<TripInforModel> object) {
                myTrace.stop();
                if (object.getData() != null && object.getData().getTrip_info().getStatus() == SocketConstants.STATUS_FINDING) {
                    changeToStateTakeTrip();
                    tripTempForStack = object.getData();
                    if (isStarting && tripInforModel != null) {
                        //In the trip need to push next trip into stack
                    } else {
                        tripInforModel = object.getData();
                        tripInforModel.getTrip_info().setIdTrip(idBooking);
                    }
                    llRootAcceptTakeTrip.setEnabled(true);
                    tvTimeTakeTrip.setText(object.getData().getTrip_info().getTime_start());
                    tvAddressFrom.setText(object.getData().getTrip_info().getStart());
                    tvAddressDes.setText(object.getData().getTrip_info().getEnd());
//                    tvAddressFrom.setSelected(true);
//                    tvAddressDes.setSelected(true);
                    tvDistanceTakeTrip.setText(CommonUtils.round(tripInforModel.getTrip_info().getDistance(), 1) + "km");
                    tvCostTakeTrip.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(object.getData().getTrip_info().getPrice()) + " vnđ");
//                    timer.start();
                } else {
                    showDialogErrorContent(object.getMessage());
                    mSwitch.setChecked(true);
                    getmSocket().getCountDownTimer().cancel();
                    getmSocket().setCancelable(false);
                }
            }
        }));
    }

    private void changeToStateTakeTrip() {
        mSwitch.setVisibility(View.GONE);
        mReceive.setVisibility(View.GONE);
        mMove.setVisibility(View.GONE);
        mPayLayout.setVisibility(View.GONE);
        mTakeTrip.setVisibility(View.VISIBLE);
        btnMylocation.setVisibility(View.GONE);
        mReady.setVisibility(View.GONE);
    }

    private void changeToStateReceiveTrip() {
        mSwitch.setVisibility(View.GONE);
        mReceive.setVisibility(View.VISIBLE);
        mMove.setVisibility(View.GONE);
        mPayLayout.setVisibility(View.GONE);
        mTakeTrip.setVisibility(View.GONE);
        btnMylocation.setVisibility(View.GONE);
        mReady.setVisibility(View.GONE);
        if (getmSetting().getBoolean(Constants.SETTING_DIRECT, true))

            imgDirection.setVisibility(View.VISIBLE);

    }

    private void changeToFinish() {
        mSwitch.setVisibility(View.GONE);
        mReceive.setVisibility(View.GONE);
        mMove.setVisibility(View.GONE);
        mPayLayout.setVisibility(View.VISIBLE);
        mTakeTrip.setVisibility(View.GONE);
        btnMylocation.setVisibility(View.GONE);
        mReady.setVisibility(View.GONE);

    }

    private void resetMap() {
        mReady.setVisibility(View.VISIBLE);
        mSwitch.setVisibility(View.VISIBLE);
        imgDirection.setVisibility(View.GONE);
        mReceive.setVisibility(View.GONE);
        mMove.setVisibility(View.GONE);
        mPayLayout.setVisibility(View.GONE);
        mTakeTrip.setVisibility(View.GONE);
        btnMylocation.setVisibility(View.VISIBLE);
        isJoinInToBooking = false;
        isStarting = false;
        if (polyline != null) polyline.remove();
        if (mMap != null) {
            mMap.clear();
            if (mLocation != null) {
                m = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_motorcycle, null), mMap, new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
                m.setVisible(true);
            } else {
                if (MainApplication.getInstance().getmLocation() != null) {
                    mLocation = MainApplication.getInstance().getmLocation();
                    m = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_motorcycle, null), mMap, new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
                    m.setVisible(true);
                }
            }
        }
    }

    @OnClick(R.id.img_direction)
    public void onViewClicked() {
        vibrate();
        if (isJoinInToBooking) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q="
                    + String.valueOf(tripInforModel.getTrip_info().getLatFrom())
                    + ","
                    + String.valueOf(tripInforModel.getTrip_info().getLngFrom())
                    + "&mode=d");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else if (isStarting) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q="
                    + String.valueOf(tripInforModel.getTrip_info().getLatTo())
                    + ","
                    + String.valueOf(tripInforModel.getTrip_info().getLngTo())
                    + "&mode=d");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    @Override
    public void onDestroy() {
//        getmSocket().updateStatusDriver(1, getCurrentIdUser());
//        mapView.onDestroy();
        super.onDestroy();
        clearInforTrip();
        unregisterReceiver(receiverBooking);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
    }

    @OnClick(R.id.ll_root_accept_take_trip)
    public void onViewAcceptClicked() {
        //// TODO: 7/27/2017 for stack trip
        if (tripTempForStack == null) return;
        if (isStarting && tripInforModel != null) {
            tripTempForStack.getTrip_info().setStatus(2);
            stackTrips.push(tripTempForStack);
            getDialogProgress().showDialog();
            Call<ApiResponse<String>> receiveTrip = getmApi().receiveTrip(ApiConstants.API_KEY, tripTempForStack.getTrip_info().getIdTrip(), getCurrentIdUser());
            receiveTrip.enqueue(new CallBackCustom<ApiResponse<String>>(this, getDialogProgress(), new OnResponse<ApiResponse<String>>() {
                @Override
                public void onResponse(ApiResponse<String> object) {
                    if (object.getData() != null && object.getStatus() == ApiConstants.CODE_SUCESS) {
                        getmSocket().updateStatusBooking(SocketConstants.STATUS_JOIN, tripTempForStack.getTrip_info().getIdTrip(), getCurrentIdUser(), tripTempForStack.getUser().getU_id());
                        tripTempForStack = null;
                        changeStateToGoin();
                        showSnackBar("Chuyến đi tiếp theo đã được đưa vào hàng đợi");
                    } else {
                        showDialogErrorContent(object.getMessage());
                        getmSocket().updateStatusBooking(SocketConstants.STATUS_DRIVER_CANCLE, tripTempForStack.getTrip_info().getIdTrip(), getCurrentIdUser(), tripInforModel.getUser().getU_id());
                        mTakeTrip.setVisibility(View.INVISIBLE);
                        isUserCancel = true;

                    }
                }
            }));

            return;
        }

        vibrate();
        //Nhan chuyen
        if (mLocation == null) {
            float lat = getmSetting().getFloat(Constants.LAT);
            float lng = getmSetting().getFloat(Constants.LNG);
            if (lat == 0 && lng == 0) {
                showDialogErrorContent("Chưa xác định được vị trí của bạn!");
                return;
            } else
                mLocation = new Location("");
            mLocation.setLatitude(lat);
            mLocation.setLongitude(lng);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(2);
        getmSocket().setCancelable(false);
        getDialogProgress().showDialog();
        Call<ApiResponse<String>> receiveTrip = getmApi().receiveTrip(ApiConstants.API_KEY, tripInforModel.getTrip_info().getIdTrip(), getCurrentIdUser());
        receiveTrip.enqueue(new CallBackCustom<ApiResponse<String>>(this, getDialogProgress(), new OnResponse<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> object) {
                if (object.getData() != null && object.getStatus() == ApiConstants.CODE_SUCESS) {
                    JoinBooking(true);
                } else {
                    showDialogErrorContent(object.getMessage());
                    getmSocket().updateStatusBooking(SocketConstants.STATUS_DRIVER_CANCLE, tripInforModel.getTrip_info().getIdTrip(), getCurrentIdUser(), tripInforModel.getUser().getU_id());
                    mTakeTrip.setVisibility(View.INVISIBLE);
                    isUserCancel = true;
                    getmSetting().put(Constants.BOOKING, "");
                    if (dialogFinish) {
                        changeToFinish();
                    } else {
                        clearInforTrip();
                        resetMap();
                    }
                }
            }
        }));
    }

    @AddTrace(name = "JoinBooking")
    private void JoinBooking(boolean isUpdateSocket) {
        changeToStateReceiveTrip();
        if (isUpdateSocket)
            getmSocket().updateStatusBooking(SocketConstants.STATUS_JOIN, tripInforModel.getTrip_info().getIdTrip(), getCurrentIdUser(), tripInforModel.getUser().getU_id());
        tvCall.setText(tripInforModel.getUser().getPhone());
        tvNameReceive.setText(tripInforModel.getUser().getName());
        tvAddressFromReceive.setText(tripInforModel.getTrip_info().getStart());
        tvAddressDesReceive.setText(tripInforModel.getTrip_info().getEnd());
        tvTypeReceive.setText(getTypeUser(tripInforModel.getUser().getType()));
        tvNameReceive.setEnabled(true);
        tvTypeReceive.setEnabled(true);
        Glide.with(MapsActivity.this).load("https://" + tripInforModel.getUser().getLinkType()).into(imgTypeUser);
//                    tvAddressDesReceive.setSelected(true);
//                    tvAddressFromReceive.setSelected(true);
//                    tvNameReceive.setSelected(true);
        isJoinInToBooking = true;
        saveInforTrip();
//                    mMarkerFrom = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_motorcycle, null), mMap, new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
//        if (polyline != null) polyline.remove();
        mMarkerDes = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pin_green2, null), mMap, new LatLng(tripInforModel.getTrip_info().getLatFrom(), tripInforModel.getTrip_info().getLngFrom()));
//                    String url = MapUtils.getDirectionsUrl(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), new LatLng(tripInforModel.getTrip_info().getLatFrom(), tripInforModel.getTrip_info().getLngFrom()));
//                    new DirectionAsynTaskBooking(getApplicationContext(), directionCustomModel, mMap, null, polyline).execute(url);
//        if (polyline != null) polyline.remove();
        showDirection(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), new LatLng(tripInforModel.getTrip_info().getLatFrom(), tripInforModel.getTrip_info().getLngFrom())
                , false);
        List<LatLng> list = new ArrayList<LatLng>();
        list.add(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
        list.add(new LatLng(tripInforModel.getTrip_info().getLatFrom(), tripInforModel.getTrip_info().getLngFrom()));
        CommonUtils.focusAllMarkers(list, mMap, MapsActivity.this);
        Glide.with(getApplicationContext()).load("https://" + tripInforModel.getUser().getAvatar()).into(imgAvtReceive);
        tripInforModel.getTrip_info().setStatus(SocketConstants.STATUS_JOIN);
        getmSetting().put(Constants.BOOKING, new Gson().toJson(tripInforModel));
        dialogFinish = false;
    }


    @Override
    protected void onStop() {
        super.onStop();

//        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        mapView.onLowMemory();
    }

    private void saveInforTrip() {
        getmSetting().put(Constants.ISGOIN, true);
        getmSetting().put(Constants.IDTRIP, tripInforModel.getTrip_info().getIdTrip());
        getmSetting().put(Constants.IDUSER, tripInforModel.getUser().getU_id());
    }

    private void clearInforTrip() {
        getmSetting().put(Constants.ISGOIN, false);
        getmSetting().put(Constants.ISSTART, false);
        getmSetting().put(Constants.IDTRIP, -1);
        getmSetting().put(Constants.IDUSER, -1);
//        getmSocket().setCancelable(true);
        isFocusDirection = false;
        dialogFinish = false;

    }

    private String getTypeUser(int type) {
        switch (type) {
            case 1: {
                return "Khách hàng mới";
            }
            case 2: {
                return "Khách hàng đồng";
            }
            case 3: {
                return "Khách hàng bạc";
            }
            case 4: {
                return "Khách hàng vàng";
            }
            case 5: {
                return "Khách hàng bạch kim";
            }
            default:
                return "Khách hàng mới";

        }
    }

    @OnClick(R.id.btn_mylocation)
    public void onViewMyLocationClicked() {
        vibrate();
        if (getmSocket() != null) {
            getmSocket().requestLocation();
        }
        if (mMap != null) mMap.clear();
        if (MainApplication.getInstance().getmLocation() != null) {
            mLocation = MainApplication.getInstance().getmLocation();
        }
        m = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_motorcycle, null), mMap, new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
        CommonUtils.focusCurrentLocation(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), Constants.ZOOM, mMap);

    }
}

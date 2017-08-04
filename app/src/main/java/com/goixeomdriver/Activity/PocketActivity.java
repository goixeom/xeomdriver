package com.goixeomdriver.Activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.models.PocketModel;
import com.goixeomdriver.utils.Constants;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class PocketActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_toolbar)
    CustomTextView txtToolbar;
    @BindView(R.id.tv_day_total)
    CustomTextView tvDayTotal;
    @BindView(R.id.tv_day_booking)
    CustomTextView tvDayBooking;
    @BindView(R.id.tv_day_km)
    CustomTextView tvDayKm;
    @BindView(R.id.tv_day_pocket)
    CustomTextView tvDayPocket;
    @BindView(R.id.tv_date_week)
    CustomTextView tvDateWeek;
    @BindView(R.id.tv_week_total)
    CustomTextView tvWeekTotal;
    @BindView(R.id.tv_week_booking)
    CustomTextView tvWeekBooking;
    @BindView(R.id.tv_week_km)
    CustomTextView tvWeekKm;
    @BindView(R.id.tv_week_price)
    CustomTextView tvWeekPrice;
    @BindView(R.id.tv_date_month)
    CustomTextView tvDateMonth;
    @BindView(R.id.tv_month_total)
    CustomTextView tvMonthTotal;
    @BindView(R.id.tv_month_booking)
    CustomTextView tvMonthBooking;
    @BindView(R.id.tv_month_km)
    CustomTextView tvMonthKm;
    @BindView(R.id.tv_month_price)
    CustomTextView tvMonthPrice;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @Override
    public void pingNotification(String title, String content) {

    }

    @Override
    public void onSocketReady() {

    }

    @Override
    public void onLocationChange(Location location) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket);
        ButterKnife.bind(this);
        txtToolbar.setText(Html.fromHtml(getString(R.string.txt_pocket)));
        getWallet();
    }

    private void getWallet() {
        getDialogProgress().showDialog();
        Call<ApiResponse<PocketModel>> getNoti = getmApi().getPocket(ApiConstants.API_KEY, getmSetting().getInt(Constants.ID));
        getNoti.enqueue(new CallBackCustom<ApiResponse<PocketModel>>(this, getDialogProgress(), new OnResponse<ApiResponse<PocketModel>>() {
            @Override
            public void onResponse(ApiResponse<PocketModel> object) {
                if (object.getData() != null) {
                    scrollView.setVisibility(View.VISIBLE);
                    tvDayTotal.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(object.getData().getDay().getTotal()) + " VNĐ");
                    setSpanCustomTextView(object.getData().getDay().getTrip() + " Chuyến", String.valueOf(object.getData().getDay().getTrip()).length() , tvDayBooking);
                    setSpanCustomTextView(getPriceFormat(object.getData().getDay().getCost())  + " vnđ", String.valueOf(object.getData().getDay().getCost()).length()+1, tvDayPocket);
                    setSpanCustomTextView(object.getData().getDay().getDistance() + " Km", String.valueOf(object.getData().getDay().getDistance()).length() , tvDayKm);

                    tvDateWeek.setText(String.format("%s đến %s", object.getData().getWeek().getStartDate(), object.getData().getWeek().getEndDate()));
                    tvWeekTotal.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(object.getData().getWeek().getTotal()) + " VNĐ");
                    setSpanCustomTextView(object.getData().getWeek().getTrip() + " Chuyến", String.valueOf(object.getData().getWeek().getTrip()).length() , tvWeekBooking);
                    setSpanCustomTextView(getPriceFormat(object.getData().getWeek().getCost()) + " vnđ", String.valueOf(getPriceFormat(object.getData().getWeek().getCost())).length() , tvWeekPrice);
                    setSpanCustomTextView(object.getData().getWeek().getDistance() + " Km", String.valueOf(object.getData().getWeek().getDistance()).length()  , tvWeekKm);

                    tvDateMonth.setText(String.format("%s đến %s", object.getData().getMonth().getStartDate(), object.getData().getMonth().getEndDate()));
                    tvMonthTotal.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(object.getData().getMonth().getTotal()) + " VNĐ");
                    setSpanCustomTextView(object.getData().getMonth().getTrip() + " Chuyến", String.valueOf(object.getData().getMonth().getTrip()).length() , tvMonthBooking);
                    setSpanCustomTextView(getPriceFormat(object.getData().getMonth().getCost()) + " vnđ", String.valueOf(getPriceFormat(object.getData().getMonth().getCost())).length() , tvMonthPrice);
                    setSpanCustomTextView(object.getData().getMonth().getDistance() + " Km", String.valueOf(object.getData().getMonth().getDistance()).length() , tvMonthKm);
                }
            }
        }));
    }

    private String getPriceFormat(double number){
        return NumberFormat.getNumberInstance(Locale.GERMAN).format(number);
    }
    private void setSpanCustomTextView(String text, int index, CustomTextView customCustomTextView) {
        String s = text;
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 0, index, 0); // set size
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, index, 0);// set color
        ss1.setSpan(boldSpan,0, index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        customCustomTextView.setText(ss1);
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        vibrate();

        finish();
    }
}

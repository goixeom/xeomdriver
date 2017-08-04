package com.goixeomdriver.Activity;

import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.models.DetailTripModel;
import com.goixeomdriver.utils.CommonUtils;
import com.goixeomdriver.utils.Constants;
import com.google.android.gms.maps.GoogleMap;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

public class DetailBooking extends BaseActivity {


    int id;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_toolbar)
    CustomTextView txtToolbar;

    @BindView(R.id.tv_from)
    CustomTextView tvFrom;
    @BindView(R.id.from)
    LinearLayout from;
    @BindView(R.id.customTextView2)
    CustomTextView customTextView2;
    @BindView(R.id.tv_des)
    CustomTextView tvDes;
    @BindView(R.id.tv_discount_booking)
    CustomTextView tvDiscountBooking;

    @BindView(R.id.tv_time)
    CustomTextView tvTime;
    @BindView(R.id.tv_total)
    CustomTextView tvTotal;
    @BindView(R.id.rate_detail)
    RatingBar rateDetail;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.tv_name_driver)
    CustomTextView tvNameDriver;
    @BindView(R.id.tv_number_driver)
    CustomTextView tvNumberDriver;
    @BindView(R.id.tv_type_driver)
    CustomTextView tvTypeDriver;
    @BindView(R.id.tv_vote_driver)
    CustomTextView tvVoteDriver;
    @BindView(R.id.discount)
    LinearLayout discount;
    @BindView(R.id.img_call)
    ImageView imgCall;
    @BindView(R.id.tv_id_booking)
    CustomTextView tvIdBooking;
    @BindView(R.id.ll_root)
    RelativeLayout llRoot;
    @BindView(R.id.img_map)
    ImageView imgMap;
    private GoogleMap mMap;
    private String urlStatic = "https://maps.googleapis.com/maps" +
            "/api/staticmap?" +
            "size=500x780" +
            "&zoom=13" +
            "&markers=458%20Minh%20Khai,%20Khu%20%C4%91%C3%B4%20th%E1%BB%8B%20Times%20City,%20V%C4%A9nh%20Tuy,%20Hai%20B%C3%A0%20Tr%C6%B0ng,%20H%C3%A0%20N%E1%BB%99i,%20Vi%E1%BB%87t%20Nam" +
            "&markers=291%20Hu%E1%BA%BF,%20L%C3%AA%20%C4%90%E1%BA%A1i%20H%C3%A0nh" +
            "&path=enc:{ec_CimdeS?`DC@KFENDLLFF?EpGWV}J@mE?e@DwC~@o@PKLlClKr@jC?PALENGNOHaIn@a@FBfAyBHeERwNt@qGVmMp@sHb@kBJkAAaAKg@GATEnDKlNAhHHfAz@pHZfCAd@BjE?d@ITIRERWRYJuCKqDM" +
            "&key=AIzaSyBkqHz9U6vIFYhBtmSWOajaC05zF97YKuQ";

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
        setContentView(R.layout.activity_detail_booking);
        ButterKnife.bind(this);
        view = llRoot;
        txtToolbar.setText(Html.fromHtml(getString(R.string.txt_detail)));
        tvDes.setSelected(true);
        tvFrom.setSelected(true);
        tvNameDriver.setSelected(true);
        if (getIntent() != null) {
            id = getIntent().getIntExtra(Constants.BOOKING, -1);
            if (id != -1) {
                getDetailBooking(id);
            }
        }
    }


    private void getDetailBooking(int idBooking) {

        getDialogProgress().showDialog();
        Call<ApiResponse<DetailTripModel>> getDetailBooking = getmApi().getDetailBooking(ApiConstants.API_KEY, idBooking);
        getDetailBooking.enqueue(new CallBackCustom<ApiResponse<DetailTripModel>>(this, getDialogProgress(), new OnResponse<ApiResponse<DetailTripModel>>() {
            @Override
            public void onResponse(ApiResponse<DetailTripModel> object) {
                if (object.getData() != null) {

                    tvFrom.setText(object.getData().getTrip_info().getStart());
                    tvDes.setText(object.getData().getTrip_info().getEnd());
                    if (object.getData().getTrip_info().getCode() == null || object.getData().getTrip_info().getCode().isEmpty()) {
                        discount.setVisibility(View.INVISIBLE);
                    } else {
                        tvDiscountBooking.setText(object.getData().getTrip_info().getCode());
                    }
                    tvTime.setText(object.getData().getTrip_info().getDate());
                    tvTotal.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(object.getData().getTrip_info().getPrice()) + "vnđ");
                    rateDetail.setRating(object.getData().getTrip_info().getVote());
                    tvNameDriver.setText(object.getData().getUser().getName());
                    tvNumberDriver.setText(getTypeUser(object.getData().getUser().getType() ));
//                    tvTypeDriver.setText(object.getData().getDriver().getModel());
//                    tvVoteDriver.setText(object.getData().getDriver().getVote() + "");
                    Glide.with(getApplicationContext()).load("https://"+object.getData().getUser().getAvatar()).asBitmap().into(profileImage);
                    tvIdBooking.setText(object.getData().getTrip_info().getIdTrip() + "");
                    Glide.with(getApplicationContext()).load("https://maps.googleapis.com/maps/api/"+object.getData().getTrip_info().getStaticmap()).asBitmap().into(imgMap);
                    llRoot.setVisibility(View.VISIBLE);
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
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
    @OnClick({R.id.img_back, R.id.img_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_call:
                CommonUtils.intentToCall("02462931011", this);
                break;
        }
    }


}

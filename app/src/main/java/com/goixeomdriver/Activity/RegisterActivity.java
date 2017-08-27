package com.goixeomdriver.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.RegexUtils;
import com.goixeom.R;
import com.goixeomdriver.View.CustomEditText;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.utils.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;


public class RegisterActivity extends BaseAuthActivity {
    @BindView(R.id.send_imv)
    ImageView sendImv;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.customTextView)
    CustomTextView customTextView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.edt_name)
    CustomEditText edtName;
    @BindView(R.id.edt_email)
    CustomEditText edtEmail;
    @BindView(R.id.edt_phone)
    CustomEditText edtPhone;
    @BindView(R.id.edt_ref_code)
    CustomEditText edtRefCode;
    @BindView(R.id.dangky_txt)
    CustomTextView dangkyTxt;
    @BindView(R.id.ll_scroll)
    ScrollView llScroll;
    @BindView(R.id.rad_group)
    RadioGroup radGroup;
    private Button mSignIn;
    private CustomTextView mDangKy;
    private boolean isTermClicked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDangKy = (CustomTextView) findViewById(R.id.dangky_txt);
//        Spannable span = Spannable.Factory.getInstance().newSpannable("  Bằng cách nhấp vào <b>ĐĂNG KÝ</b> tôi đồng ý rằng goixeom hoặc Công ty chủ quản của\n" +
//                "        Goixeom có thể liên hệ với tôi qua Email SMS, hoặc điện thoại di động kể cả mục đích quảng cáo.\n" +
//                "        Tôi đã đọc vào đồng ý với các <b>Điều khoản</b> mà GOIXEOM quy định cho tài xế.");
//        int length = span.length();
//        span.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View v) {
//                if (isTermClicked) {
//                    isTermClicked = false;
//                    return;
//                }
//                Intent intent = new Intent(RegisterActivity.this, ActivityPrivacy.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//                isTermClicked = true;
//            }
//
//            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
//            }
//        }, length - 47, length - 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
//        span.setSpan(boldSpan, length - 47, length - 37, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mDangKy.setText(span, TextView.BufferType.SPANNABLE);
//        mDangKy.setMovementMethod(LinkMovementMethod.getInstance());
        mDangKy.setText(Html.fromHtml(getString(R.string.txt_dangky)));
        mDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, ActivityPrivacy.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        mSignIn = (Button) findViewById(R.id.sigin_btn);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = edtPhone.getText().toString();
                final String lastName = edtName.getText().toString();
                final String email = edtEmail.getText().toString();
                final String refcode = edtRefCode.getText().toString();
                final int typeService  ;
                if(radGroup.getCheckedRadioButtonId() == R.id.rad_bike){
                    typeService=0;
                }else{
                    typeService=1;

                }
                if (!verifyPhoneSucess(phone)) {
                    return;
                }
                if (!verifyLastName(lastName)) {
                    return;
                }
                if (!isValidEmailAddress(email)) {
                    return;
                }
                getDialogProgress().showDialog();
                Call<ApiResponse> checkInfo = getmApi().checkInfor(ApiConstants.API_KEY, phone, lastName, email, refcode);
                checkInfo.enqueue(new CallBackCustom<ApiResponse>(getApplicationContext(), getDialogProgress(), new OnResponse<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse object) {
                        if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                            Intent intent = new Intent(RegisterActivity.this, AccuracyActivity.class);
                            intent.putExtra(Constants.PHONE, phone);
                            intent.putExtra(Constants.EMAIL, email);
                            intent.putExtra(Constants.REF_CODE, refcode);
                            intent.putExtra(Constants.NAME, lastName);
                            intent.putExtra(Constants.TYPE_SERVICE, typeService);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        } else {
                            showDialogErrorContent(object.getMessage());
                        }
                    }
                }));

            }
        });
        final View activityRootView = findViewById(R.id.ll_root);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(RegisterActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    llScroll.scrollTo(0, 180);
                }
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public boolean isValidEmailAddress(String email) {
        if (email.isEmpty()) {
            showDialogErrorContent(getString(R.string.please_enter_email_2));
            return false;
        }
        if (!RegexUtils.isEmail(email)) {
            new MaterialDialog.Builder(this).title(R.string.error)
                    .content(getString(R.string.please_enter_email))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        return true;
    }

    private boolean verifyPhoneSucess(String phone) {

        if (phone.isEmpty()) {
            new MaterialDialog.Builder(this).title(R.string.error)
                    .content(getString(R.string.empty_phone))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        if ((phone.length() < 10) || phone.length() > 11) {
            new MaterialDialog.Builder(this).title(R.string.error)
                    .content(getString(R.string.please_enter_phone))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        if (phone.length() == 10) {
            if (!phone.substring(0, 2).equals("09")) {
                new MaterialDialog.Builder(this).title(R.string.error)
                        .content(getString(R.string.please_enter_phone))
                        .positiveText(R.string.dismis)
                        .positiveColor(Color.GRAY)
                        .show();
                return false;
            }
        } else {
            if (!phone.substring(0, 2).equals("01")) {
                new MaterialDialog.Builder(this).title(R.string.error)
                        .content(getString(R.string.please_enter_phone))
                        .positiveText(R.string.dismis)
                        .positiveColor(Color.GRAY)
                        .show();
                return false;
            }
        }


        return true;
    }

    private boolean verifyLastName(String name) {
        if (name.isEmpty()) {
            new MaterialDialog.Builder(this).title(R.string.error)
                    .content(getString(R.string.please_enter_lname))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        Pattern regex = Pattern.compile("[$&+,:;=?@#|]");
        Matcher matcher = regex.matcher(name);

        if (name.matches(".*\\d.*") || matcher.find()) {
            new MaterialDialog.Builder(this).title(R.string.error)
                    .content(getString(R.string.incorrect_name))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        return true;
    }

}

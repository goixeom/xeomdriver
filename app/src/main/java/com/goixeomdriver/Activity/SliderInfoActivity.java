package com.goixeomdriver.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.goixeom.R;
import com.goixeomdriver.Fragment.FragmentIntroduceOne;
import com.goixeomdriver.Fragment.FragmentIntroduceThree;
import com.goixeomdriver.Fragment.FragmentIntroduceTwo;
import com.goixeomdriver.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class SliderInfoActivity extends BaseAuthActivity {
    ViewPager pager;
    PagerAdapter mPagerAdapter;
    CircleIndicator indicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.dangnhap_btn)
    Button dangnhapBtn;
    @BindView(R.id.dangky_btn)
    Button dangkyBtn;
    @BindView(R.id.sign)
    LinearLayout sign;
    @BindView(R.id.ll_root)
    RelativeLayout llRoot;
    private Button mDangKy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_info);
        ButterKnife.bind(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
        }
        if (getIntent() != null) {
            int status = getIntent().getIntExtra(Constants.STATUS, -1);
            if (status == Constants.KICKOUT) {
                removeDataUser();
              //  showDialogErrorContent("Tài khoản đã đăng nhập trên một thiết bị khác");
            }
            if (status == Constants.BLOCK) {
                removeDataUser();
                showDialogErrorContent("Tài khoản đã bị block. Vui lòng liên hệ với người quản trị để được hỗ trợ!");
            }
        }
        view = llRoot;
        pager = (ViewPager) findViewById(R.id.view_pager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        FragmentManager manager = getSupportFragmentManager();
        mPagerAdapter = new ScreenSlidePagerAdapter(manager);
        pager.setAdapter(mPagerAdapter);
        indicator.setViewPager(pager);

        mDangKy = (Button) findViewById(R.id.dangky_btn);
        mDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SliderInfoActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }


    private void removeDataUser() {
        getmSetting().put(Constants.PHONE, -1);
        getmSetting().put(Constants.ID, -1);
        getmSetting().put(Constants.IS_SLIDE, false);
        getmSetting().put(Constants.TYPE_PASSWORD_MAP, false);
        getmSetting().put(Constants.CLICK_GOMAP, false);
        getmSetting().put(Constants.TYPE_USER_STR, -1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            showDialogPermission();
        }
    }

    private void showDialogPermission() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this).title(getString(R.string.error))
                .content("Bạn cần cấp quyền truy cập ứng dụng để tiếp tục sử dụng dịch vụ")
                .positiveColor(Color.GRAY)
                .positiveText("Đồng ý")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ActivityCompat.requestPermissions(SliderInfoActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                    }
                }).show();
    }

    @OnClick(R.id.dangnhap_btn)
    public void onViewClicked() {
        Intent intent = new Intent(SliderInfoActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fr = null;
            switch (position) {
                case 0:
                    fr = new FragmentIntroduceOne();
                    break;
                case 1:
                    fr = new FragmentIntroduceTwo();
                    break;
                case 2:
                    fr = new FragmentIntroduceThree();
                    break;
            }
            return fr;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}

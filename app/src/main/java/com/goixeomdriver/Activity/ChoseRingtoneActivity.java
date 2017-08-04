package com.goixeomdriver.Activity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.utils.Constants;
import com.goixeomdriver.views.AudioPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChoseRingtoneActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_toolbar)
    CustomTextView txtToolbar;
    @BindView(R.id.cb_gg)
    CheckBox cbGg;
    @BindView(R.id.ll_ring_gg)
    LinearLayout llRingGg;
    @BindView(R.id.cb_ga_chong)
    CheckBox cbGaChong;
    @BindView(R.id.ll_ring_ga)
    LinearLayout llRingGa;
    @BindView(R.id.cb_ga_gay)
    CheckBox cbGaGay;
    @BindView(R.id.ll_ring_ga_gay)
    LinearLayout llRingGaGay;
    @BindView(R.id.cb_ten)
    CheckBox cbTen;
    @BindView(R.id.ll_ring_ten)
    LinearLayout llRingTen;
    @BindView(R.id.cb_5)
    CheckBox cb5;
    @BindView(R.id.ll_root_5)
    LinearLayout llRoot5;

    private AudioPlayer audioPlayer;
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
        setContentView(R.layout.activity_chose_ringtone);
        ButterKnife.bind(this);
        int pos = getmSetting().getInt(Constants.SETTING_SOUND);
        switch (pos) {
            case 0: {
                cbGg.setChecked(true);
                cbGg.setVisibility(View.VISIBLE);
                break;
            }
            case 1: {
                cbGaChong.setChecked(true);
                cbGaChong.setVisibility(View.VISIBLE);

                break;
            }
            case 2: {
                cbGaGay.setChecked(true);
                cbGaGay.setVisibility(View.VISIBLE);

                break;
            }
            case 3: {
                cbTen.setChecked(true);
                cbTen.setVisibility(View.VISIBLE);

                break;
            }
            case 4: {
                cb5.setChecked(true);
                cb5.setVisibility(View.VISIBLE);

                break;
            }
        }
        cbGaChong.setOnCheckedChangeListener(this);
        cbGaGay.setOnCheckedChangeListener(this);
        cbTen.setOnCheckedChangeListener(this);
        cb5.setOnCheckedChangeListener(this);
        cbGg.setOnCheckedChangeListener(this);
        audioPlayer  = new AudioPlayer();
    }

    @OnClick({R.id.img_back, R.id.ll_ring_gg, R.id.ll_ring_ga, R.id.ll_ring_ga_gay, R.id.ll_ring_ten,R.id.ll_root_5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                vibrate();
                finish();
                break;
            case R.id.ll_ring_gg:
                clearCheck();
                cbGg.setChecked(true);

                break;
            case R.id.ll_ring_ga:
                clearCheck();
                cbGaChong.setChecked(true);

                break;
            case R.id.ll_ring_ga_gay:
                clearCheck();
                cbGaGay.setChecked(true);

                break;
            case R.id.ll_ring_ten:
                clearCheck();
                cbTen.setChecked(true);

                break;
            case R.id.ll_root_5:
                clearCheck();
                cb5.setChecked(true);

                break;
        }
    }

    @Override
    protected void onPause() {
        audioPlayer.stop();
        super.onPause();
    }

    @Override
    protected void onStop() {


        super.onStop();
    }

    private void clearCheck(){
        if(cbGg.isChecked()){
            cbGg.setChecked(false);
            cbGg.setVisibility(View.INVISIBLE);
        }
        else if(cbGaChong.isChecked()) {
            cbGaChong.setChecked(false);
            cbGaChong.setVisibility(View.INVISIBLE);

        }
      else  if(cb5.isChecked()) {
            cb5.setChecked(false);
            cb5.setVisibility(View.INVISIBLE);
        }
      else  if(cbGaGay.isChecked()) {
            cbGaGay.setChecked(false);
            cbGaGay.setVisibility(View.INVISIBLE);

        }
      else  if(cbTen.isChecked()) {
            cbTen.setChecked(false);
            cbTen.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        vibrate();
        switch (compoundButton.getId()) {
            case R.id.cb_ga_chong: {
                getmSetting().put(Constants.SETTING_SOUND, 1);
                cbGaChong.setVisibility(View.VISIBLE);
                audioPlayer.play(this,R.raw.despacito);

                break;
            }
            case R.id.cb_ga_gay: {
                getmSetting().put(Constants.SETTING_SOUND, 2);
                cbGaGay.setVisibility(View.VISIBLE);
                audioPlayer.play(this,R.raw.mr_taxi);

                break;
            }
            case R.id.cb_gg: {
                getmSetting().put(Constants.SETTING_SOUND, 0);
                cbGg.setVisibility(View.VISIBLE);
                audioPlayer.play(this,R.raw.banana_song);

                break;
            }
            case R.id.cb_ten: {
                getmSetting().put(Constants.SETTING_SOUND, 3);
                cbTen.setVisibility(View.VISIBLE);
                audioPlayer.play(this,R.raw.shape_of_you);

                break;
            }
            case R.id.cb_5: {
                getmSetting().put(Constants.SETTING_SOUND, 4);
                cb5.setVisibility(View.VISIBLE);
                audioPlayer.play(this,R.raw.very_super_tone_ever);

                break;
            }
        }
    }
}

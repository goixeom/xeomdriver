package com.goixeomdriver.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;


/**
 * Created by Huy on 6/19/2017.
 */

public class FragmentIntroduceTwo extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_introduce, container, false);
        ImageView imgIntroduce = (ImageView) view.findViewById(R.id.img_fragment_introduce);
        imgIntroduce.setImageResource(R.drawable.ic_no_phone);
        CustomTextView txtDetail = (CustomTextView) view.findViewById(R.id.txt_detail);
        txtDetail.setText(getString(R.string.detail_3));
        return view;
    }
}
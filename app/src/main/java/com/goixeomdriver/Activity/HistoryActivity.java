package com.goixeomdriver.Activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.goixeom.R;
import com.goixeomdriver.View.CustomTextView;
import com.goixeomdriver.adapters.AdapterHistory;
import com.goixeomdriver.apis.ApiConstants;
import com.goixeomdriver.apis.ApiResponse;
import com.goixeomdriver.apis.CallBackCustom;
import com.goixeomdriver.interfaces.OnResponse;
import com.goixeomdriver.models.History;
import com.goixeomdriver.utils.Constants;
import com.goixeomdriver.views.DemoLoadMoreView;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class HistoryActivity extends BaseActivity {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_toolbar)
    CustomTextView txtToolbar;
    @BindView(R.id.rcv)
    PullToRefreshRecyclerView rcv;
    @BindView(R.id.loading)
    ProgressBar loading;
    List<History> list = new ArrayList<>();
    AdapterHistory adapterHistory;
    int page = 0;
    @BindView(R.id.img_empty)
    ImageView imgEmpty;
    private boolean needToHold;
    private boolean isFirstTime = true;

    public HistoryActivity() {
    }

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
        setContentView(R.layout.content_history);
        ButterKnife.bind(this);
        txtToolbar.setText(Html.fromHtml(getString(R.string.txt_toolbar_history)));
        rcv.setLayoutManager(new LinearLayoutManager(this));
        adapterHistory = new AdapterHistory(this, list);
//        rcv.setLoadmoreString("Đang tải");
        rcv.removeHeader();
        DemoLoadMoreView loadMoreView = new DemoLoadMoreView(this, rcv.getRecyclerView());
        loadMoreView.setLoadmoreString("Đang tải");
        loadMoreView.setLoadMorePadding(100);
        rcv.setLoadMoreFooter(loadMoreView);
        loading.setVisibility(View.GONE);
        rcv.setLoadMoreCount(5);
        rcv.setAdapter(adapterHistory);
        rcv.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                getListPromotion();
            }
        });
        rcv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListPromotion();
                needToHold = false;
            }
        });
        getListPromotion();
    }

    private void getListPromotion() {
        getDialogProgress().showDialog();
        Call<ApiResponse<List<History>>> getPromotions = getmApi().getHistories(ApiConstants.API_KEY, getmSetting().getInt(Constants.ID));
        getPromotions.enqueue(new CallBackCustom<ApiResponse<List<History>>>(this, getDialogProgress(), new OnResponse<ApiResponse<List<History>>>() {
            @Override
            public void onResponse(ApiResponse<List<History>> object) {
                loading.setVisibility(View.GONE);
//                if(!needToHold) {
//                    list.clear();
////                    rcv.setSwipeEnable(true);
//                }
                if (object != null && object.getData() != null && object.getData().size() != 0) {
                    list.addAll(object.getData());
                    imgEmpty.setVisibility(View.GONE);
                  isFirstTime = false;
                }else{
                    if (isFirstTime)

                        imgEmpty.setVisibility(View.VISIBLE);

                }
                rcv.onFinishLoading(false, false);
                rcv.setOnRefreshComplete();
            }
        }));
    }


    @OnClick(R.id.img_back)
    public void onViewClicked() {
        vibrate();

        finish();
    }
}

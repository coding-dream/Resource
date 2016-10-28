package com.syntc.dodgem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.syntc.dodgem.bean.PageBean;
import com.syntc.dodgem.bean.ResultBean;
import com.syntc.dodgem.bean.Tweet;
import com.syntc.dodgem.com.syntc.help.DialogHelper;

import static com.syntc.dodgem.BaseRecyclerAdapter.ONLY_FOOTER;

public class MainActivity extends AppCompatActivity {

    protected RecyclerRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected BaseRecyclerAdapter<Tweet> mAdapter;
    private boolean mIsRefresh;

    protected PageBean<Tweet> mBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SoftwareTweetAdapter tweetAdapter = new SoftwareTweetAdapter(this, ONLY_FOOTER);
        tweetAdapter.setOnItemLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(int position, long itemId) {
//
//                final Tweet tweet = mAdapter.getItem(position);
//                final long sourceId = tweet.getId();
//                //一些逻辑操作. .. ..
//                if (id == loginUid) {
//                    DialogHelp.getConfirmDialog
//                }
            }

    });



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                Tweet tweet = mAdapter.getItem(position);
//                ...........




            }
        });
        mRefreshLayout.setSuperRefreshLayoutListener(new RecyclerRefreshLayout.SuperRefreshLayoutListener() {
            @Override
            public void onRefreshing() {
                mIsRefresh = true;
                requestData();
            }

            @Override
            public void onLoadMore() {
                requestData();
            }
        });


        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        initData();







    }

    private void initData() {

            mBean = new PageBean<>();

            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                    mIsRefresh = true;
                    requestData();  // ========== >请求数据
                }
            });

    }

    private void requestData() {

        //网络访问，请求数据
        OSChinaApi.getSoftwareTweetList("softwareName", mIsRefresh ? null : mBean.getNextPageToken(), new OSChinaApi.ResultCallBack() {

            @Override
            public void start() {
                DialogHelper.onLoadingStart();
            }

            @Override
            public void success(String result) {
                DialogHelper.onLoadingSuccess();

                Gson gson = new Gson();
                ResultBean<PageBean<Tweet>> resultBean = gson.fromJson(result,ResultBean.class);
                if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {
                    setListData(resultBean);
                }


            }

            @Override
            public void fail() {
                DialogHelper.onLoadingFailure();
            }

            @Override
            public void end() {
                DialogHelper.onLoadingFinish();

                mRefreshLayout.onComplete();
                mIsRefresh = false;

            }
        });



    }

    //这种 只有在 请求成功的情况下 设置 PageBean的页面，不会导致加载失败时候，页码的多余增加
    protected void setListData(ResultBean<PageBean<Tweet>> resultBean) {
        mBean.setNextPageToken(resultBean.getResult().getNextPageToken());// 设置下一页

        if (mIsRefresh) {
            mBean.setItems(resultBean.getResult().getItems());//把 ResultBean中的数据转移到PageBean中
            mAdapter.clear();
            mAdapter.addAll(mBean.getItems());//PageBean里面存放的才是Adapter的数据
            mBean.setPrevPageToken(resultBean.getResult().getPrevPageToken());//previous 如果是刷新，页数再往前一次
            mRefreshLayout.setCanLoadMore(true); // 设置 能加载更多了(应该是防止正在加载的时候还没载入完 又请求加载了)）
        } else {
            mAdapter.addAll(resultBean.getResult().getItems());//如果是加载更多，不清除 继续addAll()
        }
        if (resultBean.getResult().getItems().size() < 20) {  //如果网络获取的数据一页数据不够20个，说明是最后一页了
            mAdapter.setState(BaseRecyclerAdapter.STATE_NO_MORE, true);
        }
    }





}

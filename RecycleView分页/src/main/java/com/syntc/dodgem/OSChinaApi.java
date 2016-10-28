package com.syntc.dodgem;

/**
 * Created by wangli on 16/10/28.
 */
public class OSChinaApi {

     interface  ResultCallBack{
         void start();
         void success(String result);
         void fail();
         void end();
    }

    //OSChinaApi.getSoftwareTweetList(softwareName, mIsRefresh ? null : mBean.getNextPageToken(), mHandler);

    // pageBeanToken 或者使用我们自己的int currentPage;
    public  static void getSoftwareTweetList(String softwareName, String pageBeanToken, ResultCallBack callBack){

    }

}

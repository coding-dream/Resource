package com.syntc.dodgem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.syntc.dodgem.bean.Tweet;

import butterknife.ButterKnife;

/**
 * Created by fei on 2016/7/20.
 */

public class SoftwareTweetAdapter extends BaseRecyclerAdapter<Tweet> implements View.OnClickListener {


    public SoftwareTweetAdapter(Context context, int mode) {
        super(context, mode);
        setState(BaseRecyclerAdapter.STATE_LOADING, false);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new SoftwareTweetViewholder(mInflater.inflate(R.layout.item_list_tweet_improve, parent, false));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, Tweet item, int position) {
        SoftwareTweetViewholder vh = (SoftwareTweetViewholder) holder;

//        CircleImageView icon = vh.icon;
//        icon.setTag(R.id.iv_tweet_face, position);
//        ImageLoader.loadImage(requestManager, vh.icon, item.getAuthor().getPortrait(), R.mipmap.widget_dface);
//        vh.icon.setOnClickListener(this);
//        vh.name.setText(item.getAuthor().getName());
//        CommentsUtil.formatHtml(mContext.getResources(), vh.content, item.getContent());
//        vh.pubTime.setText(StringUtils.friendly_time(item.getPubDate()));
//        PlatfromUtil.setPlatFromString(vh.deviceType, item.getAppClient());

//
//        boolean liked = item.isLiked();
//        if (liked) {
//            vh.likeStatus.setImageResource(R.mipmap.ic_thumbup_actived);
//        } else {
//            vh.likeStatus.setImageResource(R.mipmap.ic_thumbup_normal);
//        }
//        vh.likeStatus.setTag(position);
//        vh.likeStatus.setOnClickListener(this);
//        vh.likeCount.setText(item.getLikeCount() + "");
//        vh.commentCount.setText(item.getCommentCount() + "");

    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        super.setOnItemClickListener(onItemClickListener);

    }

    @Override
    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.iv_tweet_face:
//                int p = (int) v.getTag(R.id.iv_tweet_face);
//                final Tweet item = getItem(p);
//                Author author = item.getAuthor();
//                User user = new User();
//                user.setId((int) author.getId());
//                user.setName(author.getName());
//                user.setPortrait(author.getPortrait());
//                OtherUserHomeActivity.show(mContext, user);
//                break;
//            case R.id.iv_like_state:
//                int position = (int) v.getTag();
//                final Tweet tempItem = getItem(position);
//                requestEventDispatcher(tempItem);
//                break;
//            default:
//                break;
//        }
    }

    /**
     *
     */
    private void requestEventDispatcher(final Tweet item) {
//
//        if (!AppContext.getInstance().isLogin()) {
//            UIHelper.showLoginActivity(mContext);
//            return;
//        }
//        if (!TDevice.hasInternet()) {
//            AppContext.showToastShort(R.string.tip_no_internet);
//            return;
//        }
//
//        OSChinaApi.pubSoftwareLike(item.getId(), new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                Toast.makeText(mContext, "操作失败...", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                try {
//                    Type type = new TypeToken<ResultBean<TweetLikeReverse>>() {
//                    }.getType();
//                    ResultBean<TweetLikeReverse> resultBean = AppContext.createGson().fromJson(responseString, type);
//                    if (resultBean.getCode() == 1) {
//                        TweetLikeReverse result = resultBean.getResult();
//                        boolean like = result.isLiked();
//                        item.setLiked(like);
//                        int likeCount = item.getLikeCount();
//                        item.setLikeCount((!item.isLiked() ? likeCount - 1 : likeCount + 1));
//                        notifyDataSetChanged();
//                    } else {
//                        Toast.makeText(mContext, "操作失败...", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    onFailure(statusCode, headers, responseString, e);
//                }
//            }
//        });

    }

    static class SoftwareTweetViewholder extends RecyclerView.ViewHolder {
//
//        @Bind(R.id.iv_tweet_face)
//        CircleImageView icon;
//        @Bind(R.id.tv_tweet_name)
//        TextView name;
//        @Bind(R.id.tweet_item)
//        TweetTextView content;
//        @Bind(R.id.tv_tweet_time)
//        TextView pubTime;
//        @Bind(R.id.tv_tweet_platform)
//        TextView deviceType;
//        @Bind(R.id.iv_like_state)
//        ImageView likeStatus;
//        @Bind(R.id.tv_tweet_like_count)
//        TextView likeCount;
//        @Bind(R.id.tv_tweet_comment_count)
//        TextView commentCount;

        SoftwareTweetViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

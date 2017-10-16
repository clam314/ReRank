package com.clam314.rxrank.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clam314.rxrank.R;
import com.clam314.rxrank.View.CircleProgressDrawable;
import com.clam314.rxrank.entity.zhihu.DailyNews;
import com.clam314.rxrank.entity.zhihu.Story;
import com.clam314.rxrank.util.FrescoUtil;
import com.clam314.rxrank.util.TimeUtil;
import com.clam314.rxrank.util.ViewUtil;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clam314 on 2017/10/15
 */

public class DailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DailyNews> dailyNewsList;

    public DailyAdapter(List<DailyNews> list) {
        dailyNewsList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_daily_news, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            setData((Holder) holder, dailyNewsList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dailyNewsList == null ? 0 : dailyNewsList.size();
    }

    private void setData(Holder holder, DailyNews news) {
        holder.tvDay.setText(TimeUtil.timeForm(news.getDate(), "yyyyMMdd", "MM/dd"));
        for (int i = 0; i < Holder.eachCategoryMaxItem; i++) {
            View content = holder.llContentList.get(i);
            TextView tv = holder.tvTitleList.get(i);
            final SimpleDraweeView iv = holder.ivList.get(i);
            if (news.getStories() != null && news.getStories().size() > i) {
                final Story item = news.getStories().get(i);
                setItemClick(content, item);
                tv.setVisibility(View.VISIBLE);
                tv.setText(item.getTitle());
                if (item.getImages() == null
                        || item.getImages().size() == 0
                        || TextUtils.isEmpty(item.getImages().get(0))) {
                    iv.setVisibility(View.GONE);
                } else {
                    iv.setVisibility(View.VISIBLE);
                    FrescoUtil.setProgressBar(iv, CircleProgressDrawable.newDefaultInstance(iv.getContext()));
                    Uri uri = Uri.parse(item.getImages().get(0));
//                    FrescoUtil.loadImage(uri, iv, null, 0, 0, null);
                    FrescoUtil.loadImage(iv,uri);
                }
            } else {
                tv.setVisibility(View.GONE);
                iv.setVisibility(View.GONE);
            }
        }
    }

    private void setItemClick(View view, Story item) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private static class Holder extends RecyclerView.ViewHolder {
        private static final int eachCategoryMaxItem = 20;
        TextView tvDay;
        LinearLayout lyContent;
        List<FrameLayout> llContentList;
        List<TextView> tvTitleList;
        List<SimpleDraweeView> ivList;

        Holder(View itemView) {
            super(itemView);
            tvDay = (TextView) itemView.findViewById(R.id.tv_day);
            lyContent = (LinearLayout) itemView.findViewById(R.id.ly_news_content);
            llContentList = new ArrayList<>(eachCategoryMaxItem);
            tvTitleList = new ArrayList<>(eachCategoryMaxItem);
            ivList = new ArrayList<>(eachCategoryMaxItem);
            createView(itemView.getContext());
        }

        void createView(Context context) {
            int colorInt = ViewUtil.getColorByResource(context, R.color.item_category_title_color);
            for (int i = 0; i < eachCategoryMaxItem; i++) {
                TextView tv = new TextView(context);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(ViewUtil.dp2px(context, 2f), ViewUtil.dp2px(context, 2f), ViewUtil.dp2px(context, 105f), ViewUtil.dp2px(context, 2f));
                tv.setLayoutParams(lp);
                tv.setText("Content");
                tv.setTextSize(16f);
                tv.setTextColor(colorInt);
                Drawable drawable = context.getResources().getDrawable(R.drawable.shape_little_circle);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv.setCompoundDrawables(drawable, null, null, null);
                tv.setCompoundDrawablePadding(ViewUtil.dp2px(context, 3f));
                tvTitleList.add(tv);

                SimpleDraweeView sv = new SimpleDraweeView(context);
                FrameLayout.LayoutParams lpSv = new FrameLayout.LayoutParams(ViewUtil.dp2px(context, 100f), ViewGroup.LayoutParams.WRAP_CONTENT);
                int px = ViewUtil.dp2px(context, 5f);
                lpSv.setMargins(px * 3, px, px, px);
                lpSv.gravity = Gravity.END;
                sv.setLayoutParams(lpSv);
                sv.setAspectRatio(1.33f);
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(ViewUtil.dp2px(context, 5f));
                roundingParams.setRoundAsCircle(false);
                sv.getHierarchy().setRoundingParams(roundingParams);
                ivList.add(sv);

                FrameLayout ly = new FrameLayout(context);
                LinearLayout.LayoutParams lpLl = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ly.setLayoutParams(lpLl);
                ly.setForeground(ViewUtil.getSelectableItemBackground(context));
                llContentList.add(ly);

                ly.addView(tv);
                ly.addView(sv);

                lyContent.addView(ly);
            }
        }
    }
}

package com.clam314.rxrank.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.clam314.rxrank.R;
import com.clam314.rxrank.View.CircleProgressDrawable;
import com.clam314.rxrank.entity.CategoryGroup;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.http.Category;
import com.clam314.rxrank.util.DeBugLog;
import com.clam314.rxrank.util.FrescoUtil;
import com.clam314.rxrank.util.StringUtil;
import com.clam314.rxrank.util.ViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by clam314 on 2017/4/2
 */

public class DayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = DayAdapter.class.getSimpleName();
    private List<CategoryGroup> groups;

    public DayAdapter(List<CategoryGroup> groups) {
        this.groups = groups;
    }

    @Override
    public int getItemCount() {
        return groups == null ? 0 : groups.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_every_day,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof Holder){
            setData((Holder) holder,groups.get(position));
        }
    }

    private void setData(final Holder holder, final CategoryGroup group){
        if(group == null) return;
        holder.tvDay.setText(StringUtil.getStringAfterPosition(group.getDay(),5));
        if(group.getWelfare() != null
                && group.getWelfare().size() != 0
                && !TextUtils.isEmpty(group.getWelfare().get(0).getUrl())){
            FrescoUtil.loadImage(Uri.parse(group.getWelfare().get(0).getUrl()),holder.ivWelfare,null,0,0,null);
        }else {
            holder.ivWelfare.setVisibility(View.GONE);
        }
        Observable.fromArray(Category.getCategoryListBeside(Category.welfare))
                .map(new Function<String, Object[]>() {
                    @Override
                    public Object[] apply(@NonNull String s) throws Exception {
                        Object[] objects = new Object[]{s,null};
                        switch (s){
                            case Category.android: objects[1]= group.getAndroid(); break;
                            case Category.iOS: objects[1]= group.getiOS(); break;
                            case Category.frontEnd: objects[1]= group.getFrontEnd(); break;
                            case Category.app: objects[1]= group.getApp(); break;
                            case Category.recommend: objects[1]= group.getRecommend(); break;
                            case Category.resource: objects[1]= group.getResource(); break;
                            case Category.video: objects[1]= group.getVideo(); break;
                        }
                        return objects;
                    }
                })
                .subscribe(new Consumer<Object[]>() {
                    @Override
                    public void accept(@NonNull Object[] objects) throws Exception {
                        String category = (String) objects[0];
                        List<Item> items = (List<Item>)objects[1];
                        setCategoryTitle(items,category,holder.mapCategoryTvTitle.get(category));
                        setCategoryData(items,holder.mapCategoryTvContent.get(category),holder.mapCategoryIv.get(category));
                    }
        });
    }

    private void setCategoryTitle(List<Item> items,String category,TextView tv){
        if(items != null && items.size() > 0){
            tv.setVisibility(View.VISIBLE);
            tv.setText(category);
        }else {
            tv.setVisibility(View.GONE);
        }
    }

    private void setCategoryData(List<Item> items,List<TextView> tvContents, List<SimpleDraweeView> ivContents){
        for(int i = 0 ;i < Holder.eachCategoryMaxItem; i++){
            TextView tv = tvContents.get(i);
            final SimpleDraweeView iv = ivContents.get(i);
            if(items != null && items.size() > i){
                final Item item = items.get(i);
                tv.setVisibility(View.VISIBLE);
                tv.setText(String.format(Locale.CANADA,"%s\n(%s)", StringUtil.getShowStringNotNull(item.getDesc()),StringUtil.getShowStringNotNull(item.getWho())));
                ViewUtil.setClickListenerOpenItem(tv,item);
                if(item.getImages() == null
                        || item.getImages().size() == 0
                        || TextUtils.isEmpty(item.getImages().get(0))){
                    iv.setVisibility(View.GONE);
                }else {
                    iv.setVisibility(View.VISIBLE);
                    FrescoUtil.setProgressBar(iv, CircleProgressDrawable.newDefaultInstance(iv.getContext()));
                    iv.post(new Runnable() {
                        @Override
                        public void run() {
                            DeBugLog.logInfo("imageViewSize","imageViewSize ——with:"+iv.getMeasuredWidth()+" ——height:"+iv.getMeasuredHeight());
                            int ivWidth = iv.getMeasuredWidth();
                            int ivHeight = iv.getMeasuredHeight();
                            Uri uri = Uri.parse(item.getImages().get(0)+String.format(Locale.CHINA,"?imageView2/1/w/%d/h/%d",ivWidth,ivHeight));
                            FrescoUtil.loadImage(uri,iv,null,0,0,null);
                        }
                    });
                }
            }else {
                tv.setVisibility(View.GONE);
                iv.setVisibility(View.GONE);
            }
        }
    }


    private static class Holder extends RecyclerView.ViewHolder{
        private static final int eachCategoryMaxItem = 5;
        SimpleDraweeView ivWelfare;
        TextView tvDay;
        LinearLayout lyContent;
        Map<String,LinearLayout> mapCategoryLy;
        Map<String,TextView> mapCategoryTvTitle;
        Map<String,List<TextView>> mapCategoryTvContent;
        Map<String,List<SimpleDraweeView>> mapCategoryIv;

        Holder(View itemView) {
            super(itemView);
            ivWelfare = (SimpleDraweeView)itemView.findViewById(R.id.iv_welfare);
            tvDay = (TextView)itemView.findViewById(R.id.tv_day);
            lyContent = (LinearLayout)itemView.findViewById(R.id.ly_category_content);
            Context context = itemView.getContext();
            String[] categories = Category.getCategoryListBeside(Category.all,Category.welfare);
            mapCategoryLy = createCategoryLinearLayoutMap(context, categories);
            mapCategoryTvTitle = createCategoryTitleTextViewMap(context, categories);
            mapCategoryIv = createCategorySimpleDraweeViewMap(context, categories);
            mapCategoryTvContent = createCategoryContentTextViewMap(context, categories);

            for(String category : categories){
                LinearLayout content = mapCategoryLy.get(category);
                TextView title = mapCategoryTvTitle.get(category);
                List<TextView> tvContentList = mapCategoryTvContent.get(category);
                List<SimpleDraweeView> ivContent = mapCategoryIv.get(category);
                content.addView(title);
                for(int i = 0; i < eachCategoryMaxItem; i++){
                    content.addView(tvContentList.get(i));
                    content.addView(ivContent.get(i));
                }
                lyContent.addView(content);
            }
        }

        private static Map<String,LinearLayout> createCategoryLinearLayoutMap(Context context,String[] categories){
            Map<String,LinearLayout> map = new ArrayMap<>();
            for(String c : categories){
                LinearLayout ly = new LinearLayout(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ly.setLayoutParams(lp);
                ly.setOrientation(LinearLayout.VERTICAL);
                map.put(c,ly);
            }
            return map;
        }

        private static Map<String,TextView> createCategoryTitleTextViewMap(Context context,String[] categories){
            Map<String,TextView> map = new ArrayMap<>();
            for(String c : categories){
                TextView tv = new TextView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,ViewUtil.dp2px(context,10f),0,0);
                tv.setLayoutParams(lp);
                tv.setText("Title");
                tv.setTextSize(22f);
                tv.getPaint().setFakeBoldText(true);
                int color = context.getResources().getColor(R.color.item_category_title_color);
                tv.setTextColor(color);
                Drawable drawable = context.getResources().getDrawable(R.drawable.vector_icon_label);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv.setCompoundDrawables(drawable,null,null,null);
                tv.setCompoundDrawablePadding(ViewUtil.dp2px(context,5f));
                map.put(c,tv);
            }
            return map;
        }

        private static Map<String,List<TextView>> createCategoryContentTextViewMap(Context context,String[] categories){
            Map<String,List<TextView>> map = new ArrayMap<>();
            for(String c : categories){
                List<TextView> list = new ArrayList<>();
                for(int i = 0; i < eachCategoryMaxItem; i++){
                    TextView tv = new TextView(context);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(ViewUtil.dp2px(context,8f),ViewUtil.dp2px(context,2f),0,ViewUtil.dp2px(context,2f));
                    tv.setLayoutParams(lp);
                    tv.setText("Content");
                    tv.setTextSize(16f);
                    tv.setTextColor(Color.GRAY);
                    Drawable drawable = context.getResources().getDrawable(R.drawable.shape_little_circle);
                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                    tv.setCompoundDrawables(drawable,null,null,null);
                    tv.setCompoundDrawablePadding(ViewUtil.dp2px(context,3f));
                    list.add(tv);
                }
                map.put(c,list);
            }
            return map;
        }

        private static Map<String,List<SimpleDraweeView>> createCategorySimpleDraweeViewMap(Context context,String[] categories){
            Map<String,List<SimpleDraweeView>> map = new ArrayMap<>();
            for(String c : categories){
                List<SimpleDraweeView> list = new ArrayList<>();
                for(int i = 0; i < eachCategoryMaxItem; i++){
                    SimpleDraweeView sv = new SimpleDraweeView(context);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewUtil.dp2px(context,200f), ViewGroup.LayoutParams.WRAP_CONTENT);
                    int px = ViewUtil.dp2px(context,5f);
                    lp.setMargins(px,px,px,px);
                    sv.setLayoutParams(lp);
                    sv.setAspectRatio(1.33f);
                    list.add(sv);
                }
                map.put(c,list);
            }
            return map;
        }
    }
}

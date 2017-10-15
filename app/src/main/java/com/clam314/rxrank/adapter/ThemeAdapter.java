package com.clam314.rxrank.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clam314.rxrank.R;
import com.clam314.rxrank.View.CircleProgressDrawable;
import com.clam314.rxrank.entity.zhihu.Theme;
import com.clam314.rxrank.util.DeBugLog;
import com.clam314.rxrank.util.FrescoUtil;
import com.clam314.rxrank.util.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clam314 on 2017/10/15
 */

public class ThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Theme> themeList;

    public ThemeAdapter(List<Theme> list) {
        themeList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_theme,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setData((Holder) holder,themeList.get(position));
    }

    private void setData(final Holder holder, Theme theme){
        if(theme != null){
            holder.tvTheme.setText(StringUtil.getShowStringNotNull(theme.getName()));
            holder.tvDescribe.setTextColor(Color.GRAY);
            holder.tvDescribe.setText(StringUtil.getShowStringNotNull(theme.getDescription()));
            holder.ivCircle.setColorFilter(theme.getColor());
            FrescoUtil.setProgressBar(holder.svImage, CircleProgressDrawable.newDefaultInstance(holder.svImage.getContext()));
            Uri uri = Uri.parse(theme.getThumbnail());
            FrescoUtil.loadImage(uri,holder.svImage,null,0,0,null);
        }
    }

    @Override
    public int getItemCount() {
        return themeList == null ? 0 : themeList.size();
    }

    static class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_circle) ImageView ivCircle;
        @BindView(R.id.tv_theme) TextView tvTheme;
        @BindView(R.id.tv_describe) TextView tvDescribe;
        @BindView(R.id.iv_image) SimpleDraweeView svImage;
        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

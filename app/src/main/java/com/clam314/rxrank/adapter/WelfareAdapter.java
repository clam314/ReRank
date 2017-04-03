package com.clam314.rxrank.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clam314.rxrank.R;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.fragment.BaseFragment;
import com.clam314.rxrank.util.FrescoUtil;
import com.clam314.rxrank.util.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clam314 on 2017/4/3
 */

public class WelfareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> items;
    private BaseFragment baseFragment;

    public WelfareAdapter(List<Item> items, BaseFragment fragment) {
        this.items = items;
        baseFragment = fragment;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_welfare,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ImageCardHolder){
           setData((ImageCardHolder)holder,items.get(position));
        }
    }

    private void setData(ImageCardHolder holder, Item item){
        if(item != null && !TextUtils.isEmpty(item.getUrl())){
            FrescoUtil.loadImage(Uri.parse(item.getUrl()),holder.draweeView,null,0,0,null);
        }
        holder.draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.tvDate.setText(TextUtils.isEmpty(item.getPublishedAt())?" ":item.getPublishedAt().substring(5,10));
    }

    static class ImageCardHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.sv_welfare) SimpleDraweeView draweeView;
        @BindView(R.id.tv_date) TextView tvDate;

        ImageCardHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

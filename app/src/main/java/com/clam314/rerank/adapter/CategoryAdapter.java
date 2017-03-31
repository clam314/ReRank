package com.clam314.rerank.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clam314.rerank.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clam314 on 2017/3/31
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private static class ItemHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_describe) TextView tvDescribe;
        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.tv_time) TextView tvTime;
        @BindView(R.id.tv_avatar) TextView tvAvatar;
        @BindView(R.id.tv_share) TextView tvShare;
        @BindView(R.id.iv_image) SimpleDraweeView draweeView;

        ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }
}

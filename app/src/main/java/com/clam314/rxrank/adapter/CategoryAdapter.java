package com.clam314.rxrank.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clam314.rxrank.R;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.util.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clam314 on 2017/3/31
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> itemList;

    public CategoryAdapter(List<Item> items) {
        this.itemList = items;
    }

    public void setItemList(List<Item> itemList){
        this.itemList = itemList;
    }

    public void addItemList(List<Item> more){
        if(more != null && more.size() > 0 && itemList != null){
            itemList.addAll(more);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(),R.layout.item_list_category,null);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = itemList.get(position);
        if(item == null) return;
        if(holder instanceof  ItemHolder){
            setData((ItemHolder)holder,item);
        }
    }

    private void setData(ItemHolder holder,final Item item){
        holder.tvAvatar.setText(StringUtil.getCharFromString(item.getWho(),0));
        holder.tvName.setText(StringUtil.getShowStringNotNull(item.getWho()));
        holder.tvDescribe.setText(StringUtil.getShowStringNotNull(item.getDesc()));
        holder.tvTime.setText(StringUtil.getShowStringNotNull(item.getPublishedAt()));
        if(item.getImages() != null && item.getImages().size() > 0){
            String url = item.getImages().get(0);
            holder.draweeView.setVisibility(View.VISIBLE);
            holder.draweeView.setImageURI(url);
        }else {
            holder.draweeView.setVisibility(View.GONE);
        }
        holder.tvDescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    static class ItemHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_describe) TextView tvDescribe;
        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.tv_time) TextView tvTime;
        @BindView(R.id.tv_avatar) TextView tvAvatar;
        @BindView(R.id.tv_share) TextView tvShare;
        @BindView(R.id.iv_image) SimpleDraweeView draweeView;

        ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

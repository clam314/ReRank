package com.clam314.rxrank.adapter;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clam314.rxrank.R;
import com.clam314.rxrank.View.CircleProgressDrawable;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.util.FrescoUtil;
import com.clam314.rxrank.util.StringUtil;
import com.clam314.rxrank.util.ViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Locale;

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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_category,parent,false);
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

    private void setData(final ItemHolder holder,final Item item){
        holder.tvAvatar.setText(StringUtil.getCharFromString(item.getWho(),0));
        holder.tvName.setText("· "+StringUtil.getShowStringNotNull(item.getWho()));
        holder.tvDescribe.setText(StringUtil.getShowStringNotNull(item.getDesc()));
        holder.tvTime.setText(StringUtil.getStringBeforePosition(item.getPublishedAt(),10));
        if(item.getImages() != null && item.getImages().size() > 0){
            final String url = item.getImages().get(0);
            holder.draweeView.setVisibility(View.VISIBLE);
            FrescoUtil.setProgressBar(holder.draweeView, CircleProgressDrawable.newDefaultInstance(holder.draweeView.getContext()));
            holder.draweeView.post(new Runnable() {
                @Override
                public void run() {
                    int ivWidth = holder.draweeView.getMeasuredWidth();
                    int ivHeight = holder.draweeView.getMeasuredHeight();
                    Uri uri = Uri.parse(url+String.format(Locale.CHINA,"?imageView2/1/w/%d/h/%d",ivWidth,ivHeight));
                    FrescoUtil.loadImage(uri, holder.draweeView, null, 0, 0, null);
                }
            });

        }else {
            holder.draweeView.setVisibility(View.GONE);
        }
        ViewUtil.setClickListenerOpenItem(holder.cardView,item);

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
        @BindView(R.id.card_category) CardView cardView;

        ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

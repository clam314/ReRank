package com.clam314.rxrank.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clam314.rxrank.util.DeBugLog;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by clam314 on 2017/3/31.
 */

public class LoadMoreWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_LOAD_FAILED_VIEW = Integer.MAX_VALUE - 1;
    public static final int ITEM_TYPE_NO_MORE_VIEW = Integer.MAX_VALUE - 2;
    public static final int ITEM_TYPE_LOAD_MORE_VIEW = Integer.MAX_VALUE - 3;
    public static final int ITEM_TYPE_NO_VIEW = Integer.MAX_VALUE - 4;//不展示footer view

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;

    private RecyclerView.ViewHolder mLoadMoreHolder,mLoadFailedHolder,mLoadNoMoreHolder;

    private int mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
    private LoadMoreScrollListener mLoadMoreScrollListener;


    private boolean isLoadError = false;//标记是否加载出错
    private boolean isHaveStatesView = true;
    private boolean isLoadComplete = false;
    private boolean isLoading = true;

    public LoadMoreWrapperAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        this.mInnerAdapter = adapter;
        mLoadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void loadMore() {
                DeBugLog.logDebug("load"," LoadMoreWrapperAdapter loadMore()"+" -isHaveStatesView: "+isHaveStatesView);
                if (mOnLoadListener != null) {
                    if (!isLoadError && !isLoadComplete && !isLoading) {
                        DeBugLog.logDebug("load"," LoadMoreWrapperAdapter start loadMore()");
                        showLoadMore();
                        mOnLoadListener.onLoadMore();
                    }
                }
            }
        };
    }

    public void showLoadMore() {
        mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
        isLoadError = false;
        isHaveStatesView = true;
        isLoading = true;
        notifyItemChanged(getItemCount());
    }

    public void showLoadError() {
        mCurrentItemType = ITEM_TYPE_LOAD_FAILED_VIEW;
        isLoadError = true;
        isHaveStatesView = true;
        isLoading = false;
        notifyItemChanged(getItemCount());
    }

    public void showLoadComplete() {
        mCurrentItemType = ITEM_TYPE_NO_MORE_VIEW;
        isLoadError = false;
        isHaveStatesView = true;
        isLoadComplete = true;
        isLoading = false;
        notifyItemChanged(getItemCount());
    }

    public void disableLoadMore() {
        mCurrentItemType = ITEM_TYPE_NO_VIEW;
        isHaveStatesView = false;
        isLoading = false;
        notifyDataSetChanged();
    }

    public void setLoadStatusViewHolder(RecyclerView.ViewHolder loadMore, RecyclerView.ViewHolder loadFailed, RecyclerView.ViewHolder loadNoMore){
        if(loadMore != null) mLoadMoreHolder = loadMore;
        if(loadFailed != null) mLoadFailedHolder = loadFailed;
        if(loadNoMore != null) mLoadNoMoreHolder = loadNoMore;
    }

    private RecyclerView.ViewHolder getLoadMoreViewHolder(Context context) {
        if(mLoadMoreHolder == null){
            mLoadMoreHolder = LoadStatusViewHolder.getDefaultHolder(context,"正在加载中");
        }
        return mLoadMoreHolder;
    }

    private RecyclerView.ViewHolder getLoadFailedViewHolder(Context context) {
        if(mLoadFailedHolder == null){
            mLoadFailedHolder = LoadStatusViewHolder.getDefaultHolder(context,"加载出错,点击重试");
        }
        return mLoadFailedHolder;
    }

    private RecyclerView.ViewHolder getNoMoreViewHolder(Context context) {
        if(mLoadNoMoreHolder == null){
            mLoadMoreHolder = LoadStatusViewHolder.getDefaultHolder(context,"——end——");
        }
        return mLoadNoMoreHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && isHaveStatesView) {
            return mCurrentItemType;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_NO_MORE_VIEW) {
            return getNoMoreViewHolder(parent.getContext());
        } else if (viewType == ITEM_TYPE_LOAD_MORE_VIEW) {
            return getLoadMoreViewHolder(parent.getContext());
        } else if (viewType == ITEM_TYPE_LOAD_FAILED_VIEW) {
            return getLoadFailedViewHolder(parent.getContext());
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_TYPE_LOAD_FAILED_VIEW
                && holder instanceof LoadStatusViewHolder) {
            ((LoadStatusViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnLoadListener != null) {
                        mOnLoadListener.onRetry();
                        showLoadMore();
                    }
                }
            });
            return;
        }
        if (!isFooterType(holder.getItemViewType())){
            mInnerAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(position == getItemCount() - 1 && isHaveStatesView){
                        return gridLayoutManager.getSpanCount();
                    }
                    if(gridLayoutManager.getSpanSizeLookup() != null && isHaveStatesView){
                        return gridLayoutManager.getSpanSizeLookup().getSpanSize(position);
                    }
                    return 1;
                }
            });
        }

//        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
//            @Override
//            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
//                if (position == getItemCount() - 1 && isHaveStatesView) {
//                    return layoutManager.getSpanCount();
//                }
//                if (oldLookup != null && isHaveStatesView) {
//                    return oldLookup.getSpanSize(position);
//                }
//                return 1;
//            }
//        });
        recyclerView.addOnScrollListener(mLoadMoreScrollListener);
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (holder.getLayoutPosition() == getItemCount() - 1 && isHaveStatesView) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (isHaveStatesView ? 1 : 0);
    }

    public boolean isFooterType(int type) {
        return type == ITEM_TYPE_NO_VIEW
                || type == ITEM_TYPE_LOAD_FAILED_VIEW
                || type == ITEM_TYPE_NO_MORE_VIEW
                || type == ITEM_TYPE_LOAD_MORE_VIEW;
    }


    public interface OnLoadListener {
        void onRetry();

        void onLoadMore();
    }

    private OnLoadListener mOnLoadListener;

    public LoadMoreWrapperAdapter setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
        return this;
    }


    /*
    *加载状态的ViewHolder的父类,需要自定义加载状态的viewHolder请继承该类
    *
    **/
    public static class LoadStatusViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        public LoadStatusViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public static RecyclerView.ViewHolder getDefaultHolder(Context context,String text){
            TextView view = new TextView(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            view.setPadding(20, 20, 20, 20);
            view.setText(text);
            view.setGravity(Gravity.CENTER);
            return new LoadStatusViewHolder(view);
        }
    }
}

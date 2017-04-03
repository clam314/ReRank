package com.clam314.rxrank.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.clam314.rxrank.R;
import com.clam314.rxrank.entity.Item;
import com.clam314.rxrank.fragment.BaseFragment;
import com.clam314.rxrank.util.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clam314 on 2017/4/3
 */

public class WelfareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> items;
    private SimpleDraweeView ivAnimation;
    private View rootView;
    private Animator mCurrentAnimator;

    public WelfareAdapter(List<Item> items, SimpleDraweeView sv, View rootView) {
        this.items = items;
        this.rootView = rootView;
        ivAnimation = sv;
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

    private void setData(final ImageCardHolder holder, final Item item){
        Uri imageUri = null;
        if(item != null && !TextUtils.isEmpty(item.getUrl())){
            imageUri = Uri.parse(item.getUrl());
            FrescoUtil.loadImage(imageUri,holder.draweeView,null,0,0,null);
        }

        if(imageUri == null){
            holder.draweeView.setOnClickListener(null);
        }else {
            holder.draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zoomImageFromThumb(holder.draweeView,Uri.parse(item.getUrl()));
                }
            });
        }
        holder.tvDate.setText(item == null || TextUtils.isEmpty(item.getPublishedAt())?" ":item.getPublishedAt().substring(5,10));
    }

    private void zoomImageFromThumb(final View thumbView, final Uri uri) {
        // 若已经有animation在进行中，立即删除该animation，然后推进新的animation
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // 加载高分辨率的放大图片

        // ivAnimation.setImageResource(R.drawable.page1);
//        ivAnimation.setImageURI(uri);
        FrescoUtil.loadImage(uri,ivAnimation,null,0,0,null);
        if (ivAnimation ==null) Log.i("animator","ivAnimation==null");
        // 计算放大图片动画的开始边界和终止边界。该步骤设计较多的数学计算
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // 开始边界即为缩略图的全局可视正方形，终止边界即为container view的全局可视正方形。
        // The start bounds are the global visible rectangle of the thumbnail,
        // 同时设置container viewr的偏移量为边界的起点，因为这是positioning animation 参数（X,Y)的起点。
        thumbView.getGlobalVisibleRect(startBounds);

        rootView.getGlobalVisibleRect(finalBounds,globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y );
        finalBounds.offset(-globalOffset.x, -globalOffset.y );

        // 用”center crop”技术调整开始边界与终止边界的宽高比一样。这样可以防止动画执行期间的不佳的拉伸。
        // 同时计算开始的缩放比例因子（终止的比例因子总是1.0）
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // 水平方向扩展开始边界
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // 垂直方向扩展开始边界
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // 隐藏缩略图，显示放大view。当动画开始时，将把放大view安放在缩略图的位置。

        ivAnimation.setVisibility(View.VISIBLE);


        // 设置轴心点，为了SCALE_X和SCALE_Y转换到放大view的左上角（默认轴心点是view的中心）
        ivAnimation.setPivotX(0f);
        ivAnimation.setPivotY(0f);

        // 构建并运行平行的四个转换动画，即为依次对 (X, Y, SCALE_X, and SCALE_Y)四个属性执行转换.set中存放四个animator。
        AnimatorSet set = new AnimatorSet();
        AnimatorSet set1 = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(ivAnimation, View.ALPHA, 0.5f, 1f);
        animator.setStartDelay(1);
        animator.setDuration(100);
        set1.play(ObjectAnimator.ofFloat(ivAnimation, View.X,         //View的X属性从startBounds.left变到finalBounds的left
                startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(ivAnimation, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(ivAnimation, View.SCALE_X ,   //View的SCALE_X和SCALE_Y（X轴和Y轴的放大倍数，0.0代表放大为0，1.0代表正常）
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(ivAnimation, View.SCALE_Y,
                        startScale, 1f));

        set1.setDuration(500);   //设置动画持续时间
        set1.setInterpolator(new AccelerateDecelerateInterpolator());//设置动画播放为减速器播放
        set.play(set1).with(animator);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();//开始执行预定的动画
        mCurrentAnimator = set; //做好缓存

        // 逆向的操作，从大图到小图，先设定AnimatorSet，然后按设定执行AnimatorSet，然后隐藏大图，显示小图，不详细讲了。
        //
        final float startScaleFinal = startScale;
        ivAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                ObjectAnimator animator = ObjectAnimator.ofFloat(ivAnimation, View.ALPHA,0.8f, 0f);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.setDuration(50);
                AnimatorSet set = new AnimatorSet();
                AnimatorSet set1 = new AnimatorSet();
                set1.play(ObjectAnimator
                        .ofFloat(ivAnimation, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(ivAnimation,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(ivAnimation,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(ivAnimation,
                                        View.SCALE_Y, startScaleFinal));
                set1.setDuration(500);
                set1.setInterpolator(new AccelerateDecelerateInterpolator());
                set.play(set1).before(animator);
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //  thumbView.setAlpha(1f);
                        ivAnimation.setVisibility(View.INVISIBLE);
                        mCurrentAnimator = null;
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        ivAnimation.setVisibility(View.INVISIBLE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
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

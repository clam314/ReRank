package com.clam314.rxrank.util;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by clam314 on 2017/4/1
 */

public class FrescoUtil {
    public static void loadImage(Uri uri, SimpleDraweeView draweeView, BasePostprocessor postprocessor,
                                 int width, int height, BaseControllerListener<ImageInfo> listener){
        ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)//开启渐进式加载网络jpg
                .setAutoRotateEnabled(true);//根据图片里保存的反向显示
        if(postprocessor != null)requestBuilder.setPostprocessor(postprocessor);
        if(width!=0 && height!=0)requestBuilder.setResizeOptions(new ResizeOptions(width,height));

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(requestBuilder.build())
                .setControllerListener(listener)
                .setOldController(draweeView.getController())
                .setAutoPlayAnimations(true)//开启gif的动画播放
                .build();
        draweeView.setController(controller);
    }

    public static void setProgressBar(SimpleDraweeView draweeView, Drawable drawable){
        if(draweeView == null || drawable == null) return;
        draweeView.getHierarchy().setProgressBarImage(drawable);
    }
}

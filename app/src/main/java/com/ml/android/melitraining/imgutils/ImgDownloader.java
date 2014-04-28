package com.ml.android.melitraining.imgutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by marbarfa on 4/26/14.
 */
public class ImgDownloader {

    private BitmapCache bitmapCache;

    public ImgDownloader(BitmapCache bitmapCache){
        this.bitmapCache = bitmapCache;
    }


    public void loadBitmap(Context context, String imageUrl,
                           Bitmap mPlaceHolderBitmap,
                           ImageView imageView,
                           int width, int height){

        if (bitmapCache != null && bitmapCache.getBitmapFromMemCache(imageUrl) != null) {
            imageView.setImageBitmap(bitmapCache.getBitmapFromMemCache(imageUrl));
        } else {
            if (cancelPotentialWork(imageUrl, imageView)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView, context, bitmapCache, width, height);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(context.getResources(), mPlaceHolderBitmap, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(imageUrl);
            }
        }
    }

    public boolean cancelPotentialWork(String imgUrl, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.data;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == null || bitmapData != imgUrl) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }


}

package com.ml.android.melitraining.imgutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by marbarfa on 4/26/14.
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private Context context;
    private BitmapCache bitmapCache;
    String data = null;
    int width;
    int height;

    public BitmapWorkerTask(ImageView imageView, Context context, BitmapCache bitmapCache, int width, int height) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.context = context;
        this.bitmapCache = bitmapCache;
        this.width = width;
        this.height = height;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
        data = params[0];
        // LOAD DATA FROM NET.
        final Bitmap bitmap = ImgUtils.decodeSampledBitmapFromNetwork(context.getResources(), data, this.width,this.height);

        if (bitmapCache!=null){
            bitmapCache.addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
        }

        return bitmap;
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()){
            bitmap = null;
        }
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}

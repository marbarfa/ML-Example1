package com.ml.android.melitraining.app;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ml.android.melitraining.dto.ItemDTO;
import com.ml.android.melitraining.imgutils.BitmapCache;
import com.ml.android.melitraining.imgutils.ImgDownloader;
import com.ml.android.melitraining.imgutils.ImgUtils;
import com.ml.android.melitraining.net.MeliItemsApi;

public class ItemVIPActivity extends android.app.Activity {

    private static ItemDTO itemDTO;
    private ImgDownloader imgDownloader;
    private BitmapCache bitmapCache;
    private Bitmap mPlaceholderBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_vip);
        bitmapCache = new BitmapCache();
        imgDownloader = new ImgDownloader(bitmapCache);

        //retrive search string
        if (getIntent() != null && getIntent().getExtras() != null) {

            Bundle extras = getIntent().getExtras();
            String itemId = extras.getString("item_id");
            loadItem(itemId);

        } else if (itemDTO != null) {
            loadItemToUI();
        }
//        }else if (listAdapter == null){
//            listAdapter = new SearchAdapter(getApplicationContext());
//        }
    }


    private void loadItem(final String itemId){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setMessage("Loading item...");
        dialog.show();
        new android.os.AsyncTask<Void, Void, ItemDTO>() {

            @Override
            protected ItemDTO doInBackground(Void... voids) {
                if (mPlaceholderBitmap!=null){
                    mPlaceholderBitmap = ImgUtils
                            .decodeSampledBitmapFromResource(ItemVIPActivity.this.getResources(), R.drawable.placeholder,
                                    100, 100);
                }
                ItemDTO itemDTO = MeliItemsApi.getItem(itemId);
                return itemDTO;
            }

            @Override
            protected void onPostExecute(ItemDTO o) {
                super.onPostExecute(o);
                if (o != null) {
                   itemDTO = o;
                    loadItemToUI();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemVIPActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ItemVIPActivity.this.finish();
                        }
                    });
                    builder.setMessage("Error al cargar el item");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                //UPDATE UI
            }


        }.execute();
    }


    private void loadItemToUI(){

        ImageView img = (ImageView) findViewById(R.id.item_image);
        TextView title = (TextView)findViewById(R.id.item_title);
        TextView price = (TextView)findViewById(R.id.item_price);
        TextView buy = (TextView)findViewById(R.id.item_buy);
        TextView status = (TextView)findViewById(R.id.item_status);
        TextView soldAmount = (TextView)findViewById(R.id.item_soldAmount);
        TextView location = (TextView)findViewById(R.id.item_location);

        price.setText(itemDTO.price.toString());
        buy.setText(itemDTO.quantity);
        status.setText(itemDTO.status);
        soldAmount.setText(itemDTO.soldQuantity);
        location.setText(itemDTO.condition);
        title.setText(itemDTO.title);

        if (itemDTO!=null && itemDTO.pictures!=null && itemDTO.pictures.size() > 0){
            imgDownloader.
                    loadBitmap(getApplicationContext(),
                            itemDTO.pictures.get(0).url, mPlaceholderBitmap, img, 500, 500);
        }

        findViewById(R.id.main_item_container).setVisibility(View.VISIBLE);

    }




}

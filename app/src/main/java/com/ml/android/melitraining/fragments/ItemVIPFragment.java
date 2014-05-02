package com.ml.android.melitraining.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ml.android.melitraining.app.R;
import com.ml.android.melitraining.dto.ItemDTO;
import com.ml.android.melitraining.imgutils.BitmapCache;
import com.ml.android.melitraining.imgutils.ImgDownloader;
import com.ml.android.melitraining.imgutils.ImgUtils;
import com.ml.android.melitraining.net.MeliItemsApi;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class ItemVIPFragment extends Fragment {

    private static ItemDTO itemDTO;
    private ImgDownloader imgDownloader;
    private BitmapCache bitmapCache;
    private Bitmap mPlaceholderBitmap;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemVIPFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_vip_fragment, container, false);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmapCache = new BitmapCache();
        imgDownloader = new ImgDownloader(bitmapCache);

    }


    private void loadItem(final String itemId){
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(true);
        dialog.setMessage("Loading item...");
        dialog.show();
        new android.os.AsyncTask<Void, Void, ItemDTO>() {

            @Override
            protected ItemDTO doInBackground(Void... voids) {
                if (mPlaceholderBitmap!=null){
                    mPlaceholderBitmap = ImgUtils
                            .decodeSampledBitmapFromResource(getResources(), R.drawable.placeholder,
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().finish();
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

        ImageView img = (ImageView) getView().findViewById(R.id.item_image);
        TextView title = (TextView)getView().findViewById(R.id.item_title);
        TextView price = (TextView)getView().findViewById(R.id.item_price);
        TextView buy = (TextView)getView().findViewById(R.id.item_buy);
        TextView status = (TextView)getView().findViewById(R.id.item_status);
        TextView soldAmount = (TextView)getView().findViewById(R.id.item_soldAmount);
        TextView location = (TextView)getView().findViewById(R.id.item_location);

        price.setText(itemDTO.price.toString());
        buy.setText(itemDTO.quantity);
        status.setText(itemDTO.status);
        soldAmount.setText(itemDTO.soldQuantity);
        location.setText(itemDTO.condition);
        title.setText(itemDTO.title);

        if (itemDTO!=null && itemDTO.pictures!=null && itemDTO.pictures.size() > 0){
            imgDownloader.
                    loadBitmap(getActivity(),
                            itemDTO.pictures.get(0).url, mPlaceholderBitmap, img, 500, 500);
        }

//        getView().findViewById(R.id.main_item_container).setVisibility(View.VISIBLE);

    }





}

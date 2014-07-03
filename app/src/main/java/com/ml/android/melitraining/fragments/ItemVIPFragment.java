package com.ml.android.melitraining.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.ml.android.melitraining.net.robospice.ISpiceMgr;
import com.ml.android.melitraining.net.robospice.MeliAPIRequests;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

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

    private SpiceManager spiceManager;

    private ImageView img;
    private TextView title;
    private TextView price;
    private TextView buy;
    private TextView status;
    private TextView soldAmount;
    private TextView location;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemVIPFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_vip_fragment, container, true);

        img = (ImageView) view.findViewById(R.id.item_image);
        title = (TextView) view.findViewById(R.id.item_title);
        price = (TextView) view.findViewById(R.id.item_price);
        buy = (TextView) view.findViewById(R.id.item_buy);
        status = (TextView) view.findViewById(R.id.item_status);
        soldAmount = (TextView) view.findViewById(R.id.item_soldAmount);
        location = (TextView) view.findViewById(R.id.item_location);

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmapCache = new BitmapCache();
        imgDownloader = new ImgDownloader(bitmapCache);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISpiceMgr) {
            spiceManager = ((ISpiceMgr) activity).getSpiceManager();
        }
    }

    public void loadItem(String itemId) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(true);
        dialog.setMessage("Loading item...");
        dialog.show();

        if (mPlaceholderBitmap != null) {
            mPlaceholderBitmap = ImgUtils
                    .decodeSampledBitmapFromResource(getResources(), R.drawable.placeholder,
                            100, 100);
        }

        MeliAPIRequests.GetItemsSpiceRequest itemsRequest =
                new MeliAPIRequests.GetItemsSpiceRequest(itemId);

        spiceManager.execute(itemsRequest, "items/"+itemId, DurationInMillis.ONE_MINUTE,
                new RequestListener<ItemDTO>() {

                    public void onRequestFailure(SpiceException spiceException) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.e("ITEMVIPFRAGMENT", "Error al consultar WS"+spiceException.getMessage());
                    }

                    public void onRequestSuccess(ItemDTO result) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        itemDTO = result;
                        if (itemDTO != null) {
                            loadItemToUI();

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
                    }
                }
        );

    }


    private void loadItemToUI() {

        price.setText(itemDTO.price.toString());
        buy.setText(itemDTO.quantity);
        status.setText(itemDTO.status);
        soldAmount.setText(itemDTO.sold_quantity);
        location.setText(itemDTO.condition);
        title.setText(itemDTO.title);

        if (itemDTO != null && itemDTO.pictures != null && itemDTO.pictures.size() > 0) {
            Picasso.with(getActivity()).setDebugging(true);
            Picasso
                    .with(getActivity())
                    .load(itemDTO.pictures.get(0).url)
                    .placeholder(R.drawable.placeholder)
                    .resize(500,500)
                    .centerCrop()
                    .into(img);
        }
        getView().invalidate();
//        getView().findViewById(R.id.main_item_container).setVisibility(View.VISIBLE);

    }


}

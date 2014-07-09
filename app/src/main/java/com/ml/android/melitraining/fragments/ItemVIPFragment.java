package com.ml.android.melitraining.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.ml.android.melitraining.common.ICallbackHandler;
import com.ml.android.melitraining.database.dao.BookmarksDAO;
import com.ml.android.melitraining.database.entities.Bookmark;
import com.ml.android.melitraining.dto.ItemDTO;
import com.ml.android.melitraining.imgutils.ImgUtils;
import com.ml.android.melitraining.net.robospice.ISpiceMgr;
import com.ml.android.melitraining.net.robospice.MeliAPIRequests;
import com.octo.android.robospice.SpiceManager;
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
//    private ImgDownloader imgDownloader;
//    private BitmapCache bitmapCache;
    private Bitmap mPlaceholderBitmap;

    private SpiceManager spiceManager;

    private ImageView img;
    private TextView title;
    private TextView price;
    private TextView status;
    private TextView soldAmount;
    private TextView location;
    private View itemBookmark;

    private ICallbackHandler<ItemDTO, Void> onItemBookmark;
    private BookmarksDAO bookmarkDAO;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_vip_fragment, container, false);

        img = (ImageView) view.findViewById(R.id.item_image);
        title = (TextView) view.findViewById(R.id.item_title);
        price = (TextView) view.findViewById(R.id.item_price);
        status = (TextView) view.findViewById(R.id.item_status);
        soldAmount = (TextView) view.findViewById(R.id.item_soldAmount);
        location = (TextView) view.findViewById(R.id.item_location);
        itemBookmark = view.findViewById(R.id.item_bookmark);

        itemBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemBookmark.apply(itemDTO);
                bookmarkItem();
            }
        });

        if (savedInstanceState != null) {
            itemDTO = (ItemDTO) savedInstanceState.getSerializable("itemDTO");
        }

        if (itemDTO != null) {
            loadItemToUI();
        }

        return view;
    }

    private void bookmarkItem() {
        itemBookmark.setSelected(!itemBookmark.isSelected());
        if (itemBookmark.isSelected()) {
            itemBookmark.setBackgroundResource(android.R.drawable.star_big_on);
        } else {
            itemBookmark.setBackgroundResource(android.R.drawable.star_big_off);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("itemDTO", itemDTO);
        super.onSaveInstanceState(outState);
    }

    public void setSpiceManager(SpiceManager spiceManager) {
        this.spiceManager = spiceManager;
    }

    public void setOnItemBookmark(ICallbackHandler<ItemDTO, Void> callbackHandler) {
        this.onItemBookmark = callbackHandler;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISpiceMgr) {
            spiceManager = ((ISpiceMgr) activity).getSpiceManager();
        }
        bookmarkDAO = new BookmarksDAO(activity);
    }

    public void loadItem(String itemId, Context context) {
        final ProgressDialog dialog = new ProgressDialog(context);
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

        spiceManager.execute(itemsRequest, itemId, 1000 * 60 * 2, new RequestListener<ItemDTO>() {

                    public void onRequestFailure(SpiceException spiceException) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.e("ITEMVIPFRAGMENT", "Error al consultar WS" + spiceException.getMessage());
                    }

                    public void onRequestSuccess(ItemDTO result) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        itemDTO = result;
                        if (itemDTO != null && getActivity() !=null) {
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
//                    .fit()
                    .resize(getResources().getDimensionPixelSize(R.dimen.itemdetail_img_width),
                            getResources().getDimensionPixelSize(R.dimen.itemdetail_img_height))
                    .centerCrop()
                    .into(img);
        }

        Bookmark b = bookmarkDAO.getBookmarkByItem(itemDTO.id);
        if (b != null) {
            bookmarkItem();
        }
    }


}

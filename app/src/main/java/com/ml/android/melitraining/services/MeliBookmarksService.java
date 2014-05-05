package com.ml.android.melitraining.services;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import com.ml.android.melitraining.app.ItemVIPActivity;
import com.ml.android.melitraining.dto.ItemDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by marbarfa on 5/4/14.
 */
public class MeliBookmarksService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "item_id";
    private List<ItemDTO> itemsBookmarked = new ArrayList<ItemDTO>();

    public MeliBookmarksService() {
        super("MeliBookmarksService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, this.getClass());
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start every 30 seconds
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), 30 * 1000, pintent);
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent receiveIntent) {
        // if extras == null => check for item changes
        // if extras != null => add new item to item list.
        for(ItemDTO i : itemsBookmarked){
            ItemDTO itemChanged = checkItemChange(i);
            if (itemChanged!=null){
                //item has changed => notify
                notifyItemChange(itemChanged);
            }
        }


    }

    private ItemDTO checkItemChange(ItemDTO item){

        return null;
    }



    private void notifyItemChange(ItemDTO item) {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, ItemVIPActivity.class);
        intent.putExtra("item_id", item.itemId);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(this)
                .setContentTitle("Item modification ")
                .setContentText("Subject") //what changed?
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setContentInfo("Show")
                .getNotification();

        notificationManager.notify(0, n);
    }

}
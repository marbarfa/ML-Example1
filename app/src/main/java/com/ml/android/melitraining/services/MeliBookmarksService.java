package com.ml.android.melitraining.services;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.ml.android.melitraining.app.ItemVIPActivity;
import com.ml.android.melitraining.database.dao.BookmarksDAO;
import com.ml.android.melitraining.database.entities.Bookmark;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.List;

/**
 * Created by marbarfa on 5/4/14.
 */
public class MeliBookmarksService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "item_id";
    private BookmarksDAO bookmarkDAO;

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
        bookmarkDAO = new BookmarksDAO(getApplicationContext());
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent receiveIntent) {
        try{
            List<Bookmark> bookmarks = bookmarkDAO.getAllNotChecked();
            // if extras == null => check for item changes
            // if extras != null => add new item to item list.
            for(Bookmark i : bookmarks){
                DateTime dt = DateTime.now().minusMinutes(5);
                if (i.getItemEndDate().before(dt.toDate())){
                    notifyItemChange(i);
                    i.setChecked(true);
                    bookmarkDAO.getDAO().update(i);
                }
            }

        } catch (Throwable t){
            Log.e("MeliBookmarksService", "Error retriving bookmarks from DB", t);
        }
    }


    private void notifyItemChange(Bookmark bookmark) {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, ItemVIPActivity.class);
        intent.putExtra("item_id", bookmark.getItemId());
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(this)
                .setContentTitle("Item modification ")
                .setContentText("Item to finish...") //what changed?
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setContentInfo("Show")
                .getNotification();

        notificationManager.notify(0, n);
    }

}

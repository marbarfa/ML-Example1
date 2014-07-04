package com.ml.android.melitraining.database.dao;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.ml.android.melitraining.database.entities.Bookmark;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbarreto on 04/07/14.
 */
public class BookmarksDAO extends AbstractDAO<Bookmark, Long> {


    public BookmarksDAO(Context context) {
        super(context);
    }

    public Dao<Bookmark, Long> getDAO() {
        return dh.getBookmarksDao();
    }

    public List<Bookmark> getAllNotChecked() {
        List<Bookmark> bookmarks = new ArrayList<Bookmark>();
        try {
            bookmarks = getDAO().queryForEq("checked", false);
        } catch (SQLException e) {
            Log.e("BookmarksDAO", "Failed to query Bookmarks", e);
        }
        return bookmarks;
    }


    public Bookmark getBookmarkByItem(String itemId) {
        Bookmark bookmark = null;
        try {
            List<Bookmark> bookmarks = getDAO().queryForEq("itemId", itemId);
            if (bookmarks !=null && bookmarks.size() > 1){
                Log.i("BookmarksDAO", "More than 1 bookmark for the same itemId = "+itemId);
            }
            if (bookmarks != null && bookmarks.size() > 0){
                bookmark = bookmarks.get(0);
            }
        } catch (SQLException e) {
            Log.e("BookmarksDAO", "Failed to query Bookmarks", e);
        }
        return bookmark;
    }


    // create an instance of Account
    //String name = "Buzz Lightyear";
    //Account account = new Account(name);

    // persist the account object to the database
    //accountDao.create(account);

    // create an associated Order for the Account
    // Buzz bought 2 of item #21312 for a price of $12.32
    //int quantity1 = 2;
    //int itemNumber1 = 21312;
    //float price1 = 12.32F;
    //Order order1 = new Order(account, itemNumber1, price1, quantity1);
    //orderDao.create(order1);

    // create another Order for the Account
    // Buzz also bought 1 of item #785 for a price of $7.98
    //int quantity2 = 1;
    //int itemNumber2 = 785;
    //float price2 = 7.98F;
    //Order order2 = new Order(account, itemNumber2, price2, quantity2);
    //orderDao.create(order2);

    //Account accountResult = accountDao.queryForId(account.getId());
    //ForeignCollection<Order> orders = accountResult.getOrders();

}

package com.ml.android.melitraining.database.dao;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.ml.android.melitraining.database.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbarreto on 04/07/14.
 */
public abstract class AbstractDAO<T, ID> {
    protected Dao<T, ID> DAO;
    protected DatabaseHelper dh;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract Dao<T, ID> getDAO();

    public AbstractDAO(Context context){
        dh = new DatabaseHelper(context);
        DAO = getDAO();
    }

    public T getById(ID id) {
        try {
            return DAO.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    List<T> queryForAll() {
        try {
            return DAO.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    Integer create(T t) {
        try {
            return DAO.create(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    Integer update(T t){
        try {
            return DAO.update(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    void createOrUpdate(T t) {
        try {
            DAO.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    def getLast() : T = {
//        var last = DAO.queryBuilder().limit(new java.lang.Long(1)).orderBy("updated_at", false).query();
//        if (last.size() > 0)
//            return last.get(0)
//        return null.asInstanceOf[T];
//    }

}

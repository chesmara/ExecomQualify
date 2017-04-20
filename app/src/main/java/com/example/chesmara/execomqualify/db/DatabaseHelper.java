package com.example.chesmara.execomqualify.db;

/**
 * Created by androiddevelopment on 20.3.17..
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.chesmara.execomqualify.db.model.ShopList;
import com.example.chesmara.execomqualify.db.model.Articles;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "shopLists.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<ShopList, Integer> mShopListDao = null;
    private Dao<Articles, Integer> mArticlesDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, ShopList.class);
            TableUtils.createTable(connectionSource,Articles.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ShopList.class, true);
            TableUtils.dropTable(connectionSource, Articles.class,true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<ShopList, Integer> getmShopListDao() throws SQLException {
        if (mShopListDao == null) {
            mShopListDao = getDao(ShopList.class);
        }

        return mShopListDao;
    }

    public Dao<Articles,Integer> getmArticlesDao() throws SQLException {
        if (mArticlesDao == null){
            mArticlesDao=getDao(Articles.class);
        }
        return mArticlesDao;
    }
    @Override
    public void close() {

        mShopListDao = null;
        mArticlesDao=null;

        super.close();
    }



}

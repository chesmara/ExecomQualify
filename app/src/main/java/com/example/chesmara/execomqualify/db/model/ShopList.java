package com.example.chesmara.execomqualify.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable(tableName = ShopList.TABLE_NAME_SHOPLIST)
public class ShopList {

    public static final String TABLE_NAME_SHOPLIST= "shoplist";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_SHOPLIST_NAME = "name";
    public static final String TABLE_SHOPLIST_ARTICLES = "articles";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_SHOPLIST_NAME)
    private String mName;

    @ForeignCollectionField(columnName = ShopList.TABLE_SHOPLIST_ARTICLES, eager = true)
    private ForeignCollection<Articles> articles;

    public ShopList(){}

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public ForeignCollection<Articles> getArticles() {
        return articles;
    }

    public void setArticles(ForeignCollection<Articles> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return mName ;
    }
}

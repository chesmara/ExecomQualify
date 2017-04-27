package com.example.chesmara.execomqualify.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Chesmara on 4/20/2017.
 */

@DatabaseTable(tableName = Articles.TABLE_NAME_ARTICLES)
public class Articles {


    public static final String TABLE_NAME_ARTICLES ="articles";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_ARTICLE_NAME = "articlename";
    public static final String FIELD_ARTICLE_AMOUNT = "amount";
    public static final String FIELD_NAME_CHECKED = "checked";
    public static final String FIELD_NAME_SHOPLIST= "shoplist";



    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int aId;

    @DatabaseField(columnName = FIELD_ARTICLE_NAME)
    private String aName;

    @DatabaseField(columnName = FIELD_ARTICLE_AMOUNT)
    private String aAmount;

   @DatabaseField(columnName = FIELD_NAME_CHECKED , defaultValue = "false")
    public boolean aChecked;

    @DatabaseField(columnName = FIELD_NAME_SHOPLIST, foreign = true, foreignAutoRefresh = true)
    private ShopList aShopList;


    public  Articles(){}


    public int getaId() {
        return aId;
    }

    public void setaId(int aId) {
        this.aId = aId;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaAmount() {
        return aAmount;
    }

    public void setaAmount(String aAmount) {
        this.aAmount = aAmount;
    }

    public boolean isChecked() {
        return aChecked;
    }

    public void setChecked(boolean aChecked) {
        this.aChecked = aChecked;
    }

    public ShopList getaShopList() {
        return aShopList;
    }

    public void setaShopList(ShopList aShopList) {
        this.aShopList = aShopList;
    }


    @Override
    public String toString() {
        return
                 aName + ':' + ' ' +
                 aAmount ;
    }
}

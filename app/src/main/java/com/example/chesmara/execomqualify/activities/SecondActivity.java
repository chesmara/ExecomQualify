package com.example.chesmara.execomqualify.activities;

import android.app.Dialog;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.chesmara.execomqualify.R;
import com.example.chesmara.execomqualify.db.DatabaseHelper;
import com.example.chesmara.execomqualify.db.model.Articles;
import com.example.chesmara.execomqualify.db.model.ShopList;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ShopList sList;

    private EditText lName;


    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Toolbar toolbarDetail = (Toolbar) findViewById(R.id.toolbar_detail);

        if(toolbarDetail != null) {
            setSupportActionBar(toolbarDetail);
        }

        int kojaLista = getIntent().getExtras().getInt(MainActivity.LIST_KEY);

        try {
            sList= getDatabaseHelper().getmShopListDao().queryForId(kojaLista);

            lName=(EditText) findViewById(R.id.shoplist_name_detail);
            lName.setText(sList.getmName());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final ListView listView = (ListView) findViewById(R.id.list_articles);

        try {
            List<Articles> articlesList = getDatabaseHelper().getmArticlesDao().queryBuilder()
                    .where()
                    .eq(Articles.FIELD_NAME_SHOPLIST, sList.getmId())
                    .query();


          ListAdapter adapter = new ArrayAdapter<> (this, R.layout.list_item, articlesList);
          listView.setAdapter(adapter);






        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
                getMenuInflater().inflate(R.menu.articles_menu, menu);
                return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.add_article_detail:

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_article);


                Button add= (Button) dialog.findViewById(R.id.add_article);
                add.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        EditText articleName = (EditText) dialog.findViewById(R.id.article_name);
                        EditText artivleAmount = (EditText) dialog.findViewById(R.id.article_amount);

                        Articles ar = new Articles();
                        ar.setaName(articleName.getText().toString());
                        ar.setaAmount(artivleAmount.getText().toString());
                        ar.setaShopList(sList);

                        try {
                            getDatabaseHelper().getmArticlesDao().create(ar);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        refresh();

                        dialog.dismiss();
                    }
                });

                dialog.show();

            case R.id.list_edit:

                sList.setmName(lName.getText().toString());

                try {
                    getDatabaseHelper().getmShopListDao().update(sList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.list_remove:

                try {
                    getDatabaseHelper().getmShopListDao().delete(sList);

                    finish();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

        }


        return super.onOptionsItemSelected(item);
    }



        private void refresh(){
            ListView listview = (ListView) findViewById(R.id.list_articles);

            if(listview !=null){
                ArrayAdapter<Articles> adapter = (ArrayAdapter<Articles>) listview.getAdapter();

                if(adapter != null) {
                    adapter.clear();
                    List<Articles> list = null;

                    try {
                        list = getDatabaseHelper().getmArticlesDao().queryBuilder()
                                .where()
                                .eq(Articles.FIELD_NAME_SHOPLIST, sList.getmId())
                                .query();
                        adapter.addAll(list);

                        adapter.notifyDataSetChanged();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }




                }
            }

        }









    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }


}

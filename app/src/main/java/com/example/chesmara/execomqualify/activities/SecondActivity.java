package com.example.chesmara.execomqualify.activities;

import android.app.Dialog;
import android.app.ListActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.app.ListActivity;
import android.widget.Toast;


import com.example.chesmara.execomqualify.R;
import com.example.chesmara.execomqualify.db.DatabaseHelper;
import com.example.chesmara.execomqualify.db.model.Articles;
import com.example.chesmara.execomqualify.db.model.ShopList;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ShopList sList;

    private EditText lName;

//--------------------------------------onCreate---------------------------------------

    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Toolbar toolbarDetail = (Toolbar) findViewById(R.id.toolbar_detail);

        if (toolbarDetail != null) {
            setSupportActionBar(toolbarDetail);
        }
      //--------------------------------------------------------------------------------
        int kojaLista = getIntent().getExtras().getInt(MainActivity.LIST_KEY);

        try {
            sList = getDatabaseHelper().getmShopListDao().queryForId(kojaLista);

            lName = (EditText) findViewById(R.id.shoplist_name_detail);
            lName.setText(sList.getmName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
      //--------------------------------------------------------------------------------
        final ListView listView = (ListView) findViewById(R.id.list_articles);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        List<Articles> notDoneArticles = new ArrayList<>();
        List<Articles> DoneArticles = new ArrayList<>();

        try {
            final List<Articles> articlesList = getDatabaseHelper().getmArticlesDao().queryBuilder()
                    .where()
                    .eq(Articles.FIELD_NAME_SHOPLIST, sList.getmId())
                    .query();

            for (int i = 0; i < articlesList.size(); i++) {
                if (articlesList.get(i).isChecked())
                {
                    DoneArticles.add(articlesList.get(i));
                } else {
                    notDoneArticles.add(articlesList.get(i));
                }
            }


           // ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, notDoneArticles );

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, notDoneArticles );
            listView.setAdapter(adapter);

            
            //listView.setItemsCanFocus(false);
            //listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


            // listView onih artikala koji su označeni kao urađeni

            ListView listDone = (ListView) findViewById(R.id.list_done_articles);
            ListAdapter doneAdapter = new ArrayAdapter<>(this, R.layout.list_item, DoneArticles);
            listDone.setAdapter(doneAdapter);



            // onClick za brisanje uradjenih stavki - checking

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Articles deleteArticle = (Articles) listView.getItemAtPosition(position);
                    int deleteid = deleteArticle.getaId();

                    try {

                        Dao<Articles, Integer> cecked = getDatabaseHelper().getmArticlesDao();
                        Articles deleteArticle1= cecked.queryForId(deleteid);
                                deleteArticle1.setChecked(true);
                                    cecked.update(deleteArticle1);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                   refresh();

                }
            });


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

  //-----------------------------------------------------MENU-------------------------------------------------------

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

                Button cancel= (Button) dialog.findViewById(R.id.cancel_article_input);
                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
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

///--------------------------------------------on RESUME-----------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }
//---------------------------------------------REFRESH --------------------------------------------------------------------
    private void refresh(){
            ListView listview = (ListView) findViewById(R.id.list_articles);
            List<Articles> notDoneArticles = new ArrayList<>();
            List<Articles> DoneArticles = new ArrayList<>();

            if(listview !=null){
                ArrayAdapter<Articles> adapter1 = (ArrayAdapter<Articles>) listview.getAdapter();

                if(adapter1 != null) {
                    adapter1.clear();
                    List<Articles> list = null;

                    try {
                        list = getDatabaseHelper().getmArticlesDao().queryBuilder()
                                .where()
                                .eq(Articles.FIELD_NAME_SHOPLIST, sList.getmId())
                                .query();

                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).isChecked())
                            {
                                DoneArticles.add(list.get(i));
                            } else {
                                notDoneArticles.add(list.get(i));
                            }
                        }

                        adapter1.addAll(notDoneArticles);
                            adapter1.notifyDataSetChanged();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
         //--------------------------------------------------------
            ListView listDone = (ListView) findViewById(R.id.list_done_articles);
             if(listDone !=null){
                 ArrayAdapter<Articles> adapter = (ArrayAdapter<Articles>) listDone.getAdapter();

                 if(adapter !=null){
                     adapter.clear();

                     adapter.addAll(DoneArticles);
                     adapter.notifyDataSetChanged();

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

package com.example.chesmara.execomqualify.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.chesmara.execomqualify.R;
import com.example.chesmara.execomqualify.db.DatabaseHelper;
import com.example.chesmara.execomqualify.db.model.ShopList;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    public static String LIST_KEY = "LIST_KEY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

//---------------------------------------prikaz spiska ShopListi--------------------------
        final ListView listView = (ListView) findViewById(R.id.shoping_lists);


        List<ShopList> list = null;
        try {
            list = getDatabaseHelper().getmShopListDao().queryForAll();
            ListAdapter adapter= new ArrayAdapter<>(MainActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ShopList k = (ShopList) listView.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra(LIST_KEY, k.getmId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case(R.id.add_new_list):
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_new_list);

                Button add = (Button) dialog.findViewById(R.id.add_list);
                add.setOnClickListener(new View.OnClickListener()  {
                    @Override
                    public void onClick(View v){
                        EditText listName = (EditText) dialog.findViewById(R.id.new_list_name);

                        ShopList sl= new ShopList();
                        sl.setmName(listName.getText().toString());


                        try {
                            getDatabaseHelper().getmShopListDao().create(sl);

                            dialog.dismiss();
                            refresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
               });

                Button cancel = (Button) dialog.findViewById(R.id.add_list_cancel);
                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    public void refresh(){

        ListView listview = (ListView) findViewById(R.id.shoping_lists);

        if (listview != null){
            ArrayAdapter<ShopList> adapter= (ArrayAdapter<ShopList>) listview.getAdapter();

            if(adapter !=null){


                try {

                    adapter.clear();

                    List<ShopList> list = null;
                    list = getDatabaseHelper().getmShopListDao().queryForAll();

                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();


                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }
}

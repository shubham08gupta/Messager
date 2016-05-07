package com.messager;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adapter.SmsAdapter;

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {


    SmsAdapter mAdapter;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    ArrayList<String[]> list;
    HashMap<Integer, String> headerItems;

    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        getSms();

        // sets the messages list
        mAdapter = new SmsAdapter(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SendMessage.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                // show the results in a new activity
                final ArrayList<String[]> filteredModelList = filter(list, query);
                Intent intent = new Intent(getApplicationContext(), ShowQueryResults.class);
                intent.putExtra("results", filteredModelList);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    /***
     * reads all the messages from the inbox
     */
    public void getSms() {

        Uri uri = Uri.parse("content://sms/inbox");
        String[] projection = {"address", "body", "person"};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        list = new ArrayList<>();

        // only unique addresses are required
        headerItems = new HashMap<>();

        int i = 0;
        if (cursor.moveToFirst()) {

            for (i = 0; i < cursor.getCount(); i++) {
                String[] str = new String[2];
                str[0] = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                headerItems.put(i, str[0]);
                str[1] = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                list.add(str);
                cursor.moveToNext();
            }

        }
        // messages which are not grouped will be put in others section
        headerItems.put(i, "Others");
        if (!cursor.isClosed())
            cursor.close();

    }


    @Override
    public void onItemClick(View childView, int position) {
        Intent intent = new Intent(this, ShowReceivedMessage.class);
        intent.putExtra("phoneNumber", list.get(position)[0]);
        intent.putExtra("receivedMessage", list.get(position)[1]);
        startActivity(intent);

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }


    /***
     *
     * @param list
     * @param newText
     * @return the found list of messages
     */
    private ArrayList<String[]> filter(ArrayList<String[]> list, String newText) {

        // searching logic

        ArrayList<String[]> found = new ArrayList<>();

        Pattern p = Pattern.compile(Pattern.quote(newText), Pattern.CASE_INSENSITIVE);
        Matcher m1, m2;

        String[] f = new String[2];

        for (int i = 0; i < list.size(); i++) {


            m1 = p.matcher(list.get(i)[0]);
            m2 = p.matcher(list.get(i)[1]);

            if (m1.find() || m2.find()) {
                f[0] = list.get(i)[0];
                f[1] = list.get(i)[1];
                found.add(f);
            }

        }

        return found;
    }

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // wait for 2 secs
            backPressedTime = t;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        } else {
            // exit from app
            super.onBackPressed();
        }
    }
}

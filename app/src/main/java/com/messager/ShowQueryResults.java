package com.messager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import adapter.SmsAdapter;

public class ShowQueryResults extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    RecyclerView recyclerView;
    SmsAdapter mAdapter;
    ArrayList<String[]> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_query_results);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search_results);

        // get the query generated messages
        Bundle bundle = getIntent().getExtras();
        list = (ArrayList<String[]>) bundle.get("results");
        mAdapter = new SmsAdapter(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ShowQueryResults.this, ShowQueryResults.this));
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
}

package com.messager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/***
 * An activity to show the received message
 */
public class ShowReceivedMessage extends AppCompatActivity {

    TextView showMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_received_message);

        showMessage = (TextView) findViewById(R.id.showMessage);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("phoneNumber") + " \n\n " + bundle.getString("receivedMessage");
        showMessage.setText(message);
    }
}

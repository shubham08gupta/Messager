package com.messager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMessage extends AppCompatActivity {

    EditText number, messageBody;
    Button sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        number = (EditText) findViewById(R.id.enter_phone);
        messageBody = (EditText) findViewById(R.id.enter_body);
        sendMessage = (Button) findViewById(R.id.send_message);

        final SmsManager smsManager = SmsManager.getDefault();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number.getText().toString().equals("")) {
                    Toast.makeText(SendMessage.this, "Please Enter Number", Toast.LENGTH_SHORT).show();

                }
                if (messageBody.getText().toString().equals("")) {
                    Toast.makeText(SendMessage.this, "Please Enter Message", Toast.LENGTH_SHORT).show();
                }
                if (!number.getText().toString().equals("") && !messageBody.getText().toString().equals("")) {

                    String numbers = number.getText().toString();

                    try {
                        smsManager.sendTextMessage(numbers, null, messageBody.getText().toString(), null, null);
                        Toast.makeText(SendMessage.this, "Sms Sent", Toast.LENGTH_LONG).show();

                    } catch (IllegalArgumentException e) {
                        Toast.makeText(SendMessage.this, "Sms Not sent. Please try again", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }

            }
        });


    }
}

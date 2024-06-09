package com.example.kalifornia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class OrderDetailActivity extends AppCompatActivity {

    Button sendSmsButton, sendEmailButton;
    private String orderDetails;

    EditText phoneNum, emailAdrress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        TextView orderDetailsTextView = findViewById(R.id.orderDetailsTextView);

        sendSmsButton = findViewById(R.id.sendSmsButton);
        sendEmailButton = findViewById(R.id.sendEmailButton);

        phoneNum = findViewById(R.id.phone_number);
        emailAdrress = findViewById(R.id.email);

        orderDetails = getIntent().getStringExtra("order_details");
        orderDetailsTextView.setText(orderDetails);


        sendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendSms() {
        String destinationAddress = phoneNum.getText().toString();
        String text = orderDetails;

        if (!destinationAddress.isEmpty() && !text.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + Uri.encode(destinationAddress)));
            intent.putExtra("sms_body", text);

            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "SMS failed, please try again later...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter a phone number and message before sending.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail() {
        String address = emailAdrress.getText().toString();
        String subject = "ORDER DETAILS";
        String text = orderDetails;

        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setData(Uri.parse("mailto:" + address + "?subject=" + subject + "&body=" + text));
        startActivity(mailIntent);
    }
}


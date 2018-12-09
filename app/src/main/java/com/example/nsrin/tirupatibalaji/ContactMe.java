package com.example.nsrin.tirupatibalaji;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactMe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                finish();
            }
        });

        final EditText message=findViewById(R.id.contact_message);
        final EditText subject=findViewById(R.id.contact_subject);
        final EditText from_name=findViewById(R.id.contact_name);
        final EditText from_email =findViewById(R.id.contact_mail);

        Button send_message_button=findViewById(R.id.contact_me_submit);
        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/html");
                i.putExtra(Intent.EXTRA_EMAIL,new String[]{"n.srinivasulurao@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT,subject.getText());
                i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<p>"+message.getText()+"</p><br><strong>"+from_name.getText()+"</strong><br>"+from_email.getText()));
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactMe.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

package com.example.cesar.mycontacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cesar.mycontacts.dao.ContactDao;

import java.util.HashMap;
import java.util.Map;

public class NewContactActivity extends AppCompatActivity {
    TextView cnom,cap,ccor,ccel;
    ImageButton ca,sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_new_contact);
        Toolbar toolbar=findViewById(R.id.toolbar);
        cnom=findViewById(R.id.cnom);
        cap=findViewById(R.id.cap);
        ccor=findViewById(R.id.ccor);
        ccel=findViewById(R.id.ccel);
        ca=findViewById(R.id.cancel_action);
        sa=findViewById(R.id.save_action);
        final ContactDao cd=new ContactDao(this);
        final Bundle bundle=getIntent().getExtras();
        if(bundle.getBoolean("edit")){
            cnom.setText(bundle.getString("nombres"));
            cap.setText(bundle.getString("apellidos"));
            ccel.setText(bundle.getString("numero"));
            ccor.setText(bundle.getString("correo"));
        }
        sa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> mp=new HashMap<>();
                mp.put("nombres",cnom.getText().toString());
                mp.put("apellidos",cap.getText().toString());
                mp.put("numero",ccel.getText().toString());
                mp.put("email",ccor.getText().toString());
                if(bundle.getBoolean("edit")){
                    mp.put("idcontact",bundle.getString("idcontact"));
                }
                long errValue= -1;
                System.out.println(errValue);
                cd.addContact(mp);
                cd.cerrarDB();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

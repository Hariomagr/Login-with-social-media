package com.example.dellpc.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String name=getIntent().getStringExtra("name");
        String email=getIntent().getStringExtra("email");
        String photo=getIntent().getStringExtra("photo");
        TextView namme=(TextView)findViewById(R.id.name);
        TextView emaiil=(TextView)findViewById(R.id.email);
        namme.setText(name);
        emaiil.setText(email);
        final CircleImageView proima = (CircleImageView)findViewById(R.id.imageview);
        Glide.with(detail.this).load(photo).into(proima);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(detail.this,MainActivity.class);
        startActivity(i);
    }
}

package com.hencesimplified.arrwallpaper.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.hencesimplified.arrwallpaper.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Intent page_intent = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(page_intent);
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
        return true;
    }

    @Override

    public void onBackPressed() {
        finish();
        Intent page_intent = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(page_intent);
        overridePendingTransition(R.anim.left_enter, R.anim.right_out);
    }

}

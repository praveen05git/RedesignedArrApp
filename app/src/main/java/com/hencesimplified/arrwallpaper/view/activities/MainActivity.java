package com.hencesimplified.arrwallpaper.view.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hencesimplified.arrwallpaper.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private NavController navController;
    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = findViewById(R.id.bottomNav);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.photoViewFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, (DrawerLayout) null);
    }

    @Override
    public void onStart() {
        super.onStart();
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    public boolean checkPermission(String permission) {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int optionId = item.getItemId();

        switch (optionId) {

            case R.id.opt_rate:
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Rate App!");
                alertDialog.setMessage("Are you sure you want to open Play store?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent playStore = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hencesimplified.arrwallpaper"));
                        startActivity(playStore);
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialogInterface, i) -> alertDialog.dismiss());
                alertDialog.show();
                return true;

            case R.id.opt_more:
                final AlertDialog alertDialogMore = new AlertDialog.Builder(MainActivity.this).create();
                alertDialogMore.setTitle("More Apps!");
                alertDialogMore.setMessage("Are you sure you want to open Play store?");
                alertDialogMore.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/dev?id=7031227816779180923")));
                        } catch (android.content.ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:Hence Simplified")));
                        }
                    }
                });

                alertDialogMore.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialogInterface, i) -> alertDialogMore.dismiss());
                alertDialogMore.show();
                return true;

            case R.id.opt_about:
                finish();
                Intent page_intent = new Intent(this, AboutActivity.class);
                startActivity(page_intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

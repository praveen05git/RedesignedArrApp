package com.hencesimplified.arrwallpaper.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hencesimplified.arrwallpaper.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton infoButton;
    private NavController navController;
    private NavHostFragment navHostFragment;

    /*
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.weekly:
                    fragment = new ConcertsFragment();
                    break;
                case R.id.unlocked:
                    fragment = new InstrumentsFragment();
                    break;
                case R.id.locked:
                    fragment = new FamilyFragment();
                    break;
                case R.id.wildlife:
                    fragment = new EventsFragment();
                    break;
                case R.id.scenes:
                    fragment = new CasualFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNav);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        //navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());

        infoButton = findViewById(R.id.floatingActionButton);
/*
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("ArrPref", 0);
        int page = preferences.getInt("ArrPage", -1);

        if (page == 1) {
            loadFragment(new ConcertsFragment());
            bottomNavigationView.setSelectedItemId(R.id.weekly);
        } else if (page == 2) {
            loadFragment(new InstrumentsFragment());
            bottomNavigationView.setSelectedItemId(R.id.unlocked);
        } else if (page == 3) {
            loadFragment(new EventsFragment());
            bottomNavigationView.setSelectedItemId(R.id.wildlife);
        } else if (page == 4) {
            loadFragment(new CasualFragment());
            bottomNavigationView.setSelectedItemId(R.id.scenes);
        } else if (page == 5) {
            loadFragment(new FamilyFragment());
            bottomNavigationView.setSelectedItemId(R.id.locked);
        } else {
            loadFragment(new ConcertsFragment());
            bottomNavigationView.setSelectedItemId(R.id.weekly);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
*/
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page_intent = new Intent(MainActivity.this, InformationActivity.class);
                startActivity(page_intent);
            }
        });
    }
/*
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, (DrawerLayout) null);
    }
 */

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
/*
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                    .replace(R.id.fg_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
*/
    @Override
    public void onBackPressed() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Warning!");
        alertDialog.setMessage("Are you sure you want to exit?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                Intent ExitIntent = new Intent(Intent.ACTION_MAIN);
                ExitIntent.addCategory(Intent.CATEGORY_HOME);
                ExitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ExitIntent);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
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
                Intent playStore = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hencesimplified.arrwallpaper"));
                startActivity(playStore);
                return true;

            case R.id.opt_more:
                try {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/dev?id=7031227816779180923")));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:Hence Simplified")));
                }
                return true;

            case R.id.opt_about:
                finish();
                Intent page_intent = new Intent(this, AboutActivity.class);
                startActivity(page_intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

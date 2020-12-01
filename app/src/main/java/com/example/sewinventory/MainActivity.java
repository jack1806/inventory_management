package com.example.sewinventory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.FrameLayout;

import com.example.sewinventory.helper.sharePrefHelper;
import com.example.sewinventory.helper.sharePrefContract;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;
    sharePrefHelper prefHelper = new sharePrefHelper();
    FragmentTransaction fragmentTransaction;
    int nav_current_id;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(sharePrefContract.SHARED_PREF_NAME, sharePrefContract.SHARED_PREF_MODE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        changeFrame(new HomeFragment(), R.string.menu_home);
    }

    public void changeFrame(Fragment fragment, int id){
        getSupportActionBar().setTitle(id);
        fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        nav_current_id = id;

        if (id == R.id.nav_home) {
            changeFrame(new HomeFragment(), R.string.menu_home);
        } else if (id == R.id.nav_inventories) {
            changeFrame(new InventoriesFragment(), R.string.menu_inventory);
        } else if (id == R.id.nav_consumers) {
            changeFrame(new ConsumersFragment(), R.string.menu_consumers);
        } else if (id == R.id.nav_products) {
            changeFrame(new ProductsFragment(), R.string.menu_products);
        } else if (id == R.id.nav_logout) {
            doLogout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void doLogout(){
        prefHelper.setLogin(preferences, false);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nav_current_id == R.id.nav_home) {
            changeFrame(new HomeFragment(), R.string.menu_home);
        } else if (nav_current_id == R.id.nav_inventories) {
            changeFrame(new InventoriesFragment(), R.string.menu_inventory);
        } else if (nav_current_id == R.id.nav_consumers) {
            changeFrame(new ConsumersFragment(), R.string.menu_consumers);
        } else if (nav_current_id == R.id.nav_products) {
            changeFrame(new ProductsFragment(), R.string.menu_products);
        } else if (nav_current_id == R.id.nav_logout) {
            doLogout();
        }
    }
}

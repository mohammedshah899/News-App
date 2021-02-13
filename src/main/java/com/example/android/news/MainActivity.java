package com.example.android.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CustomAdapter mAdapter;
    ActionBarDrawerToggle toggle;
    String URL = "https://content.guardianapis.com/search?show-fields=headline%2Cthumbnail%2CtrailText&api-key=127cd8ad-5926-46e9-9c17-2de2bbff4548";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setSupportActionBar will setup a basic actionBar in the app from the inputted toolbar xml
        setSupportActionBar(findViewById(R.id.toolbar_top));
        showNavigationDrawer();


        ListView listView = (ListView)findViewById(R.id.list_view);
        mAdapter = new CustomAdapter(this, new ArrayList<>());
        listView.setAdapter(mAdapter);

        DataAsyncTask task = new DataAsyncTask();
        task.execute(URL);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomObject currentObject = (CustomObject) parent.getItemAtPosition(position);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentObject.getURL()));
                startActivity(browserIntent);
            }
        });

    }

    private class DataAsyncTask extends AsyncTask<String, Void, List<CustomObject>> {

        @Override
        protected List<CustomObject> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null){
                return null;
            }
            return Utils.fetchURLData(urls[0]);
        }

        @Override
        protected void onPostExecute(List<CustomObject> customObjects) {
            mAdapter.clear();

            if (customObjects != null && !customObjects.isEmpty()){
                mAdapter.addAll(customObjects);
            }
        }
    }

    private void showNavigationDrawer(){
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        // ActionBarDrawerToggle is assigned to the drawerLayout. It will act as a listener for
        // the open and close functions of the drawerLayout.
        toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        // to Sync the state of the drawerLayout with the actionBarDrawerToggle.
        toggle.syncState();
        // getSupportActionBar() will get us the actionbar of the app &
        // .setDisplayHomeAsUpEnabled will add the nav menu to it
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_id);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.world_id){
                    Toast.makeText(MainActivity.this,"World pressed", Toast.LENGTH_LONG).show();
                }
                if (item.getItemId() == R.id.sports_id){
                    Toast.makeText(MainActivity.this,"Sports pressed", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
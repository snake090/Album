package com.example.owner.album.Album;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.owner.album.Insert.Album_Insert;
import com.example.owner.album.R;
import com.example.owner.album.model.Album;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.Calendar;

public class Album_Create_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText name;
    EditText date;
    EditText keyword1;
    EditText keyword2;
    EditText keyword3;
    Button create_album;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album__create_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        name=(EditText)findViewById(R.id.Name);
        keyword1=(EditText)findViewById(R.id.KeyWord1);
        keyword2=(EditText)findViewById(R.id.KeyWord2);
        keyword3=(EditText)findViewById(R.id.KeyWord3);
        date=(EditText)findViewById(R.id.Date);
        date.setOnClickListener(v->{


            // 現在の日付を取得
            final Calendar calendar = Calendar.getInstance();
            int year  = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day   = calendar.get(Calendar.DAY_OF_MONTH);

            // 日付選択ダイアログの生成
            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view,
                                              int year, int monthOfYear, int dayOfMonth) {
                            date.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear)+"/"+String.valueOf(dayOfMonth));
                        }
                    },
                    year, month, day);

            // 表示
            datePicker.show();

        });
        create_album =(Button)findViewById(R.id.Create);
        create_album.setOnClickListener(v->{
            ArrayList<String> keywords=new ArrayList<String>();
            if(!"".equals(keyword1.getText().toString())){
                keywords.add(keyword1.getText().toString());
            }
            if(!"".equals(keyword2.getText().toString())){
                keywords.add(keyword2.getText().toString());
            }
            if(!"".equals(keyword3.getText().toString())) {
                keywords.add(keyword3.getText().toString());
            }
            String albumName=name.getText().toString();
            if(keywords.size()!=0&&albumName!=null) {
                Album_Insert album_insert = new Album_Insert();
                album_insert.Insert_DB(albumName,keywords);
                Toast.makeText(this,"Create_album",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Please enter album name or keyword",Toast.LENGTH_LONG).show();
            }

            /*
            Word_association word=new Word_association("世界遺産");
            word.execute();


            WordsAPI wordsapi=new WordsAPI("");
            wordsapi.execute();
*/

        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.album__create_, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

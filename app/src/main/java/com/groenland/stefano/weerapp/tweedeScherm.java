package com.groenland.stefano.weerapp;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class tweedeScherm extends ActionBarActivity {

    TextView nameTag;
    TextView place;
    TextView temp;
//    toets
    TextView langt;
    TextView longt;
    Long latitude;
    Long lontitude;
    TextView landcode;

    BierWeerDatabaseHandler db = new BierWeerDatabaseHandler(this);

    BierWeer bw = new BierWeer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweede_scherm);
        Bundle extras = getIntent().getExtras();
        String naam = (String) extras.getString("Naam");
//        toets
        String landcode = (String) extras.getString("Landcode");
        System.out.println(naam);
        System.out.println(landcode);

        BierWeerDatabaseHandler db = new BierWeerDatabaseHandler(this);

        BierWeer bw = new BierWeer();
        bw.setNaam(naam);
        bw.setPlaats(db.zoekPlaats(bw.getNaam()));
//        toets
        bw.setLandcode(db.zoekLand(bw.getNaam()));
        MyTask myTask = new MyTask(bw.getPlaats());
        myTask.execute();

        nameTag = (TextView) findViewById(R.id.nameTag);
        place = (TextView) findViewById(R.id.Place);
        temp = (TextView) findViewById(R.id.Temp);
        nameTag.setText(naam);
//        toets
        langt = (TextView) findViewById(R.id.langt);
        longt = (TextView) findViewById(R.id.longt);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweede_scherm, menu);
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
    private class MyTask extends AsyncTask<String, String,
            String> {
        String city;

        public MyTask(String city) {
            this.city = city;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient(
                        new BasicHttpParams());
                HttpPost httpPost = new HttpPost(
                        ("http://api.openweathermap.org/data/2.5/weather?q=" + this.city + ","+ db.zoekLand(nameTag.getText().toString())));
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();

                InputStream inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                return sb.toString();

            }catch (Exception e){
                System.out.println("Exeption :" + e.toString());
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {


            try {
                JSONObject obj = new JSONObject(s);
                String plaatsNaam = obj.getString("name");
                JSONObject mainInfo = obj.getJSONObject("main");
                Long temperature = mainInfo.getLong("temp");
                temperature = temperature - 273;
//                toets
                JSONObject coords = obj.getJSONObject("coord");
                Long lat = coords.getLong("lat");
                Long lon = coords.getLong("lon");


                temp.setText(temperature.toString());
                place.setText(plaatsNaam);
                langt.setText(lat.toString());
                longt.setText(lon.toString());

            }
            catch (Exception e){
                System.out.println("JSON Exception");
            }
            super.onPostExecute(s);
        }
    }
}

package com.groenland.stefano.weerapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.util.List;


public class MainActivity extends ActionBarActivity {

   EditText enterCity;
   EditText enterShortname;
   Button btnTag;
   Button btnEdit;
    Button btnClear;
    BierWeerDatabaseHandler db;
//    toets
    EditText landCode;


    public void btnSave (View view) {
        BierWeer bw = new BierWeer();
        bw.setPlaats(enterCity.getText().toString());
        bw.setNaam(enterShortname.getText().toString());
//        toets
        bw.setLandcode(landCode.getText().toString());
        db.addRecord(bw);
        addEntry(enterShortname.getText().toString(), enterCity.getText().toString(), landCode.getText().toString());
    }
//toets
    private void addEntry(String naam, String plaats, String landcode) {

        LayoutInflater layoutInflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newRow =
                layoutInflater.inflate(R.layout.newtagtablerow, null);

        btnTag = (Button) newRow.findViewById(R.id.newTagButton);
        btnEdit = (Button) newRow.findViewById(R.id.newEditButton);
        btnTag.setOnClickListener(tagButtonListener);
        btnTag.setText(naam);
        TableLayout tagsTableLayout = (TableLayout)
                findViewById(R.id.queryTableLayout);
        tagsTableLayout.addView(newRow);
    }

    private View.OnClickListener tagButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = ((Button)v).getText().toString();
            Toast.makeText(getApplicationContext(), tag,
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, tweedeScherm.class);
            intent.putExtra("Naam", btnTag.getText().toString());
//            toets
            intent.putExtra("Landcode", landCode.getText().toString());
            startActivity(intent);
        }
    };
    private View.OnClickListener editButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = ((Button)v).getText().toString();
            Toast.makeText(getApplicationContext(), tag,
                    Toast.LENGTH_LONG).show();
        }
    };
    private View.OnClickListener clearButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             db.emptyBierWeer();
            Toast.makeText(getApplicationContext(), "Clear!" , Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enterCity = (EditText) findViewById(R.id.enterCity);
        enterShortname = (EditText) findViewById(R.id.enterShortname);
//        toets
        landCode = (EditText) findViewById(R.id.landCode);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(clearButtonListener);
        db = new BierWeerDatabaseHandler(this);
        List<BierWeer> list =  db.getBierWeer();
        for(BierWeer bw : list){
//            toets
                addEntry(bw.getNaam(), bw.getPlaats(),bw.getLandcode());
        }
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


}


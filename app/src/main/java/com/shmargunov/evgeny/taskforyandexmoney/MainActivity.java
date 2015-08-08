package com.shmargunov.evgeny.taskforyandexmoney;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    ListView list;
    TextView title;
    Button btngetdata;
    ArrayList<HashMap<String,String>> Productlist = new ArrayList<HashMap<String,String>>();

    private static String url = "https://money.yandex.ru/api/categories-list";

    private static final String TAG_TITLE = "title";

    JSONArray PrList = null;

    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Productlist = new ArrayList<HashMap<String, String>>();

        btngetdata = (Button)findViewById(R.id.getdata);
        btngetdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Productlist.isEmpty())
                    new JSONParse().execute();
            }
        });

    }

    private class JSONParse extends AsyncTask<String,String,JSONArray>{
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            title = (TextView)findViewById(R.id.title);

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Получение данных...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... args){
            JSONParser jParser = new JSONParser();
            JSONArray json = jParser.getJSONfromURL(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONArray json){
            pDialog.dismiss();
            try {
                PrList = json;
                for(int i=0; i<PrList.length();i++){
                    JSONObject item = PrList.getJSONObject(i);
                    String title = item.getString(TAG_TITLE);

                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put(TAG_TITLE,title);

                    Productlist.add(map);
                    list =(ListView)findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, Productlist,
                            R.layout.list_item,
                            new String[] {TAG_TITLE}, new int[]{R.id.title});

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id){
                            Toast.makeText(MainActivity.this, "You clicked at " + Productlist.get(+position).get("title"), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}

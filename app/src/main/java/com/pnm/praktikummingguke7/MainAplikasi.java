package com.pnm.praktikummingguke7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainAplikasi extends AppCompatActivity {

    private String TAG = MainAplikasi.class.getSimpleName();

    private ProgressDialog progressDialog;
    private ListView listView;
    ArrayList<HashMap<String, String>> colorJsonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aplikasi);

        colorJsonList = new ArrayList<>();
        listView = findViewById(R.id.listview);
        new Getnpm().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class Getnpm extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainAplikasi.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler httpHandler = new HttpHandler();

            // JSON data url
            String jsonurl = "https://pajuts.000webhostapp.com/read.php";
            String jsonString = httpHandler.makeServiceCall(jsonurl);
            Log.e(TAG, "Response from url: " + jsonString);
            if (jsonString != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    // Getting JSON Array node
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        String npm = c.getString("npm");
                        String password = c.getString("password");

                        HashMap<String, String> resultx = new HashMap<>();

                        resultx.put("npm", npm);
                        resultx.put("password", password);

                        colorJsonList.add(resultx);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Could not get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Could not get json from server.",
                                Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressDialog.isShowing()) progressDialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(
                    MainAplikasi.this, colorJsonList, R.layout.list_item,
                    new String[]{"npm", "password"},
                    new int[]{R.id.npm, R.id.password});

            listView.setAdapter(adapter);
        }

    }
}
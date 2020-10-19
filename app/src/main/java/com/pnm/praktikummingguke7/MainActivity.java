package com.pnm.praktikummingguke7;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    EditText txtNPM,txtPassword;
    String url = "https://pajuts.000webhostapp.com/read.php";
    String sNPM,sPassword,npm,nama,password,isiNPM,isiPASSWORD,textNPM,textPASSWORD,isidariNPM,isidariPASSWORD,hasil;
    private RequestQueue mQueue;
    Button btn_Login;
    ArrayList<HashMap<String, String>> hasilNPM = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> hasilPassword = new ArrayList<HashMap<String, String>>();

    public static String md5(String message){
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2*hash.length);
            for(byte b : hash){
                sb.append(String.format("%02x", b&0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex){
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex){
            Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
        } return digest;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        txtNPM = findViewById(R.id.txtNPM);
        txtPassword = findViewById(R.id.txtPassword);
        mQueue = Volley.newRequestQueue(this);

        btn_Login = (Button) findViewById(R.id.btn_Login);
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });

    }

    public void jsonParse(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("result");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);

                                npm = result.getString("npm");
                                password = result.getString("password");

                                HashMap<String, String> dataNPM = new HashMap<>();
                                HashMap<String, String> dataPASSWORD = new HashMap<>();

                                dataNPM.put("npm", npm);
                                dataPASSWORD.put("password", password);

                                hasilNPM.add(dataNPM);
                                hasilPassword.add(dataPASSWORD);

                                CheckLogin();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);


    }
    private void CheckLogin() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        isiNPM = txtNPM.getText().toString();
        isiPASSWORD = md5(txtPassword.getText().toString());

        textNPM = "{npm="+isiNPM+"}";
        textPASSWORD = "{password="+isiPASSWORD+"}";

        for (int i = 0; i <hasilNPM.size(); i++){
            isidariNPM = hasilNPM.get(i).toString();
            isidariPASSWORD = hasilPassword.get(i).toString();
            if (textNPM.equals(isidariNPM) && textPASSWORD.equals(isidariPASSWORD)){
                benar();
                break;
            } else {
                salah();
                break;
            }
        }
    }
    public void benar(){
        Intent intent = new Intent(MainActivity.this, MainAplikasi.class);
        startActivity(intent);
        Toast.makeText(this, "Selamat Datang", Toast.LENGTH_LONG).show();
    }
    public void salah(){
        Toast.makeText(this, "NPM atau Password anda salah. \n"+isidariPASSWORD+" atau \n" + isidariNPM, Toast.LENGTH_LONG).show();
    }
}
package com.pnm.praktikummingguke7;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainRegistrasi extends AppCompatActivity {

    EditText rNPM,rPassword;
    String stringNPM,stringPassword;
    String url = "https://pajuts.000webhostapp.com/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_registrasi);

        rNPM = findViewById(R.id.rNPM);
        rPassword = findViewById(R.id.rPassword);

    }

    public void moveToLogin(View view) {

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    public void Register(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");


        if(rNPM.getText().toString().equals("")){
            Toast.makeText(this, "Masukkan NPM", Toast.LENGTH_SHORT).show();
        }
        else if(rPassword.getText().toString().equals("")){
            Toast.makeText(this, "Masukkan Password", Toast.LENGTH_SHORT).show();
        }
        else{
            progressDialog.show();
            stringNPM = rNPM.getText().toString().trim();
            stringPassword = rPassword.getText().toString().trim();

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    rNPM.setText("");
                    rPassword.setText("");
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    Toast.makeText(MainRegistrasi.this, response, Toast.LENGTH_SHORT).show();
                }
            },new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(MainRegistrasi.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();

                    params.put("npm",stringNPM);
                    params.put("password",stringPassword);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(MainRegistrasi.this);
            requestQueue.add(request);


        }

    }
}
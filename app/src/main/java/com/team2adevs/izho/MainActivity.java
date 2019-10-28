package com.team2adevs.izho;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_vrf = findViewById(R.id.verify);
        btn_vrf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://plony.hopto.org:70/authorization";
                EditText et = findViewById(R.id.editText);
                Editable tel = et.getText();
                JSONArray js = new JSONArray();
                JSONObject jsobj = new JSONObject();
                try{
                    jsobj.put("tel", tel);
                    jsobj.put("type", "auth");
                    js.put(jsobj);
                }
                catch (JSONException e){
                    System.out.println(e.getMessage());
                }
                request(url,js);
            }
        });

    }
    void request(final String url, final JSONArray js){
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST, url,js,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response){
                        System.out.println("main");
                        try {
                            System.out.println(response);
                            TextView tv = findViewById(R.id.textView);
                            if(response.length() != 0) {
                                int id = response.getInt(0);
                                String tel = response.getString(1);
                                System.out.println(tel);
                                if (id != -1) {
                                    Intent intent = new Intent(MainActivity.this, sms.class);
                                    intent.putExtra("tel", tel);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();
                                } else {
                                    tv.setText("No phone registered");
                                }
                            }
                            else{
                                tv.setText("Input phone");
                            }
                        }
                        catch(JSONException e){
                            System.out.println(response.toString());
                            System.out.println(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error");
                System.out.println(error.getMessage());
                try{
                    if (error.networkResponse.statusCode == 200){
                        request(url, js);
                    }
                } catch (Exception e){
                    request(url, js);
                }
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
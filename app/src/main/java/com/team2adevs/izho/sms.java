package com.team2adevs.izho;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class sms extends AppCompatActivity {
    String tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tel = extras.getString("tel");
        }

        Button btn_vrf = findViewById(R.id.go);
        btn_vrf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://plony.hopto.org:70/sms";
                JSONArray js = new JSONArray();
                JSONObject jsobj = new JSONObject();
                try{
                    jsobj.put("tel", tel);
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
                            EditText et = findViewById(R.id.editText2);
                            Editable sms = et.getText();
                            System.out.println(response);
                            String type = response.getString(2);
                            int code = response.getInt(3);
                            if ((Integer.parseInt(sms.toString())) == code){
                                Intent intent = new Intent(sms.this, MainWindow.class);
                                intent.putExtra("type", type);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
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

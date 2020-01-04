package com.team2adevs.izho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class FeedWindow extends AppCompatActivity {

    LinearLayout layout;
    String url = "http://plony.hopto.org:70";
    ProgressBar pb;

    public int dpToPx(int dp){
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }

    public int spToPx(int sp){
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                r.getDisplayMetrics()
        );
        return px;
    }

    private void requestFromServer(final String additional_url){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + additional_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    layout.removeView(pb);
                    for(int i = 0; i < response.length(); i++){
                        JSONArray feed_event = response.getJSONArray(i);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        String title = feed_event.getString(1);
                        String description = feed_event.getString(2);
                        final String redirect_url = feed_event.getString(3);
                        String image_path = feed_event.getString(4);
                        CardView cardView_i = new CardView(FeedWindow.this);
                        cardView_i.setClickable(true);
                        cardView_i.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent viewIntent =
                                        new Intent("android.intent.action.VIEW",
                                                Uri.parse(redirect_url));
                                startActivity(viewIntent);
                            }
                        });
                        cardView_i.setLayoutParams(layoutParams);


                        LinearLayout new_linear = new LinearLayout(FeedWindow.this);
                        new_linear.setLayoutParams(layoutParams);
                        new_linear.setBackgroundColor(getResources().getColor(R.color.FizmatLightBlue));
                        new_linear.setGravity(Gravity.CENTER_HORIZONTAL);
                        new_linear.setOrientation(LinearLayout.VERTICAL);
                        new_linear.setLayoutParams(layoutParams);

                        ImageView imageView = new ImageView(FeedWindow.this);


                        new DownLoadImageTask(imageView).execute(url + image_path);
                        imageView.setAdjustViewBounds(true);
                        imageView.setMaxHeight(dpToPx(150));
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        TextView title_tv = new TextView(FeedWindow.this);
                        TextView description_tv = new TextView(FeedWindow.this);

                        title_tv.setText(title);
                        description_tv.setText(description);
                        Typeface tf = ResourcesCompat.getFont(getApplicationContext(), R.font.archive);
                        title_tv.setTypeface(tf);
                        description_tv.setTypeface(tf);
                        title_tv.setTextColor(getResources().getColor(R.color.White));
                        title_tv.setTextSize(30);
                        title_tv.setGravity(Gravity.CENTER_HORIZONTAL);
                        description_tv.setTextColor(getResources().getColor(R.color.White));
                        description_tv.setTextSize(15);
                        description_tv.setGravity(Gravity.CENTER_HORIZONTAL);

                        ViewGroup.MarginLayoutParams layoutParams_image =
                                (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                        layoutParams_image.setMargins(0, dpToPx(10), 0, 0);

                        new_linear.addView(imageView);
                        new_linear.addView(title_tv);
                        new_linear.addView(description_tv);



                        cardView_i.addView(new_linear);
                        ViewGroup.MarginLayoutParams layoutParamsss =
                                (ViewGroup.MarginLayoutParams) cardView_i.getLayoutParams();
                        layoutParamsss.setMargins(0, dpToPx(5), 0, 0);
                        layout.addView(cardView_i);
                    }
                } catch (JSONException e){
                    System.out.println(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                requestFromServer(additional_url);

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void updateFeed(){
        requestFromServer("/feed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_window);
        BottomNavigationView bottomnavbar = findViewById(R.id.btmnavbar);
        bottomnavbar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.toolbar_feed);
        toolbar.setTitle("Feed");

        pb = new ProgressBar(FeedWindow.this);
        pb.setVisibility(View.VISIBLE);
        layout = findViewById(R.id.linear_feed);
        layout.addView(pb);

        CardView places = findViewById(R.id.places_to_go_card);
        places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.lonelyplanet.com/kazakhstan/almaty#in-detail"));
                startActivity(viewIntent);
            }
        });
        updateFeed();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    Intent intentf = new Intent(FeedWindow.this, MainWindow.class);
                    startActivity(intentf);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.mylist:
                    Intent intent = new Intent(FeedWindow.this, MyListWindow.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.settings:
                    Intent intent1 = new Intent(FeedWindow.this, SettingsWindow.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        }
    };

    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}

package add.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.testfairy.TestFairy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;

public class MainActivity extends Activity {

    public final static String MESSAGE_KEY =  "STC";
    TextView t1_temp,t2_city,t3_description,t4_date;
    ImageView img_weather, advice1, advice2;
    ImageButton button;
    RequestParams params;
    AsyncHttpClient client;
    //put own ip address
    String ip = "192.168.1.167";
    String MYURL="http://"+ip+":8080/Servlet_2/FileUploadServer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TestFairy.begin(this, "0f8d49a4eeed090575669e7c9a1d765c41b27537");
        super.onCreate(savedInstanceState);




        //getting data from other activity - City from search bar
        Intent intent = getIntent();
        String message = intent.getStringExtra(MESSAGE_KEY);

        setContentView(R.layout.activity_main);

        t1_temp = findViewById(R.id.temp);
        t2_city = findViewById(R.id.city);
        t3_description = findViewById(R.id.desc);
//        t4_date = (TextView)findViewById(R.id.date);
        img_weather = findViewById(R.id.img_weather);
        advice1 = findViewById(R.id.cloth1);
        advice2 = findViewById(R.id.cloth2);
        button = findViewById(R.id.info_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2(v);
            }
        });

        findWeather(message);

//        final WeatherView weatherView = (WeatherView)findViewById(R.id.weather);
//        final TextView txtDes = (TextView)findViewById(R.id.txtDes);

//        findViewById(R.id.btnRain).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                weatherView.setWeather(Constants.weatherStatus.RAIN)
//                        .setLifeTime(2000)
//                        .setFadeOutTime(1000)
//                        .setParticles(43)
//                        .setFPS(60)
//                        .setAngle(-5)
//                        .startAnimation();
//                txtDes.setText("It's raining...");
//            }
//        });
//
//        findViewById(R.id.btnSnow).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                weatherView.setWeather(Constants.weatherStatus.SNOW)
//                        .setLifeTime(2000)
//                        .setFadeOutTime(1000)
//                        .setParticles(43)
//                        .setFPS(60)
//                        .setAngle(-5)
//                        .startAnimation();
//                txtDes.setText("It's snowing...");
//            }
//        });
//
//        findViewById(R.id.btnSunny).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                weatherView.setWeather(Constants.weatherStatus.SUN)
//                        .setLifeTime(2000)
//                        .setFadeOutTime(1000)
//                        .setParticles(43)
//                        .setFPS(60)
//                        .setAngle(-5)
//                        .startAnimation();
//                txtDes.setText("It's a sunny day...");
//            }
//        });
    }

    public void openActivity2(View view){
        button.setColorFilter(Color.argb(255, 255, 255, 255)); // White Tint
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void findWeather(String message){
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+message+"&appid=41d9937fdb964001115c1cb63632e137&units=Imperial";
        final WeatherView weatherView = (WeatherView)findViewById(R.id.weather);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                try{Log.d("mytag", "This is my message");
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String description = object.getString("description");
                    String icon = object.getString("icon");
                    String city = response.getString("name");

                    t1_temp.setText(temp);
                    t2_city.setText(city);
                    t3_description.setText(description);

                    //Load image - compare speed of no picasso
                    Picasso.get().load(new StringBuilder("http://openweathermap.org/img/w/").append(icon)
                            .append(".png").toString()).into(img_weather);

                    if(description.contains("rain")){
                        Picasso.get().load(R.drawable.rainboots).into(advice1);
                        Picasso.get().load(R.drawable.umbrella).into(advice2);
                        weatherView.setWeather(Constants.weatherStatus.RAIN)
                                .setLifeTime(2000)
                                .setFadeOutTime(1000)
                                .setParticles(43)
                                .setFPS(60)
                                .setAngle(-5)
                                .startAnimation();
                    }


                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE=MM-DD");
                    String formatted_date = sdf.format(calendar.getTime());

//                    t4_date.setText(formatted_date);

                    double temp_int = Double.parseDouble(temp);
                    double centi = (temp_int - 32) / 1.8000;
                    centi = Math.round(centi);
                    int i = (int)centi;
                    t1_temp.setText(String.valueOf(i)+"˚C");

                    if(i<=0){
                        Picasso.get().load(R.drawable.winterhat).into(advice1);
                        Picasso.get().load(R.drawable.gloves).into(advice2);
                    }
                    if(description.contains("snow")){
                        Picasso.get().load(R.drawable.winterhat).into(advice1);
                        Picasso.get().load(R.drawable.gloves).into(advice2);
                        weatherView.setWeather(Constants.weatherStatus.SNOW)
                                .setLifeTime(2000)
                                .setFadeOutTime(1000)
                                .setParticles(43)
                                .setFPS(60)
                                .setAngle(-5)
                                .startAnimation();

                    }else
                    if(i>=27){
                        Picasso.get().load(R.drawable.sunglasses).into(advice1);
                        Picasso.get().load(R.drawable.flipflops).into(advice2);
                    }else
                    if(i>15 && i<27) {
                        Picasso.get().load(R.drawable.jeans2).into(advice1);
                        Picasso.get().load(R.drawable.tshirt).into(advice2);
                    }else
                    if(i>0 && i<=15){
                        Picasso.get().load(R.drawable.coat).into(advice1);
                        Picasso.get().load(R.drawable.jeans2).into(advice2);
                    }

                    params = new RequestParams();
                    params.put("c",city);
                    params.put("d", description);
                    params.put("t", temp);
                    client = new AsyncHttpClient();
                    client.post(MYURL, params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
//                            Toast.makeText(MainActivity.this, "Submit Success"+response, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
//                            Toast.makeText(MainActivity.this, "Something Went Wrong: "+responseString, Toast.LENGTH_SHORT).show();
                        }

                    });

                }catch (JSONException e){
                    Log.d("mytag", "This is my message2");

//                    e.printStackTrace();
//                    System.out.println("ERROR: " + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("mytag", "This is my message" + error);
            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);

    }
}

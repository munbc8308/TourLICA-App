package com.application.tourlica;

import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.application.tourlica.databinding.ActivityMainBinding;
import com.kakao.vectormap.KakaoMapSdk;

import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KakaoMapSdk.init(this, "d676018bf28505ed778825aeca90dfd6");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = location -> {
            // 위치가 변할때마다 실행된다.
            // 위치가 변경될때 마다 위도, 경도를 가져온다.
            location.getLatitude(); // 위도
            location.getLongitude(); // 경도
            Log.i("MY LOCATION", "위도 : " + location.getLatitude());
            Log.i("MY LOCATION", "경도 : " + location.getLongitude());
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한 요청 하지 않았다면,
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    100); // 다시 권한을 요청한다.
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, // GPS 센서 연결
                10000, // 몇초에 한번 씩 위치를 갱신/파악 하게 할 것인지
                2000, // 몇미터 이동에 한번씩 찾을까
                locationListener);// 어떤 코드를 실행 시킬것인지.




        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> {
                    try {
                        sign_in();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private String sign_in() throws JSONException {
        final MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String base_url = "https://tourlica.shop";
        JSONObject jo = new JSONObject();
        jo.put("email", "admin");
        jo.put("password", "admin");

        RequestBody body = RequestBody.create(jo.toString(), JSON);
        Request request = new Request.Builder()
                .url(base_url + "/api/sign-in")
                .post(body)
                .build();
        System.out.println(request);

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response);
            if (response.isSuccessful()) {
                System.out.println("excute!!!!!");
                return response.body().string();
            } else {
                System.out.println("excute error!!!!!");
                return "error";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
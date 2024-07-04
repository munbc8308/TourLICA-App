package com.application.tourlica;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.application.tourlica.data.AppData;
import com.kakao.vectormap.KakaoMapSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private String tour_information = "";
    private String location_information = "";
    private ProgressBar progressBar = null;
    private Boolean isDataLoading = false;
    private Timer timer = null;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KakaoMapSdk.init(this, "d676018bf28505ed778825aeca90dfd6");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = location -> {
            // 위치가 변할때마다 실행된다.
            // 위치가 변경될때 마다 위도, 경도를 가져온다.
            //Log.i("MY LOCATION", "위도 : " + location.getLatitude());
            //Log.i("MY LOCATION", "경도 : " + location.getLongitude());
            location_information += location.getLatitude() + "/";
            location_information += location.getLongitude();

            new Thread(() -> {
                tour_information = getLocationBasedList1(location);
            }).start();
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

        setContentView(R.layout.main_activity);
        progressBar = findViewById(R.id.progress_horizontal);
        TextView loadingText = findViewById(R.id.loading_text);
        startProgress(progressBar, loadingText);
    }

    private void moveToLogIn() {
        Intent logIn = new Intent(MainActivity.this, LoginActivity.class);
        logIn.putExtra("TOURDATA", tour_information);
        logIn.putExtra("LOCATION", location_information);
        startActivity(logIn);
        finish();
    }

    private void startProgress(ProgressBar progressBar, TextView loadingText) {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            int progress = 0;

            @Override
            public void run() {
                progressBar.setProgress(progress);
                switch (progress) {
                    case 180 :
                        loadingText.setText(getString(R.string.app_loading_30));
                        progress++;
                        break;
                    case 360 :
                        loadingText.setText(getString(R.string.app_loading_60));
                        progress++;
                        break;
                    case 540 :
                        loadingText.setText(getString(R.string.app_loading_90));
                        progress++;
                        break;
                    case 599 :
                        while (isDataLoading) {
                            progress++;
                            break;
                        }
                        break;
                    case 600 :
                        loadingText.setText("데이터 로링 완료 :)");
                        timer.cancel();
                        moveToLogIn();
                        break;

                    default:
                        progress++;
                        break;
                }
            }
        }, 0, 10);
    }

    private String getLocationBasedList1(Location location) {
        OkHttpClient client = new OkHttpClient();
        String base_url = AppData.DATAGOKR_BASEURL;
        String parameter = "?";
        parameter += "numOfRows=200&pageNo=1&MobileOS=AND&MobileApp=TourLICA&_type=json&mapX="; //경도
        parameter += location.getLongitude();
        parameter += "&mapY="; //위도
        parameter += location.getLatitude();
        parameter += "&radius=20000&contentTypeId=12&serviceKey=";
        parameter += AppData.DATAGOKR_APIKEY;
        Request request = new Request.Builder()
                .url(base_url + "/locationBasedList1" + parameter)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response);
            if (response.isSuccessful()) {
                System.out.println("관광정보 excute!!!!!");
                isDataLoading = true;
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
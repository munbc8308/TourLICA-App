package com.application.tourlica;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.application.tourlica.databinding.FragmentFirstBinding;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Bundle bundle;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundle = getArguments();

        //ArrayList<String> location_list = bundle.getStringArrayList("location_list");
        //Log.d("location_list", location_list.toString());

        //Log.d("AAAAAAAA", "TEST");
        MapView mapView = binding.mapView;

        mapView.start(new MapLifeCycleCallback() {
            @Override
            public void onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
                Log.d("map", "destroy");
            }

            @Override
            public void onMapError(Exception error) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d("map", error.toString());
            }
        }, new KakaoMapReadyCallback() {
            @Override
            public void onMapReady(KakaoMap kakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                Log.d("map", kakaoMap.getViewName());
                Log.d("map", "success");
            }

            @Override
            public LatLng getPosition() {
                // 지도 시작 시 위치 좌표를 설정
                return LatLng.from(37.421998333333335, -122.084);
            }

            @Override
            public int getZoomLevel() {
                // 지도 시작 시 확대/축소 줌 레벨 설정
                return 15;
            }

            @Override
            public String getViewName() {
                // KakaoMap 의 고유한 이름을 설정
                return "MyFirstMap";
            }

            @Override
            public boolean isVisible() {
                // 지도 시작 시 visible 여부를 설정
                return true;
            }

            @Override
            public String getTag() {
                // KakaoMap 의 tag 을 설정
                return "FirstMapTag";
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("map", "destroy");
        binding = null;
    }

    @Override
    public void onResume() {
        MapView mapView = binding.mapView;
        super.onResume();
        mapView.resume();     // MapView 의 resume 호출
    }

    @Override
    public void onPause() {
        MapView mapView = binding.mapView;
        super.onPause();
        mapView.pause();    // MapView 의 pause 호출
    }





}
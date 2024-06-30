package com.application.tourlica.ui.log_in;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.tourlica.R;
import com.application.tourlica.databinding.FragmentMainBinding;
import com.google.android.gms.common.util.JsonUtils;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapGravity;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.label.CompetitionType;
import com.kakao.vectormap.label.CompetitionUnit;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelLayerOptions;
import com.kakao.vectormap.label.LabelManager;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.LabelStyles;
import com.kakao.vectormap.label.OrderingType;
import com.kakao.vectormap.mapwidget.MapWidgetOptions;
import com.kakao.vectormap.mapwidget.component.GuiImage;
import com.kakao.vectormap.mapwidget.component.GuiLayout;
import com.kakao.vectormap.mapwidget.component.GuiText;
import com.kakao.vectormap.mapwidget.component.Orientation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private MainViewModel mViewModel;

    private FragmentMainBinding binding;

    private String location_information;
    private String tour_information;

    private JSONObject tour_information_json;


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@NonNull
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location_information = (String) getArguments().get("LOCATION");
        tour_information = (String) getArguments().get("TOURDATA");
        Log.e("LoginFragment", tour_information);
        try {
            tour_information_json = new JSONObject(tour_information);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                try {
                    doDisplayLabels(kakaoMap);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public LatLng getPosition() {
                //System.out.println("position : " +  location_information);

                // 지도 시작 시 위치 좌표를 설정
                return LatLng.from(Double.valueOf(location_information.split("/")[0]), Double.valueOf(location_information.split("/")[1]));
            }

            @Override
            public int getZoomLevel() {
                // 지도 시작 시 확대/축소 줌 레벨 설정
                return 15;
            }

            @Override
            public String getViewName() {
                // KakaoMap 의 고유한 이름을 설정
                return "TourLICA_Map";
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

    private void doDisplayLabels(KakaoMap kakaoMap) throws JSONException {

        // LabelManager 를 가져오는 방법
        LabelManager labelManager = kakaoMap.getLabelManager();
        // 1. LabelManager 에 미리 생성 된 디폴트 LabelLayer 가져오는 방법
        //labelManager.getLayer();
        //labelManager.getLodLayer();

        // 2. 사용자 커스텀으로 LabelLayer 생성 및 가져오는 방법
        LabelLayer layer = labelManager.addLayer(LabelLayerOptions.from("TourLICA_Labels")
                .setOrderingType(OrderingType.Rank)
                .setCompetitionUnit(CompetitionUnit.IconAndText)
                .setCompetitionType(CompetitionType.All));

        //Log.d("json", tour_information_json.toString());
        JSONArray items = tour_information_json.getJSONObject("response")
                .getJSONObject("body")
                .getJSONObject("items")
                .getJSONArray("item");

        Log.d("items", items.toString());

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            LabelStyles styles = kakaoMap.getLabelManager()
                    .addLabelStyles(LabelStyles.from(LabelStyle.from().setTextStyles(40, Color.BLACK)));

            // 1. LabelStyles 생성하기 - Icon 이미지 하나만 있는 스타일
            //        .addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.label)));
            // 1. 텍스트만 있는 스타일 - 글자크기 20px, 글자색깔 검정색

            // 2. LabelOptions 생성하기
            LabelOptions options =
                    LabelOptions.from(LatLng.from(Double.valueOf((String) item.get("mapy")), Double.valueOf((String) item.get("mapx"))))
                            .setTexts((String)item.get("title"))
                            .setStyles(styles);
            // 4. LabelLayer 에 LabelOptions 을 넣어 Label 생성하기
            Label label = layer.addLabel(options);
            Log.d("item", item.toString());
        }


        // 2-1. 사용자 커스텀으로 LodLabelLayer 생성 및 가져오는 방법
        //LodLabelLayer lodLayer = labelManager.addLodLayer(LabelLayerOptions.from("myLayerId")
        //        .setOrderingType(OrderingType.Rank)
        //        .setCompetitionUnit(CompetitionUnit.IconAndText)
        //        .setCompetitionType(CompetitionType.All));
    }
}
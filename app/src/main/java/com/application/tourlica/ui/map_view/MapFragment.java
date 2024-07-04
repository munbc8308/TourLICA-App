package com.application.tourlica.ui.map_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.application.tourlica.databinding.FragmentMapBinding;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.LatLng;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapFragment extends Fragment {

    private FragmentMapBinding binding = null;
    private String location_information = "";
    private String tour_information = "";
    private JSONObject tour_information_json = null;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@NonNull
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location_information = (String) getArguments().get("LOCATION");
        tour_information = (String) getArguments().get("TOURDATA");
        //Log.e("LoginFragment", tour_information);
        try {
            tour_information_json = new JSONObject(tour_information);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
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
                //Log.d("map", kakaoMap.getViewName());
                //Log.d("map", "success");
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
            // 1. LabelStyles 생성하기 - Icon 이미지 하나만 있는 스타일
            //        .addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.label)));
            // 2. 텍스트만 있는 스타일 - 글자크기 20px, 글자색깔 검정색
            LabelStyles styles = kakaoMap.getLabelManager()
                    .addLabelStyles(LabelStyles.from(LabelStyle.from().setTextStyles(40, Color.BLACK)));
            // 3. LabelOptions 생성하기
            LabelOptions options =
                    LabelOptions.from(LatLng.from(Double.valueOf((String) item.get("mapy")), Double.valueOf((String) item.get("mapx"))))
                            .setTexts((String)item.get("title"))
                            .setStyles(styles);
            // 4. LabelLayer 에 LabelOptions 을 넣어 Label 생성하기
            Label label = layer.addLabel(options);

            if (!item.getString("firstimage").equals("")) {
                new Thread(() -> {
                    try {
                        Bitmap bitmap = getBitmapFromURL((item.getString("firstimage")));
                        LabelStyles image_styles = kakaoMap.getLabelManager()
                                .addLabelStyles(LabelStyles.from(LabelStyle.from(bitmap).setTextStyles(40, Color.BLACK)));
                        label.changeStyles(image_styles);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
            //Log.d("item", item.toString());
        }


        // 2-1. 사용자 커스텀으로 LodLabelLayer 생성 및 가져오는 방법
        //LodLabelLayer lodLayer = labelManager.addLodLayer(LabelLayerOptions.from("myLayerId")
        //        .setOrderingType(OrderingType.Rank)
        //        .setCompetitionUnit(CompetitionUnit.IconAndText)
        //        .setCompetitionType(CompetitionType.All));

    }

    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            Log.d("bitmapURL", src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return aspectRatioBitamp(myBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap aspectRatioBitamp(Bitmap source) {
        int targetWidth = 100;
        double aspectRatio = (double) source.getHeight() / (double) source.getWidth(); // 종횡비 계산
        int targetHeight = (int) (targetWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
        if (result != source) {
            source.recycle();
        }
        return result;
    }
}
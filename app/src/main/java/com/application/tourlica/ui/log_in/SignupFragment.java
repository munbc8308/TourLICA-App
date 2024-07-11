package com.application.tourlica.ui.log_in;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.application.tourlica.R;
import com.application.tourlica.data.UserData;
import com.application.tourlica.databinding.FragmentSignupBinding;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding = null;
    public static SignupFragment newInstance() {
        return new SignupFragment();
    }
    @Override
    public void onCreate(@NonNull
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Use the ViewModel

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private String sign_up(UserData userData) throws JSONException {
        final MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String base_url = "https://tourlica.shop";
        JSONObject jo = new JSONObject();
        jo.put("email", userData.getEmail());
        jo.put("password", userData.getPassword());
        jo.put("type", userData.getType());
        jo.put("birthday", userData.getBirthday());
        jo.put("name", userData.getName());
        jo.put("gender", userData.getGender());

        RequestBody body = RequestBody.create(jo.toString(), JSON);
        Request request = new Request.Builder()
                .url(base_url + "/api/sign-up")
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

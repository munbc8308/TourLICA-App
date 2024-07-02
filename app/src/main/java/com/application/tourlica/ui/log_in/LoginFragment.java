package com.application.tourlica.ui.log_in;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.application.tourlica.R;
import com.application.tourlica.databinding.FragmentLoginBinding;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private String location_information = "";
    private String tour_information = "";

    private String log_in_user_info = "";

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
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_sign_in = view.findViewById(R.id.btn_sign_in);
        Button btn_sign_up = view.findViewById(R.id.btn_sign_up);

        TextInputEditText email_text = view.findViewById(R.id.email_text);
        TextInputEditText pwd_text = view.findViewById(R.id.pwd_text);

        final String[] email = {""};
        final String[] pwd = {""};
        email_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("email", s.toString());
                email[0] = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pwd_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("pwd", s.toString());
                pwd[0] = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_sign_in.setOnClickListener(v -> {
            Log.d("not null?", email[0] + " / " + pwd[0]);
            new Thread(() -> {
                try {
                    log_in_user_info = sign_in(email[0], pwd[0]);
                    Log.d("USER_INFO", log_in_user_info);
                    JSONObject user = new JSONObject(log_in_user_info);
                    if (user.get("status").equals("SUCCESS")) {
                        Log.d("LOGIN", user.get("data").toString());
                    } else {
                        Log.d("LOGIN", "FAILED");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String sign_in(String email, String pwd) throws JSONException {
        final MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String base_url = "https://tourlica.shop";
        JSONObject jo = new JSONObject();
        jo.put("email", email);
        jo.put("password", pwd);

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
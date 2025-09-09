package com.example.address.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.address.BuildConfig;
import com.example.address.R;
import com.example.address.api.ApiClient;
import com.example.address.api.ApiService;
import com.example.address.model.LocationResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextInputEditText edtSearch;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSearch = findViewById(R.id.edtSearch);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new LocationAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "RecyclerView: " + recyclerView);
        Log.d(TAG, "Adapter before set: " + adapter);

        // Khi click item -> mở Google Maps
        adapter.setOnItemClickListener(item ->
                openInGoogleMaps(item.getLat(), item.getLon(), item.getDisplay_name())
        );

        // Người dùng nhập xong bấm Search -> gọi API
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String q = edtSearch.getText() != null ? edtSearch.getText().toString().trim() : "";
                if (!q.isEmpty()) {
                    searchLocation(q);
                }
                return true;
            }
            return false;
        });
    }

    private void openInGoogleMaps(String lat, String lon, String name) {
        try {
            if (lat == null || lon == null || lat.isEmpty() || lon.isEmpty()) {
                Log.e(TAG, "Invalid coordinates: lat=" + lat + ", lon=" + lon);
                return;
            }

            // Nếu có tên thì thêm, nếu không thì chỉ dùng lat, lon
            String label = (name != null && !name.isEmpty()) ? Uri.encode(name) : "";
            Uri gmmIntentUri;
            if (!label.isEmpty()) {
                gmmIntentUri = Uri.parse("geo:" + lat + "," + lon + "?q=" + lat + "," + lon + "(" + label + ")");
            } else {
                gmmIntentUri = Uri.parse("geo:" + lat + "," + lon);
            }

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // fallback mở bằng browser
                String url = "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lon;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        } catch (Exception e) {
            Log.e(TAG, "Open maps failed: " + e.getMessage(), e);
        }
    }

    private void searchLocation(String query) {
        Log.d(TAG, "searchLocation query=" + query);
        Log.d(TAG, "Using API key=" + BuildConfig.LOCATIONIQ_KEY);

        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<List<LocationResponse>> call = api.searchLocation(
                BuildConfig.LOCATIONIQ_KEY,
                query,
                "json"
        );

        call.enqueue(new Callback<List<LocationResponse>>() {
            @Override
            public void onResponse(Call<List<LocationResponse>> call, Response<List<LocationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LocationResponse> list = response.body();
                    Log.d(TAG, "API response OK: " + new Gson().toJson(list));

                    // Cập nhật RecyclerView
                    adapter.setData(list);
                } else {
                    Log.e(TAG, "API response FAIL: code=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<LocationResponse>> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage(), t);
            }
        });
    }
}

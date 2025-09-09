package com.example.address.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.address.R;
import com.example.address.model.LocationResponse;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.VH> {

    private final List<LocationResponse> items = new ArrayList<>();
    private OnItemClickListener listener;

    // Interface click item
    public interface OnItemClickListener {
        void onItemClick(LocationResponse item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Cập nhật dữ liệu
    public void setData(List<LocationResponse> data) {
        items.clear();
        if (data != null && !data.isEmpty()) {
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        LocationResponse item = items.get(position);

        holder.txtTitle.setText(item.getDisplay_name());
        holder.txtSubtitle.setText("Lat: " + item.getLat() + " • Lon: " + item.getLon());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView txtTitle, txtSubtitle;

        VH(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtDisplayName);
            txtSubtitle = itemView.findViewById(R.id.txtLatLon);
        }
    }
}

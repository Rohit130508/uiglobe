package com.hotspot.user.app.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hotspot.user.app.R;

public class TaxiBooking extends AppCompatActivity {

    RecyclerView rvVehicleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_booking);

        initView();
    }

    void initView()
    {
        rvVehicleList = findViewById(R.id.rvVehicleList);
        rvVehicleList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        VehicleListAdapter adapter = new VehicleListAdapter();
        rvVehicleList.setAdapter(adapter);

    }



    class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder>
    {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(TaxiBooking.this).inflate(R.layout.item_vehicle_select,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 4;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}

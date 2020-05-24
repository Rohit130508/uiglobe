package com.hotspot.user.app.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hotspot.user.app.DashboardActivity;
import com.hotspot.user.app.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);



        final RecyclerView rvMainCat = root.findViewById(R.id.rvMainCat);
        rvMainCat.setLayoutManager(new GridLayoutManager(getActivity(),3));

        MainCatAdapter mainCatAdapter = new MainCatAdapter();
        rvMainCat.setAdapter(mainCatAdapter);


        final RecyclerView rvSlider = root.findViewById(R.id.rvSlider);
        rvSlider.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));

        SliderAdapter sliderAdapter = new SliderAdapter();
        rvSlider.setAdapter(sliderAdapter);


        return root;
    }

    class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder>
    {

        SliderAdapter() {
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_slider_home,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    class MainCatAdapter extends RecyclerView.Adapter<MainCatAdapter.ViewHolder>
    {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_slider_maincat,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.linClick.setOnClickListener(v -> startActivity(new Intent(getActivity(),TaxiBooking.class)));
        }

        @Override
        public int getItemCount() {
            return 9;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout linClick ;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                linClick = itemView.findViewById(R.id.linClick);
            }
        }
    }
}

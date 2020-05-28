package com.hotspot.user.app.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hotspot.user.app.DashboardActivity;
import com.hotspot.user.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    ArrayList<ModalMainCat>  arrayList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);



        final RecyclerView rvMainCat = root.findViewById(R.id.rvMainCat);
        rvMainCat.setLayoutManager(new GridLayoutManager(getActivity(),3));




        final RecyclerView rvSlider = root.findViewById(R.id.rvSlider);
        rvSlider.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));

        SliderAdapter sliderAdapter = new SliderAdapter();
        rvSlider.setAdapter(sliderAdapter);

        arrayList = new ArrayList<>();

        ModalMainCat cat = new ModalMainCat();
        cat.setName("RD");
        cat.setIcon(R.drawable.ic_ma);
        arrayList.add(cat);

        ModalMainCat cat2 = new ModalMainCat();
        cat.setName("DD");
        cat.setIcon(R.drawable.ic_ma);
        arrayList.add(cat2);

        ModalMainCat cat3 = new ModalMainCat();
        cat.setName("Saving");
        cat.setIcon(R.drawable.ic_ma);
        arrayList.add(cat3);

        ModalMainCat cat4 = new ModalMainCat();
        cat.setName("RD");
        cat.setIcon(R.drawable.ic_ma);
        arrayList.add(cat4);

        ModalMainCat cat5 = new ModalMainCat();
        cat.setName("DD");
        cat.setIcon(R.drawable.ic_ma);
        arrayList.add(cat5);

        ModalMainCat cat6 = new ModalMainCat();
        cat.setName("Saving");
        cat.setIcon(R.drawable.ic_ma);
        arrayList.add(cat6);

        MainCatAdapter mainCatAdapter = new MainCatAdapter(arrayList);
        rvMainCat.setAdapter(mainCatAdapter);

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

        ArrayList<ModalMainCat> arrayList;

        public MainCatAdapter(ArrayList<ModalMainCat> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_slider_maincat,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            ModalMainCat cat = arrayList.get(position);
//            holder.txtTitle.setText(cat.getName());
//            Picasso.with(getContext()).load(cat.getIcon()).into(holder.imgIcon);
//            holder.linClick.setOnClickListener(v -> startActivity(new Intent(getActivity(),TaxiBooking.class)));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout linClick ;
            ImageView imgIcon;
            TextView txtTitle;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                linClick = itemView.findViewById(R.id.linClick);
                imgIcon = itemView.findViewById(R.id.imgIcon);
                txtTitle = itemView.findViewById(R.id.txtTitle);
            }
        }
    }

    class ModalMainCat
    {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String name;
        public int icon;
    }
}

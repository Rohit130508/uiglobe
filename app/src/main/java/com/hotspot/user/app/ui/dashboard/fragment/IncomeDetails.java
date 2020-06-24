package com.hotspot.user.app.ui.dashboard.fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hotspot.user.app.R;
import com.hotspot.user.app.ui.dashboard.fragment.incomefrag.EarningDetails;
import com.hotspot.user.app.ui.dashboard.fragment.incomefrag.OwnBusiness;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CardView cvSalaried;
    private CardView cvSelf;
    private CardView cvNotEmployee;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IncomeDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeDetails newInstance(String param1, String param2) {
        IncomeDetails fragment = new IncomeDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_income_details, container, false);

        cvSalaried = view.findViewById(R.id.cvSalaried);
        cvSelf = view.findViewById(R.id.cvSelf);
        cvNotEmployee = view.findViewById(R.id.cvNotEmployee);

        cvSalaried.setOnClickListener(v -> getSalaried());
        cvSelf.setOnClickListener(v -> getSelf());
        cvNotEmployee.setOnClickListener(v -> getNotEmployee());

        return view;
    }

    void getSalaried()
    {
        cvSelf.setVisibility(View.GONE);
        cvNotEmployee.setVisibility(View.GONE);
        loadFragment(new EarningDetails());

    }

    void getSelf()
    {
        cvSalaried.setVisibility(View.GONE);
        cvNotEmployee.setVisibility(View.GONE);
        loadFragment(new OwnBusiness());
    }
    void getNotEmployee()
    {
        cvSalaried.setVisibility(View.GONE);
        cvSelf.setVisibility(View.GONE);
    }

    void loadFragment(Fragment fragment)
    {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameIncome, fragment);
        transaction.commit();

    }
}
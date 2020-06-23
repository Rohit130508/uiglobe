package com.hotspot.user.app.ui.dashboard.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hotspot.user.app.R;
import com.hotspot.user.app.utils.CustomPerference;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasicDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText edt_mobNumber, first_name, edtDOB, edtCity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Animation shake;
    private TextView txtFemale, txtmale;
    private String gender;

    public BasicDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicDetail newInstance(String param1, String param2) {
        BasicDetail fragment = new BasicDetail();
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

        View view = inflater.inflate(R.layout.fragment_basic_detail, container, false);
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        initView(view);




        return view;
    }

    void initView(View view)
    {
        txtmale = view.findViewById(R.id.txtmale);
        txtFemale = view.findViewById(R.id.txtFemale);


        first_name = view.findViewById(R.id.first_name);
        first_name.startAnimation(shake);

        edtDOB = view.findViewById(R.id.edtDOB);
        edtDOB.startAnimation(shake);

        edtCity = view.findViewById(R.id.edtCity);
        edtCity.startAnimation(shake);

        edt_mobNumber = view.findViewById(R.id.edt_mobNumber);
        edt_mobNumber.startAnimation(shake);
        edt_mobNumber.setText(CustomPerference.getString(getActivity(),CustomPerference.USER_ID));
        edt_mobNumber.setFocusable(false);

        view.findViewById(R.id.submit).setOnClickListener(v -> executeBasicDetails());


        view.findViewById(R.id.txtmale).setOnClickListener(v -> {
            gender = "Male";
            view.findViewById(R.id.txtmale).setBackgroundColor(Color.parseColor("#28B3DC"));
            view.findViewById(R.id.txtFemale).setBackgroundColor(Color.parseColor("#FFFFFF"));


        });
        view.findViewById(R.id.txtFemale).setOnClickListener(v ->
        {
            gender = "Female";
            view.findViewById(R.id.txtmale).setBackgroundColor(Color.parseColor("#FFFFFF"));
            view.findViewById(R.id.txtFemale).setBackgroundColor(Color.parseColor("#28B3DC"));
        });
    }

    void executeBasicDetails()
    {
        String name = first_name.getText().toString().trim();
        String dob = edtDOB.getText().toString().trim();
        String city = edtCity.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(dob)
                && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(gender))
        {
            loadFragment(new IdAddress());
        }
        else
        {
            loadFragment(new IdAddress());
            Toast.makeText(getActivity(),"All feilds are required",Toast.LENGTH_LONG).show();
        }
    }

    void loadFragment(Fragment fragment)
    {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
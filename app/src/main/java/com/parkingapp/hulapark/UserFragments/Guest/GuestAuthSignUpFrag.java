package com.parkingapp.hulapark.UserFragments.Guest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.parkingapp.hulapark.Activities.AdminScreen;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.DataBase.DBManager;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;
import com.parkingapp.hulapark.Utilities.Users.UserType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestAuthSignUpFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestAuthSignUpFrag extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GuestAuthSignUpFrag()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuestAuthSignUpFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestAuthSignUpFrag newInstance(String param1, String param2)
    {
        GuestAuthSignUpFrag fragment = new GuestAuthSignUpFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_guest_auth_signup, container, false);

        MaterialButton connectBtn = (MaterialButton)view.findViewById(R.id.auth_connect_button);
        TextInputEditText email = (TextInputEditText) view.findViewById(R.id.auth_email_field);
        TextInputEditText password = (TextInputEditText) view.findViewById(R.id.auth_password_field);

        ProgressBar authProgressBar = (ProgressBar) view.findViewById(R.id.authProgressBar);
        TextView authProgrssText = (TextView) view.findViewById(R.id.authProgressText);

        // Hide them until auth.
        authProgressBar.setVisibility(View.GONE);
        authProgrssText.setVisibility(View.GONE);

        connectBtn.setText(R.string.singUp);

        connectBtn.setOnClickListener(__ ->
        {

            // Show authenticating...
            authProgressBar.setVisibility(View.VISIBLE);
            authProgrssText.setVisibility(View.VISIBLE);

            DBManager.createNewUserFromCredentials("xridoses@gmail.com", "rgeSW445F$vsa5", e ->
            {
                if(e == null) // No exception, all good!xridoses@gmail.com
                {
                    Toast.makeText(getContext(), "You succesfully created and account!", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    Activity activity = getActivity();

                    resultIntent.putExtra("shouldNavigate", true);
                    activity.setResult(Activity.RESULT_OK, resultIntent);
                    activity.finish();
                } else {
                    Toast.makeText(getContext(), e + "", Toast.LENGTH_SHORT).show();
                }
                authProgressBar.setVisibility(View.GONE);
                authProgrssText.setVisibility(View.GONE);
            });
        });

        return view;
    }
}

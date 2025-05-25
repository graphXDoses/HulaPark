package com.parkingapp.hulapark.UserFragments.Guest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.parkingapp.hulapark.R;
import com.parkingapp.hulapark.Users.Guest;
import com.parkingapp.hulapark.Users.User;
import com.parkingapp.hulapark.Utilities.DBManager;
import com.parkingapp.hulapark.Utilities.Frags.CommonFragUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestAuthSignInFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestAuthSignInFrag extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GuestAuthSignInFrag()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuthSignInFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestAuthSignInFrag newInstance(String param1, String param2)
    {
        GuestAuthSignInFrag fragment = new GuestAuthSignInFrag();
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
        View view = inflater.inflate(R.layout.frag_guest_auth_signin, container, false);

        MaterialButton connectBtn = (MaterialButton)view.findViewById(R.id.auth_connect_button);

        connectBtn.setText(R.string.singIn);

        connectBtn.setOnClickListener(view1 -> {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            if(isChecked)
//            {
//                CommonFragUtils.FragmentSwapper.changeUserTo(new User());
//                editor.putBoolean(IS_TOGGLE, true);
//            } else {
//                CommonFragUtils.FragmentSwapper.changeUserTo(new Guest());
//                editor.putBoolean(IS_TOGGLE, false);
//            }
//            editor.commit();
            CommonFragUtils.FragmentSwapper.changeUserTo(new User());
            Activity activity = getActivity();

            DBManager.authenticateUserCredentials("example@somemail.com", "erSdsvSCD$#", new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        String uid = DBManager.getCurrentUserAuthSertificate().getUid();
                        Toast.makeText(getContext(), uid + " signed in!", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("shouldNavigate", true);
                        activity.setResult(Activity.RESULT_OK, resultIntent);
                        activity.finish();
                    } else {
                        Toast.makeText(getContext(), task.getException() + "", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });

        return view;
    }
}
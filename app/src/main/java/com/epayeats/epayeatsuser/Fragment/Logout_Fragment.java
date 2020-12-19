package com.epayeats.epayeatsuser.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epayeats.epayeatsuser.Activity.Login_Activity;
import com.epayeats.epayeatsuser.Activity.Permission_Activity;
import com.epayeats.epayeatsuser.Database.MobUser;
import com.epayeats.epayeatsuser.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.raizlabs.android.dbflow.sql.language.Delete;


public class Logout_Fragment extends Fragment
{
    SharedPreferences sharedPreferences;
    public ProgressDialog progressDialog;

    public Logout_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout_, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Signing Out...");

        sharedPreferences = getActivity().getSharedPreferences("data",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Delete.table(MobUser.class);

        progressDialog.show();

        FirebaseAuth.getInstance().signOut();

        GoogleSignIn.getClient(
                getContext(),
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut();


        Intent intent = new Intent(getContext(), Permission_Activity.class);
        startActivity(intent);
        getActivity().finish();

        progressDialog.dismiss();


        return view;
    }
}
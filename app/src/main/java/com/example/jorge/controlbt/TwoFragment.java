package com.example.jorge.controlbt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class TwoFragment extends Fragment {

    private String Probe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View PageTwo = inflater.inflate(R.layout.fragment_two, container, false);

        if (getArguments() != null) {
            Probe = this.getArguments().getString("PRUEBA");
        }

        //Toast.makeText(getContext(), "El string es: " + Probe, Toast.LENGTH_SHORT).show();

        return PageTwo;
    }
}
package com.example.jorge.controlbt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class OneFragment extends Fragment {

    private int Dato;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View PageOne = inflater.inflate(R.layout.fragment_one, container, false);

        if (getArguments() != null) {
            Dato = this.getArguments().getInt(UserInterfaz.DATO_KEY);
        }

        Toast.makeText(getContext(), "El string es: " + Dato, Toast.LENGTH_SHORT).show();

        return PageOne;
    }
}

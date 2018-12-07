package com.example.jorge.controlbt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class OneFragment extends Fragment {

    private String Dato;
    private int cost;

    TextView watts;
    TextView tv1, tv2, tv3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View PageOne = inflater.inflate(R.layout.fragment_one, container, false);

        watts = (TextView) PageOne.findViewById(R.id.IdBufferIn);
        tv1 = (TextView) PageOne.findViewById(R.id.txt_precio_watt);
        tv2 = (TextView) PageOne.findViewById(R.id.textView2);
        tv3 = (TextView) PageOne.findViewById(R.id.textView7);

        if (getArguments() != null) {
            Dato = this.getArguments().getString(UserInterfaz.DATO_KEY);
            cost = this.getArguments().getInt(UserInterfaz.DATO_KEY);
        }

        Toast.makeText(getContext(), "El string es: " + Dato, Toast.LENGTH_SHORT).show();
        tv3.setText("$ " + cost);

        return PageOne;
    }
}

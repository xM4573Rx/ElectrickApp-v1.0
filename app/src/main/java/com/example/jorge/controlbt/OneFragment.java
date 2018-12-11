package com.example.jorge.controlbt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OneFragment extends Fragment {

    private String Cost;
    private String Actualwatt;
    private String Watts;

    TextView watts;
    TextView tv1, tv2, tv3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View PageOne = inflater.inflate(R.layout.fragment_one, container, false);

        tv1 = (TextView) PageOne.findViewById(R.id.IdBufferIn);
        tv2 = (TextView) PageOne.findViewById(R.id.textView2);
        tv3 = (TextView) PageOne.findViewById(R.id.textView7);

        if (getArguments() != null) {
            Watts = this.getArguments().getString(UserInterfaz.DATO_KEY);
            Cost = this.getArguments().getString(UserInterfaz.DATO_KEY1);
            Actualwatt = this.getArguments().getString(UserInterfaz.DATO_KEY2);
        }

        tv1.setText(Watts);
        tv2.setText(Actualwatt + " KWh");
        tv3.setText("$ " + Cost);

        return PageOne;
    }
}

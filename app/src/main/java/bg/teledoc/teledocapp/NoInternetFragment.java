package bg.teledoc.teledocapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class NoInternetFragment extends BaseFragment {

    public NoInternetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_no_internet, container, false);
        Button bOK = (Button)v.findViewById(R.id.bOK);
        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GetMain().IsNetworkAvailable(GetMain()))
                    GetMain().gotoFragment(new LoginFragment());
                else
                    GetMain().gotoFragment(new NoInternetFragment());
            }
        });

        Button bCancel = (Button)v.findViewById(R.id.bCancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(1);

            }
        });
        return v;
    }

}

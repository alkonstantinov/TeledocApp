package bg.teledoc.teledocapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationSuccessFragment extends BaseFragment {


    public RegistrationSuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registration_success, container, false);
        v.findViewById(R.id.bToLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetMain().gotoFragment(new LoginFragment());
            }
        });
        return v;
    }

}

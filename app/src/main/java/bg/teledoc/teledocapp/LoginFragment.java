package bg.teledoc.teledocapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button bLogin = (Button) v.findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Requests.Login(
                        ((EditText) v.findViewById(R.id.tbUserName)).getText().toString(),
                        ((EditText) v.findViewById(R.id.tbPassword)).getText().toString(),
                        getContext(),
                        new ServerAPICallback() {
                            @Override
                            public void onResult(String result) {
                                JSONObject res = null;
                                try {
                                    res = new JSONObject(result);
                                    if (res.get("LevelId").toString().equals("-1"))
                                        Toast.makeText(getContext(),R.string.invalidusername,Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(getContext(),"ОК",Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onError(Object error) {

                            }
                        }
                );
            }
        });

        return v;
    }


}

package bg.teledoc.teledocapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bg.teledoc.teledocapp.Adapters.IssuesByExpertAdapter;
import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class ChangePasswordFragment extends BaseFragment {
    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =  inflater.inflate(R.layout.fragment_change_password, container, false);
        v.findViewById(R.id.bSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.findViewById(R.id.lError).setVisibility(View.INVISIBLE);
                v.findViewById(R.id.lSuccess).setVisibility(View.INVISIBLE);
                EditText tb =(EditText)v.findViewById(R.id.tbPass);
                if (tb.getText().toString().equals("")){
                    v.findViewById(R.id.lError).setVisibility(View.VISIBLE);
                    return;
                }

                Requests.ChangePass (GetMain().getSessionId(), tb.getText().toString(), getContext(), new ServerAPICallback() {
                    @Override
                    public void onResult(String result) {
                        v.findViewById(R.id.lSuccess).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });



            }
        });
        return v;
    }

}

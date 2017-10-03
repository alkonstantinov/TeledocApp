package bg.teledoc.teledocapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import bg.teledoc.teledocapp.Adapters.IssuesClosedAdapter;
import bg.teledoc.teledocapp.Adapters.IssuesNotClosedAdapter;
import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class PatientMainFragment extends BaseFragment {

    Timer timer;

    public PatientMainFragment() {
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
        final View v = inflater.inflate(R.layout.fragment_patient_main, container, false);
        Requests.IssuesNotClosed(GetMain().getSessionId(), getContext(), new ServerAPICallback() {
            @Override
            public void onResult(String result) {
                try {
                    JSONArray jarr = new JSONArray(result);
                    ArrayList<JSONObject> arr = new ArrayList<JSONObject>();

                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject obj = (JSONObject) jarr.get(i);
                        arr.add(obj);
                    }
                    IssuesNotClosedAdapter adapter = new IssuesNotClosedAdapter(getContext(), arr);
                    ListView listView = (ListView) v.findViewById(R.id.lvOpenIssues);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object error) {

            }
        });

        Requests.IssuesClosed(GetMain().getSessionId(), getContext(), new ServerAPICallback() {
            @Override
            public void onResult(String result) {
                try {
                    JSONArray jarr = new JSONArray(result);
                    ArrayList<JSONObject> arr = new ArrayList<JSONObject>();

                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject obj = (JSONObject) jarr.get(i);
                        arr.add(obj);
                    }
                    IssuesClosedAdapter adapter = new IssuesClosedAdapter(getContext(), arr);
                    ListView listView = (ListView) v.findViewById(R.id.lvClosedIssues);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object error) {

            }
        });

        Button bNewIssue = (Button) v.findViewById(R.id.bNewIssue);
        bNewIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetMain().gotoFragment(new IssueTargetFragment());
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity main = GetMain();
                if (main == null)
                    return;
                main.gotoFragmentFast(new PatientMainFragment());
            }
        }, 5000);

        return v;
    }


}

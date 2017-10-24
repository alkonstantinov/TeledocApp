package bg.teledoc.teledocapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import bg.teledoc.teledocapp.Adapters.IssuesByExpertAdapter;
import bg.teledoc.teledocapp.Adapters.IssuesClosedAdapter;
import bg.teledoc.teledocapp.Adapters.IssuesNotClosedAdapter;
import bg.teledoc.teledocapp.Adapters.IssuesTakenAdapter;
import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class ExpertMainFragment extends BaseFragment {

    public ExpertMainFragment() {
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
        final View v = inflater.inflate(R.layout.fragment_expert_main, container, false);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                PopulateListViews();
            }
        }, 0, 5000);

        return v;
    }

    private void PopulateListViews() {
        final View v = getView();
        Requests.IssuesByExpert(GetMain().getSessionId(), getContext(), new ServerAPICallback() {
            @Override
            public void onResult(String result) {
                try {
                    JSONArray jarr = new JSONArray(result);
                    ArrayList<JSONObject> arr = new ArrayList<JSONObject>();

                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject obj = (JSONObject) jarr.get(i);
                        arr.add(obj);
                    }
                    IssuesByExpertAdapter adapter = new IssuesByExpertAdapter(getContext(), arr);
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

//

        Requests.IssuesTaken(GetMain().getSessionId(), getContext(), new ServerAPICallback() {
            @Override
            public void onResult(String result) {
                try {
                    JSONArray jarr = new JSONArray(result);
                    ArrayList<JSONObject> arr = new ArrayList<JSONObject>();

                    for (int i = 0; i < jarr.length(); i++) {
                        JSONObject obj = (JSONObject) jarr.get(i);
                        arr.add(obj);
                    }
                    IssuesTakenAdapter adapter = new IssuesTakenAdapter(getContext(), arr);
                    ListView listView = (ListView) v.findViewById(R.id.lvTakenIssues);
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
    }


}

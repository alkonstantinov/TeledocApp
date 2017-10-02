package bg.teledoc.teledocapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class IssueSinceFragment extends BaseFragment {

    private RadioGroup rgSince;

    private Map<Integer, RadioButton> dCB = new HashMap<Integer, RadioButton>();

    public IssueSinceFragment() {
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
        final View v = inflater.inflate(R.layout.fragment_issue_since, container, false);
        rgSince = (RadioGroup) v.findViewById(R.id.rgSince);

        Requests.SinceGet(GetMain().getSessionId(), getContext(), new ServerAPICallback() {
            @Override
            public void onResult(String result) {
                try {
                    JSONArray jaSinces = new JSONArray(result);
                    DrawSinces(jaSinces);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Object error) {

            }
        });


        ImageButton bNext = (ImageButton) v.findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Next();
            }
        });

        ImageButton bPrev = (ImageButton) v.findViewById(R.id.bPrev);
        bPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prev();
            }
        });

        return v;
    }

    private void DrawSinces(JSONArray jaSinces) {

        for (int i = 0; i < jaSinces.length(); i++) {
            JSONObject obj = null;
            try {
                obj = (JSONObject) jaSinces.get(i);
                RadioButton rbSince = new RadioButton(getContext());
                rgSince.addView(rbSince);
                dCB.put(obj.getInt("sinceid"), rbSince);
                rbSince.setText(obj.getString("sincename"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject issue = GetMain().getIssue();
        if (issue.has("sinceid")) {
            try {
                int id = issue.getInt("sinceid");
                ((RadioButton) dCB.get(id)).setChecked(true);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            ((RadioButton) dCB.get(1)).setChecked(true);

        }

    }

    private void Save() {
        JSONArray jaArr = new JSONArray();
        int idx = 0;
        for (int key : dCB.keySet())
            if (((RadioButton) dCB.get(key)).isChecked()) {

                try {
                    GetMain().getIssue().put("sinceid", key);
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                idx++;
            }

        try {
            MainActivity.getMain().getIssue().put("symptom", jaArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void Prev() {


//        try {
//            final JSONObject issue = GetMain().getIssue();
//            issue.put("description",tbDescription.getText());
//
//        } catch (JSONException ex) {
//        }
        Save();
        this.GetMain().gotoFragment(new IssueSymptomsFragment());
    }


    private void Next() {
        Save();
//        try {
//            final JSONObject issue = GetMain().getIssue();
//            issue.put("description",tbDescription.getText());
//
//        } catch (JSONException ex) {
//        }

        this.GetMain().gotoFragment(new IssueChronicsFragment());
    }

}

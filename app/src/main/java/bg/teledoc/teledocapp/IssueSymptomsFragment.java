package bg.teledoc.teledocapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class IssueSymptomsFragment extends BaseFragment {

    private LinearLayout liSymptoms;

    private Map<Integer, CheckBox> dCB = new HashMap<Integer, CheckBox>();

    public IssueSymptomsFragment() {
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
        final View v = inflater.inflate(R.layout.fragment_issue_symptoms, container, false);
        liSymptoms = (LinearLayout) v.findViewById(R.id.liSymptoms);

        Requests.SymptomsGet(GetMain().getSessionId(), getContext(), new ServerAPICallback() {
            @Override
            public void onResult(String result) {
                try {
                    JSONArray jaSymptoms = new JSONArray(result);
                    DrawSymptoms(jaSymptoms);
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

    private void DrawSymptoms(JSONArray jaSymptoms) {

        for (int i = 0; i < jaSymptoms.length(); i++) {
            JSONObject obj = null;
            try {
                obj = (JSONObject) jaSymptoms.get(i);
                TextView tvTitle = new TextView(getContext());
                tvTitle.setText(obj.getString("Name"));
                liSymptoms.addView(tvTitle);
                LinearLayout liContent = new LinearLayout(getContext());
                liContent.setOrientation(LinearLayout.VERTICAL);
                liSymptoms.addView(liContent);
                JSONArray jaArr = obj.getJSONArray("Symptoms");
                for (int j = 0; j < jaArr.length(); j++) {
                    JSONObject joSymptom = (JSONObject) jaArr.get(j);
                    CheckBox cbSymptom = new CheckBox(getContext());
                    liContent.addView(cbSymptom);
                    dCB.put(joSymptom.getInt("Id"), cbSymptom);
                    cbSymptom.setText(joSymptom.getString("Name"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject issue = GetMain().getIssue();
        if (issue.has("symptom")) {
            try {
                JSONArray jArr = issue.getJSONArray("symptom");
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject obj = null;
                    obj = (JSONObject) jArr.get(i);
                    ((CheckBox) dCB.get(obj.getInt("symptomid"))).setChecked(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void Save() {
        JSONArray jaArr = new JSONArray();
        int idx = 0;
        for (int key : dCB.keySet())
            if (((CheckBox) dCB.get(key)).isChecked()) {

                try {
                    JSONObject jobj = new JSONObject();
                    jobj.put("symptomid", key);
                    jaArr.put(idx, jobj);
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
        this.GetMain().gotoFragment(new IssueSexYearsFragment());
    }


    private void Next() {
        Save();
//        try {
//            final JSONObject issue = GetMain().getIssue();
//            issue.put("description",tbDescription.getText());
//
//        } catch (JSONException ex) {
//        }

        this.GetMain().gotoFragment(new IssueSinceFragment());
    }

}

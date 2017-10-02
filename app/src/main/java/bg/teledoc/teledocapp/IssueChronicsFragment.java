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
import java.util.HashMap;
import java.util.Map;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class IssueChronicsFragment extends BaseFragment {

    private LinearLayout liChronics;
    private LinearLayout liOthers;

    private Map<Integer, CheckBox> dCB = new HashMap<Integer, CheckBox>();

    public IssueChronicsFragment() {
        // Required empty public constructor
    }


    private ArrayList<EditText> alOthers = new ArrayList<EditText>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_issue_chronics, container, false);
        liChronics = (LinearLayout) v.findViewById(R.id.liChronics);
        liOthers = (LinearLayout) v.findViewById(R.id.liOthers);
        Requests.ChronicsGet(GetMain().getSessionId(), getContext(), new ServerAPICallback() {
            @Override
            public void onResult(String result) {
                try {
                    JSONArray jaChronics = new JSONArray(result);
                    DrawChronics(jaChronics);
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

        ImageButton bAddOther = (ImageButton) v.findViewById(R.id.bAddOther);
        bAddOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddOther("");
            }
        });

        return v;
    }

    private void DrawChronics(JSONArray jaChronics) {

        for (int i = 0; i < jaChronics.length(); i++) {
            JSONObject obj = null;
            try {
                JSONObject joSymptom = (JSONObject) jaChronics.get(i);
                CheckBox cbChronic = new CheckBox(getContext());
                liChronics.addView(cbChronic);
                dCB.put(joSymptom.getInt("chronicid"), cbChronic);
                cbChronic.setText(joSymptom.getString("chronicname"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject issue = GetMain().getIssue();
        if (issue.has("chronic")) {
            try {
                JSONArray jArr = issue.getJSONArray("chronic");
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject obj = null;
                    obj = (JSONObject) jArr.get(i);
                    if (obj.getInt("chronicid") != -1)
                        ((CheckBox) dCB.get(obj.getInt("chronicid"))).setChecked(true);
                    else
                        AddOther(obj.getString("chronicfree"));
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
                    jobj.put("chronicid", key);
                    jaArr.put(idx, jobj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                idx++;
            }

        for (EditText et : alOthers)
            if (!et.getText().toString().equals("")) {

                try {
                    JSONObject jobj = new JSONObject();
                    jobj.put("chronicid", -1);
                    jobj.put("chronicfree", et.getText());
                    jaArr.put(idx, jobj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                idx++;
            }


        try {
            MainActivity.getMain().getIssue().put("chronic", jaArr);
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
        this.GetMain().gotoFragment(new IssueSinceFragment());
    }


    private void Next() {
        Save();
//        try {
//            final JSONObject issue = GetMain().getIssue();
//            issue.put("description",tbDescription.getText());
//
//        } catch (JSONException ex) {
//        }

        this.GetMain().gotoFragment(new IssueAllergiesFragment());
    }

    private void AddOther(String text) {

        EditText tbOther = new EditText(getContext());
        tbOther.setHint(getResources().getString(R.string.enterchronic));
        tbOther.setText(text);
        liOthers.addView(tbOther);
        alOthers.add(tbOther);
        tbOther.requestFocus();


    }

}

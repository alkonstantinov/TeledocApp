package bg.teledoc.teledocapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class IssueAllergiesFragment extends BaseFragment {

    private LinearLayout liOthers;


    public IssueAllergiesFragment() {
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
        liOthers = (LinearLayout) v.findViewById(R.id.liOthers);


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

        JSONObject issue = GetMain().getIssue();
        if (issue.has("allergy")) {
            try {
                JSONArray jArr = issue.getJSONArray("allergy");
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject obj = null;
                    obj = (JSONObject) jArr.get(i);

                    AddOther(obj.getString("allergy"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return v;
    }


    private void Save() {
        JSONArray jaArr = new JSONArray();
        int idx = 0;
        for (EditText et : alOthers)
            if (!et.getText().toString().equals("")) {

                try {
                    JSONObject jobj = new JSONObject();
                    jobj.put("allergy", et.getText());
                    jaArr.put(idx, jobj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                idx++;
            }


        try {
            MainActivity.getMain().getIssue().put("allergy", jaArr);
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
        this.GetMain().gotoFragment(new IssueChronicsFragment());
    }


    private void Next() {
        Save();
//        try {
//            final JSONObject issue = GetMain().getIssue();
//            issue.put("description",tbDescription.getText());
//
//        } catch (JSONException ex) {
//        }

        //this.GetMain().gotoFragment(new IssueSinceFragment());
    }

    private void AddOther(String text) {

        EditText tbOther = new EditText(getContext());
        tbOther.setHint(getResources().getString(R.string.enterallergy));
        tbOther.setText(text);
        liOthers.addView(tbOther);
        alOthers.add(tbOther);
        tbOther.requestFocus();


    }

}

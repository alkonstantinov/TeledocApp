package bg.teledoc.teledocapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class IssueMedicinesFragment extends BaseFragment {

    private LinearLayout liOthers;

    private Map<String, Integer> mSinces = new HashMap<String, Integer>();
    private Map<Integer, String> mSinces2 = new HashMap<Integer, String>();

    private List<String> lSinces = new ArrayList<String>();

    public IssueMedicinesFragment() {
        // Required empty public constructor
    }


    private ArrayList<EditText> alOthers = new ArrayList<EditText>();
    private ArrayList<Spinner> alSinces = new ArrayList<Spinner>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_issue_medicines, container, false);
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
                AddOther("", mSinces.get(lSinces.get(0)));
            }
        });

        Requests.SinceGet(GetMain().getSessionId(), getContext(), new ServerAPICallback() {
            @Override
            public void onResult(String result) {
                try {
                    JSONArray jaSinces = new JSONArray(result);
                    for (int i = 0; i < jaSinces.length(); i++) {
                        JSONObject obj = null;
                        try {
                            obj = (JSONObject) jaSinces.get(i);
                            mSinces.put(obj.getString("sincename"), obj.getInt("sinceid"));
                            mSinces2.put(obj.getInt("sinceid"), obj.getString("sincename"));
                            lSinces.add(obj.getString("sincename"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    JSONObject issue = GetMain().getIssue();
                    if (issue.has("medication")) {
                        JSONArray jArr = issue.getJSONArray("medication");
                        for (int i = 0; i < jArr.length(); i++) {
                            JSONObject obj = null;
                            obj = (JSONObject) jArr.get(i);

                            AddOther(obj.getString("medication"), obj.getInt("sinceid"));

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Object error) {

            }
        });


        return v;
    }


    private void Save() {
        JSONArray jaArr = new JSONArray();
        int idx = 0;
        for (int i = 0; i < alOthers.size(); i++) {
            EditText et = alOthers.get(i);
            if (!et.getText().toString().equals("")) {

                try {
                    JSONObject jobj = new JSONObject();
                    jobj.put("medication", et.getText());
                    jobj.put("sinceid", mSinces.get(alSinces.get(i).getSelectedItem()));
                    jaArr.put(idx, jobj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                idx++;
            }

        }
        try {
            MainActivity.getMain().getIssue().put("medication", jaArr);
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
        this.GetMain().gotoFragment(new IssueAllergiesFragment());
    }


    private void Next() {
        Save();
//        try {
//            final JSONObject issue = GetMain().getIssue();
//            issue.put("description",tbDescription.getText());
//
//        } catch (JSONException ex) {
//        }

        this.GetMain().gotoFragment(new IssueAnswerTypeFragment());
    }

    private void AddOther(String text, int sinceId) {
        LinearLayout liRow = new LinearLayout(getContext());
        liRow.setOrientation(LinearLayout.HORIZONTAL);

        EditText tbOther = new EditText(getContext());
        tbOther.setHint(getResources().getString(R.string.entermedication));
//        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                0.7f
//        );
//        tbOther.setLayoutParams(param);
        tbOther.setText(text);
        liOthers.addView(liRow);
        liRow.addView(tbOther);
        alOthers.add(tbOther);
        tbOther.requestFocus();
        Spinner ddlSince = new Spinner(getContext());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lSinces);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        ddlSince.setAdapter(dataAdapter);
        String selText = mSinces2.get(sinceId);
        ddlSince.setSelection(lSinces.indexOf(selText));
        liRow.addView(ddlSince);
        alSinces.add(ddlSince);

    }

}

package bg.teledoc.teledocapp;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bg.teledoc.teledocapp.Adapters.IssuesClosedAdapter;
import bg.teledoc.teledocapp.Adapters.IssuesNotClosedAdapter;
import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class IssueTargetFragment extends BaseFragment {

    private RadioButton rbMe;
    private RadioButton rbMyChild;
    private RadioButton rbDoctor;
    private RadioButton rbPharmacist;

    public IssueTargetFragment() {
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
        final View v = inflater.inflate(R.layout.fragment_issue_target, container, false);
        rbMe = (RadioButton) v.findViewById(R.id.rbMe);
        rbMyChild = (RadioButton) v.findViewById(R.id.rbMyChild);
        rbDoctor = (RadioButton) v.findViewById(R.id.rbDoctor);
        rbPharmacist = (RadioButton) v.findViewById(R.id.rbPharmacist);

        try {
            JSONObject issue = GetMain().getIssue();
            if (issue.has("whoid")) {
                if (issue.getInt("whoid") == 1)
                    rbMe.setChecked(true);
                else
                    rbMyChild.setChecked(true);
            }
            if (issue.has("reqexpertlevelid")) {
                if (issue.getInt("reqexpertlevelid") == 1)
                    rbDoctor.setChecked(true);
                else
                    rbPharmacist.setChecked(true);
            }
        } catch (JSONException ex) {
        }

        ImageButton bNext = (ImageButton)v.findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Next();
            }
        });
        return v;
    }

    private void Next() {
        try {
            final JSONObject issue = GetMain().getIssue();
            if (rbMe.isChecked())
                issue.put("whoid", 1);
            else
                issue.put("whoid", 2);
            if (rbDoctor.isChecked())
                issue.put("reqexpertlevelid", 1);
            else
                issue.put("reqexpertlevelid", 2);

            Requests.IssueLastGet(GetMain().getSessionId(), issue.getInt("whoid"), getContext(), new ServerAPICallback() {
                @Override
                public void onResult(String result) {
                    try {

                        if (!result.equals("")) {
                            JSONObject oldIssue = new JSONObject(result);
                            issue.put("allergy",oldIssue.getJSONArray("allergies"));
                            issue.put("answertypeid",oldIssue.getInt("answertypeid"));

                            issue.put("additionalinfo",oldIssue.getString("additionalinfo"));
                            issue.put("sexid",oldIssue.getString("genderid"));
                            issue.put("birthmonth",oldIssue.getInt("birthmonth"));
                            issue.put("birthyear",oldIssue.getInt("birthyear"));
                            issue.put("chronic",oldIssue.getJSONArray("chronics"));

                        }
                        GetMain().gotoFragment(new IssueDescriptionFragment());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Object error) {

                }
            });

        } catch (JSONException ex) {
        }
    }

}

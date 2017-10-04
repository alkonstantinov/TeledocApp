package bg.teledoc.teledocapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class IssuePreviewFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IssueId_PARAM = "IssueId";

    // TODO: Rename and change types of parameters
    private int mIssueId;

    private int answertypeid;

    public IssuePreviewFragment() {
        // Required empty public constructor
    }

    public static IssuePreviewFragment newInstance(int issueId) {
        IssuePreviewFragment fragment = new IssuePreviewFragment();
        Bundle args = new Bundle();
        args.putInt(IssueId_PARAM, issueId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIssueId = getArguments().getInt(IssueId_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_issue_preview, container, false);

        Requests.IssueGet(GetMain().getSessionId(), mIssueId, getContext(), new ServerAPICallback() {
            @Override
            public void onResult(String result) {
                DisplayIssue(result);
            }

            @Override
            public void onError(Object error) {

            }
        });

        v.findViewById(R.id.bBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetMain().gotoFragment(new ExpertMainFragment());
            }
        });

        v.findViewById(R.id.bTake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Requests.TakeIssue(GetMain().getSessionId(), mIssueId, getContext(), new ServerAPICallback() {
                    @Override
                    public void onResult(String result) {

                        if (answertypeid==1)
                            GetMain().gotoFragment(new ExpertMainFragment());//chat
                        else
                            GetMain().gotoFragment(new ExpertMainFragment());

                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

            }
        });
        return v;
    }

    private void DisplayIssue(String json) {

        try {
            JSONObject issue = new JSONObject(json);
            this.answertypeid = issue.getInt("answertypeid");
            if (issue.getInt("issuestatusid")>1)
                getView().findViewById(R.id.bTake).setVisibility(View.INVISIBLE);

            ((TextView) this.getView().findViewById(R.id.lAnswerType)).setText(issue.getString("answertypename") + (issue.get("additionalinfo") != null ? " " + issue.getString("additionalinfo") : ""));
            ((TextView) this.getView().findViewById(R.id.lWhoName)).setText(issue.getString("whoname"));
            ((TextView) this.getView().findViewById(R.id.lGenderName)).setText(issue.getString("gendername"));
            ((TextView) this.getView().findViewById(R.id.lBornOn)).setText(issue.getString("birthmonth") + "/" + issue.getString("birthyear"));
            ((TextView) this.getView().findViewById(R.id.lDescription)).setText(issue.getString("description"));
            ((TextView) this.getView().findViewById(R.id.lSince)).setText(issue.getString("sincename"));
            JSONArray arr = issue.getJSONArray("symptoms");

            StringBuilder sbSymptoms = new StringBuilder();
            for (int i = 0; i < arr.length(); i++)
                sbSymptoms.append(((JSONObject) arr.get(i)).getString("symptomname") + "\r\n");
            ((TextView) this.getView().findViewById(R.id.ulSymptoms)).setText(sbSymptoms.toString());

            arr = issue.getJSONArray("allergies");
            StringBuilder sbAllergies = new StringBuilder();
            for (int i = 0; i < arr.length(); i++)
                sbAllergies.append(((JSONObject) arr.get(i)).getString("allergy") + "\r\n");
            ((TextView) this.getView().findViewById(R.id.ulAllergies)).setText(sbAllergies.toString());

            arr = issue.getJSONArray("chronics");
            StringBuilder sbChronics = new StringBuilder();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject joChron = (JSONObject) arr.get(i);
                String val = joChron.getInt("chronicid") == -1 ? joChron.getString("chronicfree") : joChron.getString("chronicname");
                sbChronics.append(val + "\r\n");
            }

            ((TextView) this.getView().findViewById(R.id.ulChronics)).setText(sbChronics.toString());

            arr = issue.getJSONArray("medications");
            StringBuilder sbMedications = new StringBuilder();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject joMed = (JSONObject) arr.get(i);
                String val = joMed.getString("medication") + " " + getResources().getString(R.string.from) + " " + joMed.getString("sincename");
                sbMedications.append(val + "\r\n");
            }
            ((TextView) this.getView().findViewById(R.id.ulMedicines)).setText(sbMedications.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}

package bg.teledoc.teledocapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class IssueDescriptionFragment extends BaseFragment {

    private EditText tbDescription;

    public IssueDescriptionFragment() {
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
        final View v = inflater.inflate(R.layout.fragment_issue_description, container, false);
        tbDescription = (EditText)v.findViewById(R.id.tbDescription);
        try {
            JSONObject issue = GetMain().getIssue();
            if (issue.has("description")) {
                tbDescription.setText(issue.getString("description"));
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

        ImageButton bPrev = (ImageButton)v.findViewById(R.id.bPrev);
        bPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prev();
            }
        });

        return v;
    }

    private void Prev() {
        if (tbDescription.getText().toString().equals(""))
        {
            getView().findViewById(R.id.lError).setVisibility(View.VISIBLE);
            return;

        }


        try {
            final JSONObject issue = GetMain().getIssue();
            issue.put("description",tbDescription.getText());

        } catch (JSONException ex) {
        }

        this.GetMain().gotoFragment(new IssueTargetFragment());
    }


    private void Next() {
        if (tbDescription.getText().toString().equals(""))
        {
            getView().findViewById(R.id.lError).setVisibility(View.VISIBLE);
            return;

        }

        try {
            final JSONObject issue = GetMain().getIssue();
            issue.put("description",tbDescription.getText());

        } catch (JSONException ex) {
        }
    }

}

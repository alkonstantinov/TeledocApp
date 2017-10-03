package bg.teledoc.teledocapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class IssueAnswerTypeFragment extends BaseFragment {

    private RadioButton rbChat;
    private RadioButton rbEmail;
    private RadioButton rbPhone;
    private EditText tbEmail;
    private EditText tbPhone;

    public IssueAnswerTypeFragment() {
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
        final View v = inflater.inflate(R.layout.fragment_issue_answertype, container, false);
        rbChat = (RadioButton) v.findViewById(R.id.rbChat);
        rbEmail = (RadioButton) v.findViewById(R.id.rbEmail);
        rbPhone = (RadioButton) v.findViewById(R.id.rbPhone);
        tbEmail = (EditText) v.findViewById(R.id.tbEmail);
        tbPhone = (EditText) v.findViewById(R.id.tbPhone);

        rbPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbEmail.setChecked(false);
                rbChat.setChecked(false);
            }
        });

        rbEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbPhone.setChecked(false);
                rbChat.setChecked(false);
            }
        });

        rbChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rbPhone.setChecked(false);
                rbPhone.setChecked(false);
            }
        });



        try {
            JSONObject issue = GetMain().getIssue();
            if (issue.has("answertypeid")) {
                switch (issue.getInt("answertypeid")) {
                    case 1:
                        rbChat.setChecked(true);
                        break;
                    case 2:
                        rbEmail.setChecked(true);
                        tbEmail.setText(issue.getString("additionalinfo"));
                        break;
                    case 3:
                        rbPhone.setChecked(true);
                        tbPhone.setText(issue.getString("additionalinfo"));
                        break;
                }

            }
        } catch (JSONException ex) {
        }

        ImageButton bPrev = (ImageButton) v.findViewById(R.id.bPrev);
        bPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prev();
            }
        });
        ImageButton bNext = (ImageButton) v.findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Next();
            }
        });
        return v;
    }

    private boolean Save() {
        ((TextView) getView().findViewById(R.id.lNoValue)).setVisibility(View.INVISIBLE);
        ((TextView) getView().findViewById(R.id.lIncorrectValue)).setVisibility(View.INVISIBLE);
        boolean result = true;
        if (rbEmail.isChecked() && tbEmail.getText().toString().equals("")) {
            result = false;
            ((TextView) getView().findViewById(R.id.lNoValue)).setVisibility(View.VISIBLE);

        }

        if (rbPhone.isChecked() && tbPhone.getText().toString().equals("")) {
            result = false;
            ((TextView) getView().findViewById(R.id.lNoValue)).setVisibility(View.VISIBLE);

        }


        if (rbEmail.isChecked() && !tbEmail.getText().toString().equals("") &&
                !tbEmail.getText().toString().matches("^\\w+([\\.-]?\\ w+)*@\\w+([\\.-]?\\ w+)*(\\.\\w{2,3})")) {
            result = false;
            ((TextView) getView().findViewById(R.id.lIncorrectValue)).setVisibility(View.VISIBLE);

        }

        return result;
    }

    private void Prev() {
        if (Save())
            this.GetMain().gotoFragment(new IssueAllergiesFragment());
    }


    private void Next() {
        if (Save())
            this.GetMain().gotoFragment(new PatientMainFragment());
    }

}

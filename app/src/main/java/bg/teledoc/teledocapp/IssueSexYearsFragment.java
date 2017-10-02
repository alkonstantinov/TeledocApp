package bg.teledoc.teledocapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class IssueSexYearsFragment extends BaseFragment {

    private RadioButton rbFemale;
    private RadioButton rbMale;
    private RadioButton rbOtherSex;

    private Spinner ddlMonth;
    private Spinner ddlYear;

    public IssueSexYearsFragment() {
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
        final View v = inflater.inflate(R.layout.fragment_issue_sexyears, container, false);
        rbFemale = (RadioButton) v.findViewById(R.id.rbFemale);
        rbMale = (RadioButton) v.findViewById(R.id.rbMale);
        rbOtherSex = (RadioButton) v.findViewById(R.id.rbOtherSex);

        ddlMonth = (Spinner) v.findViewById(R.id.ddlMonth);
        ddlYear = (Spinner) v.findViewById(R.id.ddlYear);

        int selYear = -1;
        int selMonth = -1;

        try {
            JSONObject issue = GetMain().getIssue();
            if (issue.has("sexid")) {
                switch (issue.getString("sexid")) {
                    case "f":
                        rbFemale.setChecked(true);
                        break;
                    case "m":
                        rbMale.setChecked(true);
                        break;
                    case "b":
                        rbOtherSex.setChecked(true);
                        break;
                }
            }
            if (issue.has("birthmonth")) {
                selMonth = issue.getInt("birthmonth");
            }

            if (issue.has("birthyear")) {
                selYear = issue.getInt("birthyear");
            }


        } catch (JSONException ex) {
        }

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


        List<String> lMonths = new ArrayList<String>();
        lMonths.add(getResources().getString(R.string.january));
        lMonths.add(getResources().getString(R.string.february));
        lMonths.add(getResources().getString(R.string.march));
        lMonths.add(getResources().getString(R.string.april));
        lMonths.add(getResources().getString(R.string.may));
        lMonths.add(getResources().getString(R.string.june));
        lMonths.add(getResources().getString(R.string.july));
        lMonths.add(getResources().getString(R.string.august));
        lMonths.add(getResources().getString(R.string.september));
        lMonths.add(getResources().getString(R.string.october));
        lMonths.add(getResources().getString(R.string.november));
        lMonths.add(getResources().getString(R.string.december));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lMonths);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        ddlMonth.setAdapter(dataAdapter);


        List<String> lYears = new ArrayList<String>();
        int rowNo = 0;
        for (Integer year = Calendar.getInstance().get(Calendar.YEAR); year > Calendar.getInstance().get(Calendar.YEAR) - 100; year--) {
            if (year == selYear)
                selYear = rowNo;
            lYears.add(year.toString());
            rowNo++;
        }
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lYears);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        ddlYear.setAdapter(dataAdapter2);

        if (selMonth > -1)
            ddlMonth.setSelection(selMonth - 1);
        if (selYear > -1)
            ddlYear.setSelection(selYear);


        return v;
    }

    private void Save() {
        try {
            JSONObject issue = GetMain().getIssue();
            if (rbFemale.isChecked())
                issue.put("sexid", "f");
            if (rbMale.isChecked())
                issue.put("sexid", "m");
            if (rbOtherSex.isChecked())
                issue.put("sexid", "b");
            issue.put("birthmonth", ddlMonth.getSelectedItemPosition()+1);
            issue.put("birthyear", Integer.parseInt(ddlYear.getSelectedItem().toString()));



        } catch (JSONException ex) {
        }

    }

    private void Prev() {
        Save();


        this.GetMain().gotoFragment(new IssueDescriptionFragment());
    }


    private void Next() {
        Save();
        this.GetMain().gotoFragment(new IssueSymptomsFragment());
    }

}

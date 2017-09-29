package bg.teledoc.teledocapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bg.teledoc.teledocapp.IssuePreviewFragment;
import bg.teledoc.teledocapp.MainActivity;
import bg.teledoc.teledocapp.R;
import bg.teledoc.teledocapp.Tools.Tools;

/**
 * Created by alkon on 25-Sep-17.
 */

public class IssuesByExpertAdapter extends android.widget.ArrayAdapter<JSONObject> {
    public IssuesByExpertAdapter(Context context, List<JSONObject> tasks) {
        super(context, 0, tasks);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final JSONObject jobj = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.issue_by_expert_row, parent, false);

        TextView tvDate = (TextView)convertView.findViewById(R.id.tvDate);
        try {
            String ds = jobj.getString("ondate");
            tvDate.setText(Tools.FormatPGDateTime(ds));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView tvDescription = (TextView)convertView.findViewById(R.id.tvDescription);
        try {
            tvDescription.setText(jobj.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final Button bShowIssue = (Button)convertView.findViewById(R.id.bShowIssue);
        try {
            bShowIssue.setTag(jobj.getInt("issueid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bShowIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int issueId = (int)bShowIssue.getTag();
                MainActivity.getMain().gotoFragment(IssuePreviewFragment.newInstance(issueId));
            }
        });
        return convertView;
    }

}

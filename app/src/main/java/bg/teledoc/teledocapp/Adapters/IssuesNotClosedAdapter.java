package bg.teledoc.teledocapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class IssuesNotClosedAdapter extends android.widget.ArrayAdapter<JSONObject> {
    public IssuesNotClosedAdapter(Context context, List<JSONObject> tasks) {
        super(context, 0, tasks);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final JSONObject jobj = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.issue_not_closed_row, parent, false);

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

        TextView tvStatus = (TextView)convertView.findViewById(R.id.tvStatus);
        try {
            tvStatus.setText(jobj.getString("statusname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ImageButton bShowChat = (ImageButton)convertView.findViewById(R.id.bShowChat);
        try {
            bShowChat.setTag(jobj.getInt("issueid"));
            if(jobj.getInt("answertypeid")!=1)
                bShowChat.setVisibility(View.INVISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bShowChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int issueId = (int)bShowChat.getTag();
                MainActivity.getMain().gotoFragment(IssuePreviewFragment.newInstance(issueId));
            }
        });


        return convertView;
    }

}

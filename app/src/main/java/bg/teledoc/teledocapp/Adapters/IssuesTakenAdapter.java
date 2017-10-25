package bg.teledoc.teledocapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bg.teledoc.teledocapp.ChatFragment;
import bg.teledoc.teledocapp.IssuePreviewFragment;
import bg.teledoc.teledocapp.MainActivity;
import bg.teledoc.teledocapp.R;
import bg.teledoc.teledocapp.Tools.Tools;

/**
 * Created by alkon on 25-Sep-17.
 */

public class IssuesTakenAdapter extends android.widget.ArrayAdapter<JSONObject> {
    public IssuesTakenAdapter(Context context, List<JSONObject> tasks) {
        super(context, 0, tasks);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final JSONObject jobj = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.issue_taken_row, parent, false);

        TextView tvDate = (TextView)convertView.findViewById(R.id.tvDate);
        try {
            String ds = jobj.getString("ondate");
            tvDate.setText(Tools.FormatPGDateTime(ds));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final TextView tvDescription = (TextView)convertView.findViewById(R.id.tvDescription);
        try {
            tvDescription.setText(jobj.getString("description"));
            tvDescription.setTag(jobj.getInt("issueid"));
            tvDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int issueId = (int)tvDescription.getTag();
                    MainActivity.getMain().gotoFragment(IssuePreviewFragment.newInstance(issueId));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            switch(jobj.getInt("answertypeid"))
            {
                case 1: convertView.findViewById(R.id.bShowChat).setVisibility(View.VISIBLE);break;
                case 2: convertView.findViewById(R.id.ivMail).setVisibility(View.VISIBLE);break;
                case 3: convertView.findViewById(R.id.ivPhone).setVisibility(View.VISIBLE);break;

            }
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
                MainActivity.getMain().gotoFragment(ChatFragment.newInstance(issueId));
            }
        });


        return convertView;
    }

}

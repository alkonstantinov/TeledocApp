package bg.teledoc.teledocapp.Adapters;

import android.app.Fragment;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.MainActivity;
import bg.teledoc.teledocapp.R;
import bg.teledoc.teledocapp.Requests.Requests;
import bg.teledoc.teledocapp.Tools.Tools;

/**
 * Created by alkon on 25-Sep-17.
 */

public class IssuesClosedAdapter extends android.widget.ArrayAdapter<JSONObject> {
    public IssuesClosedAdapter(Context context, List<JSONObject> tasks) {
        super(context, 0, tasks);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final JSONObject jobj = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.issue_closed_row, parent, false);

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

        final ImageButton bRestoreChat = (ImageButton)convertView.findViewById(R.id.bRestoreChat);
        try {
            bRestoreChat.setTag(jobj.getInt("issueid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bRestoreChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Requests.RestartChat(MainActivity.getMain().getSessionId(), bRestoreChat.getTag().toString(), getContext(), new ServerAPICallback() {
                    @Override
                    public void onResult(String result) {
                        try {
                            android.support.v4.app.Fragment frg = ((android.support.v4.app.Fragment)MainActivity.getMain().getCurrentFragment().getClass().newInstance());
                            MainActivity.getMain().gotoFragmentFast(frg);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });
            }
        });




        return convertView;
    }

}

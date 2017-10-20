package bg.teledoc.teledocapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;
import bg.teledoc.teledocapp.Requests.uploadFileToServerTask;


public class ChatFragment extends BaseFragment {


    private static final int REQUEST_CAMERA = 1;

    public int getmIssueId() {
        return mIssueId;
    }

    public void setmIssueId(int mIssueId) {
        this.mIssueId = mIssueId;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView bmImage;
        private Boolean showFullSize;

        public DownloadImageTask(ImageView bmImage, Boolean showFullSize) {
            this.bmImage = bmImage;
            this.showFullSize = showFullSize;
        }

        protected void onPreExecute() {


        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", "image download error");
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            //set image of your imageview
            bmImage.setImageBitmap(result);
            if (showFullSize)
                bmImage.setAdjustViewBounds(true);
            //close

        }
    }

    private static final String IssueId_PARAM = "IssueId";

    private int mIssueId;

    private ScrollView svChat;

    private LinearLayout llChat;

    private ImageButton bSend;
    private ImageButton bImage;
    private ImageButton bEndChat;

    private TextView tbMsg;

    public ChatFragment() {
        // Required empty public constructor
    }


    public static ChatFragment newInstance(int issueId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(IssueId_PARAM, issueId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setmIssueId(getArguments().getInt(IssueId_PARAM));
            GetMain().getSocket().emit("room", "issue_" + getmIssueId());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        llChat = (LinearLayout) v.findViewById(R.id.llChat);
        svChat = (ScrollView) v.findViewById(R.id.svChat);
        tbMsg = (EditText) v.findViewById(R.id.tbMsg);
        bSend = (ImageButton) v.findViewById(R.id.bSend);
        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tbMsg.getText().toString().equals(""))
                    return;

                try {
                    JSONObject jo = new JSONObject("{}");
                    jo.put("message", tbMsg.getText());
                    jo.put("room", "issue_" + getmIssueId());
                    jo.put("userid", GetMain().getUserId());
                    jo.put("name", GetMain().getUserName());
                    jo.put("issueId", getmIssueId());
                    GetMain().getSocket().emit("send", jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tbMsg.setText("");
            }
        });

        bImage = (ImageButton) v.findViewById(R.id.bImage);
        bImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                startActivityForResult(intent, 0);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        bEndChat = (ImageButton)v.findViewById(R.id.bEndChat);
        bEndChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("room", "issue_"+mIssueId);
                    jo.put("issueId", mIssueId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GetMain().getSocket().emit("endchat", jo);
                if (GetMain().getLevelId()==4)
                GetMain().gotoFragment(new PatientMainFragment());
                else
                    GetMain().gotoFragment(new ExpertMainFragment());
            }
        });

        GetChat();
        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//            File destination = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
//            FileOutputStream fo;
//            try {
//                fo = new FileOutputStream(destination);
//                fo.write(bytes.toByteArray());
//                fo.flush();
//                fo.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            new uploadFileToServerTask(bytes.toByteArray()).execute(mIssueId + "");
        }
    }


    private void GetChat() {
        llChat.removeAllViews();
        Requests.GetChat(GetMain().getSessionId(), "" + this.getmIssueId(), getContext(), new ServerAPICallback() {
            @Override
            public void onResult(String result) {
                try {
                    JSONArray ja = new JSONArray(result);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        AddMessage(jo);
                    }
                    svChat.post(new Runnable() {
                        @Override
                        public void run() {
                            // This method works but animates the scrolling
                            // which looks weird on first load
                            // scroll_view.fullScroll(View.FOCUS_DOWN);

                            // This method works even better because there are no animations.
                            svChat.scrollTo(0, svChat.getBottom());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object error) {

            }
        });

    }


    public Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }

    public void showImage(int chatId) {
        Dialog builder = new Dialog(getContext());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView iv = new ImageView(getContext());
        //imageView.setImageURI(imageUri);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = 1;

        iv.setLayoutParams(p);

        Glide.with(this).load("http://18.194.18.118/getchatimage?ChatId=" + chatId).into(iv);
        //Glide.with(this).load("http://10.0.2.2/getchatimage?ChatId=" + chatId).into(iv);

        //new DownloadImageTask(iv, true).execute("http://10.0.2.2/getchatimage?ChatId=" + chatId);


        builder.addContentView(iv, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        builder.show();
    }


    public void AddMessage(final JSONObject data) {

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setDividerPadding(5);
        try {
            if (!data.has("hasimg") || !data.getBoolean("hasimg")) {

                TextView tvEmpty = new TextView(getContext());
                TextView tvText = new TextView(getContext());
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = (float) 0.5;


                tvEmpty.setLayoutParams(p);
                tvText.setLayoutParams(p);

                if (data.has("message"))
                    tvText.setText(data.getString("ontime") + "\n" + data.getString("name") + "\n"
                            + data.getString("message"));
                else
                    tvText.setText(data.getString("ontime") + "\n" + data.getString("name"));
                if (data.getInt("userid") == GetMain().getUserId()) {
                    ll.addView(tvEmpty);
                    ll.addView(tvText);
                } else {
                    ll.addView(tvText);
                    ll.addView(tvEmpty);
                }

            } else {
                data.put("hasimg", false);
                AddMessage(data);


                TextView tvEmpty = new TextView(getContext());
                final ImageView iv = new ImageView(getContext());

                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.WRAP_CONTENT);
                p.weight = (float) 0.5;

                tvEmpty.setLayoutParams(p);
                iv.setLayoutParams(p);


                //new DownloadImageTask(iv, false).execute("http://10.0.2.2/getchatimage?ChatId=" + data.getString("chatid"));


                //Glide.with(this).load("http://10.0.2.2/getchatimage?ChatId=" + data.getString("chatid")).into(iv);
                Glide.with(this).load("http://18.194.18.118/getchatimage?ChatId=" + data.getString("chatid")).into(iv);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            showImage(data.getInt("chatid"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                if (data.getInt("userid") == GetMain().getUserId()) {
                    ll.addView(tvEmpty);
                    ll.addView(iv);
                } else {
                    ll.addView(iv);
                    ll.addView(tvEmpty);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        llChat.addView(ll);


    }


}

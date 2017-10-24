package bg.teledoc.teledocapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class RegistrationFragment extends BaseFragment {

    Menu menu;

    public RegistrationFragment() {
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

        final View v = inflater.inflate(R.layout.fragment_registration, container, false);

        v.findViewById(R.id.bRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
        v.findViewById(R.id.lTAC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowTAC();
            }
        });
        return v;
    }


    private void Register() {

        final String username = ((EditText) getView().findViewById(R.id.tbRegEmail)).getText().toString();
        final String fullname = ((EditText) getView().findViewById(R.id.tbName)).getText().toString();
        final String password = ((EditText) getView().findViewById(R.id.tbPassword)).getText().toString();
        boolean tac = ((CheckBox) getView().findViewById(R.id.cbTAC)).isChecked();
        if (!username.matches("^\\w+([\\.-]?\\ w+)*@\\w+([\\.-]?\\ w+)*(\\.\\w{2,3})+$")) {
            Toast.makeText(getContext(), getResources().getString(R.string.invalidmail), Toast.LENGTH_LONG).show();
            return;
        }

        if (fullname.equals("")) {
            Toast.makeText(getContext(), getResources().getString(R.string.invalidname), Toast.LENGTH_LONG).show();
            return;
        }

        if (password.equals("")) {
            Toast.makeText(getContext(), getResources().getString(R.string.invalidpassword), Toast.LENGTH_LONG).show();
            return;
        }

        if (!tac) {
            Toast.makeText(getContext(), getResources().getString(R.string.TACNotAccepted), Toast.LENGTH_LONG).show();
            return;
        }


        Requests.EmailExists(username, getContext(), new ServerAPICallback() {
            @Override
            public void onResult(String result) {
                try {
                    JSONObject jo = new JSONObject(result);
                    if (jo.getBoolean("Exists")) {
                        Toast.makeText(getContext(), getResources().getString(R.string.emailexists), Toast.LENGTH_LONG).show();
                        return;
                    }

                    Requests.RegisterPatient(username, fullname, password, getContext(), new ServerAPICallback() {
                        @Override
                        public void onResult(String result) {
                            GetMain().gotoFragment(new RegistrationSuccessFragment());
                        }

                        @Override
                        public void onError(Object error) {

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

    private void ShowTAC() {
        TextView tv = new TextView(getContext());
        tv.setText(getString(R.string.termsandconditionscontent));

        ScrollView sv = new ScrollView(getContext());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT);
        p.weight = 1;

        sv.setLayoutParams(p);

        sv.addView(tv);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(""); // set the title
        alertDialog.setView(sv);  // insert the password text field in the alert box
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() { // define the 'OK' button
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

//        Dialog builder = new Dialog(getContext());
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                //nothing;
//            }
//        });
//
//
//
//
//        builder.addContentView(sv, new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        builder.show();

    }


}

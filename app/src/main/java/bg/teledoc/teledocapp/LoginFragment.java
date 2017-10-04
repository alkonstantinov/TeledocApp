package bg.teledoc.teledocapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;


public class LoginFragment extends BaseFragment {


    public LoginFragment() {
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

        final View v = inflater.inflate(R.layout.fragment_login, container, false);
        final Menu menu = GetMain().getMainMenu();
        menu.findItem(R.id.miStart).setVisible(false);
        //menu.findItem(R.id.miExit).setVisible(false);
        menu.findItem(R.id.miChangePass).setVisible(false);
        Button bLogin = (Button) v.findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Requests.Login(
                        ((EditText) v.findViewById(R.id.tbUserName)).getText().toString(),
                        ((EditText) v.findViewById(R.id.tbPassword)).getText().toString(),
                        GetMain().getSessionId(),
                        getContext(),
                        new ServerAPICallback() {
                            @Override
                            public void onResult(String result) {
                                JSONObject res = null;
                                final MainActivity main =GetMain();
                                try {
                                    res = new JSONObject(result);
                                    if (res.get("LevelId").toString().equals("-1"))
                                        Toast.makeText(getContext(),R.string.invalidusername,Toast.LENGTH_LONG).show();
                                    else
                                    {
                                        GetMain().getSocket().emit("iam",res.get("UserId"));
                                        switch(Integer.parseInt(res.get("LevelId").toString()))
                                        {
                                            case 2:
                                            case 3:

                                                GetMain().gotoFragment(new ExpertMainFragment());
                                                menu.findItem(R.id.miChangePass).setVisible(true);
                                                menu.findItem(R.id.miStart).setVisible(true);
                                                menu.findItem(R.id.miStart).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                    @Override
                                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                                        main.gotoFragment(new ExpertMainFragment());
                                                        return true;
                                                    }
                                                });
                                                break;
                                            case 4:
                                                GetMain().gotoFragment(new PatientMainFragment());
                                                menu.findItem(R.id.miChangePass).setVisible(true);
                                                menu.findItem(R.id.miStart).setVisible(true);
                                                menu.findItem(R.id.miStart).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                    @Override
                                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                                        main.gotoFragment(new PatientMainFragment());
                                                        return true;
                                                    }
                                                });

                                                break;
                                        }


                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onError(Object error) {

                            }
                        }
                );
            }
        });
        ((EditText) v.findViewById(R.id.tbUserName)).requestFocus();
        return v;
    }


}

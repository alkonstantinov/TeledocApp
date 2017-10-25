package bg.teledoc.teledocapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;
import bg.teledoc.teledocapp.Requests.Requests;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private JSONObject issue;

    public JSONObject getIssue() {
        return issue;
    }

    public void setIssue(JSONObject issue) {
        this.issue = issue;
    }

    private Socket socket;


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    private Integer levelId;

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    private String userName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    private static MainActivity main;

    public static MainActivity getMain() {
        return main;
    }

    public static void setMain(MainActivity main) {
        MainActivity.main = main;
    }

    public boolean IsNetworkAvailable(android.content.Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void CheckInternet() {
        boolean res = this.IsNetworkAvailable(this);
        if (res)
            return;
        gotoFragment(new NoInternetFragment());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prevFragments = new Stack<Fragment>();
        MainActivity.verifyStoragePermissions(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MainActivity.setMain(this);

        if (savedInstanceState != null) {
            try {
                issue = new JSONObject(savedInstanceState.getString("issue"));
                sessionId = savedInstanceState.getString("sessionId");
                if (savedInstanceState.containsKey("levelId")) {
                    levelId = savedInstanceState.getInt("levelId");
                    userId = savedInstanceState.getInt("userId");
                    userName = savedInstanceState.getString("userName");
                }
                try {
                    Fragment f = (android.support.v4.app.Fragment) Class.forName(savedInstanceState.getString("fragment")).getConstructor().newInstance();
                    if (f instanceof IssuePreviewFragment) {
                        ((IssuePreviewFragment) f).setmIssueId(savedInstanceState.getInt("issueId"));
                    }
                    if (f instanceof ChatFragment) {
                        ((ChatFragment) f).setmIssueId(savedInstanceState.getInt("issueId"));
                    }

                    gotoFragment(f);
                    if (savedInstanceState.containsKey("prevFragment")) {
                        f = (android.support.v4.app.Fragment) Class.forName(savedInstanceState.getString("prevFragment")).getConstructor().newInstance();
                        if (f instanceof IssuePreviewFragment) {
                            ((IssuePreviewFragment) f).setmIssueId(savedInstanceState.getInt("prevIssueId"));
                        }
                        if (f instanceof ChatFragment) {
                            ((ChatFragment) f).setmIssueId(savedInstanceState.getInt("prevIssueId"));
                        }
                    }

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
//            outState.putString("fragment", getCurrentFragment().getClass().getName());
//            if (getCurrentFragment() instanceof IssuePreviewFragment) {
//                outState.putInt("issueId", ((IssuePreviewFragment) getCurrentFragment()).getmIssueId());
//            }
//
//            if (getCurrentFragment() instanceof ChatFragment) {
//                outState.putInt("issueId", ((ChatFragment) getCurrentFragment()).getmIssueId());
//            }
        } else

        {

            issue = new JSONObject();
            CheckInternet();
            Requests.GetSessionId(this, new ServerAPICallback() {
                @Override
                public void onResult(String result) {
                    try {
                        JSONObject res = new JSONObject(result);
                        setSessionId(res.get("sessionId").toString());
                        gotoFragment(new LoginFragment());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Object error) {

                }
            });

        }


        try

        {
            //setSocket(IO.socket("http://18.194.18.118"));
            setSocket(IO.socket("http://10.0.2.2"));

        } catch (
                URISyntaxException e)

        {
            e.printStackTrace();
        }


        getSocket().

                on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {

                    }

                }).

                on("event", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                    }

                }).

                on("message", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {

                        ProcessMessage((JSONObject) args[0]);
                    }

                }).

                on("messageimage", new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {

                        ProcessMessage((JSONObject) args[0]);
                    }

                }).

                on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                    }

                });


        getSocket().connect();


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(getCurrentFragment() instanceof PatientMainFragment)
                    ((PatientMainFragment)getCurrentFragment()).PopulateListViews();
                else
                if(getCurrentFragment() instanceof ExpertMainFragment)
                    ((ExpertMainFragment)getCurrentFragment()).PopulateListViews();

            }
        }, 0, 5000);


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("issue", issue.toString());
        outState.putString("sessionId", sessionId);
        if (levelId != null) {
            outState.putInt("levelId", levelId);
            outState.putInt("userId", userId);
            outState.putString("userName", userName);
        }
        outState.putString("fragment", getCurrentFragment().getClass().getName());
        if (getCurrentFragment() instanceof IssuePreviewFragment) {
            outState.putInt("issueId", ((IssuePreviewFragment) getCurrentFragment()).getmIssueId());
        }

        if (getCurrentFragment() instanceof ChatFragment) {
            outState.putInt("issueId", ((ChatFragment) getCurrentFragment()).getmIssueId());
        }


    }


    public void gotoFragment(Fragment mFragment, boolean noHist) {
        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.setCustomAnimations(R.anim.slide_out_right, R.anim.slide_out_left);
        trans.replace(R.id.screen_content, mFragment);
        if (!noHist && getCurrentFragment() != null)
            setPrevFragment(getCurrentFragment());

        setCurrentFragment(mFragment);
        trans.commit();

    }

    public void gotoFragment(Fragment mFragment) {
        gotoFragment(mFragment, false);

    }

    public void gotoFragmentFast(Fragment mFragment) {
        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.screen_content, mFragment);
        setCurrentFragment(mFragment);
        trans.commit();

    }


    private void ShowHideLanguageButtons(Menu menu) {
        Configuration conf = getBaseContext().getResources().getConfiguration();
        Locale loc = null;

        MenuItem bBG = (MenuItem) menu.findItem(R.id.miBG);
        bBG.setVisible(false);

        MenuItem bEN = (MenuItem) menu.findItem(R.id.miEN);
        bEN.setVisible(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            loc = getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            loc = getResources().getConfiguration().locale;
        }
        if (loc.getDisplayLanguage().equals("English")) {
            bBG.setVisible(true);

        } else {
            bEN.setVisible(true);
        }


    }

    public void SwitchLocale(String lang) {
        String languageToLoad = lang; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.miBG:
                SwitchLocale("bg");
                return true;
            case R.id.miEN:
                SwitchLocale("en");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        ShowHideLanguageButtons(menu);
        mainMenu = menu;

        menu.findItem(R.id.miExit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                System.exit(1);
                return true;
            }
        });

        menu.findItem(R.id.miChangePass).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                gotoFragment(new ChangePasswordFragment());
                return true;
            }
        });
        return true;
    }

    private Menu mainMenu;


    public Menu getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(Menu mainMenu) {
        this.mainMenu = mainMenu;
    }

    private android.support.v4.app.Fragment currentFragment;

    public android.support.v4.app.Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(android.support.v4.app.Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }


    private Stack<Fragment> prevFragments;

    public android.support.v4.app.Fragment getPrevFragment() {

        if (!prevFragments.empty())
            return prevFragments.pop();
        else
            return null;
    }

    public void setPrevFragment(android.support.v4.app.Fragment prevFragment) {
        this.prevFragments.addElement(prevFragment);
    }

    private void ProcessMessage(final JSONObject jo) {
        try {
            if ((this.currentFragment instanceof ChatFragment)
                    &&
                    (
                            ((ChatFragment) this.currentFragment).getmIssueId() == jo.getInt("issueId")
                    ))

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ChatFragment) currentFragment).AddMessage(jo);
                    }
                });

            else
                Toast.makeText(getBaseContext(), getResources().getString(R.string.NewMessage), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        {


        }


    }


    @Override
    public void onBackPressed() {
        Fragment f = getPrevFragment();

        if (f != null) {
            gotoFragment(f, true);
            if (prevFragments.empty()) {
                Menu menu = getMainMenu();
                menu.findItem(R.id.miChangePass).setVisible(false);
                menu.findItem(R.id.miStart).setVisible(false);

            }
        }
    }

}

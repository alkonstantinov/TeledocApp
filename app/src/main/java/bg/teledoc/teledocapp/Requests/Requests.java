package bg.teledoc.teledocapp.Requests;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.util.Log;
import android.view.Window;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bg.teledoc.teledocapp.Callbacks.ServerAPICallback;

/**
 * Created by alkon on 21-Sep-17.
 */

public class Requests {
    //private static String BaseUrl = "http://18.194.18.118/";
    private static String BaseUrl = "http://10.0.2.2/";

    public static void GetSessionId(Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest getRequest = new StringRequest(Request.Method.GET, BaseUrl + "getsessionid",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        cb.onResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                        cb.onError(error.getMessage());
                    }
                }
        );
        queue.add(getRequest);
    }

    public static void Login(final String username, final String password, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, BaseUrl + "login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        cb.onResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                        cb.onError(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Username", username);
                params.put("Password", password);

                return params;
            }

        };
        queue.add(postRequest);
    }

}

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
import bg.teledoc.teledocapp.R;

/**
 * Created by alkon on 21-Sep-17.
 */

public class Requests {
    //private static String BaseUrl = "http://18.194.18.118/";


    public static void GetSessionId(Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;

        StringRequest getRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.ServerAddress) + "/getsessionid",
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

    public static void Login(final String username, final String password, final String sessionId, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/login",
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
                params.put("sessionId", sessionId);

                return params;
            }

        };
        queue.add(postRequest);
    }


    public static void IssuesNotClosed(final String sessionId, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/getissuesnotclosed",
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
                params.put("sessionId", sessionId);
                return params;
            }

        };
        queue.add(postRequest);
    }


    public static void IssuesClosed(final String sessionId, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/getclosedissues",
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
                params.put("sessionId", sessionId);
                return params;
            }

        };
        queue.add(postRequest);
    }

    public static void IssuesByExpert(final String sessionId, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/getissuesbyexpert",
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
                params.put("sessionId", sessionId);
                return params;
            }

        };
        queue.add(postRequest);
    }

    public static void IssuesTaken(final String sessionId, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/gettakenissues",
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
                params.put("sessionId", sessionId);
                return params;
            }

        };
        queue.add(postRequest);
    }


    public static void IssueGet(final String sessionId, final int issueId, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/getissue",
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
                params.put("sessionId", sessionId);
                params.put("issueId", issueId + "");
                return params;
            }

        };
        queue.add(postRequest);
    }

    public static void IssueLastGet(final String sessionId, final int whoId, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/getlastissue",
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
                params.put("sessionId", sessionId);
                params.put("whoId", whoId + "");
                return params;
            }

        };
        queue.add(postRequest);
    }

    public static void SymptomsGet(final String sessionId, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/getsymptoms",
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
                params.put("sessionId", sessionId);

                return params;
            }

        };
        queue.add(postRequest);
    }

    public static void SinceGet(final String sessionId, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/getsinces",
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
                params.put("sessionId", sessionId);

                return params;
            }

        };
        queue.add(postRequest);
    }


    public static void ChronicsGet(final String sessionId, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/getchronics",
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
                params.put("sessionId", sessionId);

                return params;
            }

        };
        queue.add(postRequest);
    }


    public static void ChangePass(final String sessionId, final String pass, Context context, final ServerAPICallback cb) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/changepass",
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
                params.put("sessionId", sessionId);
                params.put("password", pass);

                return params;
            }

        };
        queue.add(postRequest);
    }

    public static void TakeIssue(final String sessionId, final int issueId, Context context, final ServerAPICallback cb) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/takeissue",
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
                params.put("sessionId", sessionId);
                params.put("password", issueId + "");

                return params;
            }

        };
        queue.add(postRequest);
    }

    public static void SetIssue(final String sessionId, final String issue, Context context, final ServerAPICallback cb) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/setissue",
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
                params.put("sessionId", sessionId);
                params.put("issue", issue);

                return params;
            }

        };
        queue.add(postRequest);
    }

    public static void RestartChat(final String sessionId, final String issueId, Context context, final ServerAPICallback cb) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/restartchat",
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
                params.put("sessionId", sessionId);
                params.put("issueId", issueId);

                return params;
            }

        };
        queue.add(postRequest);
    }

    public static void GetChat(final String sessionId, final String issueId, Context context, final ServerAPICallback cb) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/getchat",
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
                params.put("sessionId", sessionId);
                params.put("issueId", issueId);

                return params;
            }

        };
        queue.add(postRequest);
    }


    public static void EmailExists(final String email, Context context, final ServerAPICallback cb) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/userexists",
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
                params.put("email", email);

                return params;
            }

        };
        queue.add(postRequest);
    }


    public static void RegisterPatient(final String email, final String name, final String password, Context context, final ServerAPICallback cb) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/registeruser",
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
                params.put("email", email);
                params.put("password", password);
                params.put("name", name);

                return params;
            }

        };
        queue.add(postRequest);
    }


    public static void IssueCanChat (final String sessionId, final String issueId, Context context, final ServerAPICallback cb) {

        RequestQueue queue = Volley.newRequestQueue(context);
        final String result;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.ServerAddress) + "/issuecanchat",
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
                params.put("sessionId", sessionId);
                params.put("issueId", issueId);

                return params;
            }

        };
        queue.add(postRequest);
    }


}

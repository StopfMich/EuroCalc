package com.steffen.EuroCalc;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class NetHelper {


    //Quelle API der European Central Bank
    private String URL;
    public void setURL (String url) {
     this.URL = url;
    }


    public void getWechselkurs(final VolleyCallback callback, final Context context) {

        final RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            try {
                                callback.onError();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            updateError(context);
                        }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                2000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
            queue.add(stringRequest);
            queue.start();
    }

    public interface VolleyCallback{
        void onSuccess(String result) throws JSONException;
        void onError() throws JSONException;
    }

    public void updateError(Context context){
        Toast.makeText(context, "Fehler beim Updaten" , Toast.LENGTH_SHORT).show();
    }
}


package zulfiqar.com.passwordauth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;

public class Notify extends AppCompatActivity {


    private EditText head , body ;
    private Button  notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        initialse();
    }

    private void initialse() {

        head = (EditText) findViewById(R.id.head);
        body = (EditText) findViewById(R.id.body);
        notify = (Button) findViewById(R.id.notify);

    }

    public void sendNotification(View view) {


        String main     = head.getText().toString();
        String content  =  body.getText().toString();


        boolean mainT = test(main);
        boolean contentT = test(content);

        if(mainT && contentT) {

            content = content.replaceAll("\\s", "_");

            StringRequest stringRequest = new StringRequest(Request.Method.GET, " https://damp-castle-75017.herokuapp.com/notify?head=" + main + "&contain=" + content + ".", new com.android.volley.Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    TastyToast.makeText(getApplicationContext(), "" + response, Toast.LENGTH_LONG,TastyToast.SUCCESS).show();
                }

            }
                    ,
                    new com.android.volley.Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            TastyToast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_LONG,TastyToast.ERROR).show();
                        }

                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }

    }

    private boolean test(String test) {

        if(test.length() <= 0){
            TastyToast.makeText(getApplicationContext(),
                    "Please enter valid details",
                    Toast.LENGTH_SHORT , TastyToast.CONFUSING).show();
            return false;
        }
        else {

            return true;
        }

    }


}

package zulfiqar.com.passwordauth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.tastytoast.TastyToast;

public class ChangeChannelActivity extends AppCompatActivity {


    private EditText channel ;
    private Button  changechannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_channel);

        initialse();
    }

    private void initialse() {

        channel = (EditText) findViewById(R.id.channelname);
        changechannel = (Button) findViewById(R.id.channelButton);
    }

    public void changeChannel(View view) {


        String main     = channel.getText().toString();



        boolean mainT = test(main);

        main = channel(main);

        if(mainT) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dbRef = database.getReference("/Channel/data/channelname");
//            dbRef.setValue(""+main);
            DatabaseReference.CompletionListener completionListener = new
                    DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {

                            if (databaseError != null) {
                                // Notify user of failure
                                TastyToast.makeText(getApplicationContext(),"Error",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                            }
                            else {
                                TastyToast.makeText(getApplicationContext(),"changed channel",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS).show();

                                finish();
                            }
                        }
                    };

            dbRef.setValue(""+main,
                    completionListener);

        }

    }

    private String channel(String main) {


        int doti= 0 ,qi =0;
        if(main.contains("https://www.youtube.com/channel/")){

            main =  main.replace("https://www.youtube.com/channel/",".");

            System.out.println("Quotient = " +main);



        }

        if(main.contains("?")){

            doti = main.indexOf(".");
            qi = main.indexOf("?");

        }else{
            main = main+"?";
            doti = main.indexOf(".");
            qi = main.indexOf("?");
        }

        return ""+main.substring(doti+1, qi);
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

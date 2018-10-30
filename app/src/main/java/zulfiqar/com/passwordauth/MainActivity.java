package zulfiqar.com.passwordauth;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Response;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth fbAuth;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkUser();
        setInitaialzations();
    }

    private void checkUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user ==  null){

            Intent sign = new Intent(getApplicationContext() , PasswordAuthActivity.class);
            startActivity(sign);

        }
    }

    private void setInitaialzations() {
        webView = (WebView) findViewById(R.id.watsapp);
        fbAuth = FirebaseAuth.getInstance();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new Home());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            final Fragment[] fragment = {null};


            switch (item.getItemId()) {


                case R.id.home :

                    fragment[0] = new Home();
                    break;

                case R.id.bayans :

                    Intent bayans = new Intent(getApplicationContext() , ChannelActivity.class);
                    startActivity(bayans);
                    break;

                case R.id.live :

                    live();
                    break;

                case R.id.events :

                    fragment[0] = new Events();
                    break;

                case R.id.profile :

                    fragment[0] = new Profile();
                    break;

            }
            return loadFragment(fragment[0]);

        }
    };

    public  void live(){

        final Fragment[] fragment = {null};

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final String mail = user.getEmail();


        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Sure want to Open Live Bayan !")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(Constants.EMAIL_ADMIN.equals(""+mail)){

                            Intent admin = new Intent(Intent.ACTION_VIEW);
                            admin.setData(Uri.parse("https://sharpstream.herokuapp.com/alansaarsetstream"));
                            startActivity(admin);
                        }
                        else{

                            fragment[0] = new LiveFragment();
                            loadFragment(fragment[0]);
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });

        android.support.v7.app.AlertDialog alert1 =builder1.create();
        alert1.setTitle("Al Ansaar Live Bayan");
        alert1.show();
    }
    public boolean loadFragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment).addToBackStack("tag");
            ft.commit();
            return  true;
        }
        return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final Fragment[] fragment = {null};

        //noinspection SimplifiableIfStatement
        if (id == R.id.signout) {


            android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder1.setMessage("Sure want to Sign out !")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            fbAuth.signOut();
                            Intent intent = new Intent(getApplicationContext() , PasswordAuthActivity.class);
                            startActivity(intent);
                            finish();
                            TastyToast.makeText(getApplicationContext() ,"signout" ,Toast.LENGTH_SHORT,TastyToast.INFO).show();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.cancel();
                        }
                    });

            android.support.v7.app.AlertDialog alert1 =builder1.create();
            alert1.setTitle("Sign Out");
            alert1.show();

            return true;
        }
        if (id == R.id.watsapp) {

            TastyToast.makeText(getApplicationContext(), "processing request", Toast.LENGTH_SHORT, TastyToast.DEFAULT).show();
            webView.loadUrl("https://chat.whatsapp.com/IFYGuOQouNUF1pdD3g1uXR");

            return true;
        }
        if (id == R.id.share) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Al Ansaar");
            String sAux = "\nAl Ansaar App link\n (Spreading peace in world)\n\n";
            sAux = sAux + "\n\n"+Constants.appLink;
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
            return true;
        }
        if (id == R.id.contacts) {

            fragment[0] = new Contacts();
            loadFragment(fragment[0]);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}

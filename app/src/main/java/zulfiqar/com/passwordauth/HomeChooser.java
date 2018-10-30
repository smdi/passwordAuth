package zulfiqar.com.passwordauth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.*;
import java.util.Calendar;



public class HomeChooser extends AppCompatActivity {


    private ImageButton img;
    private  Bitmap bitmap , decoded;
    private static final int RC_PHOTO_PICKER = 1;
    private static final int RESULT_OK = -1;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbreference;
    private  Uri photouri;
    private RelativeLayout lotrelay;
    private LottieAnimationView lottieAnimationView;
    private String fireDb,fireStr;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private EditText headeredit,descriptionedit,links;
    private  RadioGroup homeevent;
    private RadioButton choose;
    private ByteArrayOutputStream out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        initialise();
        initialiseclick();

    }

    private void firbaseinitialise(String chooser) {

        if(chooser.equals(Constants.home)) {
            fireDb = corrector("Al-Ansar-Home");
            fireStr = corrector("Al-Ansar-homeStorage");
        }
        else {

            fireDb = corrector("Up-Comingevents");
            fireStr = corrector("Up-Comingevents-Storage");
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference().child(fireDb);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child(fireStr);
    }
    private void initialiseclick() {

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "documents"), RC_PHOTO_PICKER);

            }
        });
    }

    private void initialise() {

        homeevent = (RadioGroup) findViewById(R.id.homeevent);
        img = (ImageButton) findViewById(R.id.add);
        headeredit = (EditText) findViewById(R.id.headingChoose);
        descriptionedit = (EditText) findViewById(R.id.descriptionChoose);
        links = (EditText) findViewById(R.id.links);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       getMenuInflater().inflate(R.menu.push,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.push :

                int selectedId = homeevent.getCheckedRadioButtonId();

                choose = (RadioButton) findViewById(selectedId);

                checkOut();


                break;
        }
        return true;
    }

    private void checkOut() {


        if(out != null) {
            getMedia();
            getLotte();
            TastyToast.makeText(getApplicationContext(),"pushing",Toast.LENGTH_SHORT, TastyToast.INFO).show();
            startPosting(out);
        }
        else{
            TastyToast.makeText(getApplicationContext(),"Enter valid Details",Toast.LENGTH_SHORT, TastyToast.CONFUSING).show();
        }


    }


    private void notifyUser(String headertext, String descriptiontext) {

        String main     = headertext;
        String content  =  descriptiontext;

        main    = main.replaceAll("\\s","_");
        content = content.replaceAll("\\s","_");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, " https://damp-castle-75017.herokuapp.com/notify?head="+main+"&contain="+content+"." , new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                TastyToast.makeText(getApplicationContext() , "" +response,Toast.LENGTH_LONG ,TastyToast.SUCCESS).show();
            }

        }
                ,
                new com.android.volley.Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        TastyToast.makeText(getApplicationContext() , "" +error,Toast.LENGTH_LONG,TastyToast.ERROR).show();
                    }

                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

                TastyToast.makeText(getApplicationContext(), "pick an image", Toast.LENGTH_SHORT, TastyToast.INFO).show();


                photouri = data.getData();
                bitmap = null; 
                 decoded = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photouri);
                    out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                img.setImageBitmap(decoded);
            }
    }

    private String corrector(String master) {

        String fin = "";

        String mod = "" ,mod1 = "",mod2 = "",mod3 = "",mod4="",mod5 = "";



        if(master.contains("."))
        {
            mod = ""+master.replace(".","dot") ;
        }
        else{
            mod = master;
        }
        if(mod.contains("$"))
        {


            mod1 = ""+mod.replace("$","dollar");
        }
        else{
            mod1 = mod;
        }
        if(mod1.contains("["))
        {

            mod2 = ""+mod1.replace("[","lsb");
        }
        else{
            mod2 = mod1;
        }
        if(mod2.contains("]"))
        {

            mod3 = ""+mod2.replace("]","rsb");
        }
        else{
            mod3 = mod2;
        }
        if(mod3.contains("#"))
        {

            mod4 = ""+mod3.replace("#","hash");
        }
        else{
            mod4 = mod3;
        }
        if(mod4.contains("/"))
        {

            mod5 = ""+mod4.replace("/","fs");

            fin = mod5;
        }
        else{
            mod5 = mod4;

            fin = mod5;

        }


        System.out.println(""+fin);

        return   fin;
    }


    public String getSystemDate() {

        Calendar calendar =Calendar.getInstance();

        return ""+calendar.getTime();
    }
    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.tweet);
        mp.start();
    }

    public void getLotte() {

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation("preloader.json");
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);
        lotrelay = (RelativeLayout) findViewById(R.id.lotteRel);
        lotrelay.setVisibility(View.VISIBLE);
    }
    private void cancelAnim() {

        lottieAnimationView.setVisibility(View.GONE);
        lottieAnimationView.cancelAnimation();
        lotrelay.setVisibility(View.GONE);
    }

    public String getCapsHead(String heading) {


        StringBuffer  str = new StringBuffer();

        String build = new String();
        String fullName = ""+heading;

        String []bank = fullName.split("\\s");

        String token ,remain;


        if(true) {

            try {
                for(int i = 0 ;i<bank.length ;i++){

                    token = bank[i].substring(0,1);

                    remain = bank[i].substring(1,bank[i].length());

                    str.append(" "+build.concat(token.toUpperCase()+remain));

                    System.out.print(" "+build.concat(token.toUpperCase()+remain));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return " "+str;
    }

    public StringBuilder getTheTime(String time) {


        StringBuilder stringBuilder = new StringBuilder();
        String split[]  =  time.split("\\s");
        for(int i=0; i<3; i++)
        {

            stringBuilder.append("  "+split[i]);
        }

        stringBuilder.append(" "+split[5]);


        return stringBuilder;
    }

    private void startPosting(ByteArrayOutputStream out) {



        byte[] data = out.toByteArray();




        final StringBuilder datei = getTheTime(getSystemDate());
        final String headertext = getCapsHead(""+headeredit.getText());
        final String descriptiontext = ""+descriptionedit.getText();
        final String link = String.valueOf(links.getText());


        boolean headertextT = test(headertext);
        boolean descriptiontextT = test(headertext);

        if(headertextT && descriptiontextT) {


            firbaseinitialise(""+choose.getText());

            if ((photouri != null) && (headertext.length() > 5) && (descriptiontext.length() > 5)) {


                StorageReference mref = storageReference.child(photouri.getLastPathSegment());

                UploadTask uploadTask = mref.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        TastyToast.makeText(getApplicationContext(), "" + exception, Toast.LENGTH_LONG,TastyToast.ERROR).show();// Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        HomeInitialiser homeInitialiser = new HomeInitialiser(downloadUrl.toString(), "" + headertext.trim().replaceAll("\\s+", " "), " " + descriptiontext.trim().replaceAll("\\s+", " "), "" + datei, link);
                        dbreference.push().setValue(homeInitialiser);

                        TastyToast.makeText(getApplicationContext(), "pushed", Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        cancelAnim();
                        finish();
                    }
                });


            }
        }


    }

    private boolean test(String test) {

        if(test.length() <= 0){
            TastyToast.makeText(getApplicationContext(),
                    "Enter valid details",
                    Toast.LENGTH_SHORT , TastyToast.CONFUSING).show();
            return false;
        }
        else {

            return true;
        }

    }


}

package zulfiqar.com.passwordauth;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdsmdg.tastytoast.TastyToast;

public class Fundamentals extends AppCompatActivity {


    private int del;
    private boolean web = true , text = true;
    private String url;
    private LottieAnimationView lottieAnimationView;
    private FirebaseStorage firebaseStorage;
    private FloatingActionButton fab;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED ,WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_fundamentals);

        getInitialise();

        getLotte();
        getFab();
        getSupportActionBar().hide();

        url = getIntent().getExtras().getString("link");

        setImageLoaded(url);
    }

    public void getLotte() {


        lottieAnimationView.setAnimation("preloader.json");
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);

    }
    public void getFab() {



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getEmail().equals(Constants.EMAIL_ADMIN))
        {
            fab.setVisibility(View.VISIBLE);
        }
        else {
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMedia();setVibrator();
                getLotte();
                String fireDb="" ;
                String fireStr="";
                    fireDb = "Al-Ansar-Home";
                    fireStr = "Al-Ansar-homeStorage";
                    getRemove("Al-Ansar-Home","uri",fireStr);


            }
        });
    }

    public void getInitialise() {
        img = (ImageView) findViewById(R.id.loadimage) ;
        fab = (FloatingActionButton) findViewById(R.id.fab_delete);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.loadvideo);
    }
    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(getApplicationContext() ,R.raw.tweet);
        mp.start();
    }
    private void setVibrator() {
        Vibrator v =(Vibrator)getSystemService(VIBRATOR_SERVICE);
        v.vibrate(1000);
    }
    private void getRemove(String s, String s1, final String fireStr) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query Uri = reference.child(s).orderByChild(s1).equalTo(url);

        Uri.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot uri : dataSnapshot.getChildren() )
                {

                    uri.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            TastyToast.makeText(getApplicationContext(),"deleted the database", Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();

                            getDeleteStore(fireStr);

                            setCancel();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                TastyToast.makeText(getApplicationContext(),"May be deleted !",Toast.LENGTH_SHORT,TastyToast.ERROR).show();
                setCancel();
                finish();
            }
        });

    }

    private void setCancel() {
        lottieAnimationView.cancelAnimation();
        lottieAnimationView.setVisibility(View.GONE);

    }
    private void getDeleteStore(String fireStr) {

        firebaseStorage = FirebaseStorage.getInstance();
//        storageReference = firebaseStorage.getReference().child(fireStr);


        StorageReference storageReference =firebaseStorage.getReferenceFromUrl(url);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                TastyToast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                TastyToast.makeText(getApplicationContext(),"try after some time",Toast.LENGTH_SHORT,TastyToast.ERROR).show();
            }
        });
    }

    public void setImageLoaded(String imageLoaded) {

        boolean isphoto = imageLoaded != null;

        if(isphoto)
        {
            Glide.with(getApplicationContext()).load(imageLoaded).into(img);
        }

        setCancel();
    }
}

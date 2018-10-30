package zulfiqar.com.passwordauth;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;

public class PasswordAuthActivity extends AppCompatActivity  {


    public static int HIDE = 1;
    private EditText emailText , emailPass;
    private EditText passwordText;
    private FirebaseAuth fbAuth;
    public static Context ctx;
    private FirebaseAuth.AuthStateListener authListener;
    private Button signIn , create , changePassword;
    private RelativeLayout lotterelay , eandp , password_relay;
    private LottieAnimationView lottieAnimationView;
    private TextView forgetPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_auth);


        ctx = getApplicationContext();

        getSupportActionBar().hide();

        checkUser();
        initialise();
        getLotte();
        initialiseClicks();
        fbAuth  = FirebaseAuth.getInstance();
        setAuthListener(emailText , passwordText ,  fbAuth);
    }

    private void initialiseClicks() {

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        forgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void hide() {

        if(HIDE == 1){

            password_relay.setVisibility(View.VISIBLE);
            eandp.setVisibility(View.INVISIBLE);

            HIDE ++;
        }
        else {

            password_relay.setVisibility(View.INVISIBLE);
            eandp.setVisibility(View.VISIBLE);
            HIDE--;
        }
    }

    private void initialise() {

        emailPass  = (EditText) findViewById(R.id.passwordchange);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        signIn   =  (Button) findViewById(R.id.signin);
        create   =  (Button) findViewById(R.id.create);
        forgetPasswordText = (TextView) findViewById(R.id.forgotpassword);
        eandp = (RelativeLayout) findViewById(R.id.eandp);
        password_relay  = (RelativeLayout) findViewById(R.id.password_relay);
        changePassword = (Button) findViewById(R.id.changePassword);
    }

    private void checkUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user !=  null){

            Intent sign = new Intent(getApplicationContext() , MainActivity.class);
            startActivity(sign);
            finish();

        }

    }

    private void setAuthListener(EditText emailText, EditText passwordText, final FirebaseAuth fbAuth) {


        authListener  = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user  =  firebaseAuth.getCurrentUser();

                if(user != null){

//                    userText.setText(user.getEmail());
//                    statusText.setText("Signed In");

                }
                else {

//                    userText.setText("");
//                    statusText.setText("Signed Out");

                }

            }
        };

    }

    private void notifyUser(String message) {
        TastyToast.makeText(PasswordAuthActivity.this, message,
                Toast.LENGTH_SHORT,TastyToast.DEFAULT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }
    public void createAccount() {



        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.length() == 0) {
            emailText.setError("Enter an email address");
            return;
        }

        if (password.length() < 6) {
            passwordText.setError("Password must be at least 6 characters");
            return;
        }

        playAnim();
        fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {

                                    notifyUser("Account creation failed");
                                }
                                setAnimVis();
                            }
                        });

    }

    public void signIn() {



        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        boolean emailT  = test(email);
        boolean passwordT  = test(email);

        if(emailT && passwordT) {

            if (email.length() == 0) {
                emailText.setError("Enter an email address");
                return;
            }

            if (password.length() < 6) {
                passwordText.setError("Password must be at least 6 characters");
                return;
            }

            playAnim();
            fbAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull
                                                               Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        notifyUser("Authentication failed");
                                        setAnimVis();
                                    } else {
                                        setAnimVis();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

        }
    }

    private void playAnim() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        lotterelay.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();

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


    public void resetPassword() {

        String email = emailPass.getText().toString();

        boolean test  = test(email);

        if(test) {

            if (email.length() == 0) {
                emailText.setError("Enter an email address");
                return;
            }

            fbAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                notifyUser("Reset email sent");
                                password_relay.setVisibility(View.INVISIBLE);
                                eandp.setVisibility(View.VISIBLE);
                            }
                        }
                    });

        }
    }

    public void getLotte() {

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation("preloader.json");
        lottieAnimationView.loop(true);
        lotterelay = (RelativeLayout) findViewById(R.id.lotteRel);
        lotterelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TastyToast.makeText(getApplicationContext(),"please wait",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT);
            }
        });

    }
    private void setAnimVis() {

        lottieAnimationView.setVisibility(View.GONE);
        lotterelay.setVisibility(View.GONE);
    }


    public static Context getContext() {
        return  ctx;
    }
}

package zulfiqar.com.passwordauth;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.sdsmdg.tastytoast.TastyToast;

public class UserProfile extends AppCompatActivity {



    private TextView username ;
    private FirebaseAuth fbAuth;
    private FirebaseUser user;
    private TextView password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.passwordchange);

        fbAuth = FirebaseAuth.getInstance();

        user  = fbAuth.getCurrentUser();


    }
    public void update(View view){

        fbAuth = FirebaseAuth.getInstance();

        user  = fbAuth.getCurrentUser();

        String changeReq =  username.getText().toString();

        boolean proceed  =   test(changeReq);

        if(proceed) {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName("" + username.getText())
                    .build();

            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    TastyToast.makeText(getApplicationContext(),
                            "Profile updated.",
                            Toast.LENGTH_SHORT ,TastyToast.INFO).show();

                    finish();

                }
            });

        }

    }
    public void changePassword(final View view){

        String pass = password.getText().toString();



//         boolean proceed  =   test(pass);

        if (password.length() < 6) {

            password.setError("Password must be at least 6 characters");

            return;
        }

            user = fbAuth.getCurrentUser();

            user.updatePassword(pass)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                signOut(view);
                                TastyToast.makeText(getApplicationContext(),
                                        "Password updated",
                                        Toast.LENGTH_SHORT, TastyToast.DEFAULT).show();

                                Intent intent = new Intent(getApplicationContext(), PasswordAuthActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
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

    public void delete(final View view){


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(UserProfile.this);
        builder1.setMessage("Sure want to Delete Account !")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            TastyToast.makeText(getApplicationContext(),
                                                    "Account deleted",
                                                    Toast.LENGTH_SHORT,TastyToast.ERROR).show();
                                            signOut(view);
                                            Intent intent = new Intent(getApplicationContext() , PasswordAuthActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });

        android.support.v7.app.AlertDialog alert1 =builder1.create();
        alert1.setTitle("Delete Account");
        alert1.show();



    }
    public void signOut(View view) {
        fbAuth.signOut();
    }

}

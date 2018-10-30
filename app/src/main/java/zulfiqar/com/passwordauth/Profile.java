package zulfiqar.com.passwordauth;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {


    private Button notify ,upload , accountSettings , changeChannel;
    private TextView usermail , user , onName ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        getActivity().setTitle("Al Ansaar"+"\t\t\t\t"+" الأنصار ");

        initialise(view);
        initialiseClicks(view);
        setUsers(view);

    }

    private void initialiseClicks(View view) {

        FirebaseAuth fbAuth = FirebaseAuth.getInstance();

        FirebaseUser user  = fbAuth.getCurrentUser();


        if (user.getEmail().equals(Constants.EMAIL_ADMIN)) {

//            notify.setVisibility(View.VISIBLE);
//            upload.setVisibility(View.VISIBLE);
//            changeChannel.setVisibility(View.VISIBLE);

            notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startIntent( Notify.class);

                }
            });

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startIntent( HomeChooser.class);

                }
            });

            changeChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startIntent(ChangeChannelActivity.class);


                }
            });
        }
        else{

//            notify.setVisibility(View.GONE);
//            upload.setVisibility(View.GONE);
//            changeChannel.setVisibility(View.GONE);
            notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    notAdmin();
                }
            });

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   notAdmin();
                }
            });

            changeChannel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   notAdmin();
                }
            });
        }



        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               startIntent(UserProfile.class);

            }
        });



    }

    private void startIntent(Class cass) {
        Intent intent = new Intent(getActivity() , cass);
        startActivity(intent);
    }

    private void notAdmin() {
        TastyToast.makeText(getActivity() ,"Admin access" ,TastyToast.LENGTH_SHORT ,TastyToast.DEFAULT).show();
    }

    private void initialise(View view) {

        user = (TextView) view.findViewById(R.id.users);
        onName = (TextView) view.findViewById(R.id.textView);
        usermail = (TextView) view.findViewById(R.id.useremail);
        notify = (Button) view.findViewById(R.id.notify);
        upload = (Button) view.findViewById(R.id.upload);
        accountSettings =  (Button) view.findViewById(R.id.updateprofile);
        changeChannel = (Button) view.findViewById(R.id.changeChannel);
    }

    public void setUsers(View view) {

        FirebaseAuth fbAuth = FirebaseAuth.getInstance();

        FirebaseUser users = fbAuth.getInstance().getCurrentUser();

        String name = users.getDisplayName();

        String email = users.getEmail();

        user.setText(name);
        usermail.setText(email);
        onName.setText(""+email.charAt(0));

    }


}

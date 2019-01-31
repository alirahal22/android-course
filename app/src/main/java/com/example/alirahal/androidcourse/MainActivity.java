package com.example.alirahal.androidcourse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements ListFragment.OnContactSelectListener, ListFragment.LogoutListener, ContactFragment.OnDismissListener{
    ListFragment listFragment;
    ContactFragment contactFragment;
    DatabaseHandler databaseHandler;

    static String REMEMBER_ME_PREFERENCES= "RememberMeFile";

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("user");
        databaseHandler = new DatabaseHandler(this);
        setContentView(R.layout.activity_main);

        listFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        listFragment.setArguments(args);
        contactFragment = new ContactFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.listFrameLayout, listFragment)
                .commit();

        if (findViewById(R.id.contactFrameLayout) != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contactFrameLayout,contactFragment)
                    .commit();
        }

    }


    @Override
    public void onContactSelectedListener(Contact contact) {
        if (findViewById(R.id.contactFrameLayout) != null){
            contactFragment.displayContact(contact);
        }
        else {
            Bundle args = new Bundle();
            args.putSerializable("contact", contact);
            contactFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.listFrameLayout, contactFragment)
                    .commit();
        }
    }

    @Override
    public void onLogoutRequestListener() {
        getSharedPreferences(REMEMBER_ME_PREFERENCES, MODE_PRIVATE).edit().clear().commit();
        finish();
    }

    @Override
    public void onDoneButtonClickedListener() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.listFrameLayout, listFragment)
                .commit();
    }
}

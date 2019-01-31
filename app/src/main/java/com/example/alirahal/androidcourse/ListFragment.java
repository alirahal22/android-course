package com.example.alirahal.androidcourse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ListFragment extends Fragment {

    OnContactSelectListener mainActivity;




    public interface OnContactSelectListener {
        void onContactSelectedListener(Contact contact);
    }

    public interface LogoutListener {
        void onLogoutRequestListener();
    }

    DatabaseHandler databaseHandler;
    User user;

    ListView listView;
    ArrayAdapter<Contact> arrayAdapter;

    TextView noContactsTextView;

    static boolean RELOADED = true;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mainActivity = (OnContactSelectListener) getActivity();

        databaseHandler = new DatabaseHandler(getActivity());
        listView = getView().findViewById(R.id.listView);
        noContactsTextView = getView().findViewById(R.id.noContactsTextView);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }
        //TODO: Remove
        if (user.getContactArrayList().size() == 0)
            databaseHandler.getContacts(user);
//            user.getContactArrayList().add(new Contact("Ali Rahal", "03727679"));

        if (user.getContactArrayList().size() > 0)
            noContactsTextView.setVisibility(View.INVISIBLE);

        listView.setOnItemClickListener((parent, view, position, id) -> mainActivity.onContactSelectedListener(user.getContactArrayList().get(position)));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deleteContact(user.getContactArrayList().get(position));
                user.getContactArrayList().remove(position);
                arrayAdapter.notifyDataSetChanged();
                listView.clearFocus();

                if (user.getContactArrayList().size() == 0)
                    noContactsTextView.setVisibility(View.VISIBLE);

                return false;
            }
        });

        loadContacts();


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.contacts_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addContactMenuItem:
                addContact();
                break;
            case R.id.logoutMenuItem:
                logout();
                break;
        }
        return true;
    }

    public void loadContacts() {
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, user.getContactArrayList());
        listView.setAdapter(arrayAdapter);
    }

    public void addContact() {
        View dialogView = getLayoutInflater().inflate(R.layout.add_contact_dialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Add Contact");
        alertDialogBuilder.setView(dialogView);
        AlertDialog alertDialog = alertDialogBuilder.create();

        dialogView.findViewById(R.id.addButton).setOnClickListener( e -> {
            String name = ((EditText) dialogView.findViewById(R.id.newContactName)).getText().toString();
            String number = ((EditText) dialogView.findViewById(R.id.newContactNumber)).getText().toString();

            databaseHandler.addContact(user, new Contact(name), number);
            user.getContactArrayList().add(new Contact(name, number));
            arrayAdapter.notifyDataSetChanged();
            noContactsTextView.setVisibility(View.INVISIBLE);
            alertDialog.dismiss();
        });

        dialogView.findViewById(R.id.cancelButton).setOnClickListener( e -> {
            alertDialog.dismiss();
        });

        alertDialog.show();

    }

    public void deleteContact(Contact contact) {
        databaseHandler.deleteContact(contact);
    }

    public void logout() {

        ((LogoutListener) mainActivity).onLogoutRequestListener();
    }
}

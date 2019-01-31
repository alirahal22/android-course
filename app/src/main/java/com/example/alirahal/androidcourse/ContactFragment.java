package com.example.alirahal.androidcourse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ContactFragment extends Fragment {

    OnDismissListener mainActivity;

    public interface OnDismissListener {
        void onDoneButtonClickedListener();
    }

    DatabaseHandler databaseHandler;
    Contact contact;

    TextView nameTextView;
    TextView noContactSelectedTextView;
    ListView numbersListView;
    ArrayAdapter<String> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mainActivity = (OnDismissListener) getActivity();

        databaseHandler = new DatabaseHandler(getActivity());

        nameTextView = getView().findViewById(R.id.contactNameTextView);
        noContactSelectedTextView = getView().findViewById(R.id.noContactSelectedTextView);
        numbersListView = getView().findViewById(R.id.numbersListView);

        if (getArguments() != null) {
            contact = (Contact) getArguments().getSerializable("contact");
            displayContact(contact);

        }
        getView().findViewById(R.id.addNumberButton).setOnClickListener( e-> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Add Number");
            EditText numberEditText = new EditText(getActivity());
            alert.setView(numberEditText);

            alert.setPositiveButton("Add", (dialog, which) -> {
                databaseHandler.addNumber(contact, numberEditText.getText().toString());
                contact.getNumbers().add(numberEditText.getText().toString());
                arrayAdapter.notifyDataSetChanged();
            });
            alert.setNegativeButton("Cancel", (dialog, which) -> {

            });

            alert.show();
        });

        getView().findViewById(R.id.doneButton).setOnClickListener( e -> {
            mainActivity.onDoneButtonClickedListener();
        });


    }

    public void displayContact(Contact contact) {
        this.contact = contact;
        noContactSelectedTextView.setVisibility(View.INVISIBLE);
        nameTextView.setText(contact.getName());
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, contact.getNumbers());
        numbersListView.setAdapter(arrayAdapter);
    }
}

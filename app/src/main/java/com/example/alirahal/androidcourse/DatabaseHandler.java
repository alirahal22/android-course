package com.example.alirahal.androidcourse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    static String DATABASE_NAME = "AndroidCourseDB";

    static String USER_TABLE = "User";
    static String USERNAME_COLUMN = "username", PASSWORD_COLUMN = "password", USER_ID_COLUMN = "user_id";

    static String CONTACTS_TABLE = "Contact";
    static String NAME_COLUMN = "name", CONTACT_ID_COLUMN = "contact_id";

    static String NUMBER_TABLE = "Number";
    static String NUMBER_ID_COLUMN = "number_id", NUMBER_COLUMN = "number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables();
        createTables(db);
    }

    public void createTables(SQLiteDatabase db) {
        String usersTableQuery =
                "Create Table If Not Exists " + USER_TABLE +
                "(" +
                    USER_ID_COLUMN + " integer Primary Key Autoincrement, " +
                    USERNAME_COLUMN + " text Not Null, " +
                    PASSWORD_COLUMN + " text Not Null" +
                ")";

        String contactsTableQuery =
                "Create Table " + CONTACTS_TABLE +
                "(" +
                    CONTACT_ID_COLUMN + " integer Primary Key Autoincrement, " +
                    NAME_COLUMN + " text Not Null, " +
                    USER_ID_COLUMN + " integer, " +
                    "Constraint contact_constraint Foreign Key (" + CONTACT_ID_COLUMN + ") References " + CONTACTS_TABLE + "(" + CONTACT_ID_COLUMN + ")" +
                ")";

        String numbersTableQuery = "Create Table " + NUMBER_TABLE +
                "(" +
                    NUMBER_ID_COLUMN + " integer Primary Key Autoincrement, " +
                    NUMBER_COLUMN + " text Not Null, " +
                    CONTACT_ID_COLUMN + " integer, " +
                    "Constraint contact_constraint Foreign Key (" + CONTACT_ID_COLUMN + ") References " + CONTACTS_TABLE + "(" + CONTACT_ID_COLUMN + ")" +
                ")";

        db.execSQL(usersTableQuery);
        db.execSQL(contactsTableQuery);
        db.execSQL(numbersTableQuery);
    }

    public User getUser(User user) {
        String query = "Select " + USER_ID_COLUMN +
                " From " + USER_TABLE +
                " Where " + USERNAME_COLUMN + "='" + user.getUsername() + "' and " + PASSWORD_COLUMN + " = '" + user.getPassword() + "'";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst())
            user.setUser_id(cursor.getInt(0));
        return user;
    }


    public boolean usernameTaken(String username) {
        String query = "Select " + USERNAME_COLUMN +
                " From " + USER_TABLE +
                " Where " + USERNAME_COLUMN + "='" + username + "'";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        if (cursor.getCount() != 0)
            return true;
        return false;
    }

    public boolean signUp(User user) {
        if(!usernameTaken(user.getUsername())) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(USERNAME_COLUMN, user.getUsername());
            contentValues.put(PASSWORD_COLUMN, user.getPassword());
            getWritableDatabase().insert(USER_TABLE, null, contentValues);

            return true;
        }
        return false;
    }

    public void getContacts(User user) {
        String query = "Select * " +
                "From " + CONTACTS_TABLE + " Join " + USER_TABLE +
                " Where " + CONTACTS_TABLE + "." + USER_ID_COLUMN + " = " + USER_TABLE + "." + USER_ID_COLUMN +
                " and " + USER_TABLE + "." + USER_ID_COLUMN + " = " + user.getUser_id();

        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToFirst())
            do {
                Contact contact = new Contact(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
                contact.setContact_id(cursor.getInt(cursor.getColumnIndex(CONTACT_ID_COLUMN)));
                getNumbers(contact);
                user.getContactArrayList().add(contact);
            }while (cursor.moveToNext());

    }

    public void getNumbers(Contact contact) {
        String query = "Select * " +
                "From " + NUMBER_TABLE +
                " Where " + CONTACT_ID_COLUMN + " = '" + contact.getContact_id() + "'";

        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        if (cursor.moveToFirst())
            do {
                contact.getNumbers().add(cursor.getString(cursor.getColumnIndex(NUMBER_COLUMN)));
            }while (cursor.moveToNext());
    }

    public void dropTables() {
        getWritableDatabase().rawQuery("Drop Table If Exists " + USER_TABLE, null);
        getWritableDatabase().rawQuery("Drop Table If Exists " + CONTACTS_TABLE, null);
        getWritableDatabase().rawQuery("Drop Table If Exists " + NUMBER_TABLE, null);
    }

    public void addContact(User user, Contact contact, String number) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN, contact.getName());
        contentValues.put(USER_ID_COLUMN, user.getUser_id());
        getWritableDatabase().insert(CONTACTS_TABLE, null, contentValues);

        String getAddedIdQuery = "Select seq From sqlite_sequence Where name='" + CONTACTS_TABLE + "'";
        Cursor cursor = getReadableDatabase().rawQuery(getAddedIdQuery, null);

        if (cursor.moveToFirst()){
            int contactid = cursor.getInt(0);
            contact.setContact_id(contactid);
            addNumber(contact, number);

        }
    }

    public void addNumber(Contact contact, String number) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_ID_COLUMN, contact.getContact_id());
        contentValues.put(NUMBER_COLUMN, number);

        getWritableDatabase().insert(NUMBER_TABLE, null, contentValues);
    }

    public void deleteContact(Contact contact) {
        getWritableDatabase().delete(CONTACTS_TABLE,  CONTACT_ID_COLUMN + "= ?", new String[]{String.valueOf(contact.getContact_id())});
        getWritableDatabase().delete(NUMBER_TABLE,  CONTACT_ID_COLUMN + "= ?", new String[]{String.valueOf(contact.getContact_id())});
    }

}























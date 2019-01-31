package com.example.alirahal.androidcourse;

import java.io.Serializable;
import java.util.ArrayList;

public class Contact implements Serializable {

    private int contact_id;
    private String name;
    private ArrayList<String> numbers = new ArrayList<>();

    public Contact(String name) {
        this.name = name;
    }

    public Contact(String name, String number) {
        this.name = name;
        numbers.add(number);
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

    @Override
    public String toString() {
        return name;
    }
}

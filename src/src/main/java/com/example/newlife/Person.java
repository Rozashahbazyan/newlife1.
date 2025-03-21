package com.example.newlife;

public class Person {
    private String name;
    private String habit;

    // Default constructor required for calls to DataSnapshot.getValue(Person.class)
    public Person() {}

    public Person(String name, String habit) {
        this.name = name;
        this.habit = habit;
    }

    public String getName() {
        return name;
    }

    public String getHabit() {
        return habit;
    }
}

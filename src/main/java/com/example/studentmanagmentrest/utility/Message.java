package com.example.studentmanagmentrest.utility;

public class Message {

    public static String courseNotExist(String name) {
        return "Course " + name + " cannot be found in the database.";
    }

    public static String studentNotExists(String name) { return "Student " + name + " cannot be found in the database."; }

    public static String teacherNotExist(String name) {return "Teacher " + name + " cannot be found in the database.";}


}

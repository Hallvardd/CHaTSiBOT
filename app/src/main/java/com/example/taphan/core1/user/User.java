package com.example.taphan.core1.user;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class User {

    //TODO: make code more secure in case of invalid data entry/access.
    private String userID; // this is the key and must therefore be unique.
    private String userType;
    private HashMap<String, ArrayList<String>> questionsAsked;
    private ArrayList<String> uCourses;
    private String email;

    public User(){
        questionsAsked = new HashMap<>();
        uCourses = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public HashMap<String, ArrayList<String>> getQuestionsAsked() {
        return questionsAsked;
    }

    public void setQuestionsAsked(HashMap<String, ArrayList<String>> questionsAsked) {
        this.questionsAsked = questionsAsked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public ArrayList<String> getuCourses() {
        return uCourses;
    }

    public void setuCourses(ArrayList<String> uCourses) {
        this.uCourses = uCourses;
    }

    public void addCourse(String course){
        uCourses.add(course);
    }

    public void putQuestion(String courseCode, String questionID){
        // Checks if a entry already exists
        if(questionsAsked.containsKey(courseCode)){
            // If the question already is in the list it is not added
            if(!questionsAsked.get(courseCode).contains(questionID)) {
                questionsAsked.get(courseCode).add(questionID);
            }
        }
        // If there is no entry corresponding to the courseCode, an new entry and ArrayList is made.
        else{
            ArrayList<String> questionIDList = new ArrayList<>();
            questionIDList.add(questionID);
            questionsAsked.put(courseCode,questionIDList);
        }

    }
}

package com.example.csci310_group15;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
* USED TO SETUP DATA BASE
* Only use when FIRST SETTING UP or MAKE CHANGE TO DB
* */

public class DB_ClassSetup {

    private DatabaseReference mRootRef;
    private ArrayList<String> departments = new ArrayList<>();
    private HashMap<String, ArrayList<Class>> departmentMap = new HashMap<>();

    public void LoadClassToDB()
    {
        // Prepare data
        Init();

        // Load to DB
        // Get the root department node
        mRootRef = FirebaseDatabase.getInstance().getReference("departments");
        for(Map.Entry<String, ArrayList<Class>> entry : departmentMap.entrySet())
        {
            // Get the specific department node
            DatabaseReference departmentRef = mRootRef.child(entry.getKey());

            for(Class currClass : entry.getValue())
            {
                DatabaseReference classRef = departmentRef.child(currClass.getId());
                classRef.setValue(currClass);
            }
        }

    }

    private void Init()
    {
        // AME Setup
        ArrayList<Class> AME = new ArrayList<>();
        AME.add(new Class("1", "Aerospace and Mechanical Engineering", "AME-201", "Potnuru, Akshay", "MW 09:00 - 10:50"));
        AME.add(new Class("2", "Aerospace and Mechanical Engineering", "AME-204", "Plucinsky, Paul", "TTH 15:30 - 16:50"));
        AME.add(new Class("3", "Aerospace and Mechanical Engineering", "AME-261", "Byahut, Saakar", "TTH 10:00 - 11:50"));
        AME.add(new Class("4", "Aerospace and Mechanical Engineering", "AME-301", "Shiflett, Geoffrey", "MWF 09:00 - 09:50"));
        AME.add(new Class("5", "Aerospace and Mechanical Engineering", "AME-301", "Maghsoodi, Neda", "TTH 09:00 - 10:20"));
        AME.add(new Class("6", "Aerospace and Mechanical Engineering", "AME-302", "Yang, Bingen", "TTH 14:00 - 15:20"));
        // BME Setup
        ArrayList<Class> BME = new ArrayList<>();
        BME.add(new Class("1", "Biomedical Engineering", "BME-201", "Yamashiro, Stan", "W 12:00 - 13:50"));
        BME.add(new Class("2", "Biomedical Engineering", "BME-210", "D'Argenio, David", "MW 14:00 - 15:50"));
        BME.add(new Class("3", "Biomedical Engineering", "BME-210", "Kay, Brittany", "MW 14:00 - 15:50"));
        BME.add(new Class("4", "Biomedical Engineering", "BME-403L", "Yamashiro, Stan", "MW 08:00 - 09:20"));
        BME.add(new Class("5", "Biomedical Engineering", "BME-406", "Shen, Keyue", "TTH 14:00 - 15:50"));
        BME.add(new Class("6", "Biomedical Engineering", "BME-410L", "McCain, Megan", "TTH 09:30 - 10:50"));
        // CSCI Setup
        ArrayList<Class> CSCI = new ArrayList<>();
        CSCI.add(new Class("1", "Computer Science","CSCI-103", "Redekopp, Mark", "TTH 08:00 - 09:20"));
        CSCI.add(new Class("2", "Computer Science","CSCI-104", "Goodney, Andrew", "TTH 11:00 - 12:20"));
        CSCI.add(new Class("3", "Computer Science","CSCI-170", "Cote, Aaron", "TTH 09:30 - 10:50"));
        CSCI.add(new Class("4", "Computer Science","CSCI-270", "Kempe, David", "MW 12:30 - 13:50"));
        CSCI.add(new Class("5", "Computer Science","CSCI-310", "Halfond, William", "MW 10:00 - 11:50"));
        CSCI.add(new Class("6", "Computer Science","CSCI-356", "Paolieri, Marco", "TTH 15:30 - 16:50"));

        // DS Setup
        ArrayList<Class> DS = new ArrayList<>();
        DS.add(new Class("1", "Data Science","DS-351", "Wu, Wensheng", "MW 10:00 - 11:50"));
        DS.add(new Class("2", "Data Science","DS-352", "Pynadath, David", "TH 15:30 - 18:50"));
        DS.add(new Class("3", "Data Science","DS-352", "Morstatter, Fred", "MW 16:00 - 17:50"));
        DS.add(new Class("4", "Data Science","DS-454", "Chan, David", "T 18:00 - 21:20"));
        DS.add(new Class("5", "Data Science","DS-454", "Chan, David", "T 15:00 - 17:20"));
        DS.add(new Class("6", "Data Science","DS-525", "Ryutov, Tanya", "M 14:00 - 17:20"));

        // EE Setup
        ArrayList<Class> EE = new ArrayList<>();
        EE.add(new Class("1", "Electrical and Computer Engineering","EE-109", "", "TTH 14:00 - 15:20"));
        EE.add(new Class("2", "Electrical and Computer Engineering","EE-109", "", "TTH 09:30 - 10:50"));
        EE.add(new Class("3", "Electrical and Computer Engineering","EE-109", "", "TTH 12:30 - 13:50"));
        EE.add(new Class("4", "Electrical and Computer Engineering","EE-109", "", "TTH 11:00 - 12:20"));
        EE.add(new Class("5", "Electrical and Computer Engineering","EE-202L", "Schober, Susan", "TTH 10:00 - 11:50"));
        EE.add(new Class("6", "Electrical and Computer Engineering","EE-250", "Krishnamachari, Bhaskar", "MW 16:00 - 17:50"));

        // Combine
        departmentMap.put("Aerospace and Mechanical Engineering", AME);
        departmentMap.put("Biomedical Engineering", BME);
        departmentMap.put("Computer Science", CSCI);
        departmentMap.put("Data Science", DS);
        departmentMap.put("Electrical and Computer Engineering", EE);

    }
}


class Class
{
    private String id;
    private String department;
    private String num;
    private String instructor;
    private String time;

    // ============================================ [ Constructor ]
    public Class(){
        this.id = "";
        this.department = "";
        this.num = "";
        this.instructor = "";
        this.time = "";
    }

    public Class(String id, String department, String num, String instructor, String time) {
        this.id = id;
        this.department = department;
        this.num = num;
        this.instructor = instructor;
        this.time = time;
    }


    // ============================================ [ Getter/Setter ]


    public String getId() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
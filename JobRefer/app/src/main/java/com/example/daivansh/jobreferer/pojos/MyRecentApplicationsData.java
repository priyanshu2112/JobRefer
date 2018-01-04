package com.example.daivansh.jobreferer.pojos;

/**
 * Created by daivansh on 09-06-2017.
 */
public class MyRecentApplicationsData {
    String jobtitle;
    String salary;
    String date;
    String location;
    String experience;
    String jdescription;
    public MyRecentApplicationsData(String jobtitle, String salary, String date, String location, String exp, String desc)
    {
        this.setJobtitle(jobtitle);
        this.setSalary(salary);
        this.setDate(date);
        this.setLocation(location);
        this.setExperience(exp);
        this.setJdescription(desc);
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setJdescription(String jdescription) {
        this.jdescription = jdescription;
    }

    public String getJdescription() {
        return jdescription;
    }

    public String getExperience() {
        return experience;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public String getSalary() {
        return salary;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}

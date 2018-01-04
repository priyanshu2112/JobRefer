package com.example.daivansh.jobreferer.pojos;

import java.util.Comparator;

/**
 * Created by daivansh on 09-06-2017.
 */
public class MySearchHomeData {
    String jobtitle;
    String salary;
    String date;
    String location;
    String experience;
    String jdescription;
    String jobid;
    public MySearchHomeData(String jobtitle, String salary, String date, String location,String exp,String jid,String desc)
    {
        this.setJobtitle(jobtitle);
        this.setSalary(salary);
        this.setDate(date);
        this.setLocation(location);
        this.setExperience(exp);
        this.setJobid(jid);
        this.setJdescription(desc);
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setJdescription(String jdescription) {
        this.jdescription = jdescription;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getJdescription() {
        return jdescription;
    }

    public String getExperience() {
        return experience;
    }

    public String getJobid() {
        return jobid;
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

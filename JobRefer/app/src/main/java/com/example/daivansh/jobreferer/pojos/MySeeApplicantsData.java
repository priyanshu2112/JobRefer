package com.example.daivansh.jobreferer.pojos;

/**
 * Created by daivansh on 10-06-2017.
 */

public class MySeeApplicantsData {
    String applicantname;
    String applicantemailid;
    String applicantcontactno;
    public MySeeApplicantsData(String name, String contact, String mail)
    {
        this.setApplicantname(name);
        this.setApplicantemailid(mail);
        this.setApplicantcontactno(contact);
    }

    public void setApplicantname(String applicantname) {
        this.applicantname = applicantname;
    }

    public void setApplicantemailid(String applicantemailid) {
        this.applicantemailid = applicantemailid;
    }

    public void setApplicantcontactno(String applicantcontactno) {
        this.applicantcontactno = applicantcontactno;
    }

    public String getApplicantname() {
        return applicantname;
    }

    public String getApplicantcontactno() {
        return applicantcontactno;
    }

    public String getApplicantemailid() {
        return applicantemailid;
    }

}

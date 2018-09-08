package gemalto.com.gemaltouser.util;

/**
 * Created by Manuramv on 9/8/2018.
 */

public class Employee {
    private long empId;
    private String firstname;
    private String lastname;
    private String gender;
    private String email;
    private String dob;

    public Employee(long empId, String firstname, String lastname, String gender, String hiredate, String dept){
        this.empId = empId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.email = hiredate;
        this.dob = dept;

    }

    public Employee(){

    }

    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }





    public String toString(){
        return "Emp id: "+getEmpId()+ "\n" +
                "Name: "+getFirstname() + " " + getLastname() + "\n" +
                "email: "+getEmail() + "\n" +
                "Dob : "+getDob();


    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}

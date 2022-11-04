package org.example;

public class Profile {

   private Long id;
   private String name;
   private UserStep step;
   Integer year;
   Integer month;
   Integer day;


    public Profile(Long id,UserStep step) {
        this.id = id;
        this.step = step;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserStep getStep() {
        return step;
    }

    public void setStep(UserStep step) {
        this.step = step;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}

package playground.testPrograms;

import java.util.List;

public class Student {

    private int SID;
    private String name;
    private List<Course> courses;
    private int age;

    public Student(int id, List<Course> c, int age){
        SID = id;
        this.name = "Student " + id;
        courses = c;
        this.age = age;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public int getAge() {
        return age;
    }

    public int getSID() {
        return SID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ": Aged - " + age + ", On courses: " + courses + "\n";
    }
}

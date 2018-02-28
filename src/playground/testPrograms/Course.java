package playground.testPrograms;

public class Course {

    private int CID;
    private String courseName;
    private String faculty;
    private int passMark;

    public Course(int id, String name, String faculty, int pass){
        CID = id;
        courseName = name;
        this.faculty = faculty;
        passMark = pass;
    }

    public int getCID() {
        return CID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getFaculty() {
        return faculty;
    }

    public int getPassMark() {
        return passMark;
    }

    @Override
    public String toString() {
        return courseName + " " + faculty + ": Require " + passMark + " marks";
    }
}

package playground.testPrograms;

import blaq.core.Enumerable;
import blaq.util.BlaqIterable;
import blaq.util.BlaqList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuerySchool {

    public static Random rand = new Random();

    public static List<Student> getStudentList(){

        List<Student> students = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            List<Course> courses = getCourseList();

            for(int j = 0; j < 5; j++)
                courses.remove(rand.nextInt(5));
            students.add(new Student(i, courses, 16 + rand.nextInt(8)));
        }

        return students;
    }

    public static List<Course> getCourseList(){
        String[] facs = {"EECS", "Law", "BioMed", "Medicine", "Extreme Trolling", "Mathematics"};

        List<Course> c = new ArrayList<>();

        for(int i = 0; i < 10; i++)
            c.add(new Course(i, "Course " + i, facs[rand.nextInt(6)], i * rand.nextInt(35)));

        return c;
    }

    public static List<Grade> getGradeList(){
        return null;
    }


    public static void main(String[] args) {
        BlaqIterable<Student> s = Enumerable.asBlaqIterable(getStudentList());
        System.out.println(s);

    }
}

package playground.testPrograms;

import blaq.core.Enumerable;
import blaq.util.BlaqIterable;
import blaq.util.BlaqList;
import blaq.util.IOrderedIterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuerySchool {

    private static Random rand = new Random();

    private static List<Student> getStudentList(){

        List<Student> students = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            List<Course> courses = getCourseList();

            for(int j = 0; j < rand.nextInt(8); j++)
                courses.remove(rand.nextInt(5));
            students.add(new Student(i, courses, 16 + rand.nextInt(8)));
        }

        return students;
    }

    private static List<Course> getCourseList(){
        String[] facs = {"EECS", "Law", "BioMed", "Medicine", "Extreme Trolling", "Mathematics"};

        List<Course> c = new ArrayList<>();

        for(int i = 0; i < 10; i++)
            c.add(new Course(i, "Course " + i, facs[rand.nextInt(6)], (i+1) * rand.nextInt(35)));

        return c;
    }

    private static List<Grade> getGradeList(){
        List<Grade> g = new ArrayList<>();
        for(int i = 0; i < 20; i++)
            g.add(new Grade(rand.nextInt(6), rand.nextInt(10), (int)(2.5*(rand.nextInt(45)+1))));
        return g;
    }


    public static void main(String[] args) {
        BlaqIterable<Student> s = Enumerable.asBlaqIterable(getStudentList());
        IOrderedIterable<Student> orderedS = s.orderByDescending(Student::getAge);//.thenBy(x -> x.getCourses().size());
        List<Grade> grades = getGradeList();

        // simple queries
        System.out.println("\nSimple Queries\n");
        System.out.println("List 1: " + s);
        System.out.println(s.where(x -> x.getAge() > 18));
        BlaqIterable<Course> c = Enumerable.asBlaqIterable(getCourseList());
        System.out.println("List 2: " + c);
        System.out.println(c.project(x -> x.passMark/3)); // get 70% of the passmark

        // sorting queries
        System.out.println("\nSorting Queries\n");
        System.out.println(orderedS.toList());
        System.out.println(orderedS.thenByDescending(x -> x.getCourses().size()).toList());
        List<Student> st = getStudentList();
        BlaqIterable<Student> students = Enumerable.asBlaqIterable(st);

        // complex queries
        System.out.println("\nComplex Queries\n");

        int avgs = students
                .where(student -> student.getAge() < 20)
                .join(grades, Student::getSID, Grade::getSID,
                        (student, grade) -> grade.getGrade() > 70)
                .count();
        System.out.println("Average: " + avgs);

        // without BLAQ Collection
        int avgs2 = Enumerable.count(
                Enumerable.join(
                Enumerable.where(students, student -> student.getAge() < 20),
                grades,
                student -> student.getSID(),
                grade -> grade.getSID(),
                (student, grade) -> grade.getGrade() > 70));
        System.out.println("Average 2: " + avgs2);


    }

    private static class Student {

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

        public void setName(String s){ name = s; }

        @Override
        public String toString() {
            return name + ": Aged - " + age + ", On courses: " + courses + "\n";
        }
    }

    private static class Course {

        private int CID;
        private String courseName;
        private String faculty;
        public int passMark;

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

    private static class Grade {

        private int SID;
        private int CID;
        private int grade;

        public Grade(int sid, int cid, int g){
            SID = sid;
            CID = cid;
            grade = g;
        }

        public int getGrade() {
            return grade;
        }

        public int getCID() {
            return CID;
        }

        public int getSID() {
            return SID;
        }
    }
}

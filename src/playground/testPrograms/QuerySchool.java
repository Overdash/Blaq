package playground.testPrograms;

import blaq.core.Enumerable;
import blaq.util.*;

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

    private static List<Grade> getGradeList(Iterable<Student> students){
        List<Grade> g = new ArrayList<>();
        for(Student s : students){
            Iterable<Course> courses = s.courses;
            for(Course c : courses)
                g.add(new Grade(s.SID, c.CID, (int)(2.5*(rand.nextInt(45)+1))));
        }
        return g;
    }


    public static void main(String[] args) {
        // TODO Task 1: Obtain sequences
        // TODO - You can create a BlaqIterable using Enumerable.asBlaqIterable(src)(optional)

        BlaqIterable<Student> students = Enumerable.asBlaqIterable(getStudentList());
        BlaqIterable<Course> courses = Enumerable.asBlaqIterable(getCourseList());
        BlaqIterable<Grade> grades = Enumerable.asBlaqIterable(getGradeList(students));

        // TODO Task 2: simple queries.
        // TODO Get all the students over the age of 18 & Show 70% of the passmarks of all courses.
        System.out.println("\n**Simple Queries**\n");

        System.out.println("Students over 18: " + students.where(s -> s.age > 18));
        System.out.println("70% of pass marks: " + courses.project(c -> (int)(c.passMark * 0.7)));

        // TODO Task 3: sorting queries
        // TODO Sort the students in order of their age, then by the number of the courses they are enrolled in (lowest to highest)
        System.out.println("\n**Sorting Queries**\n");

        IOrderedIterable<Student> orderedAge = students.orderBy(s -> s.age);
        System.out.println("Students ordered by age: " + orderedAge.toList());
        System.out.println("Students order by age, then by the # of enrolled courses: " +
                orderedAge.thenByDescending(s -> s.courses.size()).toList());

        // TODO Task 4: complex queries
        // TODO Capture the number of students younger than 20, and with an overall grade of 70 or above
        System.out.println("\n**Complex Queries**\n");

        int avgs = students
                .where(student -> student.age < 20)
                .join(grades, student -> student.SID, grade -> grade.SID,
                        (student, grade) -> grade.grade >= 70)
                .count(); // Query here
        System.out.println("Average: " + avgs);

        // TODO Task 5: Switch
        // TODO If you were using BlaqIterable, purely use Enumerable (and vice versa) and redo Task 4
        int avgs2 = Enumerable.count(
                Enumerable.join(
                        Enumerable.where(students, student -> student.age < 20),
                        grades,
                        student -> student.SID,
                        grade -> grade.SID,
                        (student, grade) -> grade.grade >= 70)); // Query here
        System.out.println("Average 2: " + avgs2);

        // TODO Task 6: Extremely Complex
        // TODO Get the top most student with the highest average score across all their courses,
        // TODO Then take the remaining top nine students and rank them
        // TODO based on the difference of their average against the overall average in the school, whilst only taking
        // TODO those who got higher than the overall average in consideration.
        System.out.println("\n**Final Task**\n");

        System.out.println(students
                .join(grades, student -> student.SID, grade -> grade.SID, Tuple2::new).toList());

        IOrderedIterable<Tuple2<Student, Grade>> sortedByGrades = students
                .join(grades, student -> student.SID, grade -> grade.SID, Tuple2::new)
                .orderBy(tuple -> tuple.getItem2().grade);

        Tuple2<Student, Grade> topPair = sortedByGrades.first();
        System.out.println("Top most student: " + topPair.getItem1() + " with grade: " + topPair.getItem2().grade);

        double overallAverage = sortedByGrades.average(x -> x.getItem2().grade);

        BlaqIterable<Tuple2<Student, Grade>> top9 = sortedByGrades.skip(1).take(9)
                .where(t -> t.getItem2().grade - overallAverage > 0);

        System.out.println("Ranked top 9: ");
        if(!top9.any())
            System.out.println("No top 9s above threshold");
        top9.forEach(e ->System.out.println(e.getItem1()));
    }

    private static class Student {

        private int SID;
        private String name;
        private List<Course> courses;
        private int age;
        private static String[] firstName = {"Yanira", "Virgil", "Adalberto", "Marjorie", "Sherrie", "Colene",
                "Glayds", "Nick", "Laurence", "Josef", "Larita", "Cathrine", "Liza", "Berna", "Quinton", "Effie",
                "Hsiu", "Hector", "Erlene", "Dewayne", "Chiquita", "Shandi", "Lavonda", "Jenee", "Denisse", "Krysten",
                "Bryanna", "Gerry", "Alline", "Graciela"};
        private static String[] lastName = {"Windholz", "Ferrier", "Dougan", "Anding", "Stults", "Nell", "Slaugh",
                "Padillo", "Pellegrini", "Creswell", "Mattison", "Victory", "Frady", "Antilla", "Kershner", "Renfrow",
                "Fite", "Dineen", "Giraud", "Perras", "Winters", "Henrichs", "Dobrowolski", "Heckler", "Rupe", "Toft",
                "Dimmitt", "Longino", "Mathieu", "Lucke"};

        public Student(int id, List<Course> c, int age){
            SID = id;
            this.name = firstName[rand.nextInt(firstName.length)] + " " + lastName[rand.nextInt(lastName.length)];
            courses = c;
            this.age = age;
        }

        @Override
        public String toString() {
            return "ID: " + SID + " ; Name: " + name + " ; Aged - " + age + " ; On " + courses.size() + " Courses \n";
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

        @Override
        public String toString() {
            return courseName + " " + faculty + ": Require " + passMark + " marks to pass";
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

        public String toString(){return "ID: " + SID + " Course ID: " + CID + " Grade: " + grade;}
    }
}

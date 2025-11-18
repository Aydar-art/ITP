package Assigment.Abs;

import java.util.ArrayList;
import java.util.List;

public class Course {
    final private int CAPACITY = 3;
//    private int numberOfCources;
    private int courseId;

    String courseName;
    private List<Student> students;
    CourseLevel courseLvl;

    public Course(String courseName, CourseLevel courseLvl, int courseId) {
        this.courseName = courseName;
        this.courseLvl = courseLvl;
        this.students = new ArrayList<>();
        this.courseId = courseId;
    }

    public int getId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public boolean removeMember(Student student) {
        if (students.contains(student)) {
            students.remove(student);
            return true;
        } else {
            System.out.println("Student is not enrolled in this course");
            return false;
        }
    }

    public boolean addMember(Student student) {
        if (students.size() < CAPACITY) {
            students.add(student);
            return true;
        }
        System.out.println("Course is full");
        System.exit(0);
        return false;
    }

    public boolean isFull() {
        return students.size() == CAPACITY;
    }

}

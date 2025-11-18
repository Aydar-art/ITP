package Assigment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import Assigment.Abs.*;

public class Main {
    public void main(String[] args) {
        Scanner input = new Scanner(System.in);

        List<Course> courses = fillInitialCourses();

        List<Student> students = fillInitialStudents(courses);
        List<Professor> professors = fillInitialProf(courses);

        String cmdLine = input.nextLine();


        int memberId = 7;

        while (!cmdLine.isEmpty()) {
            switch (cmdLine) {
                case "course":
                    Course newCourse = this.addCourse(courses);
                    courses.add(newCourse);
                    System.out.println("Added successfully");
                    break;

                case "student":
                    Student newStudent = this.addStudent(students, memberId);
                    memberId++;
                    students.add(newStudent);
                    System.out.println("Added successfully");
                    break;

                case "professor":
                    Professor newProfessor = this.addProfessor(professors, memberId);
                    memberId++;
                    professors.add(newProfessor);
                    System.out.println("Added successfully");
                    break;

                case "enroll":
                    if (enrollStudent(students, courses)) {
                        System.out.println("Enrolled successfully");
                    } else {
                        System.out.println("Wrong inputs");
                    }
                    break;

                case "drop":
                    if (dropStudent(students, courses)) {
                        System.out.println("Dropped successfully");
                    } else {
                        System.out.println("Wrong inputs");
                    }
                    break;

                case "teach":
                    if (assignProf(professors, courses)) {
                        System.out.println("Professor is successfully assigned to teach this course");
                    } else {
                        System.out.println("Wrong inputs");
                    }
                    break;

                case "exempt":
                    if (exemptProf(professors, courses)) {
                        System.out.println("Professor is exempted");
                    } else {
                        System.out.println("Wrong inputs");
                    }
                    break;

                default:
                    System.out.println("Wrong inputs");
                    System.exit(0);
                    break;
            }

            cmdLine = input.nextLine();
        }



    }

    public List<Course> fillInitialCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("java_beginner", CourseLevel.BACHELOR, 1));
        courses.add(new Course("java_intermediate", CourseLevel.BACHELOR, 2));
        courses.add(new Course("python_basics", CourseLevel.BACHELOR, 3));
        courses.add(new Course("algorithms", CourseLevel.MASTER, 4));
        courses.add(new Course("advanced_programming", CourseLevel.MASTER, 5));
        courses.add(new Course("mathematical_analysis", CourseLevel.MASTER, 6));
        courses.add(new Course("computer_vision", CourseLevel.MASTER, 7));

        return courses;
    }

    public List<Student> fillInitialStudents(List<Course> courses) {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Alice"));
        students.add(new Student(1, "Bob"));
        students.add(new Student(1, "Alex"));

        for (Student student : students) {
            if (student.getName().equals("Alice")) {
                student.enroll(courses.get(0));
                student.enroll(courses.get(1));
                student.enroll(courses.get(2));
            } else if (student.getName().equals("Bob")) {
                student.enroll(courses.get(0));
                student.enroll(courses.get(3));
            } else {
                student.enroll(courses.get(4));
            }
        }

        return students;
    }

    public List<Professor> fillInitialProf(List<Course> courses) {
        List<Professor> professors = new ArrayList<>();
        professors.add(new Professor(4, "Ali"));
        professors.add(new Professor(5, "Ahmed"));
        professors.add(new Professor(6, "Andrey"));

        for (Professor professor : professors) {
            if (professor.getId() == 4) {
                professor.teach(courses.get(0));
                professor.teach(courses.get(1));
            } else if (professor.getId() == 5) {
                professor.teach(courses.get(2));
                professor.teach(courses.get(4));
            } else {
                professor.teach(courses.get(5));
            }
        }

        return professors;
    }

    //
    // ================= FUNCTION TO ADD NEW COURSE =================
    //

    private Course addCourse(List<Course> courses) {
        Scanner input = new Scanner(System.in);

        Course plug = new Course("", CourseLevel.NONE, 0);

        String courseName = input.nextLine().toLowerCase();


        // --------------- CHECK IF COURSE NAME IS CORRECT ----------------------
        char[] chars = courseName.toCharArray();
        for (int i = 0; i < chars.length; i++) { // loop for every character of course name
            char el = chars[i];// element with index i

            if ((el >= 'a' && el <= 'z') || el == '_') { // check if el is permissible
                if (el == '_') { // if el equals _
                    if (i - 1 >= 0 && i + 1 < courseName.length()) { // if el is neither first nor last element
                        char el0 = courseName.toCharArray()[i - 1]; // previous character
                        char el2 = courseName.toCharArray()[i + 1]; // next character

                        if (!(el0 >= 'a' && el0 <= 'z' && el2 >= 'a' && el2 <= 'z')) { // if last and previous elements are not permissible
                            System.out.println("Wrong inputs");
                            System.exit(0);
                            return plug;
                        }
                    } else {
                        System.out.println("Wrong inputs");
                        System.exit(0);
                        return plug;
                    }
                }
            } else {
                System.out.println("Wrong inputs");
                System.exit(0);
                return plug;
            }
        }


        String courseString = input.nextLine().toLowerCase();

        CourseLevel courseLevel = CourseLevel.NONE;
        if (courseString.equals("bachelor")) {
            courseLevel = CourseLevel.BACHELOR;
        } else if (courseString.equals("master")) {
            courseLevel = CourseLevel.MASTER;
        }

        if (courseLevel == CourseLevel.NONE) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return plug;
        }

        Course course = new Course(courseName, courseLevel, courses.size() + 1);


        // ----------------- CHECK IF COURSE ALREADY EXISTS -----------------------
        for (Course course1 : courses) { // loop for every exist course
            if (course1.getCourseName().equals(course.getCourseName())) { // check if course with such name already exits
                System.out.println("Course exists");
                System.exit(0);
                return plug;
            }
        }


        return course;
    }


    //
    // ===================== FUNCTION TO ADD NEW STUDENT =======================
    //
    private Student addStudent(List<Student> members, int id) {
        Scanner input = new Scanner(System.in);

        String memberName = input.nextLine().toLowerCase();

        // --------------- CHECK IF COURSE NAME IS CORRECT ----------------------
        char[] chars = memberName.toCharArray();
        for (char el : memberName.toCharArray()) { // loop for every character of course
            if (!(el >= 'a' && el <= 'z')) { // check if el is permissible
                System.out.println("Wrong inputs");
                System.exit(0);
            }
        }

        return new Student(id, memberName);
    }



    //
    // ===================== FUNCTION TO ADD NEW PROFESSOR =======================
    //
    private Professor addProfessor(List<Professor> members, int id) {
        Scanner input = new Scanner(System.in);

        String memberName = input.nextLine().toLowerCase();

        // --------------- CHECK IF COURSE NAME IS CORRECT ----------------------
        char[] chars = memberName.toCharArray();
        for (char el : memberName.toCharArray()) { // loop for every character of course
            if (!(el >= 'a' && el <= 'z')) { // check if el is permissible
                System.out.println("Wrong inputs");
                System.exit(0);
            }
        }

        return new Professor(id, memberName);
    }


    //
    // ====================== FUNCTION TO ENROLL STUDENT TO COURSE =======================
    //
    private boolean enrollStudent(List<Student> students, List<Course> courses) {
        Scanner input = new Scanner(System.in);

        int sId = 0;
        try {
           sId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
           System.out.println("Wrong inputs");
           System.exit(0);
        }

        Student currStudent = new Student(0,"");
        boolean isFind = false;
        for (Student student: students) {
           if (student.getId() == sId) {
               currStudent = student;
               isFind = true;
               break;
           }
        }

        int cId = 0;
        try {
            cId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }

        Course currCourse = new Course("", CourseLevel.NONE, 0);
        isFind = false;
        for (Course course: courses) {
            if (course.getId() == cId) {
                currCourse = course;
                isFind = true;
                break;
            }
        }

        if (!isFind) {
           System.out.println("Wrong inputs");
           System.exit(0);
        }

        return currStudent.enroll(currCourse);
    }


    //
     // ================ FUNCTION TO DROP STUDENT FROM COURSE ================
    //
    private boolean dropStudent(List<Student> students, List<Course> courses) {
        Scanner input = new Scanner(System.in);

        int sId = 0;
        try {
            sId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }

        Student currStudent = new Student(0,"");
        boolean isFind = false;
        for (Student student: students) {
            if (student.getId() == sId) {
                currStudent = student;
                isFind = true;
                break;
            }
        }

        int cId = 0;
        try {
            cId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }

        Course currCourse = new Course("", CourseLevel.NONE, 0);
        isFind = false;
        for (Course course: courses) {
            if (course.getId() == cId) {
                currCourse = course;
                isFind = true;
                break;
            }
        }

        if (!isFind) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }

        return currStudent.drop(currCourse);
    }


    //
     // ==================== FUNCTION TO ASSIGN PROFESSOR TO COURSE =========================
    //
    private boolean assignProf(List<Professor> professors, List<Course> courses) {
        Scanner input = new Scanner(System.in);

        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }

        Professor currProf = new Professor(0,"");
        boolean isFind = false;
        for (Professor professor: professors) {
            if (professor.getId() == pId) {
                currProf = professor;
                isFind = true;
                break;
            }
        }

        int cId = 0;
        try {
            cId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }

        Course currCourse = new Course("", CourseLevel.NONE, 0);
        isFind = false;
        for (Course course: courses) {
            if (course.getId() == cId) {
                currCourse = course;
                isFind = true;
                break;
            }
        }

        if (!isFind) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }

        return currProf.teach(currCourse);

    }


    //
     // ====================== FUNCTION TO EXEMPT PROFESSOR =======================
    //
    private boolean exemptProf(List<Professor> professors, List<Course> courses) {
        Scanner input = new Scanner(System.in);

        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }

        Professor currProf = new Professor(0,"");
        boolean isFind = false;
        for (Professor professor: professors) {
            if (professor.getId() == pId) {
                currProf = professor;
                isFind = true;
                break;
            }
        }

        int cId = 0;
        try {
            cId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }

        Course currCourse = new Course("", CourseLevel.NONE, 0);
        isFind = false;
        for (Course course: courses) {
            if (course.getId() == cId) {
                currCourse = course;
                isFind = true;
                break;
            }
        }

        if (!isFind) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }

        return currProf.exempt(currCourse);

    }
}

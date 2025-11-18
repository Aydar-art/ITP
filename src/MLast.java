import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MLast {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        List<Course> courses = fillInitialCourses();
        List<Student> students = fillInitialStudents(courses);
        List<Professor> professors = fillInitialProf(courses);

        String cmdLine = input.nextLine();
        int memberId = 7;

        while (!cmdLine.isEmpty()) {
            switch (cmdLine) {
                case "course":
                    Course newCourse = addCourse(courses);
                    courses.add(newCourse);
                    System.out.println("Added successfully");
                    break;

                case "student":
                    Student newStudent = addStudent(students, memberId);
                    memberId++;
                    students.add(newStudent);
                    System.out.println("Added successfully");
                    break;

                case "professor":
                    Professor newProfessor = addProfessor(professors, memberId);
                    memberId++;
                    professors.add(newProfessor);
                    System.out.println("Added successfully");
                    break;

                case "enroll":
                    if (enrollStudent(students, courses)) {
                        System.out.println("Enrolled successfully");
                    } else {
                        System.out.println("Wrong inputs");
                        System.exit(0);
                    }
                    break;

                case "drop":
                    if (dropStudent(students, courses)) {
                        System.out.println("Dropped successfully");
                    } else {
                        System.out.println("Wrong inputs");
                        System.exit(0);
                    }
                    break;

                case "teach":
                    if (assignProf(professors, courses)) {
                        System.out.println("Professor is successfully assigned to teach this course");
                    } else {
                        System.out.println("Wrong inputs");
                        System.exit(0);
                    }
                    break;

                case "exempt":
                    if (exemptProf(professors, courses)) {
                        System.out.println("Professor is exempted");
                    } else {
                        System.out.println("Wrong inputs");
                        System.exit(0);
                    }
                    break;

                default:
                    System.out.println("Wrong inputs");
                    System.exit(0);
                    break;
            }

            cmdLine = input.nextLine();
        }
        input.close();
    }

    public static List<Course> fillInitialCourses() {
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

    public static List<Student> fillInitialStudents(List<Course> courses) {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Alice"));
        students.add(new Student(2, "Bob"));
        students.add(new Student(3, "Alex"));

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

    public static List<Professor> fillInitialProf(List<Course> courses) {
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

    // ================= FUNCTION TO ADD NEW COURSE =================
    private static Course addCourse(List<Course> courses) {
        Scanner input = new Scanner(System.in);

        String courseName = input.nextLine().toLowerCase();

        // Проверка имени курса
        char[] chars = courseName.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char el = chars[i];
            if ((el >= 'a' && el <= 'z') || el == '_') {
                if (el == '_') {
                    if (i - 1 >= 0 && i + 1 < courseName.length()) {
                        char el0 = courseName.toCharArray()[i - 1];
                        char el2 = courseName.toCharArray()[i + 1];
                        if (!(el0 >= 'a' && el0 <= 'z' && el2 >= 'a' && el2 <= 'z')) {
                            System.out.println("Wrong inputs");
                            System.exit(0);
                            return null;
                        }
                    } else {
                        System.out.println("Wrong inputs");
                        System.exit(0);
                        return null;
                    }
                }
            } else {
                System.out.println("Wrong inputs");
                System.exit(0);
                return null;
            }
        }

        if ((courseName.equals("course") || courseName.equals("student") || courseName.equals("professor") || courseName.equals("enroll") || courseName.equals("drop") || courseName.equals("teach") || courseName.equals("exempt"))) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return null;
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
            return null;
        }

        Course course = new Course(courseName, courseLevel, courses.size() + 1);

        // Проверка существования курса
        for (Course course1 : courses) {
            if (course1.getCourseName().equals(course.getCourseName())) {
                System.out.println("Course exists");
                System.exit(0);
                return null;
            }
        }
        return course;
    }

    // ===================== FUNCTION TO ADD NEW STUDENT =======================
    private static Student addStudent(List<Student> members, int id) {
        Scanner input = new Scanner(System.in);
        String memberName = input.nextLine().toLowerCase();

        // Проверка имени
        for (char el : memberName.toCharArray()) {
            if (!(el >= 'a' && el <= 'z')) {
                System.out.println("Wrong inputs");
                System.exit(0);
                return null;
            }
        }

        if ((memberName.equals("course") || memberName.equals("student") || members.equals("professor") || memberName.equals("enroll") || memberName.equals("drop") || memberName.equals("teach") || memberName.equals("exempt"))) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return null;
        }

        return new Student(id, memberName);
    }

    // ===================== FUNCTION TO ADD NEW PROFESSOR =======================
    private static Professor addProfessor(List<Professor> members, int id) {
        Scanner input = new Scanner(System.in);
        String memberName = input.nextLine().toLowerCase();

        // Проверка имени
        for (char el : memberName.toCharArray()) {
            if (!(el >= 'a' && el <= 'z')) {
                System.out.println("Wrong inputs");
                System.exit(0);
                return null;
            }
        }

        if ((memberName.equals("course") || memberName.equals("student") || memberName.equals("professor") || memberName.equals("enroll") || memberName.equals("drop") || memberName.equals("teach") || memberName.equals("exempt"))) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return null;
        }

        return new Professor(id, memberName);
    }

    // ====================== FUNCTION TO ENROLL STUDENT TO COURSE =======================
    private static boolean enrollStudent(List<Student> students, List<Course> courses) {
        Scanner input = new Scanner(System.in);

        int sId = 0;
        try {
            sId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        Student currStudent = null;
        for (Student student : students) {
            if (student.getId() == sId) {
                currStudent = student;
                break;
            }
        }

        if (currStudent == null) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        int cId = 0;
        try {
            cId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        Course currCourse = null;
        for (Course course : courses) {
            if (course.getId() == cId) {
                currCourse = course;
                break;
            }
        }

        if (currCourse == null) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        return currStudent.enroll(currCourse);
    }

    // ================ FUNCTION TO DROP STUDENT FROM COURSE ================
    private static boolean dropStudent(List<Student> students, List<Course> courses) {
        Scanner input = new Scanner(System.in);

        int sId = 0;
        try {
            sId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        Student currStudent = null;
        for (Student student : students) {
            if (student.getId() == sId) {
                currStudent = student;
                break;
            }
        }

        if (currStudent == null) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        int cId = 0;
        try {
            cId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        Course currCourse = null;
        for (Course course : courses) {
            if (course.getId() == cId) {
                currCourse = course;
                break;
            }
        }

        if (currCourse == null) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        return currStudent.drop(currCourse);
    }

    // ==================== FUNCTION TO ASSIGN PROFESSOR TO COURSE =========================
    private static boolean assignProf(List<Professor> professors, List<Course> courses) {
        Scanner input = new Scanner(System.in);

        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        Professor currProf = null;
        for (Professor professor : professors) {
            if (professor.getId() == pId) {
                currProf = professor;
                break;
            }
        }

        if (currProf == null) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        int cId = 0;
        try {
            cId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        Course currCourse = null;
        for (Course course : courses) {
            if (course.getId() == cId) {
                currCourse = course;
                break;
            }
        }

        if (currCourse == null) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        return currProf.teach(currCourse);
    }

    // ====================== FUNCTION TO EXEMPT PROFESSOR =======================
    private static boolean exemptProf(List<Professor> professors, List<Course> courses) {
        Scanner input = new Scanner(System.in);

        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        Professor currProf = null;
        for (Professor professor : professors) {
            if (professor.getId() == pId) {
                currProf = professor;
                break;
            }
        }

        if (currProf == null) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        int cId = 0;
        try {
            cId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        Course currCourse = null;
        for (Course course : courses) {
            if (course.getId() == cId) {
                currCourse = course;
                break;
            }
        }

        if (currCourse == null) {
            System.out.println("Wrong inputs");
            System.exit(0);
            return false;
        }

        return currProf.exempt(currCourse);
    }
}

// //////////////////////////////////
// //////////////////////////////////

interface Enrollable {
    boolean drop(Course course);
    boolean enroll(Course course);
}

enum CourseLevel {
    BACHELOR,
    MASTER,
    NONE
}

abstract class UniversityMember {
    protected int memberId;
    protected String memberName;
    protected List<Course> courses;

    public int getId() {
        return memberId;
    }

    public String getName() {
        return memberName;
    }
}

class Student extends UniversityMember implements Enrollable {
    private final int MAX_ENROLLMENT = 3;

    public Student(int memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.courses = new ArrayList<>();
    }

    @Override
    public boolean drop(Course course) {
        if (courses.contains(course) && course.removeMember(this)) {
            courses.remove(course);
            return true;
        } else {
            System.out.println("Student is not enrolled in this course");
            System.exit(0);
            return false;
        }
    }

    @Override
    public boolean enroll(Course course) {
        if (courses.contains(course)) {
            System.out.println("Student is already enrolled in this course");
            System.exit(0);
            return false;
        }
        if (courses.size() >= MAX_ENROLLMENT) {
            System.out.println("Maximum enrollment is reached for the student");
            System.exit(0);
            return false;
        }
        if (course.addMember(this)) {
            courses.add(course);
            return true;
        }
        return false;
    }
}

class Professor extends UniversityMember {
    private final int MAX_LOAD = 2;

    public Professor(int memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.courses = new ArrayList<>();
    }

    public boolean teach(Course course) {
        if (courses.contains(course)) {
            System.out.println("Professor is already teaching this course");
            System.exit(0);
            return false;
        }
        if (courses.size() >= MAX_LOAD) {
            System.out.println("Professor's load is complete");
            System.exit(0);
            return false;
        }
        courses.add(course);
        return true;
    }

    public boolean exempt(Course course) {
        if (courses.contains(course)) {
            courses.remove(course);
            return true;
        } else {
            System.out.println("Professor is not teaching this course");
            System.exit(0);
            return false;
        }
    }
}

class Course {
    final private int CAPACITY = 3;
    private int courseId;
    private String courseName;
    private List<Student> students;
    private CourseLevel courseLvl;

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
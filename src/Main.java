/**
 * ai.gaifullin@innopolis.university
 * B25-DSAI-03
 * University Management System
 *
 * SYSTEM ARCHITECTURE:
 * Main classes and interfaces:
 * - Main: main application class
 * - Enrollable: interface
 * - CourseLevel: enumeration (BACHELOR, MASTER, NONE)
 * - UniversityMember: abstract class for all members
 * - Student: student class (extends UniversityMember, implements Enrollable)
 * - Professor: professor class (extends UniversityMember)
 * - Course: course class
 *
 * DATA VALIDATION:
 * Course name validation:
 * - Only lowercase letters and underscores
 * - Cannot start or end with underscore
 * - Cannot be empty
 * - Cannot contain consecutive underscores
 * - Cannot match system commands
 *
 * Member name validation:
 * - Only lowercase letters
 * - Cannot be empty
 * - Cannot match system commands
 *
 * SYSTEM CONSTANTS:
 * - INITIAL_MEMBER_ID = 7
 * - MAX_STUDENT_ENROLLMENT = 3
 * - MAX_PROFESSOR_LOAD = 2
 * - COURSE_CAPACITY = 3
 *
 * INITIAL DATA:
 * Courses:
 * 1. "java_beginner" (Bachelor) - ID: 1
 * 2. "java_intermediate" (Bachelor) - ID: 2
 * 3. "python_basics" (Bachelor) - ID: 3
 * 4. "algorithms" (Master) - ID: 4
 * 5. "advanced_programming" (Master) - ID: 5
 * 6. "mathematical_analysis" (Master) - ID: 6
 * 7. "computer_vision" (Master) - ID: 7
 *
 * Students:
 * 1. "Alice" (ID: 1) - enrolled in courses 1, 2, 3
 * 2. "Bob" (ID: 2) - enrolled in courses 1, 4
 * 3. "Alex" (ID: 3) - enrolled in course 5
 *
 * Professors:
 * 1. "Ali" (ID: 4) - teaching courses 1, 2
 * 2. "Ahmed" (ID: 5) - teaching courses 3, 5
 * 3. "Andrey" (ID: 6) - teaching course 6
 *
 * ERROR HANDLING:
 * - Course exists
 * - Student is already enrolled in this course
 * - Maximum enrollment is reached for the student
 * - Course is full
 * - Student is not enrolled in this course
 * - Professor's load is complete
 * - Professor is already teaching this course
 * - Professor is not teaching this course
 * - Wrong inputs
 *
 * SYSTEM LIMITATIONS:
 * - Maximum courses per student: 3
 * - Maximum professor load: 2 courses
 * - Course capacity: 3 students
 * - Names must be in lowercase
 *
 * AVAILABLE COMMANDS:
 *
 * 1. "course" - Add a new course (like "math" or "programming")
 *    Example:
 *    Input: course
 *    Next input: course name (like "java_basics")
 *    Next input: course level ("bachelor" or "master")
 *
 * 2. "student" - Add a new student
 *    Example:
 *    Input: student
 *    Next input: student name (like "alice")
 *
 * 3. "professor" - Add a new professor
 *    Example:
 *    Input: professor
 *    Next input: professor name (like "dr_smith")
 *
 * 4. "enroll" - Put a student in a course
 *    Example:
 *    Input: enroll
 *    Next input: student ID number
 *    Next input: course ID number
 *
 * 5. "drop" - Remove a student from a course
 *    Example:
 *    Input: drop
 *    Next input: student ID number
 *    Next input: course ID number
 *
 * 6. "teach" - Assign a professor to teach a course
 *    Example:
 *    Input: teach
 *    Next input: professor ID number
 *    Next input: course ID number
 *
 * 7. "exempt" - Remove a professor from teaching a course
 *    Example:
 *    Input: exempt
 *    Next input: professor ID number
 *    Next input: course ID number
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * University Management System
 * Handles courses, students, professors and their relationships
 */
public class Main {

    // Constants for system constraints
    private static final int INITIAL_MEMBER_ID = 7;
    private static final int MAX_STUDENT_ENROLLMENT = 3;
    private static final int MAX_PROFESSOR_LOAD = 2;
    private static final int COURSE_CAPACITY = 3;

    /**
     * Main entry point of application
     * Processes commands until empty input is received
     */
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            List<Course> courses = fillInitialCourses();
            List<Student> students = fillInitialStudents(courses);
            List<Professor> professors = fillInitialProf(courses);

            int memberId = INITIAL_MEMBER_ID;
            String cmdLine;
            cmdLine = input.nextLine();
            while (!(cmdLine).isEmpty()) {
                boolean success = processCommand(cmdLine, courses, students, professors, memberId, input);

                if (success && (cmdLine.equals("student") || cmdLine.equals("professor"))) {
                    memberId++;
                }
                if (!success || !(input.hasNextLine())) {
                    return;
                }
                cmdLine = input.nextLine();
            }
        }
    }

    /**
     * Processes single command
     * true if memberId should be incremented, false otherwise
     */
    private static boolean processCommand(String command, List<Course> courses,
                                          List<Student> students, List<Professor> professors,
                                          int memberId, Scanner input) {
        switch (command) {
            case "course" -> {
                Course newCourse = addCourse(courses, input);
                if (newCourse != null) {
                    courses.add(newCourse);
                    System.out.println("Added successfully");
                    return true;
                }
                return false;
            }

            case "student" -> {
                Student newStudent = addStudent(memberId, input);
                if (newStudent != null) {
                    students.add(newStudent);
                    System.out.println("Added successfully");
                    return true;
                }
                return false;
            }

            case "professor" -> {
                Professor newProfessor = addProfessor(memberId, input);
                if (newProfessor != null) {
                    professors.add(newProfessor);
                    System.out.println("Added successfully");
                    return true;
                }
                return false;
            }

            case "enroll" -> {
                if (enrollStudent(students, courses, input)) {
                    System.out.println("Enrolled successfully");
                    return true;
                } else {
                    // System.out.println("Wrong inputs");
                    return false;
                }
            }

            case "drop" -> {
                if (dropStudent(students, courses, input)) {
                    System.out.println("Dropped successfully");
                    return true;
                } else {
                    // System.out.println("Wrong inputs");
                    return false;
                }
            }

            case "teach" -> {
                if (assignProf(professors, courses, input)) {
                    System.out.println("Professor is successfully assigned to teach this course");
                    return true;
                } else {
                    // System.out.println("Wrong inputs");
                    return false;
                }
            }

            case "exempt" -> {
                if (exemptProf(professors, courses, input)) {
                    System.out.println("Professor is exempted");
                    return true;
                } else {
                    // System.out.println("Wrong inputs");
                    return false;
                }
            }

            default -> {
                System.out.println("Wrong inputs");
                return false;
            }
        }
    }

    private static final int COURSE_ID_JAVA_BEGINNER = 1;
    private static final int COURSE_ID_JAVA_INTERMEDIATE = 2;
    private static final int COURSE_ID_PYTHON_BASICS = 3;
    private static final int COURSE_ID_ALGORITHMS = 4;
    private static final int COURSE_ID_ADVANCED_PROGRAMMING = 5;
    private static final int COURSE_ID_MATHEMATICAL_ANALYSIS = 6;
    private static final int COURSE_ID_COMPUTER_VISION = 7;
    /**
     * Initializes system with predefined courses
     */
    static List<Course> fillInitialCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("java_beginner", CourseLevel.BACHELOR,
                COURSE_ID_JAVA_BEGINNER, COURSE_CAPACITY));
        courses.add(new Course("java_intermediate", CourseLevel.BACHELOR,
                COURSE_ID_JAVA_INTERMEDIATE, COURSE_CAPACITY));
        courses.add(new Course("python_basics", CourseLevel.BACHELOR,
                COURSE_ID_PYTHON_BASICS, COURSE_CAPACITY));
        courses.add(new Course("algorithms", CourseLevel.MASTER,
                COURSE_ID_ALGORITHMS, COURSE_CAPACITY));
        courses.add(new Course("advanced_programming", CourseLevel.MASTER,
                COURSE_ID_ADVANCED_PROGRAMMING, COURSE_CAPACITY));
        courses.add(new Course("mathematical_analysis", CourseLevel.MASTER,
                COURSE_ID_MATHEMATICAL_ANALYSIS, COURSE_CAPACITY));
        courses.add(new Course("computer_vision", CourseLevel.MASTER,
                COURSE_ID_COMPUTER_VISION, COURSE_CAPACITY));
        return courses;
    }

    private static final int STUDENT_ALICE_ID = 1;
    private static final int STUDENT_BOB_ID = 2;
    private static final int STUDENT_ALEX_ID = 3;
    private static final int COURSE_INDEX_JAVA_BEGINNER = 0;
    private static final int COURSE_INDEX_JAVA_INTERMEDIATE = 1;
    private static final int COURSE_INDEX_PYTHON_BASICS = 2;
    private static final int COURSE_INDEX_ALGORITHMS = 3;
    private static final int COURSE_INDEX_ADVANCED_PROGRAMMING = 4;

    /**
     * Initializes system with predefined students and enrolls them in courses
     */
    static List<Student> fillInitialStudents(List<Course> courses) {
        List<Student> students = new ArrayList<>();
        students.add(new Student(STUDENT_ALICE_ID, "Alice", MAX_STUDENT_ENROLLMENT));
        students.add(new Student(STUDENT_BOB_ID, "Bob", MAX_STUDENT_ENROLLMENT));
        students.add(new Student(STUDENT_ALEX_ID, "Alex", MAX_STUDENT_ENROLLMENT));

        for (Student student : students) {
            switch (student.getName()) {
                case "Alice" -> {
                    student.enroll(courses.get(COURSE_INDEX_JAVA_BEGINNER));
                    student.enroll(courses.get(COURSE_INDEX_JAVA_INTERMEDIATE));
                    student.enroll(courses.get(COURSE_INDEX_PYTHON_BASICS));
                }
                case "Bob" -> {
                    student.enroll(courses.get(COURSE_INDEX_JAVA_BEGINNER));
                    student.enroll(courses.get(COURSE_INDEX_ALGORITHMS));
                }
                default -> student.enroll(courses.get(COURSE_INDEX_ADVANCED_PROGRAMMING));
            }
        }
        return students;
    }

    private static final int PROFESSOR_ALI_ID = 4;
    private static final int PROFESSOR_AHMED_ID = 5;
    private static final int PROFESSOR_ANDREY_ID = 6;
    private static final int COURSE_INDEX_MATHEMATICAL_ANALYSIS = 5;
    /**
     * Initializes system with predefined professors and assigns them to courses
     */
    static List<Professor> fillInitialProf(List<Course> courses) {
        List<Professor> professors = new ArrayList<>();
        professors.add(new Professor(PROFESSOR_ALI_ID, "Ali", MAX_PROFESSOR_LOAD));
        professors.add(new Professor(PROFESSOR_AHMED_ID, "Ahmed", MAX_PROFESSOR_LOAD));
        professors.add(new Professor(PROFESSOR_ANDREY_ID, "Andrey", MAX_PROFESSOR_LOAD));

        for (Professor professor : professors) {
            switch (professor.getId()) {
                case PROFESSOR_ALI_ID -> {
                    professor.teach(courses.get(COURSE_INDEX_JAVA_BEGINNER));
                    professor.teach(courses.get(COURSE_INDEX_JAVA_INTERMEDIATE));
                }
                case PROFESSOR_AHMED_ID -> {
                    professor.teach(courses.get(COURSE_INDEX_PYTHON_BASICS));
                    professor.teach(courses.get(COURSE_INDEX_ADVANCED_PROGRAMMING));
                }
                default -> professor.teach(courses.get(COURSE_INDEX_MATHEMATICAL_ANALYSIS));
            }
        }
        return professors;
    }

    /**
     * Creates new course with user input validation
     */
    private static Course addCourse(List<Course> courses, Scanner input) {
        if (!input.hasNextLine()) {
            System.out.println("Wrong inputs");
            return null;
        }

        String courseName = input.nextLine().toLowerCase();

        if (isCourseExists(courses, courseName)) {
            System.out.println("Course exists");
            return null;
        }

        if (!isValidCourseName(courseName)) {
            System.out.println("Wrong inputs");
            return null;
        }

        if (!input.hasNextLine()) {
            System.out.println("Wrong inputs");
            return null;
        }

        String courseString = input.nextLine().toLowerCase();

        CourseLevel courseLevel = parseCourseLevel(courseString);

        if (courseLevel == CourseLevel.NONE) {
            System.out.println("Wrong inputs");
            return null;
        }

        return new Course(courseName, courseLevel, courses.size() + 1, COURSE_CAPACITY);
    }

    /**
     * Creates new student with user input validation
     */
    private static Student addStudent(int id, Scanner input) {
        if (!input.hasNextLine()) {
            System.out.println("Wrong inputs");
            return null;
        }

        String memberName = input.nextLine().toLowerCase();

        if (isValidMemberName(memberName)) {
            System.out.println("Wrong inputs");
            return null;
        }

        return new Student(id, memberName, MAX_STUDENT_ENROLLMENT);
    }

    /**
     * Creates new professor with user input validation
     */
    private static Professor addProfessor(int id, Scanner input) {

        if (!input.hasNextLine()) {
            System.out.println("Wrong inputs");
            return null;
        }

        String memberName = input.nextLine().toLowerCase();

        if (isValidMemberName(memberName)) {
            System.out.println("Wrong inputs");
            return null;
        }

        return new Professor(id, memberName, MAX_PROFESSOR_LOAD);
    }

    /**
     * Enrolls student in course
     */
    private static boolean enrollStudent(List<Student> students,
                                         List<Course> courses, Scanner input) {

        try {
            int studentId = readIntWithValidation(input);
            if (studentId == -1) {
                return false;
            }

            Student student = findStudentById(students, studentId);

            if (student == null) {
                System.out.println("Wrong inputs");
                return false;
            }

            int courseId = readIntWithValidation(input);
            if (courseId == -1) {
                return false;
            }

            Course course = findCourseById(courses, courseId);

            if (course == null) {
                System.out.println("Wrong inputs");
                return false;
            }

            return student.enroll(course);

        } catch (Exception e) {
            System.out.println("Wrong inputs");
            return false;
        }
    }

    /**
     * Drops student from course
     */
    private static boolean dropStudent(List<Student> students,
                                       List<Course> courses, Scanner input) {

        try {
            int studentId = readIntWithValidation(input);
            if (studentId == -1) {
                return false;
            }

            int courseId = readIntWithValidation(input);
            if (courseId == -1) {
                return false;
            }

            Student student = findStudentById(students, studentId);
            Course course = findCourseById(courses, courseId);

            if (student == null || course == null) {
                System.out.println("Wrong inputs");
                return false;
            }

            return student.drop(course);

        } catch (Exception e) {
            System.out.println("Wrong inputs");
            return false;
        }
    }

    /**
     * Assigns professor to teach course
     */
    private static boolean assignProf(List<Professor> professors,
                                      List<Course> courses, Scanner input) {

        try {
            int professorId = readIntWithValidation(input);
            if (professorId == -1) {
                return false;
            }

            int courseId = readIntWithValidation(input);
            if (courseId == -1) {
                return false;
            }

            Professor professor = findProfessorById(professors, professorId);
            Course course = findCourseById(courses, courseId);

            if (professor == null || course == null) {
                System.out.println("Wrong inputs");
                return false;
            }

            return professor.teach(course);

        } catch (Exception e) {
            System.out.println("Wrong inputs");
            return false;
        }
    }

    /**
     * Removes professor from teaching course
     */
    private static boolean exemptProf(List<Professor> professors,
                                      List<Course> courses, Scanner input) {

        try {
            int professorId = readIntWithValidation(input);
            if (professorId == -1) {
                return false;
            }

            int courseId = readIntWithValidation(input);
            if (courseId == -1) {
                return false;
            }

            Professor professor = findProfessorById(professors, professorId);
            Course course = findCourseById(courses, courseId);

            if (professor == null || course == null) {
                System.out.println("Wrong inputs");
                return false;
            }

            return professor.exempt(course);

        } catch (Exception e) {
            System.out.println("Wrong inputs");
            return false;
        }
    }

    // ==================== VALIDATION METHODS ====================

    /**
     * Validates course name according to naming conventions
     */
    private static boolean isValidCourseName(String courseName) {
        if (courseName == null || courseName.isEmpty()) {
            return false;
        }

        char[] chars = courseName.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];
            if ((currentChar >= 'a' && currentChar <= 'z') || currentChar == '_') {
                if (currentChar == '_') {
                    if (i == 0 || i == chars.length - 1) {
                        return false;
                    }
                    char prevChar = chars[i - 1];
                    char nextChar = chars[i + 1];
                    if (!(prevChar >= 'a' && prevChar <= 'z') || !(nextChar >= 'a' && nextChar <= 'z')) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return isNotEqualToCommand(courseName);
    }

    /**
     * Validates member name (only lowercase letters)
     */
    private static boolean isValidMemberName(String memberName) {
        if (memberName == null || memberName.isEmpty()) {
            return true;
        }

        for (char currentChar : memberName.toCharArray()) {
            if (!(currentChar >= 'a' && currentChar <= 'z')) {
                return true;
            }
        }
        return !isNotEqualToCommand(memberName);
    }

    /**
     * Parses course level from string
     */
    private static CourseLevel parseCourseLevel(String levelString) {
        return switch (levelString) {
            case "bachelor" -> CourseLevel.BACHELOR;
            case "master" -> CourseLevel.MASTER;
            default -> CourseLevel.NONE;
        };
    }

    /**
     * Reads and validates integer input
     */
    private static int readIntWithValidation(Scanner input) {
        if (!input.hasNextLine()) {
            System.out.println("Wrong inputs");
            return -1;
        }

        try {
            return Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            return -1;
        }
    }

    private static boolean isNotEqualToCommand(String name) {
        return !(name.equals("student") || name.equals("course")
                || name.equals("professor") || name.equals("enroll")
                || name.equals("drop") || name.equals("teach") || name.equals("exempt"));
    }

    // ==================== SEARCH METHODS ====================

    private static Student findStudentById(List<Student> students, int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    private static Course findCourseById(List<Course> courses, int id) {
        for (Course course : courses) {
            if (course.getId() == id) {
                return course;
            }
        }
        return null;
    }

    private static Professor findProfessorById(List<Professor> professors, int id) {
        for (Professor professor : professors) {
            if (professor.getId() == id) {
                return professor;
            }
        }
        return null;
    }

    private static boolean isCourseExists(List<Course> courses, String courseName) {
        for (Course course : courses) {
            if (course.getCourseName().equals(courseName)) {
                return true;
            }
        }
        return false;
    }
}

/**
 * Interface for entities that can enroll in courses
 */
interface Enrollable {
    boolean drop(Course course);

    boolean enroll(Course course);
}

/**
 * Represents the academic level of courses
 */
enum CourseLevel {
    BACHELOR,
    MASTER,
    NONE
}

/**
 * Abstract base class for all university members
 */
abstract class UniversityMember {
    protected int memberId;
    protected String memberName;
    protected List<Course> courses;

    public UniversityMember(int memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.courses = new ArrayList<>();
    }

    public int getId() {
        return memberId;
    }

    public String getName() {
        return memberName;
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }
}

/**
 * Represents university student
 */
class Student extends UniversityMember implements Enrollable {
    private final int maxEnrollment;

    public Student(int memberId, String memberName, int maxEnrollment) {
        super(memberId, memberName);
        this.maxEnrollment = maxEnrollment;
    }

    @Override
    public boolean drop(Course course) {
        if (courses.contains(course) && course.removeMember(this)) {
            courses.remove(course);
            return true;
        } else {
            System.out.println("Student is not enrolled in this course");
            return false;
        }
    }

    @Override
    public boolean enroll(Course course) {
        if (courses.contains(course)) {
            System.out.println("Student is already enrolled in this course");
            return false;
        }
        if (courses.size() >= maxEnrollment) {
            System.out.println("Maximum enrollment is reached for the student");
            return false;
        }
        if (course.addMember(this)) {
            courses.add(course);
            return true;
        }
        return false;
    }
}

/**
 * Represents university professor
 */
class Professor extends UniversityMember {
    private final int maxLoad;

    public Professor(int memberId, String memberName, int maxLoad) {
        super(memberId, memberName);
        this.maxLoad = maxLoad;
    }

    public boolean teach(Course course) {
        if (courses.contains(course)) {
            System.out.println("Professor is already teaching this course");
            return false;
        }
        if (courses.size() >= maxLoad) {
            System.out.println("Professor's load is complete");
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
            return false;
        }
    }
}
// to make unique
/**
 * Represents university course
 */
class Course {
    private final int capacity;
    private final int courseId;
    private final String courseName;
    private final List<Student> students;
    private final CourseLevel courseLevel;

    public Course(String courseName, CourseLevel courseLevel, int courseId, int capacity) {
        this.courseName = courseName;
        this.courseLevel = courseLevel;
        this.courseId = courseId;
        this.capacity = capacity;
        this.students = new ArrayList<>();
    }

    public int getId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
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
        if (students.size() < capacity) {
            students.add(student);
            return true;
        }
        System.out.println("Course is full");
        return false;
    }

    public boolean isFull() {
        return students.size() == capacity;
    }

    public int getStudentCount() {
        return students.size();
    }

    public int getCapacity() {
        return capacity;
    }
}



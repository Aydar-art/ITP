package Assigment.Abs;

import java.util.ArrayList;
import java.util.List;

public class Student extends UniversityMember implements Enrollable {
    private final int MAX_ERROLMENT = 3;

    public Student(int memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.cources = new ArrayList<>();
    }

    public String getName() {
        return memberName;
    }

    public int getId() {
        return memberId;
    }

    @Override
    public boolean drop(Course course) {
        boolean isRemoved = false;
        if (cources.contains(course) && course.removeMember(this)) {
            isRemoved = true;

        } else {
            System.out.println("Student is not enrolled in this course");
            System.exit(0);
        }

        return isRemoved;
    }

    @Override
    public boolean enroll(Course course) {
        boolean isAdd = false;
        if (!(cources.contains(course)) && cources.size() < MAX_ERROLMENT && course.addMember(this)) {
            cources.add(course);
            isAdd = true;

        } else {
            if (cources.contains(course)) {
                System.out.println("Student is already enrolled in this course");
                System.exit(0);
            } else {
                System.out.println("Maximum enrollment is reached for the student");
                System.exit(0);
            }
        }

        return isAdd;
    }

}

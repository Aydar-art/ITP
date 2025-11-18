package Assigment.Abs;

import java.util.ArrayList;

public class Professor extends UniversityMember{
    private final int MAX_LOAD = 2;

    public Professor(int memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.cources = new ArrayList<>();
    }

    public int getId() {
        return memberId;
    }

    public boolean teach(Course course) {
        boolean isAdd = false;
        if (!(cources.contains(course)) && cources.size() < MAX_LOAD) {
            cources.add(course);
            isAdd = true;

        } else if (cources.size() >= MAX_LOAD) {
            System.out.println("Professor's load is complete");
            System.exit(0);
        } else if (cources.contains(course)) {
            System.out.println("Professor is already teaching this course");
            System.exit(0);
        }

        return isAdd;
    }


    public boolean exempt(Course course) {
        boolean isRemoved = false;
        if (cources.contains(course)) {
            cources.remove(course);
            isRemoved = true;

        } else {
            System.out.println("Professor is not teaching this course");
            System.exit(0);
        }

        return isRemoved;
    }

}

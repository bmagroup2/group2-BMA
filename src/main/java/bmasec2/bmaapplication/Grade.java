package bmasec2.bmaapplication;

public class Grade {
    private String grade;
    private int count;

    public Grade(String grade, int count) {
        this.grade = grade;
        this.count = count;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getCount() {
        return count;
    }



    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "grade='" + grade + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}

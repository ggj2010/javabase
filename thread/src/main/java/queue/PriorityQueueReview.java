package queue;

import lombok.Data;

import java.util.PriorityQueue;

/**
 * 和ABQ类似，但是依据对象的自然排序顺序或者是构造函数的Comparator决定的顺序.
 * @author gaoguangjin
 */
public class PriorityQueueReview {


    public static void main(String[] args) {
        PriorityQueue<Student> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(new Student(7, "学生7"));
        priorityQueue.add(new Student(1, "学生1"));
        priorityQueue.add(new Student(3, "学生3"));
        priorityQueue.add(new Student(5, "学生5"));
        Student student =null;
        while ((student = priorityQueue.poll())!=null) {
            System.out.println(student);
        }

    }

    @Data
    static class Student implements Comparable<Student> {
        private Integer studentId;
        private String studentName;

        public Student(Integer studentId, String studentName) {
            this.studentId = studentId;
            this.studentName = studentName;
        }

        @Override
        public int compareTo(Student o) {
            if (this.studentId > o.studentId) {
                return 1;
            } else if (this.studentId < o.studentId) {
                return -1;
            }
            return 0;
        }
    }
}

package com.example.csci310_group15;

public class Rating {
    private String workload = null;
    private String score = null;
    private String attendance = null;
    private String lateHW = null;
    private String comment = null;

    public Rating() {

    }

    public Rating(String workload, String score, String attendance, String lateHW, String comment) {
        this.workload = workload;
        this.score = score;
        this.attendance = attendance;
        this.lateHW = lateHW;
        this.comment = comment;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getLateHW() {
        return lateHW;
    }

    public void setLateHW(String lateHW) {
        this.lateHW = lateHW;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

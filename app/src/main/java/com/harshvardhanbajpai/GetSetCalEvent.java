package com.harshvardhanbajpai;

class GetSetCalEvent {
    String meeting_detail;
    String meeting_date;





    public String getMeeting_detail() {
        return meeting_detail;
    }

    public String getMeeting_date() {
        return meeting_date;
    }


    public GetSetCalEvent(String meeting_detail, String meeting_date ) {
        this.meeting_detail = meeting_detail;
        this.meeting_date = meeting_date;

    }
}

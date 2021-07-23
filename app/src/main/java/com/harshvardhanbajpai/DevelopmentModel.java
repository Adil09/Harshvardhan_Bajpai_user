package com.harshvardhanbajpai;

public class DevelopmentModel {

    String id,financial_year,area,work_description,nidhi,status,image,estimate, subject;


    public DevelopmentModel(String id, String financial_year, String area, String work_description, String nidhi, String status, String image,
                            String estimate, String subject) {
        this.id = id;
        this.financial_year = financial_year;
        this.area = area;
        this.work_description = work_description;
        this.nidhi = nidhi;
        this.status = status;
        this.image = image;
        this.estimate=estimate;
        this.subject=subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEstimate() {
        return estimate;
    }

    public String getId() {
        return id;
    }

    public String getFinancial_year() {
        return financial_year;
    }

    public String getArea() {
        return area;
    }

    public String getWork_description() {
        return work_description;
    }

    public String getNidhi() {
        return nidhi;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }
}

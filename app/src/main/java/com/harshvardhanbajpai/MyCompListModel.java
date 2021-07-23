package com.harshvardhanbajpai;

public class MyCompListModel {
    String comid;
    String com_image;
    String created;
String com_video;
    String id;
    String com_text;
    String area_in_english;
    String sub_area_in_english;
    String cat_in_english;
    String subcat_in_english1;

    public MyCompListModel(String comid,String com_image,String created,String com_video,String id, String com_text, String area_in_english, String sub_area_in_english, String cat_in_english, String subcat_in_english1) {
     this.comid=comid;
      this.com_image=com_image;
       this.created=created;
        this.com_video=com_video;
        this.id = id;
        this.com_text = com_text;
        this.area_in_english = area_in_english;
        this.sub_area_in_english = sub_area_in_english;
        this.cat_in_english = cat_in_english;
        this.subcat_in_english1 = subcat_in_english1;
    }

    public String getComid() {
        return comid;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    public String getCom_image() {
        return com_image;
    }

    public void setCom_image(String com_image) {
        this.com_image = com_image;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCom_video() {
        return com_video;
    }

    public void setCom_video(String com_video) {
        this.com_video = com_video;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCom_text() {
        return com_text;
    }

    public void setCom_text(String com_text) {
        this.com_text = com_text;
    }

    public String getArea_in_english() {
        return area_in_english;
    }

    public void setArea_in_english(String area_in_english) {
        this.area_in_english = area_in_english;
    }

    public String getSub_area_in_english() {
        return sub_area_in_english;
    }

    public void setSub_area_in_english(String sub_area_in_english) {
        this.sub_area_in_english = sub_area_in_english;
    }

    public String getCat_in_english() {
        return cat_in_english;
    }

    public void setCat_in_english(String cat_in_english) {
        this.cat_in_english = cat_in_english;
    }

    public String getSubcat_in_english1() {
        return subcat_in_english1;
    }

    public void setSubcat_in_english1(String subcat_in_english1) {
        this.subcat_in_english1 = subcat_in_english1;
    }
}

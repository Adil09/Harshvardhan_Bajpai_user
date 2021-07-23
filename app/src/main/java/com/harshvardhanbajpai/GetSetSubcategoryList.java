package com.harshvardhanbajpai;

  class GetSetSubcategoryList {
    public GetSetSubcategoryList(String id, String name,String video_req,String img_req) {
        this.id = id;
        Name = name;
        this.video_req=video_req;
        this.img_req=img_req;
    }

    public String getImg_req() {
        return img_req;
    }

    public void setImg_req(String img_req) {
        this.img_req = img_req;
    }

    String id , Name,video_req,img_req;

    public String getVideo_req() {
        return video_req;
    }

    public void setVideo_req(String video_req) {
        this.video_req = video_req;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}

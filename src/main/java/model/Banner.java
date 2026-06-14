package model;

import dao.BannerProductDAO;
import util.DBContext;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Banner implements Serializable {
    private int id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private int priority;
    private boolean isActive;
    private Timestamp createdAt;

    public Banner() {
    }

    public Banner(int id, String title, String imageUrl, String linkUrl, int priority, boolean isActive, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.priority = priority;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public String getPromotionName(){
        try(Connection conn = DBContext.getConnection()){
            BannerProductDAO bpDAO = new BannerProductDAO(conn);
            String result = bpDAO.getPromotionName(this.id);


            if (result == null) {
                return "Chưa áp dụng";
            }
            return result;
        } catch(Exception e){
            System.out.println(">>> LỖI TẠI GETPROMOTIONNAME CỦA CLASS BANNER: " + e.getMessage());
            e.printStackTrace();
        }
        return "Lỗi kết nối DB";
    }

}
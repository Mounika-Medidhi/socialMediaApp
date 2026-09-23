package com.instagram.model;
import java.time.LocalDateTime;
import java.util.List;
public class Post {
    private int post_id;
    private User user;
    private String caption;
    private String image_url;
    private  String status;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private List<Comment> comments;

    public Post(){
    }
    public Post(int post_id,User user,String caption,String image_url,String status,LocalDateTime created_at,LocalDateTime updated_at,List<Comment> comments){
        this.post_id=post_id;
        this.user=user;
        this.caption=caption;
        this.image_url=image_url;
        this.status=status;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.comments=comments;
    }
    public void setPost_id(int post_id){
        this.post_id=post_id;
    }
    public int getPost_id(){
        return post_id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user){
        this.user=user;
    }
    public String getCaption(){
        return  caption;
    }
    public void setCaption(String caption){
        this.caption=caption;
    }
    public String getImage_url(){
        return image_url;
    }
    public void setImage_url(String image_url){
        this.image_url=image_url;
    }
    public void setstatus(String status){
        this.status=status;
    }
    public String getstatus(){
        return status;
    }
    public void setCreated_at(LocalDateTime created_at){
        this.created_at=created_at;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public List<Comment> getcomments(){
        return comments;
    }
    public void setComments(List<Comment> comments){
        this.comments=comments;
    }
}


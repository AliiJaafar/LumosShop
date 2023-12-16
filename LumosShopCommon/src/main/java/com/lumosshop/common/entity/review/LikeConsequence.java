package com.lumosshop.common.entity.review;

public class LikeConsequence {

    private boolean success;
    private int likes;
    private String content;

    public LikeConsequence(boolean success, int likes, String content) {
        this.success = success;
        this.likes = likes;
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static LikeConsequence successful(int likes,String content) {
        return new LikeConsequence(true, likes, content);
    }
    public static LikeConsequence unsuccessful(String content) {
        return new LikeConsequence(false, 0, content);
    }
}

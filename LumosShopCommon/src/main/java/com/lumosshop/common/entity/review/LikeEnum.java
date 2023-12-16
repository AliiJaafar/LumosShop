package com.lumosshop.common.entity.review;

public enum LikeEnum {
    LIKE("Like"),
    DISLIKE("Dislike");

    private final String displayText;

    LikeEnum(String displayText) {
        this.displayText = displayText;
    }

    @Override
    public String toString() {
        return displayText;
    }
}

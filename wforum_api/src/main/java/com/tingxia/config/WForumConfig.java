package com.tingxia.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "wforum.server")
@Component
public class WForumConfig {

    private String baseFilePath = "E:/WForum";
    // kb
    private Integer imageMaxSize = 512;
    // px
    private Integer imageMaxWidth = 400;
    // px
    private Integer imageMaxHeight = 400;

    private String avatarBasePath = "/avatars";

    private Integer avatarWidth = 200;

    private Integer avatarHeight = 200;

    private Integer avatarMaxSize = 512;

    private String coverBasePath = "covers";

    private Integer coverWidth = 800;

    private Integer coverHeight = 200;

    private Integer coverMaxSize = 2048;

    private Integer maxTopArticleCount = 10;

    public String getBaseFilePath() {
        return baseFilePath;
    }

    public void setBaseFilePath(String baseFilePath) {
        System.out.println("目前文件目录设置为："+baseFilePath);
        this.baseFilePath = baseFilePath;
    }

    public Integer getImageMaxSize() {
        return imageMaxSize;
    }

    public void setImageMaxSize(Integer imageMaxSize) {
        this.imageMaxSize = imageMaxSize;
    }

    public Integer getImageMaxWidth() {
        return imageMaxWidth;
    }

    public void setImageMaxWidth(Integer imageMaxWidth) {
        this.imageMaxWidth = imageMaxWidth;
    }

    public Integer getImageMaxHeight() {
        return imageMaxHeight;
    }

    public void setImageMaxHeight(Integer imageMaxHeight) {
        this.imageMaxHeight = imageMaxHeight;
    }

    public String getAvatarBasePath() {
        return avatarBasePath;
    }

    public void setAvatarBasePath(String avatarBasePath) {
        this.avatarBasePath = avatarBasePath;
    }

    public Integer getAvatarWidth() {
        return avatarWidth;
    }

    public void setAvatarWidth(Integer avatarWidth) {
        this.avatarWidth = avatarWidth;
    }

    public Integer getAvatarHeight() {
        return avatarHeight;
    }

    public void setAvatarHeight(Integer avatarHeight) {
        this.avatarHeight = avatarHeight;
    }

    public Integer getAvatarMaxSize() {
        return avatarMaxSize;
    }

    public void setAvatarMaxSize(Integer avatarMaxSize) {
        this.avatarMaxSize = avatarMaxSize;
    }

    public String getCoverBasePath() {
        return coverBasePath;
    }

    public void setCoverBasePath(String coverBasePath) {
        this.coverBasePath = coverBasePath;
    }

    public Integer getCoverWidth() {
        return coverWidth;
    }

    public void setCoverWidth(Integer coverWidth) {
        this.coverWidth = coverWidth;
    }

    public Integer getCoverHeight() {
        return coverHeight;
    }

    public void setCoverHeight(Integer coverHeight) {
        this.coverHeight = coverHeight;
    }

    public Integer getCoverMaxSize() {
        return coverMaxSize;
    }

    public void setCoverMaxSize(Integer coverMaxSize) {
        this.coverMaxSize = coverMaxSize;
    }

    public Integer getMaxTopArticleCount() {
        return maxTopArticleCount;
    }

    public void setMaxTopArticleCount(Integer maxTopArticleCount) {
        this.maxTopArticleCount = maxTopArticleCount;
    }
}

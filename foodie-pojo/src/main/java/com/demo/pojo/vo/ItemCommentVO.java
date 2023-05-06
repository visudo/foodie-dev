package com.demo.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ItemCommentVO {

    private Integer commentLevel;
    private String content;
    private String sepcName;
    private Date createdTime;
    private String userFace;
    private String nickname;
}

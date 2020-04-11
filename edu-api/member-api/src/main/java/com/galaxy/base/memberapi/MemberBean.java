package com.galaxy.base.memberapi;


import lombok.Data;

import java.time.LocalDateTime;
@Data
public class MemberBean {
    private String id;



    private String mobile;


    private String username;

    private Integer sex;

    private Integer age;

    private String avatar;

    private String salt;


    private Integer prohibit;


    private String points;

    private LocalDateTime eduCreate;

    private LocalDateTime eduModified;
}

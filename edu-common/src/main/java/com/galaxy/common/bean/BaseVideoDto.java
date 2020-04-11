package com.galaxy.common.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class BaseVideoDto implements Serializable {
    private static final long serialVersionUID = 3624988181265L;

    private String id;
    private String VideoId;
    private String Title;
    private Float Duration;
    private Long Size;

}

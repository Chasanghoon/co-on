package com.ssafy.api.request.user;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@Setter
@ApiModel("TeacherProfilePutReq")
public class StudentProfilePutReq {
    @ApiModelProperty
    String stId;
    @ApiModelProperty
    List<MultipartFile> stProfFile;
}

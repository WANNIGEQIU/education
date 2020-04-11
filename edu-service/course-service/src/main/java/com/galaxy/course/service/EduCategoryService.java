package com.galaxy.course.service;



import com.galaxy.common.bean.CourseVo;
import com.galaxy.course.entity.EduSubject;
import com.galaxy.course.entity.dto.OneSubjectDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
public interface EduCategoryService {

    List<String> impoetExcel(MultipartFile file);

    List<OneSubjectDto> getSubjectTree();

    boolean deleteId(String id);

    boolean addOneCategory(EduSubject subject);

    boolean addTwoCategory(EduSubject subject);


    CourseVo queryCategory(String cate1, String cate2);
}


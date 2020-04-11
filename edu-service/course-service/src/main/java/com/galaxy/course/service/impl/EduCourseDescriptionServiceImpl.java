package com.galaxy.course.service.impl;


import com.galaxy.course.mapper.EduCourseDescriptionMapper;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.course.entity.EduCourseDescription;
import com.galaxy.course.entity.dto.CourseInfoDto;
import com.galaxy.course.service.EduCourseDescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
@Service
@Slf4j
public class EduCourseDescriptionServiceImpl   implements EduCourseDescriptionService {

    @Autowired
    private EduCourseDescriptionMapper descriptionMapper;
    /**
     * 添加课程描述信息
     */
        @Override
        public Boolean insertCourseDesc(CourseInfoDto desc) {
            if (StringUtils.isEmpty(desc.getDescription())) {
                log.info("课程添加失败 事务已回滚");
                throw new MyException(ResultEnum.NO_COURSE_DESC);
            }
            EduCourseDescription description = new EduCourseDescription();
            BeanUtils.copyProperties(desc,description);
            int insert = descriptionMapper.insert(description);
            return insert > 0;
        }


    /**
     * 更新描述
     */
    public Boolean uploadDesc(EduCourseDescription description) {
        int i = descriptionMapper.updateById(description);
        return i> 0;


    }

    @Override
    public Boolean descDelete(String id) {
        return null;
    }


}

package com.galaxy.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galaxy.course.entity.EduSubject;
import org.mapstruct.Mapper;


@Mapper
public interface EduCategoryMapper extends BaseMapper<EduSubject> {
}

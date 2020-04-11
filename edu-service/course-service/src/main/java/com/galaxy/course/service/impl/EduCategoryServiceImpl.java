package com.galaxy.course.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.galaxy.course.service.EduCategoryService;
import com.galaxy.common.bean.CourseVo;
import com.galaxy.common.enums.ResultEnum;
import com.galaxy.common.exception.MyException;
import com.galaxy.course.entity.EduSubject;
import com.galaxy.course.entity.dto.OneSubjectDto;
import com.galaxy.course.entity.dto.TwoSubjectDto;
import com.galaxy.course.mapper.EduCategoryMapper;
import com.galaxy.course.service.EduCourseService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
@Service
public class EduCategoryServiceImpl implements EduCategoryService {

    @Autowired
    private EduCategoryMapper mapper;

    @Autowired
    private EduCourseService courseService;

    @Override
    public List<String> impoetExcel(MultipartFile file) {
        List<String> msg = new ArrayList<>();

        try {
            //获取输入流
            InputStream in = file.getInputStream();
            // 创建 workbook
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            // 获取sheet
            //Sheet sheet = workbook.getSheetAt(0);
            XSSFSheet sheet = workbook.getSheetAt(0);

            // 获取行 从第二行开始
            int lastRowNum = sheet.getLastRowNum(); // 获取最后一行索引
            for (int i = 1; i <= lastRowNum; i++) {
                XSSFRow row = sheet.getRow(i);
                //Row row = sheet.getRow(i);
                if (row == null) {
                    String s = "表格数据为空";
                    msg.add(s);
                    continue;
                }
                // 获取列 1
                XSSFCell cellOne = row.getCell(0);
                if (cellOne == null) {
                    String s1 = "第" + (i + 1) + "行第一列数据为空";
                    msg.add(s1);
                    continue;
                }

                // 取值
                String v1 = cellOne.getStringCellValue();
                // 添加一级分类 父id 0，excel 添加一级分类有重复的名称 添加时判断
                String parent_id = null;
                EduSubject judgeSubject = this.judgeName(v1);
                if (judgeSubject == null) { // 添加
                    EduSubject eduSubject1 = new EduSubject();
                    eduSubject1.setTitle(v1);
                    eduSubject1.setParentId("0");
                    eduSubject1.setSort(0);
                    mapper.insert(eduSubject1);
                    parent_id = eduSubject1.getId();
                } else { // 不添加
                    parent_id = judgeSubject.getId();

                }

                // 获取列 2
                XSSFCell cellTwo = row.getCell(1);

                if (cellTwo == null) {
                    String s2 = "第" + (i + 1) + "行第二列数据为空";
                    msg.add(s2);
                    continue;
                }
                String v2 = cellTwo.getStringCellValue();
                EduSubject judge2Name = this.judge2Name(v2, parent_id);
                if (judge2Name == null) {
                    EduSubject eduSubject2 = new EduSubject();
                    eduSubject2.setTitle(v2);
                    eduSubject2.setSort(0);
                    eduSubject2.setParentId(parent_id);
                    mapper.insert(eduSubject2);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.IMPORT_EXCEL);
        }
        return msg;

    }

    /**
     * 判断是否存在一级分类
     */
    public EduSubject judgeName(String name) {

        LambdaQueryWrapper<EduSubject> wrapper = new LambdaQueryWrapper<EduSubject>()
                .eq(EduSubject::getTitle, name)
                .eq(EduSubject::getParentId, 0);
        EduSubject eduSubject = mapper.selectOne(wrapper);
        return eduSubject;
    }


    /**
     * 判断是否存在二级分类
     */
    public EduSubject judge2Name(String name, String pid) {
        LambdaQueryWrapper<EduSubject> wrapper = new LambdaQueryWrapper<EduSubject>()
                .eq(EduSubject::getTitle,name)
                .eq(EduSubject::getParentId,pid);
        EduSubject eduSubject = mapper.selectOne(wrapper);
        return eduSubject;
    }


    /**
     * 获取分类数据
     */
    @Override
    public List<OneSubjectDto> getSubjectTree() {
        List<OneSubjectDto> result = new ArrayList<>();
        // 获取一级分类列表
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        List<EduSubject> subjects = mapper.selectList(wrapper);
        // 遍历
        subjects.forEach(subject -> {
            OneSubjectDto os = new OneSubjectDto();
            // 复制到oneSubject 中
            BeanUtils.copyProperties(subject, os);
            //根据每一个一级列表获取二级列表
            QueryWrapper<EduSubject> w = new QueryWrapper<>();
            w.eq("parent_id", os.getId());
            List<EduSubject> subject2 = mapper.selectList(w);
            //  遍历
            subject2.forEach(s2 -> {
                TwoSubjectDto ow = new TwoSubjectDto();
                // 复制到twoSubject 中
                BeanUtils.copyProperties(s2, ow);
                // twosubject对象放入到 onesubject chrildren
                os.getChildren().add(ow);
            });
            result.add(os);

        });

        return result;
    }

    @Override
    public boolean deleteId(String id) {
        //查询是否有二级类别 有就无法删除一级类别
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<EduSubject> query = mapper.selectList(wrapper);
        if (query.size() != 0) {
            return false;
        }
        //删除
        int i = mapper.deleteById(id);
        return i == 1;
    }

    @Override
    public boolean addOneCategory(EduSubject subject) {
        if (StringUtils.isEmpty(subject.getTitle())) {
            throw new  MyException(ResultEnum.PARAM_ERROR);
        }
        // 是否重复一级分类
        EduSubject existSubject = this.judgeName(subject.getTitle());
        if (existSubject == null) {
            subject.setParentId("0");
            int insert = mapper.insert(subject);
            return insert >0 ;

        }
        return false;
    }

    @Override
    public boolean addTwoCategory(EduSubject subject) {
        if (StringUtils.isEmpty(subject.getTitle()) || StringUtils.isEmpty(subject.getParentId())) {
            throw new  MyException(ResultEnum.PARAM_ERROR);
        }
        EduSubject judge2Name = this.judge2Name(subject.getTitle(), subject.getParentId());
        if (judge2Name == null) {
            int insert = mapper.insert(subject);
            return insert >0;
        }
        return false;
    }

    @Override
    public CourseVo queryCategory(String cate1, String cate2) {
        CourseVo courseVo = new CourseVo();
        //一级
        EduSubject eduSubject = this.mapper.selectOne(new LambdaQueryWrapper<EduSubject>()
                .eq(EduSubject::getParentId, "0")
                .eq(EduSubject::getId, cate1));
        // 二级
        EduSubject subject = this.mapper.selectOne(new LambdaQueryWrapper<EduSubject>()
                .eq(EduSubject::getParentId, cate1)
                .eq(EduSubject::getId, cate2));
        if (eduSubject != null){
            courseVo.setCategoryPname(eduSubject.getTitle());
        }
        if (subject !=null){
            courseVo.setCategoryName(subject.getTitle());
        }
        return courseVo;

    }


}

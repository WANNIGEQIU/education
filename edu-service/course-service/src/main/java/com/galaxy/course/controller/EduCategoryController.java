package com.galaxy.course.controller;


import com.galaxy.common.util.ResultCommon;
import com.galaxy.course.service.EduCategoryService;
import com.galaxy.course.entity.EduSubject;
import com.galaxy.course.entity.dto.OneSubjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
@RestController
@RequestMapping("/category")
public class EduCategoryController {

    @Autowired
    private EduCategoryService service;

    /**
     * 导入excel中内容添加到分类列表
     * @return
     */
    @PostMapping("/import")
    public ResultCommon importExcelSubject(@RequestParam("file")MultipartFile file) {
        // 上传file 文件
        List<String> strings = service.impoetExcel(file);
        if (strings.size() == 0) {
            return ResultCommon.resultOk("导入成功啦!");
        }else {
            return ResultCommon.resultFail().data(strings);
        }

    }


    /**
     * 获取分类数据
     */
    @GetMapping("/tree")
    public ResultCommon getSubjectTree() {
        List<OneSubjectDto> r = service.getSubjectTree();
        return ResultCommon.resultOk(r);
    }

    /**
     * 分类删除
     */
    @DeleteMapping("{id}")
    public ResultCommon deleteId(@PathVariable("id") String id) {
        boolean b = service.deleteId(id);
        if (b) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }
    }

    /**
     * 添加一级分类
     * 参数 tilte: 标题名称
     */
    @PostMapping("/addOneCategory")
     public ResultCommon addOneCategory(@RequestBody EduSubject subject) {

        boolean b = service.addOneCategory(subject);
        if (b) {
            return  ResultCommon.resultOk();
        }else {
            return  ResultCommon.resultFail();
        }

    }

    /**
     * 添加二级分类
     * 参数 tilte: 标题名称
     *  parentId:
     */
    @PostMapping("/addTwoCategory")
    public ResultCommon addTwoCategory(@RequestBody EduSubject subject) {

        boolean b = service.addTwoCategory(subject);
        if (b) {
            return  ResultCommon.resultOk();
        }else {
            return  ResultCommon.resultFail();
        }
    }
}


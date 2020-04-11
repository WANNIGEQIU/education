package com.galaxy.base.helper;


import com.galaxy.base.entity.EduMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 根据权限数据构建菜单数据
 * </p>
 *
 * @author qy
 * @since 2019-11-11
 */
public class PermissionHelper {

    /**
     * 使用递归方法建菜单
     * @param treeNodes
     * @return
     */
    public static List<EduMenu> bulid(List<EduMenu> treeNodes) {
        List<EduMenu> trees = new ArrayList<>();
        for (EduMenu treeNode : treeNodes) {
            if (0 ==treeNode.getMid()) {
                treeNode.setLevel(1);
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    public static EduMenu findChildren(EduMenu treeNode,List<EduMenu> treeNodes) {
        treeNode.setChildren(new ArrayList<EduMenu>());

        for (EduMenu it : treeNodes) {
            if(treeNode.getId()==(it.getMid())) {
                int level = treeNode.getLevel() + 1;
                it.setLevel(level);
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return treeNode;
    }
}

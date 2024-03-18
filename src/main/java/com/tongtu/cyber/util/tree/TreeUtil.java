package com.tongtu.cyber.util.tree;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 树结构工具类
 *
 * @author : 陈世恩
 * @date : 2023/5/12 15:18
 */
public class TreeUtil {
    /**
     * 单根tree
     *
     * @param list 数据源
     * @return 跟节点
     */
    public static TreeNode createTree(List<TreeNode> list) {
        TreeNode node = new TreeNode();
        toTree(list, node);
        return node;
    }

    /**
     * 多跟tree
     *
     * @param list   数据源
     * @param rootId 根节点的父id
     * @return 根结点集合
     */
    public static List<TreeNode> createTree(List<TreeNode> list, String rootId) {
        List<TreeNode> retVal = new LinkedList<TreeNode>();
        toTree(list, rootId, retVal);
        return retVal;
    }

    /**
     * 单根tree
     *
     * @param list
     * @param tree
     */
    private static void toTree(List<TreeNode> list, TreeNode tree) {
        String parentId = tree.getId();
        for (TreeNode node : list) {
            if (node.getParentId().equals(parentId)) {
                //添加到tree上
                if (tree.getChildren() == null) {
                    List<TreeNode> children = new LinkedList<TreeNode>();
                    children.add(node);
                    tree.setChildren(children);
                } else {
                    tree.addChildren(node);
                }
                //寻找子节点
                toTree(list, node);
            }
        }
    }

    /**
     * 多根tree
     *
     * @author liudonghe
     */
    private static void toTree(List<TreeNode> list, String pid, List<TreeNode> tree) {
        for (TreeNode node : list) {
            if (node.getParentId().equals(pid)) {
                tree.add(node);
                toTree(list, node);
            }
        }
    }

    /**
     * 排序
     *
     * @param list
     * @param desc 是否为降序
     * @return
     */
    public static List<TreeNode> sort(List<TreeNode> list, boolean desc) {
        if (desc) {
            list.sort(new Comparator<TreeNode>() {
                @Override
                public int compare(TreeNode o1, TreeNode o2) {
                    return (int) (o1.getLevel() - o2.getLevel());
                }
            });
        } else {
            Collections.sort(list);
        }
        return list;
    }

}

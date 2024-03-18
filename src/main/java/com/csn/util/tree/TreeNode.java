package com.csn.util.tree;

import lombok.Data;

import java.util.List;

@Data
public class TreeNode implements Comparable<TreeNode> {
    private String id;
    private String parentId;
    private Integer type;
    private Integer level;
    private String position;
    private String name;
    private String description;
    private String version;
    private List<TreeNode> children;

    public void addChildren(TreeNode treeNode) {
        children.add(treeNode);
    }

    @Override
    public int compareTo(TreeNode o) {
        return (int) (o.level - this.level);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TreeNode other = (TreeNode) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

}
package com.devjpah.appbst;

public class BinaryNode {

    private int data;
    private BinaryNode left;
    private BinaryNode right;
    private int x;
    private int y;
    private boolean ChildPosition;

    public BinaryNode(int data) {
        setData(data);
        setLeft(null);
        setRight(null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isLeaf() {
        return ((left == null) && (right == null)) ? true : false;
    }

    public boolean hasOneChild() {
        if (left == null && right != null) {
            ChildPosition = true;
            return true;
        } else if (left != null && right == null) {
            ChildPosition = false;
            return true;
        }else{
            return false;
        }
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public BinaryNode getLeft() {
        return left;
    }

    public void setLeft(BinaryNode left) {
        this.left = left;
    }

    public BinaryNode getRight() {
        return right;
    }

    public void setRight(BinaryNode right) {
        this.right = right;
    }

    public boolean isChildPosition() {
        return ChildPosition;
    }
}

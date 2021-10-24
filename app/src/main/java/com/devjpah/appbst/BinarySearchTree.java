package com.devjpah.appbst;

import android.view.View;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class BinarySearchTree {

    private BinaryNode root;
    private BinaryNode father;
    private boolean position;
    private int nodes;
    private int leafs;

    public BinarySearchTree() {
        root = null;
        nodes = 0;
    }

    public BinarySearchTree(int data) {
        root = new BinaryNode(data);
        nodes = 1;
    }

    public BinaryNode getRoot() {
        return root;
    }

    //Punto 1
    private String rInorden;
    public String InOrden() {
        rInorden = "";
        InOrden(root);
        return rInorden;
    }

    private void InOrden(BinaryNode currentRoot) {
        if (currentRoot != null) {
            InOrden(currentRoot.getLeft());
            rInorden += currentRoot.getData() + ", ";
            InOrden(currentRoot.getRight());
        }
    }

    //Punto 2
    private String rPostorden;
    public String Postorden() {
        rPostorden = "";
        Postorden(root);
        return rPostorden;
    }

    private void Postorden(BinaryNode currentRoot) {
        if (currentRoot != null) {
            Postorden(currentRoot.getLeft());
            Postorden(currentRoot.getRight());
            rPostorden += currentRoot.getData() + ", ";
        }
    }

    //Punto 3
    private String rPreorden = "";

    public String PreOrden() {
        rPreorden = "";
        PreOrden(root);
        return rPreorden;
    }

    private void PreOrden(BinaryNode currentRoot) {
        if (currentRoot != null) {
            rPreorden += currentRoot.getData() + ", ";
            PreOrden(currentRoot.getLeft());
            PreOrden(currentRoot.getRight());
        }
    }

    //Punto 4
    public int CountNodes() {
        return nodes;
    }

    //Punto 5
    public int CountLeafs() {
        InOrden();
        return leafs;
    }

    public void Add(int data) throws Exception{
        if (root == null) {
            root = new BinaryNode(data);
        } else //validar si el dato ya existe
            if (Search(data) != null) {
                throw new Exception("DATO REPETIDO NO SE PUEDE INSERTAR");
            } else {
                Add(data, root);
            }
        nodes++;
    }

    private void Add(int data, BinaryNode currentRoot) {
        if (data < currentRoot.getData()) {
            if (currentRoot.getLeft() == null) {
                currentRoot.setLeft(new BinaryNode(data));
            } else {
                Add(data, currentRoot.getLeft());
            }

        } else if (currentRoot.getRight() == null) {
            currentRoot.setRight(new BinaryNode(data));
        } else {
            Add(data, currentRoot.getRight());
        }
    }

    public BinaryNode Search(int data) {
        return Search(data, root);
    }

    private BinaryNode Search(int data, BinaryNode currentRoot) {
        if (currentRoot == null) {
            return null;
        }
        if (data == currentRoot.getData()) {
            return currentRoot;
        }

        father = currentRoot;

        if (data < currentRoot.getData()) {
            position = false;
            return Search(data, currentRoot.getLeft());
        } else {
            position = true;
            return Search(data, currentRoot.getRight());
        }
    }

    //Punto 6
    public void Delete(int data) throws Exception{
        if (root == null) {
            throw new Exception("Árbol vacío");
        }else {
            Delete(data, root);
        }
        nodes--;
    }

    private void Delete (int data, BinaryNode currentRoot) throws Exception {

        BinaryNode v = Search(data);
        if (v.isLeaf()) {
            if (position) {
                father.setRight(null);
            } else {
                father.setLeft(null);
            }
        } else if (v.hasOneChild()) {
            if (v.isChildPosition()) {

                if (v == root) {
                    root = root.getRight();
                    return;
                }

                if (position) {
                    father.setRight(v.getRight());
                } else {
                    father.setLeft(v.getRight());
                }
            } else if (position) {
                father.setRight(v.getLeft());
            } else {
                father.setLeft(v.getLeft());
            }
        } else {
            BinaryNode minimum = getMinor(v.getRight());
            Delete(minimum.getData());
            v.setData(minimum.getData());
        }
    }

    //Punto 7
    public int LastLevel() {
        return LastLevel(root);
    }

    private int LastLevel(BinaryNode currentRoot) {
        if (currentRoot == null) {
            return 0;
        }
        int rightLevels = LastLevel(currentRoot.getRight());
        int leftLevels = LastLevel(currentRoot.getLeft());

        int max = (rightLevels > leftLevels) ? rightLevels : leftLevels;
        return (max + 1);
    }

    private void showLevel(BinaryNode currentRoot, int level) {
        if (currentRoot == null) {
            return;
        }
        if (level == 1) {
            System.out.print(currentRoot.getData() + " ");
        } else if (level > 1) {
            showLevel(currentRoot.getLeft(), level - 1);
            showLevel(currentRoot.getRight(), level - 1);
        }
    }

    //ArrayList<BinaryNode> niveles = new ArrayList<>();
    public String[] niveles;
    public String[] imprimirNivel() {
        niveles = new String[LastLevel()];
        imprimirNivel(root, 0);
        return niveles;
    }

    private void imprimirNivel(BinaryNode currentRoot, int level) {
        if (currentRoot != null) {
            niveles[level] = currentRoot.getData() + "," + ((niveles[level] != null) ? niveles[level] : "");
            imprimirNivel(currentRoot.getRight(), level + 1);
            imprimirNivel(currentRoot.getLeft(), level + 1);
        }
    }

    //Punto 8
    public void LevelOrder() {
        //...
        /*
        Para mostrar los datos se recomienda usar:
            System.out.print(x.getData()+" ");
        donde x representa un nodo del árbol

        Para generar un salto de linea se recomienda usar:
            System.out.print("\n");

         */

        int totalLevels = LastLevel(root);
        for (int i = 1; i <= totalLevels; i++) {
            showLevel(root, i);
            System.out.print("\n");

        }
    }

    public BinaryNode getMinor(BinaryNode currentRoot) {
        if (currentRoot.getLeft() == null) {
            return currentRoot;
        } else {
            return getMinor(currentRoot.getLeft());
        }
    }
}
package com.javarush.task.task36.task3604;

/* 
Разбираемся в красно-черном дереве
*/
public class Solution {
    public static void main(String[] args) {
        RedBlackTree redBlackTree = new RedBlackTree();
        for (int i = 0; i < 10; i++) {
            redBlackTree.insert(i);
        }
    }
}

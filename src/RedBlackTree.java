// == CS400 Spring 2024 File Header Information ==
// Name: Ryan Murphy
// Email: rjmurphy8@wisc.edu
// Lecturer: Florian
// Notes to Grader: N/A

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {
    protected static class RBTNode<T> extends Node<T> {
        public boolean isBlack = false;

        public RBTNode(T data) {
            super(data);
        }

        public RBTNode<T> getUp() {
            return (RBTNode<T>) this.up;
        }

        public RBTNode<T> getDownLeft() {
            return (RBTNode<T>) this.down[0];
        }

        public RBTNode<T> getDownRight() {
            return (RBTNode<T>) this.down[1];
        }
    }

    /**
     * After a naive insert, this method is called to make any adjustments necessary adjustments to maintain
     * RBT properties. Sometimes recursive
     *
     * @param child the node which is the basis for investigation for RBT property violations
     */
    protected void enforceRBTreePropertiesAfterInsert(RBTNode child) {
        // Instantiates parent if child isn't null, which case the method terminates
        if (child == null)
            return;
        RBTNode parent = child.getUp();

        // Instantiates grandparent if child isn't null, which case the method terminates
        // Also terminates if parent is red since there's no need to continue if parent is black as no repair
        // operations are needed
        if (parent == null || parent.isBlack)
            return;

        RBTNode grandparent = parent.getUp();

        // Instantiates aunt and helpful booleans if child isn't null, which case the method terminates
        if (grandparent == null)
            return;
        boolean isParentLeft = grandparent.getDownLeft() == parent;
        boolean isNewNodeLeft = parent.getDownLeft() == child;
        RBTNode aunt = isParentLeft ? grandparent.getDownRight() : grandparent.getDownLeft();

        // Recolors and checks up
        if (aunt != null && !aunt.isBlack) {
            parent.isBlack = true;
            grandparent.isBlack = false;
            aunt.isBlack = true;

            enforceRBTreePropertiesAfterInsert(grandparent);
        }
        // Rotates and recolors
        else {
            // One right/left rotation
            if (!(isParentLeft ^ isNewNodeLeft)) {
                rotate(parent, grandparent);
                parent.isBlack = true;
            }
            // One right and one left rotation (Not necessarily in that order)
            else {
                rotate(child, parent);
                rotate(child, grandparent);
                child.isBlack = true;
            }
            grandparent.isBlack = false;
        }
    }

    @Override
    public boolean insert(T data) throws NullPointerException {
        if (data == null)
            throw new NullPointerException("Cannot insert data value null into the tree.");

        RBTNode<T> newNode = new RBTNode<T>(data);

        if (!insertHelper(newNode))
            return false;

        enforceRBTreePropertiesAfterInsert(newNode);

        return ((RBTNode) root).isBlack = true;
    }

    /**
     * Tests the recoloring operation without any rotations
     */
    @Test
    public void testRecoloring() {
        RedBlackTree testTree = new RedBlackTree();

        testTree.insert(3);
        RBTNode rootNode = (RBTNode) testTree.root;
        Assertions.assertTrue(rootNode.isBlack);

        testTree.insert(1);
        Assertions.assertTrue(!rootNode.getDownLeft().isBlack);

        testTree.insert(4);
        Assertions.assertTrue(!rootNode.getDownRight().isBlack);

        testTree.insert(2);
        Assertions.assertTrue(!rootNode.getDownLeft().getDownRight().isBlack && rootNode.getDownLeft().isBlack &&
                rootNode.isBlack);
    }

    /**
     * Tests the right rotation operation and insures the colors are correct
     */
    @Test
    public void testRotatingRight() {
        RedBlackTree testTree = new RedBlackTree();

        testTree.insert(3);
        testTree.insert(2);
        testTree.insert(1);

        RBTNode rootNode = (RBTNode) testTree.root;

        // Checks the structure is correct
        Assertions.assertTrue(rootNode.data.equals(2) && rootNode.getDownLeft().data.equals(1)
                && rootNode.getDownRight().data.equals(3));

        // Checks the colors are correct
        Assertions.assertTrue(rootNode.isBlack && !rootNode.getDownLeft().isBlack
                && !rootNode.getDownRight().isBlack);
    }

    /**
     * Tests the right, then left rotation operation and insures the colors are correct
     */
    @Test
    public void testRotatingRightAndLeft() {
        RedBlackTree<Integer> testTree = new RedBlackTree();

        testTree.insert(3);
        testTree.insert(1);
        testTree.insert(2);

        RBTNode rootNode = (RBTNode) testTree.root;

        // Checks the structure is correct
        Assertions.assertTrue(rootNode.data.equals(2) && rootNode.getDownLeft().data.equals(1)
                && rootNode.getDownRight().data.equals(3));

        // Checks the colors are correct
        Assertions.assertTrue(rootNode.isBlack && !rootNode.getDownLeft().isBlack
                && !rootNode.getDownRight().isBlack);
    }

    /**
     * Tests a tree with more nodes in it
     */
    @Test
    public void testComplexRotation() {
        RedBlackTree<Integer> testTree = new RedBlackTree();

        testTree.insert(10);
        testTree.insert(20);
        testTree.insert(1);
        testTree.insert(6);
        testTree.insert(5);

        RBTNode rootNode = (RBTNode) testTree.root;

        // Checks the structure is correct
        Assertions.assertTrue(rootNode.data.equals(10) && rootNode.getDownLeft().data.equals(5)
                && rootNode.getDownLeft().getDownLeft().data.equals(1)
                && rootNode.getDownLeft().getDownRight().data.equals(6));


        // Checks the colors are correct
        Assertions.assertTrue(rootNode.isBlack && rootNode.getDownLeft().isBlack
                && rootNode.getDownRight().isBlack && !rootNode.getDownLeft().getDownLeft().isBlack
                && !rootNode.getDownLeft().getDownRight().isBlack);
    }
}

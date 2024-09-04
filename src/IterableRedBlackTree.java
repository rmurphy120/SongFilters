import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class IterableRedBlackTree<T extends Comparable<T>>
        extends RedBlackTree<T> implements IterableSortedCollection<T> {
    // Start point of the iterator. Initialized with a lambda expression which causes startPoint to always be smaller
    // than what it's compared to (So it negates any affect of the startPoint until it's initialized)
    private Comparable<T> startPoint = (other) -> -1;

    /**
     * Sets the startPoint of the iterator
     *
     * @param startPoint the startPoint of the iterator. If null sets this.startPoint to something always smaller than
     *                   anything it's compared to
     */
    public void setIterationStartPoint(Comparable<T> startPoint) {
        if (startPoint == null)
            this.startPoint = (other) -> -1;
        this.startPoint = startPoint;
    }

    @Override
    public Iterator<T> iterator() {
        return new RBTIterator<>(this.root, startPoint);
    }

    /**
     * Overrides this class in BinarySearchTree which allows for duplicates
     *
     * @param newNode the new node to be inserted
     * @return true if insertion was successful
     * @throws NullPointerException if newNode is null
     */
    @Override
    protected boolean insertHelper(Node<T> newNode) throws NullPointerException {
        if (newNode == null) throw new NullPointerException("new node cannot be null");

        if (this.root == null) {
            // add first node to an empty tree
            root = newNode;
            size++;
            return true;
        } else {
            // insert into subtree
            Node<T> current = this.root;
            while (true) {
                int compare = newNode.data.compareTo(current.data);
                if (compare <= 0) {
                    // insert in left subtree
                    if (current.down[0] == null) {
                        // empty space to insert into
                        current.down[0] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[0];
                    }
                } else {
                    // insert in right subtree
                    if (current.down[1] == null) {
                        // empty space to insert into
                        current.down[1] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[1];
                    }
                }
            }
        }
    }

    private static class RBTIterator<R> implements Iterator<R> {
        private Comparable<R> startPoint;
        private Stack<Node<R>> stack;

        public RBTIterator(Node<R> root, Comparable<R> startPoint) {
            this.startPoint = startPoint;
            stack = new Stack<>();
            buildStackHelper(root);
        }

        /**
         * Builds the stack to be used by the iterator
         *
         * @param node the root node of the subtree which is added to the stack if it should as well as recursively
         *             calling this method on its children (If it should)
         */
        private void buildStackHelper(Node<R> node) {
            // Base case
            if (node == null)
                return;

            // Recursive cases
            // Checks the largest (right child) nodes first
            buildStackHelper(node.down[1]);

            // If this node is greater than startPoint, adds it to stack and checks left subtree
            if (startPoint.compareTo(node.data) <= 0) {
                stack.add(node);
                buildStackHelper(node.down[0]);
            }
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public R next() {
            if (!hasNext())
                throw new NoSuchElementException("No next value exists");
            return stack.pop().data;
        }
    }

    /**
     * Tests the iteration of string values using a for loop
     */
    @Test
    public void testIntegerIteration() {
        IterableRedBlackTree<Integer> testTree = new IterableRedBlackTree();
        testTree.insert(2);
        testTree.insert(6);
        testTree.insert(4);
        testTree.insert(1);
        testTree.insert(3);
        testTree.insert(5);

        int i = 1;
        for (Integer each : testTree) {
            Assertions.assertTrue(each.equals(i));
            i++;
        }
    }

    /**
     * Tests the iteration of string values using a for loop
     */
    @Test
    public void testStringIteration() {
        IterableRedBlackTree<String> testTree = new IterableRedBlackTree();
        testTree.insert("c");
        testTree.insert("e");
        testTree.insert("d");
        testTree.insert("b");
        testTree.insert("a");

        String i = "a";
        for (String each : testTree) {
            Assertions.assertTrue(each.equals(i));

            // Increments the ascii value of the one-char string
            i = Character.toString((char) ((int) i.charAt(0) + 1));
        }
    }

    /**
     * Tests duplicate integer values as well as throwing a NoSuchElementException if there is no
     * next in the stack
     */
    @Test
    public void testDuplicates() {
        IterableRedBlackTree<Integer> testTree = new IterableRedBlackTree();
        testTree.insert(2);
        testTree.insert(6);
        testTree.insert(4);
        testTree.insert(6);

        Iterator<Integer> testIterator = testTree.iterator();

        Assertions.assertTrue(testIterator.next().equals(2));
        Assertions.assertTrue(testIterator.next().equals(4));
        Assertions.assertTrue(testIterator.next().equals(6));
        Assertions.assertTrue(testIterator.next().equals(6));

        try {
            testIterator.next();
            Assertions.fail();
        } catch (NoSuchElementException e) {
        }
    }

    @Test
    public void testSetStartPoint() {
        IterableRedBlackTree<Integer> testTree = new IterableRedBlackTree();
        testTree.insert(2);
        testTree.insert(6);
        testTree.insert(4);
        testTree.insert(1);
        testTree.insert(10);

        testTree.setIterationStartPoint(6);

        Iterator<Integer> testIterator = testTree.iterator();

        Assertions.assertTrue(testIterator.next().equals(6));
        Assertions.assertTrue(testIterator.next().equals(10));
        Assertions.assertTrue(!testIterator.hasNext());
    }
}

public class BinarySearchTree {
    Node root;

    public BinarySearchTree() {
        this.root = null;
    }


    public void insert(String elem) {
        // create a new Node instance
        Node newNode = new Node(elem);
        int result;

        // now, insert n into the tree
        // trace down the tree until we hit a NULL
        Node current = root, parent = null;
        while (current != null)
        {
            result = current.data.compareTo(elem);
            if (result == 0) {
                // they are equal - attempting to enter a duplicate - do nothing
                return;
            }
            else if (result > 0)
            {
                // current.Data > elem, must add n to current's left subtree
                parent = current;
                current = current.left;
            }
            else if (result < 0)
            {
                // current.Data < elem, must add n to current's right subtree
                parent = current;
                current = current.right;
            }
        }


        if (parent == null)
            // the tree was empty, make n the root
            root = newNode;
        else
        {
            result = parent.data.compareTo(elem);
            if (result > 0)
                // parent.Data > elem, therefore n must be added to the left subtree
                parent.left = newNode;
            else
                // parent.Data < elem, therefore n must be added to the right subtree
                parent.right = newNode;
        }
    }


    public boolean exists(String elem){
        Node current = root;
        while (current != null){
            if (current.data.compareTo(elem) == 0){
                return true;
            } else if (current.data.compareTo(elem) > 0){
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    void printPreorder(Node current)
    {
        if (current != null)
        {
            // Output the value of the current node
            System.out.println(current.data);

            // Recursively print the left and right children
            printPreorder(current.left);
            printPreorder(current.right);
        }
    }

//    public void insert(String elem) {
//        Node newNode = new Node(elem);
//        if (root == null) {
//            root = newNode;
//            return;
//        }
//        Node current = root;
//        Node parent = null;
//        while (true) {
//            parent = current;
//            if (elem.compareTo(current.data) < 0) {
//                current = current.left;
//                if (current == null) {
//                    parent.left = newNode;
//                    return;
//                }
//            } else {
//                current = current.right;
//                if (current == null) {
//                    parent.right = newNode;
//                    return;
//                }
//            }
//        }
//    }



//    public int getPosition(String val) {
//        return positionHelper(val, root, 0);
//    }
//
//    public int positionHelper(String val, Node currentNode, int steps) {
//        // In-order search checks left node, then current node, then right node
//        if(currentNode.left != null) {
//            steps = positionHelper(val, currentNode.left, steps++);
//        }
//
//        // We found the node or have already moved over the node, return current steps
//        if(currentNode.data.compareTo(val) >= 0){
//            return steps;
//        }
//
//        // Next Node Index
//        steps++;
//
//        if(currentNode.right != null) {
//            steps = positionHelper(val, currentNode.right, steps++);
//        }
//        return steps;
//    }
}

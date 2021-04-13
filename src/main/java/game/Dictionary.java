package game;

import ft.NodeWordlist;

/**
 * @author yuzun
 * <p>
 * Dictionary
 */

public class Dictionary {

    private NodeWordlist root;      // root of the binary search tree

    public Dictionary(NodeWordlist root) {
        this.root = root;
    }

    public boolean wordExists(String word) {
        if (word == null) {
            return false;
        }
        if (word.equals("")) {
            return false;
        }
        return wordExists(root, word);
    }

    /**
     * recursive function to check if word exists
     */
    public boolean wordExists(NodeWordlist helper, String word) {
        if (helper == null) {
            return false;
        }
        int comparison = helper.getData().compareTo(word);
        if (comparison < 0) {
            return wordExists(helper.getRight(), word);
        } else if (comparison > 0) {
            return wordExists(helper.getLeft(), word);
        } else {
            return true;
        }


    }
}

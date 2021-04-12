package ft;

import java.util.ArrayList;

/** @author Class for creation of BST */
public class BinarySearchTreeWordlist {

  /**
   * @author Creates BST from ArrayList<String>
   * @param words Requires ArrayList<String> with all words
   * @param start Requires int value to start with, usually 0 at creation
   * @param end Requires int value to end with, usually words.size() - 1 at creation
   * @return Returns root node from BST
   */
  public NodeWordlist createBSTFromArrayList(ArrayList<String> words, int start, int end) {
    if (start > end) {
      return null;
    }
    int middle = (start + end) / 2;
    NodeWordlist node = new NodeWordlist(words.get(middle));
    node.setLeft(createBSTFromArrayList(words, start, middle - 1));
    node.setRight(createBSTFromArrayList(words, middle + 1, end));
    return node;
  }
}

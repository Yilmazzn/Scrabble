package ft;

/** @author vkaczmar Class for data in BST */
public class NodeWordlist {
  private String data;
  private NodeWordlist left, right;

  /**
   * @author vkaczmar Constructor for creation of Node, data and both children get values.
   * @param data String value with data to save.
   */
  public NodeWordlist(String data) {
    this.data = data;
    left = null;
    right = null;
  }

  /**
   * @author vkaczmar
   * @return Returns String Data
   */
  public String getData() {
    return data;
  }

  /**
   * @author vkaczmar
   * @return Returns left child of Node
   */
  public NodeWordlist getLeft() {
    return left;
  }

  /**
   * @author vkaczmar Sets left child to given Node
   * @param left Value for left child
   */
  public void setLeft(NodeWordlist left) {
    this.left = left;
  }

  /**
   * @author vkaczmar
   * @return Returns right child of Node
   */
  public NodeWordlist getRight() {
    return right;
  }

  /**
   * @author vkaczmar Sets right child to given Node
   * @param right Value for right child
   */
  public void setRight(NodeWordlist right) {
    this.right = right;
  }
}

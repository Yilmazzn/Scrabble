package ft;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/**
 * @author vkaczmar Class that is able to load, read and manage all words from the wordlist in a BST
 *     Accessible via root Node
 */
public class TxtFileHandler {
  private BufferedReader br;
  private ArrayList<String> uneditedLines, words;
  private NodeWordlist root;

  /**
   * @author vkaczmar Constructor with parameter to the wordlist.txt file. Does everything up to the
   *     creation of the binary search tree
   * @param absolutePath Requires the absolute Path to the wordlist itself
   */
  public TxtFileHandler(String absolutePath) {
    File f = new File(absolutePath);
    try {
      br = new BufferedReader(new FileReader(f));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    uneditedLines = new ArrayList<String>();
    getUneditedLines();
    words = new ArrayList<String>();
    getWords();
    BinarySearchTreeWordlist bst = new BinarySearchTreeWordlist();
    root = bst.createBSTFromArrayList(words, 0, words.size() - 1);
  }

  /**
   * @author vkaczmar Private method to get all lines from the wordlist, which are neither empty nor
   *     an introduction line. These lines get added to uneditedLines.
   */
  private void getUneditedLines() {
    String line;
    try {
      while ((line = br.readLine()) != null) {
        if (line.matches("[A-z][A-Z].*")) {
          uneditedLines.add(line);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @author vkaczmar Private method to get words from uneditedLines. words get added to
   *     ArrayList<String> words
   */
  private void getWords() {
    String[] splitLine;
    Iterator<String> it = uneditedLines.iterator();
    while (it.hasNext()) {
      splitLine = it.next().split("\\s");
      words.add(splitLine[0]);
    }
  }

  /**
   * @author vkaczmar
   * @return Returns root node of BST
   */
  public NodeWordlist getRoot() {
    return root;
  }

  /**
   * @author vkaczmar Checks wether a certain word exists in wordlist.
   * @param node Requires node to start searching with
   * @param word Requires word, in a non case sensitive way
   * @return Returns true, if word exists
   */
  public boolean wordExists(NodeWordlist node, String word) {
    if (node == null) {
      return false;
    } else if (node.getData().compareTo(word.toUpperCase()) == 0) {
      return true;
    } else if (node.getData().compareTo(word.toUpperCase()) > 0) {
      return wordExists(node.getLeft(), word);
    } else if (node.getData().compareTo(word.toUpperCase()) < 0) {
      return wordExists(node.getRight(), word);
    } else {
      return false;
    }
  }
}

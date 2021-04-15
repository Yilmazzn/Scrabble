package ft;

import game.Dictionary;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author vkaczmar Class that is able to load, read and manage all words from the wordlist in a BST
 * Accessible via root Node
 */
public class TxtFileHandler {
  private BufferedReader br;
  private ArrayList<String> uneditedLines = new ArrayList<>();
  private ArrayList<String> words = new ArrayList<>();
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
    getUneditedLines();
    getWords();
    root = createBSTFromArrayList(words, 0, words.size() - 1);  // creates binary search tree
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
      words.add(splitLine[0].toUpperCase());
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

  public Dictionary readDictionary() {
    return new Dictionary(root);
  }
}

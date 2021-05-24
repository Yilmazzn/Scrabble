package game;

/** @author vihofman for Dictionary test */
class DictionaryTest {

  /*
      private String sep = System.getProperty("file.separator");
      private String xmlPathTest =
              System.getProperty("user.dir")
                      + sep
                      + "src"
                      + sep
                      + "main"
                      + sep
                      + "resources"
                      + sep
                      + "scrabbleWords"
                      + sep
                      + "ScrabbleWordsExample.txt";
      // variables setup
      private BufferedReader br;
      private ArrayList<String> uneditedLines, words;
      private NodeWordlist root;
      //create private method createBSTFromArrayList from Dictionary.java
      private NodeWordlist createBSTFromArrayListTest(ArrayList<String> words, int start, int end) {
          if (start > end) {
              return null;
          }
          int middle = (start + end) / 2;
          NodeWordlist node = new NodeWordlist(words.get(middle),);
          node.setLeft(createBSTFromArrayListTest(words, start, middle - 1));
          node.setRight(createBSTFromArrayListTest(words, middle + 1, end));
          return node;
      }
      //setup private method getUnitedLines
      private void getUneditedLinesTest() {
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
      //setup private method getWords
      private void getWordsTest() {
          String[] splitLine;
          Iterator<String> it = uneditedLines.iterator();
          while (it.hasNext()) {
              splitLine = it.next().split("\\s");
              words.add(splitLine[0]);
          }
      }
      Dictionary dictionary = new Dictionary(xmlPathTest);
      NodeWordlist helpNode = createBSTFromArrayListTest(words, 0, 100);
      //Assertions.assertEquals(wordExists(root, "CHURROS"), true);
  */
}

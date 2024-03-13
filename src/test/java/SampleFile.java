import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class SampleFile {
  public SampleFile() {
  }

  public static void main(String[] var0) {
    List<String> myList = new ArrayList<>();
    List<String> NameList = new ArrayList<>();
    myList.add("Hello");
    myList.add("World");

    // Scenario 1: unsafe
    System.out.println(myList.get(0));

    // Scenario 2: safe
    if (!myList.isEmpty()) {
      System.out.println(myList.get(1));
    }

    // Scenario 3: unsafe
    for (String item : myList) {
      System.out.println(item);
    }

    // Scenario 4: safe
    if (!myList.isEmpty()) {
      for (String item : myList) {
        System.out.println(item);
      }
    }

    // Scenario 5: safe
    myList.clear();
    if (!myList.isEmpty()) {
      System.out.println(myList.get(0));
    }

    // Scenario 6: safe
    List<String> item = new ArrayList<>();
    if (!item.isEmpty()) {
      System.out.println(item.get(0));
    }

    // Scenario 7: unsafe
    System.out.println(item.get(1));

    // Scenario 8: unsafe
    Iterator<String> iterator = myList.iterator();
    while (iterator.hasNext()) {
      String next = iterator.next();
      System.out.println(next);
    }

    // Scenario 9: safe
    if (!myList.isEmpty()) {
      Iterator<String> iterator1 = myList.iterator();
      while (iterator1.hasNext()) {
        String next = iterator1.next();
        System.out.println(next);
      }
    }

    // Scenario 10: unsafe
    myList.clear();
    System.out.println(myList.get(1));

    // Scenario 11: unsafe
    if (!myList.isEmpty()) {
      myList.clear();
      System.out.println(myList.get(1));
    }

    // Scenario 12: unsafe
    if (!myList.isEmpty()) {
      myList.remove("Hello");
      System.out.println(myList.get(1));
    }

    // Scenario 13: safe
    if (!myList.isEmpty()) {
      myList.remove("Hello");
      if (!myList.isEmpty()) {
        System.out.println(myList.get(1));
      }
    }

    // Scenario 14: unsafe
    if (!myList.isEmpty()) {
      // assign NameList to myList
      myList = NameList;
      System.out.println(myList.get(1));
    }

    // Scenario 15: safe
    if (!myList.isEmpty()) {
      // assign NameList to myList
      myList = NameList;
      if (!myList.isEmpty()) {
        System.out.println(myList.get(1));
      }
    }
  }
}

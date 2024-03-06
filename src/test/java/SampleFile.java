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

    // remove the second element from myList
    myList.remove(1);

    // Scenario 1: Accessing list without checking if it's empty
    System.out.println(myList.get(0));

    // Scenario 2: Accessing list after checking it's not empty
    if (!myList.isEmpty()) {
      System.out.println(myList.get(1));
    }

    // Scenario 3: Potentially problematic loop (if list were empty)
    for (String item : myList) {
      System.out.println(item);
    }

    // Scenario 4: Safely iterating over the list
    if (!myList.isEmpty()) {
      for (String item : myList) {
        System.out.println(item);
      }
    }

    // Scenario 5: Clearing the list and then trying to access it
    myList.clear();
    if (!myList.isEmpty()) {
      System.out.println(myList.get(0));
    }

    // Scenario 6:
    List<String> item = new ArrayList<>();
    if (!item.isEmpty()) {
      System.out.println(item.get(0));
    }

    // Scenario 7:
    System.out.println(item.get(1));

    // Scenario 8:
    Iterator<String> iterator = myList.iterator();
    while (iterator.hasNext()) {
      String next = iterator.next();
      System.out.println(next);
    }

    // Scenario 9:
    if (!myList.isEmpty()) {
      Iterator<String> iterator1 = myList.iterator();
      while (iterator1.hasNext()) {
        String next = iterator1.next();
        System.out.println(next);
      }
    }

    // Scenario 10: Should be counted as unsafe usage
    myList.clear();
    System.out.println(myList.get(1));

    // Scenario 11: Should be counted as unsafe usage
    if (!myList.isEmpty()) {
      myList.clear();
      System.out.println(myList.get(1));
    }

    // Scenario 12: Should be counted as unsafe usage
    if (!myList.isEmpty()) {
      myList.remove("Hello");
      System.out.println(myList.get(1));
    }

    // Scenario 13: Should be counted as safe usage
    if (!myList.isEmpty()) {
      myList.remove("Hello");
      if (!myList.isEmpty()) {
        System.out.println(myList.get(1));
      }
    }

    // Scenario 14: Should be counted as unsafe usage
    if (!myList.isEmpty()) {
      // assign NameList to myList
      myList = NameList;
      System.out.println(myList.get(1));
    }

    // Scenario 15: Should be counted as safe usage
    if (!myList.isEmpty()) {
      // assign NameList to myList
      myList = NameList;
      if (!myList.isEmpty()) {
        System.out.println(myList.get(1));
      }
    }
  }
}

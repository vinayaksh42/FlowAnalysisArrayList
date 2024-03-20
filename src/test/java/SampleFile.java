import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class SampleFile {
  public SampleFile() {
  }

  public static void main(String[] var0) {
    List<String> myList = new ArrayList<>();
    List<String> ListNumber2 = new ArrayList<>();

    // Scenario 1: unsafe
    System.out.println(myList.get(0));

    // Scenario 2:
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
    if (!ListNumber2.isEmpty()) {
      System.out.println(ListNumber2.get(0));
    }

    // Scenario 6: unsafe
    System.out.println(ListNumber2.get(1));

    // Scenario 7: unsafe
    Iterator<String> iterator = myList.iterator();
    while (iterator.hasNext()) {
      String next = iterator.next();
      System.out.println(next);
    }

    // Scenario 8: safe
    if (!myList.isEmpty()) {
      Iterator<String> iterator1 = myList.iterator();
      while (iterator1.hasNext()) {
        String next = iterator1.next();
        System.out.println(next);
      }
    }

    // Scenario 9: safe
    myList.clear();
    if (myList.isEmpty()) {
      System.out.println(myList.get(1));
    }

    // Scenario 10: unsafe
    if (!myList.isEmpty()) {
      myList.clear();
      System.out.println(myList.get(1));
    }

    // Scenario 11: unsafe
    if (!myList.isEmpty()) {
      myList.remove("Hello");
      System.out.println(myList.get(1));
    }

    // Scenario 12: safe
    if (!myList.isEmpty()) {
      myList.remove("Hello");
      if (!myList.isEmpty()) {
        System.out.println(myList.get(1));
      }
    }

    // Scenario 13: unsafe
    System.out.println(myList.get(1));

    // Scenario 14: safe
    myList.isEmpty();
    System.out.println(myList.get(1));
  }
}

package vinayak.flowanalysis.app.Runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

import vinayak.flowanalysis.app.ArrayListAnalysis;

import vinayak.flowanalysis.app.base.Setup;
import org.junit.Test;

public class ListAnalysisTest extends Setup {

  @Before
  public void setUp() {
    executeArrayListAnalysis();
  }

  @Test
  public void testArrayListUsage() {
    assertEquals(8, ArrayListAnalysis.getArrayUnsafeUsageCount());
    assertEquals(7, ArrayListAnalysis.getArraySafeUsageCount());
  }

  // make a test if variableMap from ArrayListAnalysis has myList, NameList and
  // myList2
  @Test
  public void testVariableMap() {
    assertTrue("variableMap should contain myList",
        ArrayListAnalysis.getVariableMap().keySet().stream().anyMatch(value -> value.toString().equals("myList")));
    assertTrue("variableMap should contain NameList",
        ArrayListAnalysis.getVariableMap().keySet().stream().anyMatch(value -> value.toString().equals("NameList")));
    assertTrue("variableMap should contain myList2",
        ArrayListAnalysis.getVariableMap().keySet().stream().anyMatch(value -> value.toString().equals("item")));
  }

  @Test
  public void testNumberOfVariables() {
    // checking if both tempName and variableMap have same size
    assertEquals(ArrayListAnalysis.getVariableMap().size(),
        ArrayListAnalysis.getTempNames().size());
  }
}

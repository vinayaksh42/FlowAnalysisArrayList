package vinayak.flowanalysis.app.Runner;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import vinayak.flowanalysis.app.ArrayListAnalysis;
import vinayak.flowanalysis.app.base.Setup;

public class ListAnalysisTest extends Setup {

  @Before
  public void setUp() {
    executeArrayListAnalysis();
  }

  @Test
  public void testArrayListUsage() {
    assertEquals(6, ArrayListAnalysis.getArrayUnsafeUsageCount());
    assertEquals(6, ArrayListAnalysis.getArraySafeUsageCount());
  }
}

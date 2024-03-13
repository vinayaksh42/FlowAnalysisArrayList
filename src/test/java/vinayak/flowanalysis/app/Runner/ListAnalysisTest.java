package vinayak.flowanalysis.app.Runner;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import vinayak.flowanalysis.app.ArrayListAnalysis;
import vinayak.flowanalysis.app.base.Setup;
import vinayak.flowanalysis.app.ArrayAnalysisFact;

import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;

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
}

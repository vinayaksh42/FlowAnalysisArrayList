package vinayak.flowanalysis.app.Runner;

import static org.junit.Assert.assertEquals;

import vinayak.flowanalysis.app.VulnerabilityReporter;

import vinayak.flowanalysis.app.base.Setup;
import org.junit.Test;

public class Runner1 extends Setup {

  @Test
  public void testArrayListUsage() {
    reporter = new VulnerabilityReporter();
    executeArrayListAnalysis();
    assertEquals(4, reporter.getArrayUnsafeUsageCount());
    assertEquals(5, reporter.getArraySafeUsageCount());
  }
}

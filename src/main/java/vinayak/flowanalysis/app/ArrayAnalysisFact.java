package vinayak.flowanalysis.app;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import sootup.core.jimple.basic.Value;

public class ArrayAnalysisFact {

  public enum ArrayAnalysis {
    Safe, Unsafe, ArrayDeclaration
  }

  private ArrayAnalysis state;

  public ArrayAnalysisFact(@Nonnull ArrayAnalysis state) {
    this.state = state;
  }

  public boolean isUnsafe() {
    return state == ArrayAnalysis.Unsafe;
  }

  @Nonnull
  public ArrayAnalysis getState() {
    return this.state;
  }
}

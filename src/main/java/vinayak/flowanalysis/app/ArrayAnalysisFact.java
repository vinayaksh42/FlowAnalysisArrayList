package vinayak.flowanalysis.app;

import javax.annotation.Nonnull;
import sootup.core.jimple.basic.Value;

public class ArrayAnalysisFact {

  public enum ArrayAnalysis {
    Safe, Unsafe, ArrayDeclaration
  }

  private ArrayAnalysis state;
  private Value variable;

  public ArrayAnalysisFact(@Nonnull ArrayAnalysis state) {
    this.state = state;
    this.variable = null; // Indicates a general state
  }

  public ArrayAnalysisFact(@Nonnull ArrayAnalysis state, @Nonnull Value variable) {
    this.state = state;
    this.variable = variable;
  }

  @Nonnull
  public ArrayAnalysis getState() {
    return this.state;
  }

  @Nonnull
  public Value getVariable() {
    return this.variable;
  }
}

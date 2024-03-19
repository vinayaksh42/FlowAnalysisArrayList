package vinayak.flowanalysis.app;

import javax.annotation.Nonnull;

public class ArrayAnalysisFact {

  public enum ArrayAnalysis {
    Safe, Unsafe
  }

  public ArrayAnalysis state;

  public ArrayAnalysisFact(@Nonnull ArrayAnalysis state) {
    this.state = state;
  }

  @Nonnull
  public ArrayAnalysis getState() {
    return this.state;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((state == null) ? 0 : state.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ArrayAnalysisFact other = (ArrayAnalysisFact) obj;
    return state.equals(other.state);
  }
}

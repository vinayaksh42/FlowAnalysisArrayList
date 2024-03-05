package vinayak.flowanalysis.app;

import javax.annotation.Nonnull;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.SootMethod;
import sootup.java.core.JavaSootMethod;

public abstract class AbstractAnalysis {

  @Nonnull
  protected SootMethod method;

  protected AbstractAnalysis(@Nonnull JavaSootMethod method) {
    this.method = method;
  }

  protected abstract void flowThrough(Stmt stmt);

  public void execute() {
    for (Stmt stmt : method.getBody().getStmts()) {
      flowThrough(stmt);
    }
  }

  protected void prettyPrint(@Nonnull Stmt stmt) {
    String s = String.format("\t%10s%s\n", "Stmt: ", stmt);
    System.out.println(s);
  }
}

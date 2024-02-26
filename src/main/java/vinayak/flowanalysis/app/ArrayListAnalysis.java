package vinayak.flowanalysis.app;

import javax.annotation.Nonnull;
import java.util.List;

import sootup.core.jimple.common.stmt.Stmt;
import sootup.java.core.JavaSootMethod;
import sootup.core.jimple.common.stmt.*;
import sootup.core.jimple.common.expr.JInterfaceInvokeExpr;
import sootup.core.jimple.common.expr.JNewExpr;
import sootup.core.jimple.basic.Value;

public class ArrayListAnalysis extends AbstractAnalysis {
  public ArrayListAnalysis(@Nonnull JavaSootMethod method, @Nonnull VulnerabilityReporter reporter) {
    super(method, reporter);
  }

  @Override
  protected void flowThrough(@Nonnull Stmt stmt) {
    prettyPrint(stmt);
    if (stmt instanceof JAssignStmt) {
      // logic for storing ArrayList temporary stack variable names:
      AbstractDefinitionStmt defstmt = (AbstractDefinitionStmt) stmt;
      if (defstmt.getRightOp() instanceof JNewExpr) {
        JNewExpr newExpr = (JNewExpr) defstmt.getRightOp();
        String className = newExpr.getType().getClassName();

        if (className.equals(reporter.getArrayListClasString())) {
          reporter.storingTempNames(defstmt.getLeftOp());
        }
      }

      // logic for storing ArrayList variable names:
      for (Value temp : reporter.getTempNames()) {
        if (defstmt.getRightOp().equals(temp)) {
          reporter.storingVariableMap(defstmt.getLeftOp(), false);
        }
      }

      if (defstmt.getRightOp() instanceof JInterfaceInvokeExpr) {
        JInterfaceInvokeExpr invokeStmt = (JInterfaceInvokeExpr) defstmt.getRightOp();
        String methodName = invokeStmt.getMethodSignature().getName();

        switch (methodName) {
          case "isEmpty":
            List<Value> InvokeExprUsesIsEmpty = invokeStmt.getUses();
            for (Value use : InvokeExprUsesIsEmpty) {
              if (reporter.getVariableMap().containsKey(use)) {
                reporter.storingVariableMap(use, true);
              }
            }
            break;
          case "get":
            List<Value> InvokeExprUsesGet = invokeStmt.getUses();
            for (Value use : InvokeExprUsesGet) {
              if (reporter.getVariableMap().containsKey(use)) {
                if (!reporter.getVariableMap().get(use)) {
                  reporter.updateArrayUsageCount(true);
                } else {
                  reporter.updateArrayUsageCount(false);
                  reporter.storingVariableMap(use, false);
                }
              }
            }
            break;
          case "iterator":
            List<Value> InvokeExprUsesIterator = invokeStmt.getUses();
            for (Value use : InvokeExprUsesIterator) {
              if (reporter.getVariableMap().containsKey(use)) {
                if (!reporter.getVariableMap().get(use)) {
                  reporter.updateArrayUsageCount(true);
                } else {
                  reporter.updateArrayUsageCount(false);
                  reporter.storingVariableMap(use, false);
                }
              }
            }
            break;
          default:
            // Add other cases as needed.
        }
      }
    }
  }

}

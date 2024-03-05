package vinayak.flowanalysis.app;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import sootup.core.jimple.common.stmt.Stmt;
import sootup.java.core.JavaSootMethod;
import sootup.core.jimple.common.stmt.*;
import sootup.core.jimple.common.expr.JInterfaceInvokeExpr;
import sootup.core.jimple.common.expr.JNewExpr;
import sootup.core.jimple.basic.Value;

public class ArrayListAnalysis extends AbstractAnalysis {

  public static List<Value> TempNames;
  public static Map<Value, Boolean> variableMap;
  public static int arrayUnsafeUsageCount;
  public static int arraySafeUsageCount;

  public ArrayListAnalysis(@Nonnull JavaSootMethod method) {
    super(method);
    TempNames = new ArrayList<>();
    variableMap = new HashMap<>();
  }

  @Override
  protected void flowThrough(Stmt stmt) {
    System.out.println(stmt);
    if (stmt instanceof JInvokeStmt) {
      JInvokeStmt invokeStmt = (JInvokeStmt) stmt;
      if (invokeStmt.getInvokeExpr() instanceof JInterfaceInvokeExpr) {
        JInterfaceInvokeExpr interfaceInvokeExpr = (JInterfaceInvokeExpr) invokeStmt.getInvokeExpr();
        String methodName = interfaceInvokeExpr.getMethodSignature().getName();
        System.out.println(methodName);
        if (methodName.equals("clear")) {
          List<Value> InvokeExprUsesclear = invokeStmt.getUses();
          for (Value use : InvokeExprUsesclear) {
            if (this.getVariableMap().containsKey(use)) {
              this.storingVariableMap(use, false);
            }
          }
        }
        if (methodName.equals("remove")) {
          List<Value> InvokeExprUsesremove = invokeStmt.getUses();
          for (Value use : InvokeExprUsesremove) {
            if (this.getVariableMap().containsKey(use)) {
              this.storingVariableMap(use, false);
            }
          }
        }
      }
    }
    if (stmt instanceof JAssignStmt) {
      AbstractDefinitionStmt defstmt = (AbstractDefinitionStmt) stmt;
      if (defstmt.getRightOp() instanceof JNewExpr) {
        JNewExpr newExpr = (JNewExpr) defstmt.getRightOp();
        String className = newExpr.getType().getClassName();

        if (className.equals(this.getArrayListClasString())) {
          this.storingTempNames(defstmt.getLeftOp());
        }
      }

      for (Value temp : this.getTempNames()) {
        if (defstmt.getRightOp().equals(temp)) {
          this.storingVariableMap(defstmt.getLeftOp(), false);
        }
      }

      if (defstmt.getRightOp() instanceof JInterfaceInvokeExpr) {
        JInterfaceInvokeExpr invokeStmt = (JInterfaceInvokeExpr) defstmt.getRightOp();
        String methodName = invokeStmt.getMethodSignature().getName();

        switch (methodName) {
          case "isEmpty":
            List<Value> InvokeExprUsesIsEmpty = invokeStmt.getUses();
            for (Value use : InvokeExprUsesIsEmpty) {
              if (this.getVariableMap().containsKey(use)) {
                this.storingVariableMap(use, true);
              }
            }
            break;
          case "get":
            List<Value> InvokeExprUsesGet = invokeStmt.getUses();
            for (Value use : InvokeExprUsesGet) {
              if (this.getVariableMap().containsKey(use)) {
                if (!this.getVariableMap().get(use)) {
                  this.updateArrayUsageCount(true);
                } else {
                  this.updateArrayUsageCount(false);
                  this.storingVariableMap(use, false);
                }
              }
            }
            break;
          case "iterator":
            List<Value> InvokeExprUsesIterator = invokeStmt.getUses();
            for (Value use : InvokeExprUsesIterator) {
              if (this.getVariableMap().containsKey(use)) {
                if (!this.getVariableMap().get(use)) {
                  this.updateArrayUsageCount(true);
                } else {
                  this.updateArrayUsageCount(false);
                  this.storingVariableMap(use, false);
                }
              }
            }
            break;
          default:
            break;
        }
      }
    }
  }

  public void storingTempNames(Value value) {
    TempNames.add(value);
  }

  public void storingVariableMap(Value value, Boolean isUnsafe) {
    variableMap.put(value, isUnsafe);
  }

  public void updateArrayUsageCount(boolean isUnsafe) {
    if (isUnsafe) {
      arrayUnsafeUsageCount++;
    } else {
      arraySafeUsageCount++;
    }
  }

  public String getArrayListClasString() {
    return "ArrayList";
  }

  public static List<Value> getTempNames() {
    return TempNames;
  }

  public static Map<Value, Boolean> getVariableMap() {
    return variableMap;
  }

  public static int getArrayUnsafeUsageCount() {
    return arrayUnsafeUsageCount;
  }

  public static int getArraySafeUsageCount() {
    return arraySafeUsageCount;
  }

}

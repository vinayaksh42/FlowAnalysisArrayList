package vinayak.flowanalysis.app;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import vinayak.flowanalysis.app.ArrayAnalysisFact.ArrayAnalysis;

import sootup.core.jimple.common.stmt.Stmt;
import sootup.java.core.JavaSootMethod;
import sootup.core.jimple.common.stmt.*;
import sootup.core.jimple.common.expr.JInterfaceInvokeExpr;
import sootup.core.jimple.common.expr.JNewExpr;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;

public class ArrayListAnalysis extends ForwardAnalysis<Set<ArrayAnalysisFact>> {

  // change from static to non-static
  private final Map<Value, Boolean> variableMap;
  static int arrayUnsafeUsageCount;
  static int arraySafeUsageCount;

  public ArrayListAnalysis(@Nonnull JavaSootMethod method) {
    super(method);
    variableMap = new HashMap<>();
  }

  @Override
  protected void flowThrough(@Nonnull Set<ArrayAnalysisFact> in, @Nonnull Stmt stmt,
      @Nonnull Set<ArrayAnalysisFact> out) {
    if (stmt.containsInvokeExpr()) {
      AbstractInvokeExpr invokeStmt = stmt.getInvokeExpr();
      if (invokeStmt instanceof JInterfaceInvokeExpr) {
        JInterfaceInvokeExpr interfaceInvokeExpr = (JInterfaceInvokeExpr) invokeStmt;
        String methodName = invokeStmt.getMethodSignature().getName();
        if (methodName.equals(ConstantValue.REMOVE) || methodName.equals(ConstantValue.CLEAR)) {
          Value base = interfaceInvokeExpr.getBase();
          if (this.getVariableMap().containsKey(base)) {
            this.storingVariableMap(base, false);
            updateStateVariable(out, ArrayAnalysis.Unsafe, base);
            updateState(out, ArrayAnalysis.Unsafe);
          }
        }
        if (methodName.equals(ConstantValue.ISEMPTY)) {
          Value baseIsEmpty = interfaceInvokeExpr.getBase();
          if (this.getVariableMap().containsKey(baseIsEmpty)) {
            this.storingVariableMap(baseIsEmpty, true);
            updateStateVariable(out, ArrayAnalysis.Safe, baseIsEmpty);
            updateState(out, ArrayAnalysis.Safe);
          }
        }
        if (methodName.equals(ConstantValue.ITERATOR) || methodName.equals(ConstantValue.GET)) {
          Value base = interfaceInvokeExpr.getBase();
          if (this.getVariableMap().containsKey(base)) {
            if (!this.getVariableMap().get(base)) {
              this.updateArrayUsageCount(true);
              updateState(out, ArrayAnalysis.Unsafe);
            } else {
              this.updateArrayUsageCount(false);
              this.storingVariableMap(base, false);
              updateStateVariable(out, ArrayAnalysis.Unsafe, base);
              updateState(out, ArrayAnalysis.Safe);
            }
          }
        }
      }
    }

    // Tracking declration of ArrayList Vairable
    if (stmt instanceof JAssignStmt) {
      JAssignStmt defstmt = (JAssignStmt) stmt;
      if (defstmt.getRightOp() instanceof JNewExpr) {
        JNewExpr newExpr = (JNewExpr) defstmt.getRightOp();
        String className = newExpr.getType().getClassName();

        if (className.equals(ConstantValue.ARRAYLIST_CLASS_STRING)) {
          updateState(out, ArrayAnalysis.ArrayDeclaration);
        }
      }

      if (this.getVariableMap().containsKey(defstmt.getLeftOp())) {
        this.storingVariableMap(defstmt.getLeftOp(), false);
        updateStateVariable(out, ArrayAnalysis.Unsafe, defstmt.getLeftOp());
        updateState(out, ArrayAnalysis.Unsafe);
      }

      for (ArrayAnalysisFact fact : in) {
        if (fact.getState() == ArrayAnalysis.ArrayDeclaration) {
          in.remove(fact);
          this.storingVariableMap(defstmt.getLeftOp(), false);
          updateStateVariable(out, ArrayAnalysis.Unsafe, defstmt.getLeftOp());
        }
      }
    }

    for (ArrayAnalysisFact fact : in) {
      if (fact.getState() == ArrayAnalysis.ArrayDeclaration) {
        copy(in, out);
      }
    }
    prettyPrint(in, stmt, out);
  }

  @Nonnull
  @Override
  protected Set<ArrayAnalysisFact> newInitialFlow() {
    return new HashSet<>();
  }

  @Override
  protected void copy(@Nonnull Set<ArrayAnalysisFact> source, @Nonnull Set<ArrayAnalysisFact> dest) {
    for (ArrayAnalysisFact fact : source) {
      dest.add(fact);
    }
  }

  @Override
  protected void merge(@Nonnull Set<ArrayAnalysisFact> in1, @Nonnull Set<ArrayAnalysisFact> in2,
      @Nonnull Set<ArrayAnalysisFact> out) {
    out.clear();
    out.addAll(in1);
    out.addAll(in2);
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

  public Map<Value, Boolean> getVariableMap() {
    return variableMap;
  }

  public static int getArrayUnsafeUsageCount() {
    return arrayUnsafeUsageCount;
  }

  public static int getArraySafeUsageCount() {
    return arraySafeUsageCount;
  }

  private void updateStateVariable(Set<ArrayAnalysisFact> facts, ArrayAnalysisFact.ArrayAnalysis newState,
      Value variable) {
    ArrayAnalysisFact newFact = new ArrayAnalysisFact(newState, variable);
    facts.add(newFact);
  }

  private void updateState(Set<ArrayAnalysisFact> facts, ArrayAnalysisFact.ArrayAnalysis newState) {
    ArrayAnalysisFact newFact = new ArrayAnalysisFact(newState);
    facts.add(newFact);
  }
}

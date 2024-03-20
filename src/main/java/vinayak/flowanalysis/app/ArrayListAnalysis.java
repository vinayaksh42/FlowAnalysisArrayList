package vinayak.flowanalysis.app;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

import vinayak.flowanalysis.app.ArrayAnalysisFact.ArrayAnalysis;

import sootup.core.jimple.common.stmt.Stmt;
import sootup.java.core.JavaSootMethod;
import sootup.core.jimple.common.expr.JInterfaceInvokeExpr;
import sootup.core.jimple.basic.Value;
import sootup.analysis.intraprocedural.ForwardFlowAnalysis;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.types.ClassType;

public class ArrayListAnalysis extends ForwardFlowAnalysis<Map<Value, ArrayAnalysisFact>> {

  @Nonnull
  protected final JavaSootMethod method;

  public ArrayListAnalysis(@Nonnull JavaSootMethod method) {
    super(method.getBody().getStmtGraph());
    this.method = method;
  }

  public Map<Stmt, Map<Value, ArrayAnalysisFact>> getStmtToAfterFlow() {
    return this.stmtToAfterFlow;
  }

  @Override
  protected void flowThrough(@Nonnull Map<Value, ArrayAnalysisFact> in, @Nonnull Stmt stmt,
      @Nonnull Map<Value, ArrayAnalysisFact> out) {
    copy(in, out);
    if (stmt.containsInvokeExpr()) {
      AbstractInvokeExpr invokeStmt = stmt.getInvokeExpr();
      if (invokeStmt instanceof JInterfaceInvokeExpr) {
        JInterfaceInvokeExpr interfaceInvokeExpr = (JInterfaceInvokeExpr) invokeStmt;
        String methodName = invokeStmt.getMethodSignature().getName();
        ClassType baseClass = (interfaceInvokeExpr.getMethodSignature().getDeclClassType());
        Value base = interfaceInvokeExpr.getBase();
        if (baseClass.getClassName().equals(ConstantValue.LIST_CLASS_NAME)) {
          if (methodName.equals(ConstantValue.REMOVE) || methodName.equals(ConstantValue.CLEAR)) {
            updateState(out, ArrayAnalysis.Unsafe, base);
          }
          if (methodName.equals(ConstantValue.ISEMPTY)) {
            updateState(out, ArrayAnalysis.Safe, base);
          }
          if (methodName.equals(ConstantValue.ITERATOR) || methodName.equals(ConstantValue.GET)) {
            if (findStateByLocalName(in, base) == ArrayAnalysis.Unsafe || findStateByLocalName(in, base) == null) {
              updateState(out, ArrayAnalysis.Unsafe, base);
            }
          }
        }
      }
    }
  }

  public ArrayAnalysisFact.ArrayAnalysis findStateByLocalName(Map<Value, ArrayAnalysisFact> facts, Value localName) {
    ArrayAnalysisFact fact = facts.get(localName);
    return fact != null ? fact.getState() : null;
  }

  @Nonnull
  @Override
  protected Map<Value, ArrayAnalysisFact> newInitialFlow() {
    return new HashMap<>();
  }

  @Override
  protected void copy(@Nonnull Map<Value, ArrayAnalysisFact> source, @Nonnull Map<Value, ArrayAnalysisFact> dest) {
    dest.clear();
    dest.putAll(source);
  }

  @Override
  protected void merge(@Nonnull Map<Value, ArrayAnalysisFact> in1, @Nonnull Map<Value, ArrayAnalysisFact> in2,
      @Nonnull Map<Value, ArrayAnalysisFact> out) {
    out.clear();
    for (Value key : in1.keySet()) {
      out.put(key, new ArrayAnalysisFact(ArrayAnalysisFact.ArrayAnalysis.Unsafe));
    }
    for (Value key : in2.keySet()) {
      out.put(key, new ArrayAnalysisFact(ArrayAnalysisFact.ArrayAnalysis.Unsafe));
    }
  }

  private void updateState(Map<Value, ArrayAnalysisFact> facts, ArrayAnalysisFact.ArrayAnalysis newState,
      Value variable) {
    facts.put(variable, new ArrayAnalysisFact(newState));
  }

  @Override
  public void execute() {
    super.execute();
  }
}

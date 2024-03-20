package vinayak.flowanalysis.app.Runner;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import vinayak.flowanalysis.app.base.Setup;
import vinayak.flowanalysis.app.ArrayAnalysisFact;
import vinayak.flowanalysis.app.ConstantValue;

import sootup.core.model.SootMethod;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.expr.JInterfaceInvokeExpr;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;

public class ListAnalysisTest extends Setup {

  public Map<SootMethod, Map<Stmt, Map<Value, ArrayAnalysisFact>>> outFacts;

  public ArrayAnalysisFact.ArrayAnalysis findStateByLocalName(Map<Value, ArrayAnalysisFact> facts, Value localName) {
    ArrayAnalysisFact fact = facts.get(localName);
    return fact != null ? fact.getState() : null;
  }

  @Before
  public void Setup() {
    outFacts = executeArrayListAnalysis();
  }

  @Test
  public void ArrayListAnalysis_IsEmpty_SafeFact() {
    for (SootMethod method : outFacts.keySet()) {
      Map<Stmt, Map<Value, ArrayAnalysisFact>> result = outFacts.get(method);
      for (Stmt stmt : result.keySet()) {
        if (stmt.containsInvokeExpr()) {
          AbstractInvokeExpr invokeStmt = stmt.getInvokeExpr();
          if (invokeStmt instanceof JInterfaceInvokeExpr) {
            JInterfaceInvokeExpr interfaceInvokeExpr = (JInterfaceInvokeExpr) invokeStmt;
            Value base = interfaceInvokeExpr.getBase();
            if (invokeStmt.getMethodSignature().getName().equals(ConstantValue.ISEMPTY)) {
              assertEquals(ArrayAnalysisFact.ArrayAnalysis.Safe, findStateByLocalName(result.get(stmt), base));
            }
          }
        }
      }
    }
  }

  @Test
  public void ArrayListAnalysis_RemoveAndClear_UnsafeFact() {
    for (SootMethod method : outFacts.keySet()) {
      Map<Stmt, Map<Value, ArrayAnalysisFact>> result = outFacts.get(method);
      for (Stmt stmt : result.keySet()) {
        if (stmt.containsInvokeExpr()) {
          AbstractInvokeExpr invokeStmt = stmt.getInvokeExpr();
          if (invokeStmt instanceof JInterfaceInvokeExpr) {
            JInterfaceInvokeExpr interfaceInvokeExpr = (JInterfaceInvokeExpr) invokeStmt;
            Value base = interfaceInvokeExpr.getBase();
            if (invokeStmt.getMethodSignature().getName().equals(ConstantValue.REMOVE)
                || invokeStmt.getMethodSignature().getName().equals(ConstantValue.CLEAR)) {
              assertEquals(ArrayAnalysisFact.ArrayAnalysis.Unsafe, findStateByLocalName(result.get(stmt), base));
            }
          }
        }
      }
    }
  }

  @Test
  public void ArrayListAnalysis_GetAndIterator_SafeOrUnsafeFact() {
    for (SootMethod method : outFacts.keySet()) {
      Map<Stmt, Map<Value, ArrayAnalysisFact>> result = outFacts.get(method);
      for (Stmt stmt : result.keySet()) {
        if (stmt.containsInvokeExpr()) {
          AbstractInvokeExpr invokeStmt = stmt.getInvokeExpr();
          if (invokeStmt instanceof JInterfaceInvokeExpr) {
            JInterfaceInvokeExpr interfaceInvokeExpr = (JInterfaceInvokeExpr) invokeStmt;
            Value base = interfaceInvokeExpr.getBase();
            if (invokeStmt.getMethodSignature().getName().equals(ConstantValue.ITERATOR)
                || invokeStmt.getMethodSignature().getName().equals(ConstantValue.GET)) {
              if (findStateByLocalName(result.get(stmt), base) == ArrayAnalysisFact.ArrayAnalysis.Unsafe
                  || findStateByLocalName(result.get(stmt), base) == null) {
                assertEquals(ArrayAnalysisFact.ArrayAnalysis.Unsafe, findStateByLocalName(result.get(stmt), base));
              } else if (findStateByLocalName(result.get(stmt), base) == ArrayAnalysisFact.ArrayAnalysis.Safe) {
                assertEquals(ArrayAnalysisFact.ArrayAnalysis.Safe, findStateByLocalName(result.get(stmt), base));
              }
            }
          }
        }
      }
    }
  }

  @Test
  public void ArrayListAnalysis_flowThrough_SafeOrUnsafeFact() {
    for (SootMethod method : outFacts.keySet()) {
      Map<Stmt, Map<Value, ArrayAnalysisFact>> result = outFacts.get(method);
      Map<Value, ArrayAnalysisFact> lastValue = result.get(result.keySet().toArray()[result.size() - 1]);
      for (Value localName : lastValue.keySet()) {
        if (localName.toString().equals("ListNumber2")) {
          assertEquals(ArrayAnalysisFact.ArrayAnalysis.Unsafe, findStateByLocalName(lastValue, localName));
        } else if (localName.toString().equals("myList")) {
          assertEquals(ArrayAnalysisFact.ArrayAnalysis.Safe, findStateByLocalName(lastValue, localName));
        }
      }
    }
  }
}

package rlpark.plugin.rltoys.experiments.testing.predictions;

import rlpark.plugin.rltoys.algorithms.predictions.td.OnPolicyTD;
import rlpark.plugin.rltoys.experiments.testing.results.TestingResult;
import rlpark.plugin.rltoys.math.vector.RealVector;
import rlpark.plugin.rltoys.math.vector.implementations.PVector;
import rlpark.plugin.rltoys.math.vector.implementations.Vectors;
import rlpark.plugin.rltoys.problems.stategraph.FSGAgentState;
import rlpark.plugin.rltoys.problems.stategraph.FiniteStateGraph;
import rlpark.plugin.rltoys.problems.stategraph.FiniteStateGraph.StepData;

public class FiniteStateGraphOnPolicy {

  static public interface OnPolicyTDFactory {
    OnPolicyTD create(int nbFeatures);
  }

  static public double distanceToSolution(double[] solution, PVector theta) {
    double max = 0;
    for (int i = 0; i < Math.max(solution.length, theta.size); i++)
      max = Math.max(max, Math.abs(solution[i] - theta.data[i]));
    return max;
  }

  public static TestingResult<OnPolicyTD> testTD(FiniteStateGraph problem,
      FiniteStateGraphOnPolicy.OnPolicyTDFactory tdFactory) {
    FSGAgentState agentState = new FSGAgentState(problem);
    OnPolicyTD td = tdFactory.create(agentState.size);
    int nbEpisode = 0;
    double[] solution = problem.expectedDiscountedSolution();
    RealVector x_t = null;
    while (distanceToSolution(solution, td.weights()) > 0.05) {
      StepData stepData = agentState.step();
      RealVector x_tp1 = agentState.currentFeatureState();
      td.update(x_t, x_tp1, stepData.r_tp1);
      if (stepData.s_tp1 == null) {
        nbEpisode += 1;
        if (nbEpisode >= 100000)
          return new TestingResult<OnPolicyTD>(false, "Not learning", td);
      }
      x_t = x_tp1 != null ? x_tp1.copy() : null;
    }
    if (nbEpisode <= 2)
      return new TestingResult<OnPolicyTD>(false, "That was too quick!", td);
    if (!Vectors.checkValues(td.weights()))
      return new TestingResult<OnPolicyTD>(false, "Weights are incorrect!", td);
    return new TestingResult<OnPolicyTD>(true, null, td);
  }
}

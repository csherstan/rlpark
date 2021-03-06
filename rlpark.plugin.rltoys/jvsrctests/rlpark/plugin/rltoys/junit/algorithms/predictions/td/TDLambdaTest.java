package rlpark.plugin.rltoys.junit.algorithms.predictions.td;

import rlpark.plugin.rltoys.algorithms.predictions.td.OnPolicyTD;
import rlpark.plugin.rltoys.algorithms.predictions.td.TDLambda;
import rlpark.plugin.rltoys.experiments.testing.predictions.OnPolicyTests;

public class TDLambdaTest extends OnPolicyTests {

  @Override
  protected OnPolicyTD newOnPolicyTD(double lambda, double gamma, double vectorNorm, int vectorSize) {
    return new TDLambda(lambda, gamma, 0.01 / vectorNorm, vectorSize);
  }

  @Override
  protected double[] lambdaValues() {
    return new double[] { .5, 1.0 };
  }

  @Override
  protected double precision() {
    return 0.01;
  }
}

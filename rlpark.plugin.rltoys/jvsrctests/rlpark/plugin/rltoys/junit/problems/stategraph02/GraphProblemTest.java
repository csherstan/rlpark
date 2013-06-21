package rlpark.plugin.rltoys.junit.problems.stategraph02;

import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;

import rlpark.plugin.rltoys.envio.actions.Action;
import rlpark.plugin.rltoys.envio.policy.Policies;
import rlpark.plugin.rltoys.envio.policy.SingleActionPolicy;
import rlpark.plugin.rltoys.envio.rl.TRStep;
import rlpark.plugin.rltoys.experiments.helpers.Runner;
import rlpark.plugin.rltoys.experiments.helpers.Runner.RunnerEvent;
import rlpark.plugin.rltoys.problems.stategraph02.GraphProblem;
import rlpark.plugin.rltoys.problems.stategraph02.LineProblem;
import rlpark.plugin.rltoys.problems.stategraph02.State;
import rlpark.plugin.rltoys.problems.stategraph02.StateGraph;
import zephyr.plugin.core.api.signals.Listener;

public class GraphProblemTest {
  static private final State A = new State("A", 0.0);
  static private final State B = new State("B", 0.0);
  static private final State C = new State("C", 0.0);
  static private final State D = new State("D", 0.0);
  @SuppressWarnings("serial")
  static private Action Move = new Action() {
  };
  static private final SingleActionPolicy acting = new SingleActionPolicy(Move);
  static private final double atob = .12;
  static private final double atoc = .35;
  static private final double atod = 1.0 - atob - atoc;

  static private GraphProblem create(Random random) {
    StateGraph stateGraph = new StateGraph(A, new State[] { A, B, C, D }, new Action[] { Move });
    stateGraph.addTransition(A, Move, B, atob);
    stateGraph.addTransition(A, Move, C, atoc);
    stateGraph.addTransition(A, Move, D, atod);
    return new GraphProblem(random, A, stateGraph);
  }

  @Test
  public void testProbabilityDistribution() {
    GraphProblem problem = create(new Random(0));
    Runner runner = new Runner(problem, Policies.toAgent(acting), 10000, -1);
    final int[] count = new int[problem.stateGraph().nbStates()];
    runner.onTimeStep.connect(new Listener<Runner.RunnerEvent>() {
      @Override
      public void listen(RunnerEvent eventInfo) {
        TRStep step = eventInfo.step;
        if (!step.isEpisodeEnding())
          return;
        count[((int) step.o_tp1[0]) - 1]++;
      }
    });
    runner.run();
    int total = 0;
    for (int i : count)
      total += i;
    Assert.assertEquals(atob, count[0] / (double) total, 0.01);
    Assert.assertEquals(atoc, count[1] / (double) total, 0.01);
    Assert.assertEquals(atod, count[2] / (double) total, 0.01);
  }

  @Test
  public void lineProblemTest() {
    GraphProblem problem = LineProblem.create(new Random(0));
    for (int e = 0; e < 3; e++) {
      Assert.assertEquals(0.0, problem.initialize().o_tp1[0], 0.0);
      for (int i = 0; i < problem.stateGraph().nbStates() - 2; i++) {
        problem.step(LineProblem.Move);
        Assert.assertEquals(i + 1, problem.lastStep().o_tp1[0], 0.0);
        Assert.assertEquals(0, problem.lastStep().r_tp1, 0.0);
        Assert.assertFalse(problem.lastStep().isEpisodeEnding());
      }
      problem.step(LineProblem.Move);
      Assert.assertEquals(problem.stateGraph().nbStates() - 1, problem.lastStep().o_tp1[0], 0.0);
      Assert.assertEquals(LineProblem.Reward, problem.lastStep().r_tp1, 0);
      Assert.assertTrue(problem.lastStep().isEpisodeEnding());
    }
  }
}

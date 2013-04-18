package rlpark.plugin.rltoys.algorithms.representations.discretizer.partitions;

import rlpark.plugin.rltoys.algorithms.representations.discretizer.Discretizer;
import rlpark.plugin.rltoys.math.ranges.Range;

public class WrappedPartitionFactory extends AbstractPartitionFactory {
  private static final long serialVersionUID = -5578336702743121475L;

  class WrappedPartition extends AbstractPartition {
    private static final long serialVersionUID = -1445471984953765916L;

    public WrappedPartition(double min, double max, int resolution) {
      super(min, max, resolution);
    }

    @Override
    public int discretize(double input) {
      double n = (input - min) / intervalWidth;
      if (n < 0)
        n += ((int) (-n / resolution) + 1) * resolution;
      return (int) (n % resolution);
    }
  }

  public WrappedPartitionFactory(double min, double max, int inputSize) {
    super(min, max, inputSize);
  }

  public WrappedPartitionFactory(Range... ranges) {
    super(ranges);
  }

  @Override
  public Discretizer createDiscretizer(int inputIndex, int resolution, int tilingIndex, int nbTilings) {
    Range range = ranges[inputIndex];
    double offset = range.length() / resolution / nbTilings;
    double shift = computeShift(offset, tilingIndex, inputIndex);
    double min = range.min() + shift;
    double max = range.max() + shift;
    return new WrappedPartition(min, max, resolution);
  }
}

package rltoys.experiments;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import rltoys.experiments.parametersweep.tests.OnPolicySweepTest;
import rltoys.experiments.parametersweep.tests.ParametersTest;
import rltoys.experiments.parametersweep.tests.SweepTest;
import rltoys.experiments.scheduling.tests.JobPoolTest;
import rltoys.experiments.scheduling.tests.SchedulerNetworkUnreliableTest;
import rltoys.experiments.scheduling.tests.SchedulerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ParametersTest.class, SchedulerTest.class, JobPoolTest.class,
    SchedulerNetworkUnreliableTest.class, SweepTest.class, OnPolicySweepTest.class })
public class Tests {
}
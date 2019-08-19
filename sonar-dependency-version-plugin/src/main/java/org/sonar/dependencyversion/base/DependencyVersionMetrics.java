/*
 * Dependency-Version Plugin for SonarQube
 * Copyright (C) 2019 EDEKA DIGITAL GmbH
 * dpp@edeka.de
 */
package org.sonar.dependencyversion.base;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

public class DependencyVersionMetrics implements Metrics {
  private static final String DOMAIN = "Dependency Version";
  private static final String TOTAL_DEPENDENCIES_KEY = "totalDependencies";
  public static final Metric<Integer> TOTAL_DEPENDENCIES =
      new Metric.Builder(TOTAL_DEPENDENCIES_KEY, "Total Dependencies", Metric.ValueType.INT)
          .setDomain(DependencyVersionMetrics.DOMAIN)
          .setDescription("Total Dependencies")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(Boolean.FALSE)
          .setHidden(false)
          .create();
  private static final String TOTAL_OUTDATED_DEPENDENCIES_KEY = "totalOutdatedDependencies";
  public static final Metric<Integer> TOTAL_OUTDATED_DEPENDENCIES =
      new Metric.Builder(
              TOTAL_OUTDATED_DEPENDENCIES_KEY, "Total Outdated Dependencies", Metric.ValueType.INT)
          .setDomain(DependencyVersionMetrics.DOMAIN)
          .setDescription("Total Outdated Dependencies")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(Boolean.FALSE)
          .setHidden(false)
          .create();

  private static final String NEXT_INCREMENTAL_AVAILABLE_KEY = "nextIncrementalAvailable";
  public static final Metric<Integer> NEXT_INCREMENTAL_AVAILABLE =
      new Metric.Builder(
              NEXT_INCREMENTAL_AVAILABLE_KEY, "Next Incremental Available", Metric.ValueType.INT)
          .setDomain(DependencyVersionMetrics.DOMAIN)
          .setDescription("Next Incremental Available")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(Boolean.FALSE)
          .setHidden(false)
          .create();

  private static final String NEXT_VERSION_AVAILABLE_KEY = "nextVersionAvailable";
  public static final Metric<Integer> NEXT_VERSION_AVAILABLE =
      new Metric.Builder(NEXT_VERSION_AVAILABLE_KEY, "Next Version Available", Metric.ValueType.INT)
          .setDomain(DependencyVersionMetrics.DOMAIN)
          .setDescription("Next Version Available")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(Boolean.FALSE)
          .setHidden(false)
          .create();
  private static final String NEXT_MINOR_AVAILABLE_KEY = "nextMinorAvailable";
  public static final Metric<Integer> NEXT_MINOR_AVAILABLE =
      new Metric.Builder(NEXT_MINOR_AVAILABLE_KEY, "Next Minor Available", Metric.ValueType.INT)
          .setDomain(DependencyVersionMetrics.DOMAIN)
          .setDescription("Next Minor Available")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(Boolean.FALSE)
          .setHidden(false)
          .create();
  private static final String NEXT_MAJOR_AVAILABLE_KEY = "nextMajorAvailable";
  public static final Metric<Integer> NEXT_MAJOR_AVAILABLE =
      new Metric.Builder(NEXT_MAJOR_AVAILABLE_KEY, "Next Major Available", Metric.ValueType.INT)
          .setDomain(DependencyVersionMetrics.DOMAIN)
          .setDescription("Next Major Available")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(Boolean.FALSE)
          .setHidden(false)
          .create();

  private static final String USING_LAST_VERSION_KEY = "usingLastVersion";
  public static final Metric<Integer> USING_LAST_VERSION =
      new Metric.Builder(USING_LAST_VERSION_KEY, "Using Last Version", Metric.ValueType.INT)
          .setDomain(DependencyVersionMetrics.DOMAIN)
          .setDescription("Using Last Version")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(Boolean.FALSE)
          .setHidden(false)
          .create();
  private static final String REPORT_KEY = "report";

  public static final Metric<String> REPORT =
      new Metric.Builder(REPORT_KEY, "Dependency Version Report", Metric.ValueType.DATA)
          .setHidden(false)
          .setDescription("Report HTML")
          .setQualitative(Boolean.FALSE)
          .setDomain(DependencyVersionMetrics.DOMAIN)
          .setDeleteHistoricalData(true)
          .create();

  @Override
  public List<Metric> getMetrics() {
    return Arrays.asList(
        DependencyVersionMetrics.NEXT_INCREMENTAL_AVAILABLE,
        DependencyVersionMetrics.NEXT_VERSION_AVAILABLE,
        DependencyVersionMetrics.NEXT_MAJOR_AVAILABLE,
        DependencyVersionMetrics.NEXT_MINOR_AVAILABLE,
        DependencyVersionMetrics.TOTAL_DEPENDENCIES,
        DependencyVersionMetrics.USING_LAST_VERSION,
        DependencyVersionMetrics.REPORT);
  }
}

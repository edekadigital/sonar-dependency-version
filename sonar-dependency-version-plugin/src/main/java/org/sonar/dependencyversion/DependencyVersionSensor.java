/*
 * Dependency-Version Plugin for SonarQube
 * Copyright (C) 2019 EDEKA DIGITAL GmbH
 * dpp@edeka.de
 */
package org.sonar.dependencyversion;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.measures.Metric;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.scanner.sensor.ProjectSensor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.api.utils.log.Profiler;
import org.sonar.dependencyversion.base.DependencyVersionMetrics;
import org.sonar.dependencyversion.parser.Dependency;
import org.sonar.dependencyversion.parser.DependencyUpdatesReport;
import org.sonar.dependencyversion.parser.Summary;
import org.sonar.dependencyversion.report.HtmlReport;
import org.sonarsource.analyzer.commons.xml.XmlFile;
import org.sonarsource.analyzer.commons.xml.XmlTextRange;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.sonar.dependencyversion.base.DependencyVersionConstants.*;
import static org.sonar.dependencyversion.base.DependencyVersionMetrics.TOTAL_DEPENDENCIES;
import static org.sonar.dependencyversion.base.DependencyVersionMetrics.TOTAL_OUTDATED_DEPENDENCIES;
import static org.sonarsource.analyzer.commons.xml.XmlFile.create;

public class DependencyVersionSensor implements ProjectSensor {

  private static final Logger LOGGER = Loggers.get(DependencyVersionSensor.class);
  private static final String SENSOR_NAME = "Dependency Version";

  private final InputFile inputFile;
  private final FileSystem fileSystem;
  private final PathResolver pathResolver;
  private final HashMap<String, Pair<XmlTextRange, XmlTextRange>> textRanges;
  private int totalOutdatedDependencies;
  private int nextIncremetalAvailable;
  private int nextVersionAlailable;
  private int nextMinorAvailable;
  private int nextMajorAvailable;
  private int totalDependencies;
  private int usingLastVersion;

  /**
   * Constructor.
   *
   * @param fileSystem
   * @param pathResolver
   */
  public DependencyVersionSensor(FileSystem fileSystem, PathResolver pathResolver) {
    this(
        fileSystem,
        pathResolver,
        Objects.requireNonNull(
            fileSystem.inputFile(fileSystem.predicates().hasPath(POM_PATH_DEFAULT))));
  }

  /**
   * Constructor.
   *
   * @param fileSystem
   * @param pathResolver
   * @param inputFile
   */
  public DependencyVersionSensor(
      FileSystem fileSystem, PathResolver pathResolver, InputFile inputFile) {
    this.inputFile = inputFile;
    this.fileSystem = fileSystem;
    this.pathResolver = pathResolver;
    textRanges = getTextRanges();
  }

  /**
   * Populate {@link SensorDescriptor} of this sensor.
   *
   * @param descriptor
   */
  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name(SENSOR_NAME);
  }

  /**
   * The actual sensor code.
   *
   * @param context
   */
  @Override
  public void execute(SensorContext context) {
    Profiler profiler = Profiler.create(LOGGER);
    profiler.startInfo("Process Dependency Version report");
    try {
      DependencyUpdatesReport report = parseReport(context);
      Summary summary = report.getSummary();
      List<Dependency> dependencies = report.getDependencies();
      this.totalDependencies = dependencies.size();
      this.nextIncremetalAvailable = summary.getNextIncremetalAvailable();
      this.nextVersionAlailable = summary.getNextVersionAlailable();
      this.nextMinorAvailable = summary.getNextMinorAvailable();
      this.nextMajorAvailable = summary.getNextMajorAvailable();
      this.usingLastVersion = summary.getUsingLastVersion();

      totalOutdatedDependencies = (int) dependencies.stream().filter(Objects::nonNull).count();
      addIssues(context, report);
    } catch (FileNotFoundException e) {
      LOGGER.info("Analysis skipped/aborted due to missing report file");
      LOGGER.debug(e.getMessage(), e);
    } catch (IOException e) {
      LOGGER.warn("Analysis aborted due to: IO Errors", e);
    }
    saveMeasures(context);
    profiler.stopInfo();
  }

  private HashMap<String, Pair<XmlTextRange, XmlTextRange>> getTextRanges() {
    XmlFile xmlFile;
    HashMap<String, Pair<XmlTextRange, XmlTextRange>> hashMap = new HashMap<>();
    try {
      xmlFile = create(this.inputFile);
      NodeList nodes = xmlFile.getDocument().getElementsByTagName("dependency");
      for (int i = 0; i < nodes.getLength(); i++) {
        Element element = (Element) nodes.item(i);
        XmlTextRange start = XmlFile.startLocation(element);
        XmlTextRange end = XmlFile.endLocation(element);
        String id =
            String.format(
                "%s:%s",
                element.getElementsByTagName("groupId").item(0).getTextContent(),
                element.getElementsByTagName("artifactId").item(0).getTextContent());
        hashMap.put(id, Pair.of(start, end));
      }
    } catch (IOException e) {
      LOGGER.info("pom.xml does not exist.");
    }
    return hashMap;
  }
  /**
   * helper method
   *
   * @param context sensor content
   */
  private void saveMeasures(SensorContext context) {
    context
        .<Integer>newMeasure()
        .forMetric(TOTAL_DEPENDENCIES)
        .on(context.project())
        .withValue(totalDependencies)
        .save();

    context
        .<Integer>newMeasure()
        .forMetric(DependencyVersionMetrics.USING_LAST_VERSION)
        .on(context.project())
        .withValue(usingLastVersion)
        .save();

    context
        .<Integer>newMeasure()
        .forMetric(DependencyVersionMetrics.NEXT_INCREMENTAL_AVAILABLE)
        .on(context.project())
        .withValue(nextIncremetalAvailable)
        .save();

    context
        .<Integer>newMeasure()
        .forMetric(DependencyVersionMetrics.NEXT_VERSION_AVAILABLE)
        .on(context.project())
        .withValue(nextVersionAlailable)
        .save();

    context
        .<Integer>newMeasure()
        .forMetric(DependencyVersionMetrics.NEXT_MINOR_AVAILABLE)
        .on(context.project())
        .withValue(nextMinorAvailable)
        .save();

    context
        .<Integer>newMeasure()
        .forMetric(DependencyVersionMetrics.NEXT_MAJOR_AVAILABLE)
        .on(context.project())
        .withValue(nextMajorAvailable)
        .save();

    try {
      HtmlReport htmlReportFile =
          HtmlReport.getHtmlReport(context.config(), fileSystem, pathResolver);
      String htmlReport = htmlReportFile.getReportContent();
      if (htmlReport != null) {
        LOGGER.info("Upload Dependency Version HTML-Report");
        htmlReport =
            htmlReport
                .replace("./css/", "/static/dependencyversion/css/")
                .replaceAll("\\./images/|images/", "/static/dependencyversion/images/");
        context
            .<String>newMeasure()
            .forMetric(DependencyVersionMetrics.REPORT)
            .on(context.project())
            .withValue(htmlReport)
            .save();
      }
    } catch (FileNotFoundException e) {
      LOGGER.info(e.getMessage());
      LOGGER.debug(e.getMessage(), e);
    }
  }

  /**
   * helper method
   *
   * @param context sensor context
   * @param report dependency report
   */
  private void addIssues(SensorContext context, DependencyUpdatesReport report) {
    if (Objects.nonNull(report.getDependencies())) {
      final String includes = context.config().get(DEPENDENCY_INCLUDES).orElse(".*");
      final List<Dependency> dependencies = report.getDependencies();
      final List<Dependency> filtered = filterDependencies(dependencies, includes);

      for (Dependency d : filtered) {
        if (Objects.nonNull(d.getNextVersion())) {
          addIssue(context, d);
        }
        saveMetricOnFile(context, inputFile, TOTAL_DEPENDENCIES, this.totalDependencies);
        saveMetricOnFile(
            context, inputFile, TOTAL_OUTDATED_DEPENDENCIES, totalOutdatedDependencies);
      }
    }
  }

  /**
   * helper method for adding a new issues
   *
   * @param context sensor context
   * @param dependency dependency from report
   */
  private void addIssue(SensorContext context, Dependency dependency) {

    NewIssue newIssue =
        context
            .newIssue()
            .forRule(RuleKey.of(REPOSITORY_KEY, RULE_KEY))
            .overrideSeverity(Severity.MAJOR);

    NewIssueLocation location = newIssue.newLocation().on(context.project());

    if (Objects.nonNull(inputFile)) {
      Pair<XmlTextRange, XmlTextRange> textRange =
          textRanges.get(
              String.format("%s:%s", dependency.getGroupId(), dependency.getArtifactId()));

      if (Objects.nonNull(textRange)) {
        location =
            newIssue
                .newLocation()
                .on(inputFile)
                .at(
                    inputFile.newRange(
                        textRange.getLeft().getStartLine(),
                        textRange.getLeft().getStartColumn(),
                        textRange.getRight().getEndLine(),
                        textRange.getRight().getEndColumn()));
      }
    }

    newIssue.at(location.message(formatDescription(dependency))).save();
  }
  /**
   * helper method for filtering dependencies
   *
   * @param dependencies all maven dependencies n project
   * @param includes The includes parameters follow the format groupId:artifactId:type:classifier.
   * @return filtered dependencies
   */
  private List<Dependency> filterDependencies(List<Dependency> dependencies, String includes) {
    return dependencies.stream()
        .filter(
            d ->
                String.format(
                        "%s:%s:%s:%s",
                        d.getGroupId(), d.getArtifactId(), d.getType(), d.getClassifier())
                    .matches(includes))
        .collect(Collectors.toList());
  }

  /**
   * helper method ...
   *
   * @param context
   * @param inputFile
   * @param metric
   * @param value
   */
  private void saveMetricOnFile(
      SensorContext context, @Nullable InputFile inputFile, Metric<Integer> metric, int value) {
    if (Objects.nonNull(inputFile)) {
      context.<Integer>newMeasure().on(inputFile).forMetric(metric).withValue(value);
    }
  }

  @Override
  public String toString() {
    return SENSOR_NAME;
  }

  /**
   * helper method for description formationg
   *
   * @param dependency parsed dependency
   * @return description for issue
   */
  private String formatDescription(Dependency dependency) {
    return String.format(
        "The following dependency has newer version: %s:%s:%s (next version: %s)",
        dependency.getGroupId(),
        dependency.getArtifactId(),
        dependency.getCurrentVersion(),
        dependency.getNextVersion());
  }

  /**
   * method...
   *
   * @param context sonar context
   * @return {@link DependencyUpdatesReport}
   * @throws IOException if no file exists
   */
  private DependencyUpdatesReport parseReport(SensorContext context) throws IOException {

    String path = context.config().get(REPORT_PATH_PROPERTY).orElse(REPORT_PATH_DEFAULT);

    File report = pathResolver.relativeFile(fileSystem.baseDir(), path);

    if (report == null) {
      throw new FileNotFoundException("XML-Dependency-Version report does not exist.");
    }
    try (FileInputStream fis = new FileInputStream(report)) {

      XmlMapper xmlMapper = new XmlMapper();
      xmlMapper.configure(
          com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      return xmlMapper.readValue(fis, DependencyUpdatesReport.class);
    }
  }
}

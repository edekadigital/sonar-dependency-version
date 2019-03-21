package org.sonar.dependencyversion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.measure.Metric;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.scan.filesystem.PathResolver;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.sonar.dependencyversion.base.DependencyVersionConstants.*;
import static org.sonar.dependencyversion.base.DependencyVersionMetrics.REPORT;

class DependencyVersionSensorTest {

  private File xmlReport;
  private File htmlReport;
  private InputFile inputFile;
  private Configuration config;
  private FileSystem fileSystem;
  private PathResolver pathResolver;
  private DependencyVersionSensor sensor;

  @BeforeEach
  void init() throws URISyntaxException {
    inputFile = TestInputFileBuilder.create("projectKey", "report/pom.xml").build();
    this.pathResolver = mock(PathResolver.class);
    fileSystem = mock(FileSystem.class, RETURNS_DEEP_STUBS);
    this.sensor = new DependencyVersionSensor(this.fileSystem, this.pathResolver, this.inputFile);

    // Mock config
    MapSettings settings = new MapSettings();
    settings.setProperty(REPORT_PATH_PROPERTY, "dependency-updates-report.xml");
    config = settings.asConfig();

    ClassLoader classLoader = getClass().getClassLoader();

    // mock a sample report
    final URL xmlResourceURI = classLoader.getResource("report/dependency-updates-report.xml");
    assertThat(xmlResourceURI).isNotNull();
    this.xmlReport = Paths.get(xmlResourceURI.toURI()).toFile();

    final URL htmlResourceURI = classLoader.getResource("report/dependency-updates-report.html");
    assertThat(htmlResourceURI).isNotNull();
    this.htmlReport = Paths.get(htmlResourceURI.toURI()).toFile();

    when(this.pathResolver.relativeFile(
            any(File.class), eq(config.get(REPORT_PATH_PROPERTY).orElse(REPORT_PATH_DEFAULT))))
        .thenReturn(xmlReport);
    when(this.pathResolver.relativeFile(
            any(File.class),
            eq(config.get(HTML_REPORT_PATH_PROPERTY).orElse(HTML_REPORT_PATH_DEFAULT))))
        .thenReturn(htmlReport);
  }

  @Test
  void toStringTest() {
    assertThat(this.sensor.toString()).isEqualTo("Dependency Version");
  }

  @Test
  void describe() {
    final SensorDescriptor descriptor = mock(SensorDescriptor.class);
    sensor.describe(descriptor);
    verify(descriptor).name("Dependency Version");
  }

  @Test
  void shouldAnalyse() {
    final SensorContext context = mock(SensorContext.class, RETURNS_DEEP_STUBS);
    when(context.config()).thenReturn(config);
    when(pathResolver.relativeFile(
            any(File.class), eq(config.get(REPORT_PATH_PROPERTY).orElse(REPORT_PATH_DEFAULT))))
        .thenReturn(xmlReport);
    sensor.execute(context);
  }

  @Test
  void shouldSkipIfReportWasNotFound() {
    final SensorContext context = mock(SensorContext.class, RETURNS_DEEP_STUBS);

    when(context.config()).thenReturn(config);
    when(pathResolver.relativeFile(
            any(File.class), eq(config.get(REPORT_PATH_PROPERTY).orElse(REPORT_PATH_DEFAULT))))
        .thenReturn(null);
    sensor.execute(context);
    verify(context, never()).newIssue();
  }

  @Test
  void shouldPersistTotalMetrics() {
    final SensorContext context = mock(SensorContext.class, RETURNS_DEEP_STUBS);

    when(context.config()).thenReturn(config);
    when(pathResolver.relativeFile(
            any(File.class), eq(config.get(REPORT_PATH_PROPERTY).orElse(REPORT_PATH_DEFAULT))))
        .thenReturn(xmlReport);
    sensor.execute(context);

    verify(context.newMeasure(), times(7)).forMetric(any(Metric.class));
  }

  @Test
  void shouldPersistMetricsOnReport() {
    final SensorContext context = mock(SensorContext.class, RETURNS_DEEP_STUBS);

    when(context.config()).thenReturn(config);
    when(pathResolver.relativeFile(
            any(File.class), eq(config.get(REPORT_PATH_PROPERTY).orElse(REPORT_PATH_DEFAULT))))
        .thenReturn(xmlReport);
    sensor.execute(context);

    verify(context.newMeasure(), atLeastOnce()).on(any(InputComponent.class));
  }

  @Test
  void shouldPersistHtmlReport() {
    final SensorContext context = mock(SensorContext.class, RETURNS_DEEP_STUBS);

    when(context.config()).thenReturn(config);

    when(pathResolver.relativeFile(
            any(File.class),
            eq(config.get(HTML_REPORT_PATH_PROPERTY).orElse(HTML_REPORT_PATH_DEFAULT))))
        .thenReturn(htmlReport);

    sensor.execute(context);

    verify(context.<String>newMeasure().forMetric(REPORT), times(1)).on(any(InputComponent.class));
  }
}

/*
 * Dependency-Version Plugin for SonarQube
 * Copyright (C) 2019 EDEKA DIGITAL GmbH
 * dpp@edeka.de
 */
package org.sonar.dependencyversion.report;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Configuration;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.sonar.dependencyversion.base.DependencyVersionConstants.*;

public class HtmlReport {

  private static final Logger LOGGER = Loggers.get(HtmlReport.class);

  private final File report;

  public HtmlReport(File report) {
    this.report = report;
  }

  /**
   * @param config
   * @param fileSystem
   * @param pathResolver
   * @return
   * @throws FileNotFoundException
   */
  public static HtmlReport getHtmlReport(
      Configuration config, FileSystem fileSystem, PathResolver pathResolver)
      throws FileNotFoundException {
    String path = config.get(HTML_REPORT_PATH_PROPERTY).orElse(HTML_REPORT_PATH_DEFAULT);
    File report = pathResolver.relativeFile(fileSystem.baseDir(), path);
    report = checkReport(report);
    if (report == null) {
      throw new FileNotFoundException("HTML-Dependency-Version report does not exist.");
    }
    return new HtmlReport(report);
  }

  /**
   * helper method for checking report file
   *
   * @param report
   * @return
   */
  @CheckForNull
  protected static File checkReport(@Nullable File report) {
    if (report != null) {
      if (!report.exists()) {
        LOGGER.info(
            "Dependency Version {} report does not exists. Please check property {}",
            report.getAbsolutePath(),
            HTML_REPORT_PATH_PROPERTY);
        return null;
      }
      if (!report.isFile()) {
        LOGGER.info("Dependency Version {} report is not a normal file");
        return null;
      }
      if (!report.canRead()) {
        LOGGER.info("Dependency Version report is not readable");
        return null;
      }
    }
    return report;
  }

  /** @return */
  @CheckForNull
  public String getReportContent() {
    String reportContent = null;
    try {
      reportContent = new String(Files.readAllBytes(report.toPath()), StandardCharsets.UTF_8);
    } catch (IOException e) {
      LOGGER.warn("Could not read HTML-Report", e);
    }
    return reportContent;
  }
}

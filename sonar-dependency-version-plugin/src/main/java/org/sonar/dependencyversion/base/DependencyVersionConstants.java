/*
 * Dependency-Version Plugin for SonarQube
 * Copyright (C) 2019 EDEKA DIGITAL GmbH
 * dpp@edeka.de
 */
package org.sonar.dependencyversion.base;

public final class DependencyVersionConstants {
  public static final String DEPENDENCY_INCLUDES = "sonar.dependencyVersion.includes";
  public static final String REPORT_PATH_PROPERTY = "sonar.dependencyVersion.reportPath";
  public static final String HTML_REPORT_PATH_PROPERTY = "sonar.dependencyVersion.htmlReportPath";

  public static final String POM_PATH_DEFAULT = "pom.xml";
  public static final String REPORT_PATH_DEFAULT = "./target/dependency-updates-report.xml";
  public static final String HTML_REPORT_PATH_DEFAULT = "./target/site/dependency-updates-report.html";
  public static final String SUB_CATEGORY_CONTROLLING = "Controlling which dependencies are processed";

  public static final String RULE_KEY = "UsingOutdatedDependency";
  public static final String REPOSITORY_KEY = "edekadigital";

  public static final String LANGUAGE_KEY = "neutral";
  public static final String PROFILE_NAME = "DependencyVersion";

  private DependencyVersionConstants() {}
}

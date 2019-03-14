/*
 * Dependency Version Plugin for SonarQube
 * Copyright (C) 2019 EDEKA DIGITAL GmbH
 * dpp@edeka.de
 */
package org.sonar.dependencyversion;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.scanner.ScannerSide;
import org.sonar.dependencyversion.base.DependencyVersionConstants;

import java.util.Arrays;
import java.util.List;

@ScannerSide
public class DependencyVersionConfiguration {

  private DependencyVersionConfiguration() {}

  public static List<PropertyDefinition> getPropertyDefinitions() {
    return Arrays.asList(
        PropertyDefinition.builder(DependencyVersionConstants.REPORT_PATH_PROPERTY)
            .subCategory("Paths")
            .name("Dependency-Version report path")
            .description("path to the 'dependency-updates-report.xml' file")
            .defaultValue(DependencyVersionConstants.REPORT_PATH_DEFAULT)
            .build(),
        PropertyDefinition.builder(DependencyVersionConstants.HTML_REPORT_PATH_PROPERTY)
            .subCategory("Paths")
            .name("Dependency-Version HTML report path")
            .description("path to the 'dependency-updates-report.html' file")
            .defaultValue(DependencyVersionConstants.HTML_REPORT_PATH_DEFAULT)
            .build(),
        PropertyDefinition.builder(DependencyVersionConstants.DEPENDENCY_INCLUDES)
            .subCategory(DependencyVersionConstants.SUB_CATEGORY_CONTROLLING)
            .name("Includes")
            .description("The includes parameters can restrict which dependencies should be processed. (groupId:artifactId:type:classifier)")
            .type(PropertyType.REGULAR_EXPRESSION)
            .build());
  }
}

/*
 * Dependency-Version Plugin for SonarQube
 * Copyright (C) 2019 EDEKA DIGITAL GmbH
 * dpp@edeka.de
 */
package org.sonar.dependencyversion;

import org.sonar.api.Plugin;
import org.sonar.dependencyversion.base.DependencyVersionMetrics;
import org.sonar.dependencyversion.page.DependencyVersionReportPage;
import org.sonar.dependencyversion.rule.DependencyVersionRuleDefinition;
import org.sonar.dependencyversion.rule.DependencyVersionLanguage;
import org.sonar.dependencyversion.rule.DependencyVersionProfile;

import java.util.Arrays;
import java.util.Locale;

public class DependencyVersionPlugin implements Plugin {
  /**
   * This method is executed at runtime when:
   *
   * <ul>
   *   <li>Web Server starts
   *   <li>Compute Engine starts
   *   <li>Scanner starts
   * </ul>
   *
   * @param context
   */
  @Override
  public void define(Context context) {
    Locale.setDefault(Locale.ENGLISH);
    context.addExtensions(
        Arrays.asList(
            DependencyVersionProfile.class,
            DependencyVersionLanguage.class,
            DependencyVersionSensor.class,
            DependencyVersionMetrics.class,
            DependencyVersionReportPage.class,
            DependencyVersionRuleDefinition.class));
    context.addExtensions(DependencyVersionConfiguration.getPropertyDefinitions());
  }
}

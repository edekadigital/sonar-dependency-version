/*
 * Dependency-Version Plugin for SonarQube
 * Copyright (C) 2019 EDEKA DIGITAL GmbH
 * dpp@edeka.de
 */
package org.sonar.dependencyversion.rule;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

import static org.sonar.dependencyversion.base.DependencyVersionConstants.*;

public class DependencyVersionProfile implements BuiltInQualityProfilesDefinition {

  /**
   * This method is executed when server is started.
   *
   * @param context
   */
  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile dependencyVersionWay = context.createBuiltInQualityProfile("DependencyVersion", LANGUAGE_KEY);
    dependencyVersionWay.activateRule(REPOSITORY_KEY, RULE_KEY);
    dependencyVersionWay.done();
  }
}

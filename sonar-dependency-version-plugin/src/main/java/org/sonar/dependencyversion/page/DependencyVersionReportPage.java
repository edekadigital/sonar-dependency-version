/*
 * Dependency-Version Plugin for SonarQube
 * Copyright (C) 2019 EDEKA DIGITAL GmbH
 * dpp@edeka.de
 */
package org.sonar.dependencyversion.page;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.PageDefinition;

public class DependencyVersionReportPage implements PageDefinition {
  /**
   * This method is executed when server is started
   *
   * @param context
   */
  @Override
  public void define(Context context) {
    context.addPage(
        Page.builder("dependencyversion/report")
            .setScope(Page.Scope.COMPONENT)
            .setComponentQualifiers(Page.Qualifier.PROJECT)
            .setName("Dependency Version")
            .setAdmin(false)
            .build());
  }
}

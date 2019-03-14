/*
 * Dependency-Version Plugin for SonarQube
 * Copyright (C) 2019 EDEKA DIGITAL GmbH
 * dpp@edeka.de
 */
package org.sonar.dependencyversion.rule;

import org.sonar.api.resources.AbstractLanguage;

import static org.sonar.dependencyversion.base.DependencyVersionConstants.LANGUAGE_KEY;

public class DependencyVersionLanguage extends AbstractLanguage {

  public DependencyVersionLanguage() {

    super(LANGUAGE_KEY, "Neutral");
  }

  /**
   * Better to use AbstractLanguage(key, name). In this case, key and name will be the same
   *
   * @param key The key of the language. Must not be more than 20 chars.
   */
  public DependencyVersionLanguage(String key) {
    super(key);
  }

  /**
   * Should be the constructor used to build an AbstractLanguage.
   *
   * @param key the key that will be used to retrieve the language. Must not be more than 20 chars.
   *     This key is important as it will be used to teint rules repositories...
   * @param name the display name of the language in the interface
   */
  public DependencyVersionLanguage(String key, String name) {
    super(key, name);
  }

  /**
   * For example ["jav", "java"]. If empty, then all files in source directories are considered as
   * sources.
   */
  @Override
  public String[] getFileSuffixes() {
    return new String[0];
  }
}

package org.sonar.dependencyversion.rule;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class DependencyVersionLanguageTest {
  @Test
  void getFileSuffixes() {
    assertThat(new DependencyVersionLanguage().getFileSuffixes()).isEmpty();
  }
}

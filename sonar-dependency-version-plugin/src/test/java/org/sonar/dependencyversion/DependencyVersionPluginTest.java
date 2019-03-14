package org.sonar.dependencyversion;

import org.junit.jupiter.api.Test;
import org.sonar.api.Plugin;

import static org.mockito.Mockito.mock;

class DependencyVersionPluginTest {

  @Test
  public void define() {
    final Plugin.Context context = mock(Plugin.Context.class);
    new DependencyVersionPlugin().define(context);
  }
}

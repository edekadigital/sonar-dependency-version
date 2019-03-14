package org.sonar.dependencyversion.parser;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class DependencyUpdatesReport {

  private Summary summary;

  @JacksonXmlProperty(localName = "dependency")
  @JacksonXmlElementWrapper(localName = "dependencies")
  private List<Dependency> dependencies;

  public Summary getSummary() {
    return summary;
  }

  public void setSummary(Summary value) {
    this.summary = value;
  }

  public List<Dependency> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<Dependency> dependencies) {
    this.dependencies = dependencies;
  }
}

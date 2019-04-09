package org.sonar.dependencyversion.parser;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Dependency {
  private String groupId;
  private String artifactId;
  private String scope;
  private String classifier;
  private String type;
  private String currentVersion;
  private String nextVersion;

  @JacksonXmlProperty(localName = "incremental")
  @JacksonXmlElementWrapper(localName = "incrementals")
  private List<String> incrementals;

  @JacksonXmlProperty(localName = "minor")
  @JacksonXmlElementWrapper(localName = "minors")
  private List<String> minors;

  @JacksonXmlProperty(localName = "major")
  @JacksonXmlElementWrapper(localName = "majors")
  private List<String> majors;

  private String status;

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String value) {
    this.groupId = value;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public void setArtifactId(String value) {
    this.artifactId = value;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String value) {
    this.scope = value;
  }

  public String getClassifier() {
    return classifier;
  }

  public void setClassifier(String value) {
    this.classifier = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String value) {
    this.type = value;
  }

  public String getCurrentVersion() {
    return currentVersion;
  }

  public void setCurrentVersion(String value) {
    this.currentVersion = value;
  }

  public String getNextVersion() {
    return nextVersion;
  }

  public void setNextVersion(String value) {
    this.nextVersion = value;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String value) {
    this.status = value;
  }

  public List<String> getIncrementals() {
    return incrementals;
  }

  public void setIncrementals(List<String> incrementals) {
    this.incrementals = incrementals;
  }

  public List<String> getMinors() {
    return minors;
  }

  public void setMinors(List<String> minors) {
    this.minors = minors;
  }

  public List<String> getMajors() {
    return majors;
  }

  public void setMajors(List<String> majors) {
    this.majors = majors;
  }
}

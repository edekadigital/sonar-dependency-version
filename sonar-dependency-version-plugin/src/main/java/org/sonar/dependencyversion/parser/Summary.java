package org.sonar.dependencyversion.parser;

public class Summary {

  private int usingLastVersion;
  private int nextVersionAlailable;
  private int nextIncremetalAvailable;
  private int nextMinorAvailable;
  private int nextMajorAvailable;

  public int getUsingLastVersion() {
    return usingLastVersion;
  }

  public void setUsingLastVersion(int value) {
    this.usingLastVersion = value;
  }

  public int getNextVersionAlailable() {
    return nextVersionAlailable;
  }

  public void setNextVersionAlailable(int value) {
    this.nextVersionAlailable = value;
  }

  public int getNextIncremetalAvailable() {
    return nextIncremetalAvailable;
  }

  public void setNextIncremetalAvailable(int value) {
    this.nextIncremetalAvailable = value;
  }

  public int getNextMinorAvailable() {
    return nextMinorAvailable;
  }

  public void setNextMinorAvailable(int value) {
    this.nextMinorAvailable = value;
  }

  public int getNextMajorAvailable() {
    return nextMajorAvailable;
  }

  public void setNextMajorAvailable(int value) {
    this.nextMajorAvailable = value;
  }
}

package org.sonar.dependencyversion.parser;

public class Summary {

  private int usingLastVersion;
  private int nextVersionAvailable;
  private int nextIncrementalAvailable;
  private int nextMinorAvailable;
  private int nextMajorAvailable;

  public int getUsingLastVersion() {
    return usingLastVersion;
  }

  public void setUsingLastVersion(int value) {
    this.usingLastVersion = value;
  }

  public int getNextVersionAvailable() {
    return nextVersionAvailable;
  }

  public void setnextVersionAvailable(int value) {
    this.nextVersionAvailable = value;
  }

  public int getNextIncrementalAvailable() {
    return nextIncrementalAvailable;
  }

  public void setnextIncrementalAvailable(int value) {
    this.nextIncrementalAvailable = value;
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

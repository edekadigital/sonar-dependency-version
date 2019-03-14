package org.sonar.dependencyversion.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyUpdatesReportTest {
  private File xmlReport;

  @BeforeEach
  void init() throws URISyntaxException {
    final URL xmlResourceURI =
        getClass().getClassLoader().getResource("report/dependency-updates-report.xml");
    assertThat(xmlResourceURI).isNotNull();
    this.xmlReport = Paths.get(xmlResourceURI.toURI()).toFile();
  }

  @Test
  void whenJavaSerializedToXmlStr_thenCorrect() throws JsonProcessingException {
    XmlMapper xmlMapper = new XmlMapper();
    String xml = xmlMapper.writeValueAsString(new DependencyUpdatesReport());
    assertThat(xml).isNotNull();
  }

  @Test
  void whenJavaGotFromXmlStr_thenCorrect() throws IOException {
    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.configure(
        com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    String xml = inputStreamToString(new FileInputStream(xmlReport));
    DependencyUpdatesReport report = xmlMapper.readValue(xml, DependencyUpdatesReport.class);
    assertThat(report.getDependencies().size()).isEqualTo(8);
  }

  String inputStreamToString(InputStream is) throws IOException {
    StringBuilder sb = new StringBuilder();
    String line;
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    while ((line = br.readLine()) != null) {
      sb.append(line);
    }
    br.close();
    return sb.toString();
  }
}

package org.sonar.dependencyversion.rule;

import org.junit.jupiter.api.Test;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.BuiltInActiveRule;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.BuiltInQualityProfile;
import org.sonar.api.utils.ValidationMessages;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.Context;
import static org.sonar.dependencyversion.base.DependencyVersionConstants.*;

class DependencyVersionProfileTest {

  @Test
  void define() {}

  @Test
  public void should_create_dependency_version_profile() {
    ValidationMessages validation = ValidationMessages.create();

    DependencyVersionProfile dependencyVersionProfile = new DependencyVersionProfile();
    Context context = new Context();
    dependencyVersionProfile.define(context);
    BuiltInQualityProfile profile = context.profile(LANGUAGE_KEY, PROFILE_NAME);
    List<BuiltInActiveRule> activeRules = profile.rules();

    assertThat(activeRules.stream().filter(r -> r.repoKey().equals(REPOSITORY_KEY))).hasSize(1);
    assertThat(activeRules.size()).isGreaterThanOrEqualTo(1);
    assertThat(profile.name()).isEqualTo(PROFILE_NAME);

    Set<String> keys =
        activeRules.stream().map(BuiltInActiveRule::ruleKey).collect(Collectors.toSet());
    assertThat(keys).contains(RULE_KEY);

    assertThat(activeRules.get(0).overriddenSeverity()).isNull();
    assertThat(validation.hasErrors()).isFalse();
  }

  @Test
  public void should_activate_hotspots_when_supported() {
    DependencyVersionProfile dependencyVersionProfile = new DependencyVersionProfile();
    Context context = new Context();
    dependencyVersionProfile.define(context);
    BuiltInQualityProfile profile = context.profile(LANGUAGE_KEY, PROFILE_NAME);
    BuiltInActiveRule rule = profile.rule(RuleKey.of(REPOSITORY_KEY, RULE_KEY));
    assertThat(rule).isNotNull();
  }
}

package org.sonar.dependencyversion.rule;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.sonar.api.server.rule.RulesDefinition;

import static org.mockito.Mockito.*;
import static org.sonar.dependencyversion.base.DependencyVersionConstants.*;

class DependencyVersionRuleDefinitionTest {

  private DependencyVersionRuleDefinition rule = new DependencyVersionRuleDefinition();

  @Test
  void define() {
    final RulesDefinition.Context context = mock(RulesDefinition.Context.class);
    final RulesDefinition.NewRepository repo = mock(RulesDefinition.NewRepository.class);
    final RulesDefinition.NewRule rule = mock(RulesDefinition.NewRule.class, RETURNS_SMART_NULLS);

    when(repo.createRule(RULE_KEY)).thenReturn(rule);
    when(context.createRepository(anyString(), anyString())).thenReturn(repo);

    this.rule.define(context);

    InOrder inOrder = inOrder(context, repo);

    inOrder.verify(context).createRepository(REPOSITORY_KEY, LANGUAGE_KEY);
    inOrder.verify(repo).createRule(RULE_KEY);

    inOrder.verify(repo).done();
    inOrder.verifyNoMoreInteractions();
  }
}

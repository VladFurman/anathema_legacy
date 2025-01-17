package net.sf.anathema;

import com.google.inject.Inject;
import cucumber.api.java.en.Then;
import net.sf.anathema.character.impl.model.advance.ExperiencePointManagement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExperiencePointSteps {

  private final CharacterHolder character;

  @Inject
  public ExperiencePointSteps(CharacterHolder character) {
    this.character = character;
  }

  @Then("^she has spent (\\d+) experience points$")
  public void she_has_spent_bonus_points(int amount) throws Throwable {
    ExperiencePointManagement pointManagement = new ExperiencePointManagement(character.getCharacter());
    int spentPoints = pointManagement.getTotalCosts();
    assertThat(spentPoints, is(amount));
  }
}

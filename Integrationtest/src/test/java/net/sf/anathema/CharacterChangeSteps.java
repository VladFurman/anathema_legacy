package net.sf.anathema;

import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.sf.anathema.character.generic.caste.ICasteCollection;
import net.sf.anathema.character.generic.caste.ICasteType;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.IModifiableBasicTrait;
import net.sf.anathema.character.generic.impl.traits.TraitTypeUtils;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.character.generic.traits.types.AbilityType;
import net.sf.anathema.character.library.trait.favorable.IFavorableTrait;
import net.sf.anathema.character.model.background.IBackground;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharacterChangeSteps {

  private final CharacterHolder character;

  @Inject
  public CharacterChangeSteps(CharacterHolder character) {
    this.character = character;
  }

  @Given("^her current Essence is (\\d+)$")
  public void herCurrentEssenceIs(int value) throws Throwable {
    I_set_her_trait_to("Essence", value);
  }

  @Given("^she is experienced")
  public void setToExperienced() {
    character.getCharacter().setExperienced(true);
  }

  @When("^I set her Caste to (.*)$")
  public void I_set_her_Caste(String casteName) throws Throwable {
    ICasteCollection casteCollection = character.getCharacterTemplate().getCasteCollection();
    ICasteType caste = casteCollection.getById(casteName);
    character.getCharacterConcept().getCaste().setType(caste);
  }

  @When("^I set the background (.*) to (\\d+)$")
  public void she_has_the_background_at(String name, int value) throws Throwable {
    IBackground background = character.getTraitConfiguration().getBackgrounds().addBackground(name, "");
    background.setCreationValue(value);
  }

  @When("^I set her (.*) to (\\d+)$")
  public void I_set_her_trait_to(String traitId, int value) throws Throwable {
    ITraitType type = new TraitTypeUtils().getTraitTypeById(traitId);
    IModifiableBasicTrait trait = (IModifiableBasicTrait) character.getTraitConfiguration().getTrait(type);
    if (character.getCharacter().isExperienced()) {
      trait.setExperiencedValue(value);
    } else {
      trait.setCreationValue(value);
    }
  }

  @Then("^she has (\\d+) dots in (.*)$")
  public void she_has_dots_in_Ability(int amount, String abilityName) throws Throwable {
    IFavorableTrait ability = character.getTraitConfiguration().getFavorableTrait(AbilityType.valueOf(abilityName));
    assertThat(ability.getCurrentValue(), is(amount));
  }
}
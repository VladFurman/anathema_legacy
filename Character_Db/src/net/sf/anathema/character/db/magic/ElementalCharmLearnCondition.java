package net.sf.anathema.character.db.magic;

import net.sf.anathema.character.db.DbCharacterType;
import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.ICharmLearnableArbitrator;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffect;
import net.sf.anathema.character.generic.magic.charms.special.SubEffects;
import net.sf.anathema.lib.data.Condition;

public class ElementalCharmLearnCondition implements Condition {
  private final SubEffects allEffects;
  private final ICharmLearnableArbitrator arbitrator;
  private final ICharm charm;
  private final IBasicCharacterData data;
  private final Element element;

  public ElementalCharmLearnCondition(SubEffects allEffects, ICharmLearnableArbitrator arbitrator,
                                      ICharm charm, IBasicCharacterData data, Element element) {
    this.allEffects = allEffects;
    this.arbitrator = arbitrator;
    this.charm = charm;
    this.data = data;
    this.element = element;
  }

  @Override
  public boolean isFulfilled() {
    boolean learnable = arbitrator.isLearnable(charm);
    if (!data.getCharacterType().equals(new DbCharacterType())) {
      return learnable;
    }
    if (data.getCasteType().getId() == null) {
      return false;
    }
    if (element.matches(data.getCasteType())) {
      return learnable;
    }
    for (ISubeffect effect : allEffects) {
      ElementalSubeffect elemental = (ElementalSubeffect) effect;
      if (effect.isLearned() && elemental.matches(data.getCasteType())) {
        return learnable;
      }
    }
    return false;
  }
}
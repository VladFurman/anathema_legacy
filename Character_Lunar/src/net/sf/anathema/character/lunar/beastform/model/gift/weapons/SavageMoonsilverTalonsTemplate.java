package net.sf.anathema.character.lunar.beastform.model.gift.weapons;

import net.sf.anathema.character.equipment.MagicalMaterial;
import net.sf.anathema.character.equipment.MaterialComposition;
import net.sf.anathema.character.equipment.template.IEquipmentTemplate;
import net.sf.anathema.character.generic.equipment.weapon.IEquipmentStats;
import net.sf.anathema.character.generic.equipment.weapon.IWeaponStats;
import net.sf.anathema.character.generic.rules.IExaltedRuleSet;
import net.sf.anathema.character.generic.rules.IRuleSetVisitor;

public class SavageMoonsilverTalonsTemplate implements IEquipmentTemplate {

  private static final String TALONS = "Lunar.SavageMoonsilverTalons"; //$NON-NLS-1$
  private static final IWeaponStats CORERULES_CLAWS = new CoreRulesTalonsClaws();
  private static final IWeaponStats CORERULES_BITE = new CoreRulesTalonsBite();

  @Override
  public MaterialComposition getComposition() {
    return MaterialComposition.Fixed;
  }

  @Override
  public String getDescription() {
    return TALONS;
  }

  @Override
  public MagicalMaterial getMaterial() {
    return MagicalMaterial.Moonsilver;
  }

  @Override
  public String getName() {
    return TALONS;
  }

  @Override
  public IEquipmentStats[] getStats(IExaltedRuleSet ruleSet) {
    final IEquipmentStats[] stats = new IEquipmentStats[2];
    ruleSet.accept(new IRuleSetVisitor() {
      @Override
      public void visitCoreRules(IExaltedRuleSet set) {
        stats[0] = CORERULES_CLAWS;
        stats[1] = CORERULES_BITE;
      }

      @Override
      public void visitSecondEdition(IExaltedRuleSet set) {
        throw new UnsupportedOperationException("Second Edition Lunars not yet supported"); //$NON-NLS-1$
      }
    });
    return stats;
  }
}
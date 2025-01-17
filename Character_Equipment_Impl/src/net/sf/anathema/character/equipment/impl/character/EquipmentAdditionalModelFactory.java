package net.sf.anathema.character.equipment.impl.character;

import net.sf.anathema.character.equipment.IEquipmentAdditionalModelTemplate;
import net.sf.anathema.character.equipment.MaterialRules;
import net.sf.anathema.character.equipment.character.EquipmentCharacterDataProvider;
import net.sf.anathema.character.equipment.impl.character.model.EquipmentAdditionalModel;
import net.sf.anathema.character.equipment.impl.character.model.natural.DefaultNaturalSoak;
import net.sf.anathema.character.equipment.impl.character.model.natural.NaturalWeaponTemplate;
import net.sf.anathema.character.equipment.item.model.IEquipmentTemplateProvider;
import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModel;
import net.sf.anathema.character.generic.equipment.weapon.IArmourStats;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.IAdditionalModelFactory;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.template.additional.IAdditionalTemplate;
import net.sf.anathema.character.generic.type.ICharacterType;

public class EquipmentAdditionalModelFactory implements IAdditionalModelFactory {

  private final IEquipmentTemplateProvider equipmentTemplateProvider;
  private final MaterialRules materialRules;

  public EquipmentAdditionalModelFactory(IEquipmentTemplateProvider equipmentTemplateProvider, MaterialRules materialRules) {
    this.equipmentTemplateProvider = equipmentTemplateProvider;
    this.materialRules = materialRules;
  }

  @Override
  public IAdditionalModel createModel(IAdditionalTemplate additionalTemplate, ICharacterModelContext context) {
    IEquipmentAdditionalModelTemplate template = (IEquipmentAdditionalModelTemplate) additionalTemplate;
    IBasicCharacterData basicCharacterContext = context.getBasicCharacterContext();
    ICharacterType characterType = basicCharacterContext.getCharacterType();
    IArmourStats naturalArmour = new DefaultNaturalSoak(context);
    EquipmentCharacterDataProvider dataProvider = new EquipmentCharacterDataProvider(context, materialRules);
    return new EquipmentAdditionalModel(
            characterType,
            naturalArmour,
            equipmentTemplateProvider, context.getSpecialtyContext(), dataProvider, materialRules,
            new NaturalWeaponTemplate(),
            template.getNaturalWeaponTemplate(characterType));
  }
}
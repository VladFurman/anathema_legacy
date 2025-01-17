package net.sf.anathema.character.sidereal;

import net.sf.anathema.character.generic.backgrounds.IBackgroundTemplate;
import net.sf.anathema.character.generic.framework.ICharacterGenerics;
import net.sf.anathema.character.generic.framework.additionaltemplate.IAdditionalViewFactory;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.IAdditionalModelFactory;
import net.sf.anathema.character.generic.framework.additionaltemplate.persistence.IAdditionalPersisterFactory;
import net.sf.anathema.character.generic.framework.module.CharacterModule;
import net.sf.anathema.character.generic.framework.module.CharacterTypeModule;
import net.sf.anathema.character.generic.impl.backgrounds.CharacterTypeBackgroundTemplate;
import net.sf.anathema.character.generic.impl.backgrounds.TemplateTypeBackgroundTemplate;
import net.sf.anathema.character.generic.impl.caste.CasteCollection;
import net.sf.anathema.character.generic.template.ITemplateType;
import net.sf.anathema.character.generic.template.TemplateType;
import net.sf.anathema.character.generic.traits.LowerableState;
import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.character.sidereal.caste.SiderealCaste;
import net.sf.anathema.character.sidereal.colleges.SiderealCollegeModelFactory;
import net.sf.anathema.character.sidereal.colleges.SiderealCollegeTemplate;
import net.sf.anathema.character.sidereal.colleges.SiderealCollegeViewFactory;
import net.sf.anathema.character.sidereal.colleges.persistence.SiderealCollegePersisterFactory;
import net.sf.anathema.character.sidereal.flawedfate.SiderealFlawedFateModelFactory;
import net.sf.anathema.character.sidereal.flawedfate.SiderealFlawedFatePersisterFactory;
import net.sf.anathema.character.sidereal.flawedfate.SiderealFlawedFateTemplate;
import net.sf.anathema.character.sidereal.flawedfate.SiderealFlawedFateViewFactory;
import net.sf.anathema.character.sidereal.paradox.SiderealParadoxModelFactory;
import net.sf.anathema.character.sidereal.paradox.SiderealParadoxPersisterFactory;
import net.sf.anathema.character.sidereal.paradox.SiderealParadoxTemplate;
import net.sf.anathema.character.sidereal.paradox.SiderealParadoxViewFactory;
import net.sf.anathema.lib.registry.IIdentificateRegistry;
import net.sf.anathema.lib.registry.IRegistry;
import net.sf.anathema.lib.util.Identifier;

@CharacterModule
public class SiderealCharacterModule extends CharacterTypeModule {
  public static final ICharacterType type = new SiderealCharacterType();
  public static final String BACKGROUND_ID_ACQUAINTANCES = "Acquaintances";
  public static final String BACKGROUND_ID_CONNECTIONS = "Connections";
  public static final String BACKGROUND_ID_CELESTIAL_MANSE = "CelestialManse";
  public static final String BACKGROUND_ID_SALARY = "Salary";
  public static final String BACKGROUND_ID_SAVANT = "Savant";
  public static final String BACKGROUND_ID_SIFU = "Sifu";

  private static final TemplateType defaultTemplateType = new TemplateType(type);
  public static final TemplateType dreamsType = new TemplateType(type, new Identifier("Dreams"));
  public static final TemplateType dreamsEstablishedType = new TemplateType(type, new Identifier("DreamsEstablished"));
  public static final TemplateType dreamsInfluentialType = new TemplateType(type, new Identifier("DreamsInfluential"));
  public static final TemplateType dreamsLegendaryType = new TemplateType(type, new Identifier("DreamsLegendary"));

  public static final TemplateType[] dreams = {dreamsType, dreamsEstablishedType, dreamsInfluentialType, dreamsLegendaryType};

  public static final String BACKGROUND_ID_ARSENAL = "SiderealDreamsArsenal";
  public static final String BACKGROUND_ID_COMMAND = "SiderealDreamsCommand";
  public static final String BACKGROUND_ID_HENCHMEN = "SiderealDreamsHenchmen";
  public static final String BACKGROUND_ID_PANOPLY = "SiderealDreamsPanoply";
  public static final String BACKGROUND_ID_REPUTATION = "SiderealDreamsReputation";
  public static final String BACKGROUND_ID_RETAINERS = "SiderealDreamsRetainers";
  public static final String BACKGROUND_ID_WEALTH = "SiderealDreamsWealth";

  @Override
  public void registerCommonData(ICharacterGenerics characterGenerics) {
    characterGenerics.getCasteCollectionRegistry().register(type, new CasteCollection(SiderealCaste.values()));
  }

  @Override
  public void addBackgroundTemplates(ICharacterGenerics generics) {
    IIdentificateRegistry<IBackgroundTemplate> backgroundRegistry = generics.getBackgroundRegistry();
    ITemplateType[] defaultTemplateType = new ITemplateType[]{SiderealCharacterModule.defaultTemplateType};
    backgroundRegistry.add(new CharacterTypeBackgroundTemplate(BACKGROUND_ID_ACQUAINTANCES, type));
    backgroundRegistry.add(new CharacterTypeBackgroundTemplate(BACKGROUND_ID_CONNECTIONS, type));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_CELESTIAL_MANSE, defaultTemplateType));
    backgroundRegistry.add(new CharacterTypeBackgroundTemplate(BACKGROUND_ID_SALARY, type));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_SAVANT, defaultTemplateType, LowerableState.Default));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_SIFU, defaultTemplateType));

    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_ARSENAL, dreams));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_COMMAND, dreams));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_HENCHMEN, dreams));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_PANOPLY, dreams));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_REPUTATION, dreams));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_RETAINERS, dreams));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_SAVANT, dreams));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_SIFU, dreams));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_WEALTH, dreams));
  }

  @Override
  public void addAdditionalTemplateData(ICharacterGenerics characterGenerics) {
    IRegistry<String, IAdditionalModelFactory> additionalModelFactoryRegistry = characterGenerics.getAdditionalModelFactoryRegistry();
    IRegistry<String, IAdditionalViewFactory> additionalViewFactoryRegistry = characterGenerics.getAdditionalViewFactoryRegistry();
    IRegistry<String, IAdditionalPersisterFactory> persisterFactory = characterGenerics.getAdditonalPersisterFactoryRegistry();
    registerSiderealColleges(additionalModelFactoryRegistry, additionalViewFactoryRegistry, persisterFactory);
    registerFlawedFate(additionalModelFactoryRegistry, additionalViewFactoryRegistry, persisterFactory);
    registerParadox(additionalModelFactoryRegistry, additionalViewFactoryRegistry, persisterFactory);
  }

  private void registerSiderealColleges(IRegistry<String, IAdditionalModelFactory> additionalModelFactoryRegistry,
                                        IRegistry<String, IAdditionalViewFactory> additionalViewFactoryRegistry, IRegistry<String, IAdditionalPersisterFactory> persisterFactory) {
    String templateId = SiderealCollegeTemplate.ID;
    additionalModelFactoryRegistry.register(templateId, new SiderealCollegeModelFactory());
    additionalViewFactoryRegistry.register(templateId, new SiderealCollegeViewFactory());
    persisterFactory.register(templateId, new SiderealCollegePersisterFactory());
  }

  private void registerFlawedFate(IRegistry<String, IAdditionalModelFactory> additionalModelFactoryRegistry,
                                  IRegistry<String, IAdditionalViewFactory> additionalViewFactoryRegistry, IRegistry<String, IAdditionalPersisterFactory> persisterFactory) {
    String templateId = SiderealFlawedFateTemplate.ID;
    additionalModelFactoryRegistry.register(templateId, new SiderealFlawedFateModelFactory());
    additionalViewFactoryRegistry.register(templateId, new SiderealFlawedFateViewFactory());
    persisterFactory.register(templateId, new SiderealFlawedFatePersisterFactory());
  }

  private void registerParadox(IRegistry<String, IAdditionalModelFactory> additionalModelFactoryRegistry,
                               IRegistry<String, IAdditionalViewFactory> additionalViewFactoryRegistry, IRegistry<String, IAdditionalPersisterFactory> persisterFactory) {
    String templateId = SiderealParadoxTemplate.ID;
    additionalModelFactoryRegistry.register(templateId, new SiderealParadoxModelFactory());
    additionalViewFactoryRegistry.register(templateId, new SiderealParadoxViewFactory());
    persisterFactory.register(templateId, new SiderealParadoxPersisterFactory());
  }

  @Override
  protected ICharacterType getType() {
	  return type;
  }
}
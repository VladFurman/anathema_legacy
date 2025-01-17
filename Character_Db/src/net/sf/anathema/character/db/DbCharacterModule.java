package net.sf.anathema.character.db;

import net.sf.anathema.character.db.aspect.DBAspect;
import net.sf.anathema.character.db.virtueflaw.DbVirtueFlawModelFactory;
import net.sf.anathema.character.db.virtueflaw.DbVirtueFlawTemplate;
import net.sf.anathema.character.db.virtueflaw.DbVirtueFlawViewFactory;
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
import net.sf.anathema.character.library.virtueflaw.persistence.DefaultVirtueFlawPersisterFactory;
import net.sf.anathema.initialization.InitializationException;
import net.sf.anathema.lib.registry.IIdentificateRegistry;
import net.sf.anathema.lib.registry.IRegistry;
import net.sf.anathema.lib.util.Identifier;

@CharacterModule
public class DbCharacterModule extends CharacterTypeModule {

  public static final ICharacterType type = new DbCharacterType();
  public static final String BACKGROUND_ID_ARSENAL = "Arsenal";
  public static final String BACKGROUND_ID_BREEDING = "Breeding";
  public static final String BACKGROUND_ID_FAMILY = "Family";
  public static final String BACKGROUND_ID_COMMAND = "Command";
  public static final String BACKGROUND_ID_CONNECTIONS = "Connections";
  public static final String BACKGROUND_ID_HENCHMEN = "Henchmen";
  public static final String BACKGROUND_ID_REPUTATION = "Reputation";
  public static final String BACKGROUND_ID_RETAINERS = "Retainers";
  public static final String BACKGROUND_ID_PANOPLY = "DBDreamsPanoply";
  public static final String BACKGROUND_ID_SAVANT = "DBDreamsSavant";
  public static final String BACKGROUND_ID_SIFU = "DBDreamsSifu";
  private static final TemplateType dynastTemplateType = new TemplateType(type);
  private static final TemplateType lookshyNativeTemplateType = new TemplateType(type,
          new Identifier("LookshySubtype"));
  private static final TemplateType lookshyOutcasteTemplateType = new TemplateType(type,
          new Identifier("LookshyOutcasteSubtype"));
  private static final TemplateType lookshyRealmTemplateType = new TemplateType(type,
          new Identifier("LookshyRealmSubtype"));
  private static final TemplateType cherakiTemplateType = new TemplateType(type,
          new Identifier("Cherak"));
  private static final TemplateType immaculateTemplateType = new TemplateType(type,
          new Identifier("ImmaculateSubtype"));
  private static final TemplateType dreamsTemplateType = new TemplateType(type,
          new Identifier("DreamsSubtype"));
  private static final TemplateType dreamsYoungType = new TemplateType(type, new Identifier("DreamsYoung"));
  private static final TemplateType dreamsInfluentialType = new TemplateType(type, new Identifier("DreamsInfluential"));
  private static final TemplateType dreamsLegendaryType = new TemplateType(type, new Identifier("DreamsLegendary"));
  private static final TemplateType[] dreams = {dreamsTemplateType, dreamsYoungType, dreamsInfluentialType, dreamsLegendaryType};

  @Override
  public void registerCommonData(ICharacterGenerics characterGenerics) {
    characterGenerics.getCasteCollectionRegistry().register(type, new CasteCollection(DBAspect.values()));
  }

  @Override
  public void addBackgroundTemplates(ICharacterGenerics generics) {
    IIdentificateRegistry<IBackgroundTemplate> backgroundRegistry = generics.getBackgroundRegistry();
    IBackgroundTemplate breedingTemplate = new CharacterTypeBackgroundTemplate(BACKGROUND_ID_BREEDING, type,
            LowerableState.Immutable);
    backgroundRegistry.add(breedingTemplate);
    backgroundRegistry.add(new CharacterTypeBackgroundTemplate(BACKGROUND_ID_COMMAND, type));
    backgroundRegistry.add(new CharacterTypeBackgroundTemplate(BACKGROUND_ID_CONNECTIONS, type));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_FAMILY,
            new ITemplateType[]{dynastTemplateType, lookshyNativeTemplateType, lookshyRealmTemplateType, dreamsTemplateType}));
    backgroundRegistry.add(new CharacterTypeBackgroundTemplate(BACKGROUND_ID_HENCHMEN, type));
    backgroundRegistry.add(new CharacterTypeBackgroundTemplate(BACKGROUND_ID_REPUTATION, type));
    addLookshyBackgrounds(backgroundRegistry);

    backgroundRegistry.add(
            new TemplateTypeBackgroundTemplate(BACKGROUND_ID_PANOPLY, dreams));
    backgroundRegistry.add(
            new TemplateTypeBackgroundTemplate(BACKGROUND_ID_SAVANT, dreams));
    backgroundRegistry.add(
            new TemplateTypeBackgroundTemplate(BACKGROUND_ID_SIFU, dreams));
  }

  private void addLookshyBackgrounds(IIdentificateRegistry<IBackgroundTemplate> backgroundRegistry) {
    ITemplateType[] lookshyTemplateTypes = new ITemplateType[]{lookshyNativeTemplateType, lookshyRealmTemplateType, lookshyOutcasteTemplateType, dreamsTemplateType};
    ITemplateType[] retainerTemplateTypes = new ITemplateType[]{dynastTemplateType, immaculateTemplateType, cherakiTemplateType, lookshyNativeTemplateType, lookshyRealmTemplateType, lookshyOutcasteTemplateType, dreamsTemplateType};
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_ARSENAL, lookshyTemplateTypes));
    backgroundRegistry.add(new TemplateTypeBackgroundTemplate(BACKGROUND_ID_RETAINERS, retainerTemplateTypes));
  }

  @Override
  public void addAdditionalTemplateData(ICharacterGenerics characterGenerics) throws InitializationException {
    IRegistry<String, IAdditionalModelFactory> additionalModelFactoryRegistry = characterGenerics.getAdditionalModelFactoryRegistry();
    String templateId = DbVirtueFlawTemplate.TEMPLATE_ID;
    additionalModelFactoryRegistry.register(templateId, new DbVirtueFlawModelFactory());
    IRegistry<String, IAdditionalViewFactory> additionalViewFactoryRegistry = characterGenerics.getAdditionalViewFactoryRegistry();
    additionalViewFactoryRegistry.register(templateId, new DbVirtueFlawViewFactory());
    IRegistry<String, IAdditionalPersisterFactory> persisterFactory = characterGenerics.getAdditonalPersisterFactoryRegistry();
    persisterFactory.register(templateId, new DefaultVirtueFlawPersisterFactory());
  }

  @Override
  protected ICharacterType getType() {
	  return type;
  }
}
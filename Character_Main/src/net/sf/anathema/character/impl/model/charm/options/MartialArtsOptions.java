package net.sf.anathema.character.impl.model.charm.options;

import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterModelContext;
import net.sf.anathema.character.generic.impl.magic.charm.MartialArtsCharmTree;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.charms.ICharmGroup;
import net.sf.anathema.character.generic.magic.charms.ICharmIdMap;
import net.sf.anathema.character.generic.magic.charms.ICharmLearnableArbitrator;
import net.sf.anathema.character.generic.template.HeroTemplate;
import net.sf.anathema.character.generic.template.ITemplateRegistry;
import net.sf.anathema.character.generic.template.ITemplateType;
import net.sf.anathema.character.generic.template.magic.ICharmTemplate;
import net.sf.anathema.character.generic.template.magic.IMagicTemplate;
import net.sf.anathema.character.generic.template.magic.MartialArtsRules;

import static net.sf.anathema.character.generic.impl.magic.MartialArtsUtilities.isMartialArtsCharm;

public class MartialArtsOptions implements ICharmIdMap, ICharmLearnableArbitrator {

  private final MartialArtsCharmTree martialArtsCharmTree;
  private final ICharacterModelContext context;
  private ITemplateRegistry registry;

  public MartialArtsOptions(ICharacterModelContext context, ITemplateRegistry registry) {
    this.context = context;
    this.registry = registry;
    this.martialArtsCharmTree = new MartialArtsCharmTree(getNativeCharmTemplate());
  }

  private ICharmTemplate getNativeCharmTemplate() {
    IBasicCharacterData basicCharacterContext = context.getBasicCharacterContext();
    ITemplateType templateType = basicCharacterContext.getTemplateType();
    HeroTemplate template = registry.getTemplate(templateType);
    IMagicTemplate magicTemplate = template.getMagicTemplate();
    return magicTemplate.getCharmTemplate();
  }

  @Override
  public ICharm getCharmById(String charmId) {
    return martialArtsCharmTree.getCharmById(charmId);
  }

  public ICharmGroup[] getAllCharmGroups() {
    return martialArtsCharmTree.getAllCharmGroups();
  }

  @Override
  public boolean isLearnable(ICharm charm) {
    return !isMartialArtsCharm(charm) || martialArtsCharmTree.isLearnable(charm);
  }

  public MartialArtsRules getMartialArtsRulesForCharacterType() {
    return getNativeCharmTemplate().getMartialArtsRules();
  }
}
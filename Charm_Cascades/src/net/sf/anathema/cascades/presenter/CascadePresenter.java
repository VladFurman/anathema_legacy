package net.sf.anathema.cascades.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.anathema.cascades.module.ICascadeViewFactory;
import net.sf.anathema.cascades.presenter.view.ICascadeView;
import net.sf.anathema.character.generic.framework.configuration.AnathemaCharacterPreferences;
import net.sf.anathema.character.generic.impl.magic.charm.CharmTree;
import net.sf.anathema.character.generic.impl.magic.charm.MartialArtsCharmTree;
import net.sf.anathema.character.generic.impl.rules.ExaltedEdition;
import net.sf.anathema.character.generic.impl.rules.ExaltedRuleSet;
import net.sf.anathema.character.generic.magic.charms.ICharmGroup;
import net.sf.anathema.character.generic.magic.charms.ICharmTree;
import net.sf.anathema.character.generic.rules.IExaltedEdition;
import net.sf.anathema.character.generic.rules.IExaltedRuleSet;
import net.sf.anathema.character.generic.template.ICharacterTemplate;
import net.sf.anathema.character.generic.template.ITemplateRegistry;
import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.charmtree.presenter.AbstractCascadeSelectionPresenter;
import net.sf.anathema.charmtree.presenter.view.IDocumentLoadedListener;
import net.sf.anathema.charmtree.presenter.view.IExaltTypeChangedListener;
import net.sf.anathema.framework.view.IdentificateSelectCellRenderer;
import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;
import net.sf.anathema.lib.gui.widgets.ChangeableJComboBox;
import net.sf.anathema.lib.gui.widgets.IChangeableJComboBox;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.util.IIdentificate;

public class CascadePresenter extends AbstractCascadeSelectionPresenter implements ICascadePresenter {

  private CascadeCharmGroupChangeListener selectionListener;
  private IExaltedRuleSet selectedRuleset;
  private IIdentificate selectedType;
  private final Map<IExaltedRuleSet, CharmTreeIdentificateMap> charmMapsByRules = new HashMap<IExaltedRuleSet, CharmTreeIdentificateMap>();
  private final CascadeCharmTreeViewProperties viewProperties;
  private final ICascadeView view;

  public CascadePresenter(IResources resources, ITemplateRegistry templateRegistry, ICascadeViewFactory factory) {
    super(resources, templateRegistry);
    this.viewProperties = new CascadeCharmTreeViewProperties(resources);
    this.view = factory.createCascadeView(viewProperties);
  }

  public void initPresentation() {
    this.selectionListener = new CascadeCharmGroupChangeListener(view, viewProperties, this, getTemplateRegistry());
    for (IExaltedRuleSet ruleSet : ExaltedRuleSet.values()) {
      charmMapsByRules.put(ruleSet, new CharmTreeIdentificateMap());
    }
    List<IIdentificate> supportedCharmTypes = new ArrayList<IIdentificate>();
    List<ICharmGroup> allCharmGroups = new ArrayList<ICharmGroup>();
    initCharacterTypeCharms(supportedCharmTypes, allCharmGroups);
    initMartialArts(allCharmGroups, CharacterType.SIDEREAL, ExaltedEdition.FirstEdition);
    initMartialArts(allCharmGroups, CharacterType.SOLAR, ExaltedEdition.SecondEdition);
    supportedCharmTypes.add(MARTIAL_ARTS);
    createCharmTypeSelector(
        supportedCharmTypes.toArray(new IIdentificate[supportedCharmTypes.size()]),
        view,
        "CharmTreeView.GUI.CharmType"); //$NON-NLS-1$
    createCharmGroupSelector(view, selectionListener, allCharmGroups.toArray(new ICharmGroup[allCharmGroups.size()]));
    initRules();
    initCharmTypeSelectionListening();
    view.addDocumentLoadedListener(new IDocumentLoadedListener() {
      public void documentLoaded() {
        selectionListener.updateColors();
      }
    });
  }

  private void initCharacterTypeCharms(List<IIdentificate> supportedCharmTypes, List<ICharmGroup> allCharmGroups) {
    for (CharacterType type : CharacterType.getAllCharacterTypes()) {
      for (IExaltedEdition edition : ExaltedEdition.values()) {
        ICharacterTemplate defaultTemplate = getTemplateRegistry().getDefaultTemplate(type, edition);
        if (defaultTemplate == null) {
          continue;
        }
        if (defaultTemplate.getMagicTemplate().getCharmTemplate().knowsCharms(edition.getDefaultRuleset())) {
          for (IExaltedRuleSet ruleSet : ExaltedRuleSet.getRuleSetsByEdition(edition)) {
            CharmTree charmTree = new CharmTree(defaultTemplate.getMagicTemplate().getCharmTemplate(), ruleSet);
            getCharmTreeMap(ruleSet).put(type, charmTree);
            allCharmGroups.addAll(Arrays.asList(charmTree.getAllCharmGroups()));
          }
          supportedCharmTypes.add(type);
        }
      }
    }
  }

  private void initMartialArts(List<ICharmGroup> allCharmGroups, CharacterType type, ExaltedEdition rules) {
    ICharacterTemplate template = getTemplateRegistry().getDefaultTemplate(type, rules);
    for (IExaltedRuleSet ruleSet : ExaltedRuleSet.getRuleSetsByEdition(rules)) {
      ICharmTree martialArtsTree = new MartialArtsCharmTree(template.getMagicTemplate().getCharmTemplate(), ruleSet);
      getCharmTreeMap(ruleSet).put(MARTIAL_ARTS, martialArtsTree);
      allCharmGroups.addAll(Arrays.asList(martialArtsTree.getAllCharmGroups()));
    }
  }

  private void initRules() {
    IChangeableJComboBox<IExaltedRuleSet> rulesComboBox = new ChangeableJComboBox<IExaltedRuleSet>(
        ExaltedRuleSet.values(),
        false);
    rulesComboBox.setRenderer(new IdentificateSelectCellRenderer("Ruleset.", getResources())); //$NON-NLS-1$
    view.addRuleSetComponent(rulesComboBox.getComponent(), getResources().getString("CharmCascades.RuleSetBox.Title")); //$NON-NLS-1$
    rulesComboBox.addObjectSelectionChangedListener(new IObjectValueChangedListener<IExaltedRuleSet>() {
      public void valueChanged(IExaltedRuleSet newValue) {
        IExaltedEdition currentEdition = null;
        if (selectedRuleset != null) {
          currentEdition = selectedRuleset.getEdition();
        }
        selectedRuleset = newValue;
        viewProperties.setCharmTree(getCharmTree(selectedType));
        if (selectedRuleset.getEdition() == currentEdition) {
          return;
        }
        selectionListener.setEdition(selectedRuleset.getEdition());
        final IIdentificate[] cascadeTypes = getCharmTreeMap(selectedRuleset).keySet().toArray(new IIdentificate[0]);
        Arrays.sort(cascadeTypes, new Comparator<IIdentificate>() {
          public int compare(IIdentificate o1, IIdentificate o2) {
            final boolean firstCharacterType = o1 instanceof CharacterType;
            final boolean secondCharacterType = o2 instanceof CharacterType;
            if (firstCharacterType) {
              if (secondCharacterType) {
                return ((CharacterType) o1).compareTo((CharacterType) o2);
              }
              return -1;
            }
            if (secondCharacterType) {
              return 1;
            }
            return 0;
          }
        });
        view.fillCharmTypeBox(cascadeTypes);
        view.unselect();
        view.fillCharmGroupBox(new IIdentificate[0]);
      }
    });
    rulesComboBox.setSelectedObject(AnathemaCharacterPreferences.getDefaultPreferences().getPreferredRuleset());
  }

  private CharmTreeIdentificateMap getCharmTreeMap(IExaltedRuleSet ruleSet) {
    return charmMapsByRules.get(ruleSet);
  }

  private void initCharmTypeSelectionListening() {
    view.addCharmTypeSelectionListener(new IExaltTypeChangedListener() {
      public void valueChanged(Object cascadeType) {
        handleTypeSelectionChange(cascadeType);
      }
    });
  }

  private void handleTypeSelectionChange(Object cascadeType) {
    this.selectedType = (IIdentificate) cascadeType;
    if (cascadeType == null) {
      view.fillCharmGroupBox(new IIdentificate[0]);
      return;
    }
    CharmTreeIdentificateMap charmTreeMap = getCharmTreeMap(selectedRuleset);
    final ICharmTree charmTree = charmTreeMap.get(selectedType);
    if (charmTree == null) {
      view.fillCharmGroupBox(new IIdentificate[0]);
      return;
    }
    ICharmGroup[] allCharmGroups = charmTree.getAllCharmGroups();
    view.fillCharmGroupBox(sortCharmGroups(allCharmGroups));
  }

  public ICharmTree getCharmTree(IIdentificate type) {
    return getCharmTreeMap(selectedRuleset).get(type);
  }
}
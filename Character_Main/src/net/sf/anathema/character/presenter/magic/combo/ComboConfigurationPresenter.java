package net.sf.anathema.character.presenter.magic.combo;

import com.google.common.base.Strings;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.DedicatedCharacterChangeAdapter;
import net.sf.anathema.character.generic.framework.magic.MagicDisplayLabeler;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.model.charm.CharmLearnAdapter;
import net.sf.anathema.character.model.charm.ICharmConfiguration;
import net.sf.anathema.character.model.charm.ICharmLearnListener;
import net.sf.anathema.character.model.charm.ICombo;
import net.sf.anathema.character.model.charm.IComboConfiguration;
import net.sf.anathema.character.model.charm.IComboConfigurationListener;
import net.sf.anathema.character.model.charm.ILearningCharmGroup;
import net.sf.anathema.character.presenter.magic.IContentPresenter;
import net.sf.anathema.character.view.magic.IComboConfigurationView;
import net.sf.anathema.character.view.magic.IComboView;
import net.sf.anathema.character.view.magic.IComboViewListener;
import net.sf.anathema.character.view.magic.IMagicViewFactory;
import net.sf.anathema.framework.presenter.resources.BasicUi;
import net.sf.anathema.framework.presenter.view.ContentView;
import net.sf.anathema.framework.presenter.view.SimpleViewContentView;
import net.sf.anathema.framework.view.util.ContentProperties;
import net.sf.anathema.interaction.Command;
import net.sf.anathema.interaction.Tool;
import net.sf.anathema.lib.compare.I18nedIdentificateComparator;
import net.sf.anathema.lib.control.IChangeListener;
import net.sf.anathema.lib.resources.Resources;
import net.sf.anathema.lib.workflow.textualdescription.ITextView;
import net.sf.anathema.lib.workflow.textualdescription.TextualPresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ComboConfigurationPresenter implements IContentPresenter {

  private final ICharmConfiguration charmConfiguration;
  private final IComboConfiguration comboConfiguration;
  private final Map<ICombo, IComboView> viewsByCombo = new HashMap<>();
  private final Map<ICombo, Tool> toolsByCombo = new HashMap<>();
  private final ComboConfigurationModel comboModel;
  private final Resources resources;
  private final IComboConfigurationView view;
  private final MagicDisplayLabeler labeler;

  public ComboConfigurationPresenter(Resources resources, ComboConfigurationModel comboModel,
                                     IMagicViewFactory factory) {
    this.resources = resources;
    this.comboModel = comboModel;
    this.charmConfiguration = comboModel.getCharmConfiguration();
    this.comboConfiguration = comboModel.getCombos();
    this.labeler = new MagicDisplayLabeler(resources);
    this.view = factory.createCharmComboView();
  }

  @Override
  public void initPresentation() {
    view.initGui(new ComboViewProperties(resources, comboConfiguration, comboModel.getMagicDescriptionProvider()));
    initCharmLearnListening(view);
    ITextView nameView = view.addComboNameView(
            resources.getString("CardView.CharmConfiguration.ComboCreation.NameLabel"));
    ICombo editCombo = comboConfiguration.getEditCombo();
    TextualPresentation textualPresentation = new TextualPresentation();
    textualPresentation.initView(nameView, editCombo.getName());
    ITextView descriptionView = view.addComboDescriptionView(
            resources.getString("CardView.CharmConfiguration.ComboCreation.DescriptionLabel"));
    textualPresentation.initView(descriptionView, editCombo.getDescription());
    updateCharmListsInView(view);
    initViewListening(view);
    initComboModelListening(view);
    initComboConfigurationListening(view);
    comboModel.addCharacterChangeListener(new DedicatedCharacterChangeAdapter() {
      @Override
      public void casteChanged() {
        enableCrossPrerequisiteTypeCombos();
      }
    });
    enableCrossPrerequisiteTypeCombos();
  }

  @Override
  public ContentView getTabContent() {
    String header = resources.getString("CardView.CharmConfiguration.ComboCreation.Title");
    return new SimpleViewContentView(new ContentProperties(header), view);
  }

  private void enableCrossPrerequisiteTypeCombos() {
    boolean alienCharms = comboModel.isAlienCharmsAllowed();
    comboConfiguration.setCrossPrerequisiteTypeComboAllowed(alienCharms);
  }

  private void initComboConfigurationListening(final IComboConfigurationView comboView) {
    comboConfiguration.addComboConfigurationListener(new IComboConfigurationListener() {
      @Override
      public void comboAdded(ICombo combo) {
        addComboToView(comboView, combo);
      }

      @Override
      public void comboChanged(ICombo combo) {
        viewsByCombo.get(combo).updateCombo(createComboNameString(combo), convertToHtml(combo));
      }

      @Override
      public void comboDeleted(ICombo combo) {
        view.deleteView(viewsByCombo.get(combo));
      }

      @Override
      public void editBegun(ICombo combo) {
        setViewsToNotEditing();
        setViewToEditing(combo);
        comboView.setEditState(true);
      }

      @Override
      public void editEnded() {
        setViewsToNotEditing();
        comboView.setEditState(false);
      }

    });
    for (ICombo combo : comboConfiguration.getAllCombos()) {
      addComboToView(comboView, combo);
    }
  }

  private String createComboNameString(ICombo combo) {
    String comboName = combo.getName().getText();
    if (Strings.isNullOrEmpty(comboName)) {
      comboName = resources.getString("CardView.CharmConfiguration.ComboCreation.UnnamedCombo");
    }
    return comboName;
  }

  private void initCharmLearnListening(final IComboConfigurationView comboView) {
    ICharmLearnListener charmLearnListener = new CharmLearnAdapter() {
      @Override
      public void charmLearned(ICharm charm) {
        updateCharmListsInView(comboView);
      }

      @Override
      public void charmForgotten(ICharm charm) {
        updateCharmListsInView(comboView);
      }
    };
    for (ILearningCharmGroup group : charmConfiguration.getAllGroups()) {
      group.addCharmLearnListener(charmLearnListener);
    }
  }

  private void addComboToView(IComboConfigurationView comboConfigurationView, final ICombo combo) {
    IComboView comboView = comboConfigurationView.addComboView(createComboNameString(combo), convertToHtml(combo));
    Tool editTool = comboView.addTool();
    editTool.setIcon(new BasicUi().getEditIconPath());
    editTool.setText(resources.getString("CardView.CharmConfiguration.ComboCreation.EditLabel"));
    editTool.setCommand(new Command() {
      @Override
      public void execute() {
        comboConfiguration.beginComboEdit(combo);
      }
    });
    Tool deleteTool = comboView.addTool();
    deleteTool.setIcon(new BasicUi().getClearIconPath());
    deleteTool.setText(resources.getString("CardView.CharmConfiguration.ComboCreation.DeleteLabel"));
    deleteTool.setCommand(new Command() {
      @Override
      public void execute() {
        comboConfiguration.deleteCombo(combo);
      }
    });
    viewsByCombo.put(combo, comboView);
    toolsByCombo.put(combo, editTool);
  }

  private String convertToHtml(ICombo combo) {
    String text = combo.getDescription().getText();
    ICharm[] charms = combo.getCharms();
    String charmList = "<b>";
    Iterator<ICharm> charmIterator = Arrays.asList(charms).iterator();
    if (charmIterator.hasNext()) {
      charmList = charmList.concat(labeler.getLabelForMagic(charmIterator.next()));
    }
    while (charmIterator.hasNext()) {
      charmList = charmList.concat(", " + labeler.getLabelForMagic(charmIterator.next()));
    }
    charmList += "</b>";
    if (Strings.isNullOrEmpty(text)) {
      return wrapHtml(charmList);
    }
    String converted = text.replace("\n", "<br>");
    return wrapHtml(charmList + " - <i>" + converted + "</i>");
  }

  private String wrapHtml(String text) {
    return "<html><body>" + text + "</body></html>";
  }

  private void updateCharmListsInView(IComboConfigurationView comboView) {
    comboView.setComboCharms(comboConfiguration.getEditCombo().getCharms());
    ICharm[] learnedCharms = comboModel.getLearnedCharms();
    Arrays.sort(learnedCharms, new I18nedIdentificateComparator(resources));
    comboView.setAllCharms(learnedCharms);
  }

  private void initComboModelListening(final IComboConfigurationView comboView) {
    comboConfiguration.addComboModelListener(new IChangeListener() {
      @Override
      public void changeOccurred() {
        updateCharmListsInView(comboView);
      }
    });
  }

  private void initViewListening(IComboConfigurationView comboView) {
    comboView.addComboViewListener(new IComboViewListener() {
      @Override
      public void charmAdded(Object addedCharm) {
        comboConfiguration.addCharmToCombo((ICharm) addedCharm, comboModel.isExperienced());
      }

      @Override
      public void charmRemoved(Object[] removedCharms) {
        List<ICharm> removedCharmList = new ArrayList<>();
        for (Object charmObject : removedCharms) {
          removedCharmList.add((ICharm) charmObject);
        }
        comboConfiguration.removeCharmsFromCombo(removedCharmList.toArray(new ICharm[removedCharmList.size()]));
      }

      @Override
      public void comboFinalized() {
        comboConfiguration.finalizeCombo();
      }

      @Override
      public void comboCleared() {
        comboConfiguration.clearCombo();
      }
    });
  }

  private void setViewToEditing(ICombo combo) {
    IComboView comboView = viewsByCombo.get(combo);
    createComboNameString(combo);
    comboView.updateCombo(createComboNameString(combo) + " (" + resources.getString(
            "CardView.CharmConfiguration.ComboCreation.EditingLabel") + ")", convertToHtml(combo));
    toolsByCombo.get(combo).setText(resources.getString("CardView.CharmConfiguration.ComboCreation.RestartEditLabel"));
  }

  private void setViewsToNotEditing() {
    for (ICombo currentCombo : viewsByCombo.keySet()) {
      IComboView comboView = viewsByCombo.get(currentCombo);
      comboView.updateCombo(createComboNameString(currentCombo), convertToHtml(currentCombo));
      toolsByCombo.get(currentCombo).setText(
              resources.getString("CardView.CharmConfiguration.ComboCreation.EditLabel"));
    }
  }
}
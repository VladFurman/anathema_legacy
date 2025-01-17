package net.sf.anathema.character.equipment.character;

import net.sf.anathema.character.equipment.MaterialComposition;
import net.sf.anathema.character.equipment.character.model.IEquipmentItem;
import net.sf.anathema.character.equipment.character.model.IEquipmentStatsOption;
import net.sf.anathema.character.equipment.character.view.IEquipmentObjectView;
import net.sf.anathema.character.generic.equipment.ArtifactAttuneType;
import net.sf.anathema.character.generic.equipment.ArtifactStats;
import net.sf.anathema.character.generic.equipment.weapon.IEquipmentStats;
import net.sf.anathema.character.generic.equipment.weapon.IWeaponStats;
import net.sf.anathema.character.generic.traits.INamedGenericTrait;
import net.sf.anathema.interaction.Tool;
import net.sf.anathema.lib.control.IChangeListener;
import net.sf.anathema.lib.gui.Presenter;
import net.sf.anathema.lib.model.BooleanModel;
import net.sf.anathema.lib.resources.Resources;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class EquipmentObjectPresenter implements Presenter {

  public static final String EQUIPMENT_NAME_PREFIX = "Equipment.Name.";
  private static final String DESCRIPTION_PREFIX = "Equipment.Description.";
  private final Map<IEquipmentStats, BooleanModel> attuneStatFlags = new HashMap<>();
  private final Map<IEquipmentStats, BooleanModel> otherStatFlags = new HashMap<>();
  private final IEquipmentItem model;
  private final IEquipmentObjectView view;
  private final IEquipmentStringBuilder stringBuilder;
  private final IEquipmentCharacterOptionProvider characterOptionProvider;
  private final IEquipmentCharacterDataProvider dataProvider;
  private final Resources resources;

  public EquipmentObjectPresenter(IEquipmentItem model, IEquipmentObjectView view,
                                  IEquipmentStringBuilder stringBuilder, IEquipmentCharacterDataProvider dataProvider,
                                  IEquipmentCharacterOptionProvider characterOptionProvider, Resources resources) {
    this.model = model;
    this.view = view;
    this.stringBuilder = stringBuilder;
    this.characterOptionProvider = characterOptionProvider;
    this.resources = resources;
    this.dataProvider = dataProvider;
  }

  @Override
  public void initPresentation() {
    String itemTitle = model.getTitle();
    boolean customTitle = !model.getTemplateId().equals(itemTitle);
    if (resources.supportsKey(EQUIPMENT_NAME_PREFIX + itemTitle)) {
      itemTitle = resources.getString(EQUIPMENT_NAME_PREFIX + itemTitle);
    }
    if (!customTitle && model.getMaterialComposition() == MaterialComposition.Variable) {
      String materialString = resources.getString("MagicMaterial." + model.getMaterial().name());
      itemTitle += " (" + materialString + ")";
    }
    view.setItemTitle(itemTitle);
    String description = model.getDescription();
    if (resources.supportsKey(DESCRIPTION_PREFIX + description)) {
      description = resources.getString(DESCRIPTION_PREFIX + description);
    }
    if (description != null) {
      view.setItemDescription(description);
    }

    prepareContents();
  }

  private void prepareContents() {
    view.clearContents();
    attuneStatFlags.clear();
    otherStatFlags.clear();

    boolean isRequireAttuneArtifact = false;
    boolean isAttuned = false;
    for (final IEquipmentStats equipment : model.getStats()) {
      if (equipment instanceof ArtifactStats)
        isRequireAttuneArtifact = isRequireAttuneArtifact || ((ArtifactStats) equipment).requireAttunementToUse();
      if (!viewFilter(equipment)) continue;
      final BooleanModel booleanModel = view.addStats(createEquipmentDescription(model, equipment));
      if (equipment instanceof ArtifactStats) {
        attuneStatFlags.put(equipment, booleanModel);
        if (model.isPrintEnabled(equipment)) {
          isAttuned = true;
        }
      } else {
        otherStatFlags.put(equipment, booleanModel);
      }
      booleanModel.setValue(model.isPrintEnabled(equipment));
      booleanModel.addChangeListener(new IChangeListener() {
        @Override
        public void changeOccurred() {
          model.setPrintEnabled(equipment, booleanModel.getValue());
          if (equipment instanceof ArtifactStats) {
            // if we are enabling an attunement stats ...
            if (booleanModel.getValue()) {
              // disable all other attunement stats
              for (IEquipmentStats stats : attuneStatFlags.keySet()) {
                if (!equipment.equals(stats) && model.isPrintEnabled(stats)) {
                  model.setPrintEnabled(stats, false);
                }
              }
            }
            prepareContents();
          }
        }
      });

      addOptionalModels(booleanModel, equipment);
    }
    disableAllStatsIfAttunementRequiredButNotGiven(isRequireAttuneArtifact, isAttuned);
  }

  private void disableAllStatsIfAttunementRequiredButNotGiven(boolean requireAttuneArtifact, boolean attuned) {
    if (requireAttuneArtifact && !attuned) {
      for (BooleanModel bool : otherStatFlags.values()) {
        view.setEnabled(bool, false);
        bool.setValue(false);
      }
    }
  }

  private void addOptionalModels(BooleanModel baseModel, IEquipmentStats stats) {
    if (stats instanceof IWeaponStats) {
      IWeaponStats weaponStats = (IWeaponStats) stats;
      INamedGenericTrait[] specialties = dataProvider.getSpecialties(((IWeaponStats) stats).getTraitType());
      for (INamedGenericTrait specialty : specialties) {
        String label = MessageFormat.format(resources.getString("Equipment.Specialty"), specialty.getName());
        final BooleanModel booleanModel = view.addOptionFlag(baseModel, label);
        final IEquipmentStatsOption specialtyOption = new EquipmentSpecialtyOption(specialty,
                weaponStats.getTraitType());
        final IEquipmentStats baseStat = model.getStat(stats.getId());
        booleanModel.setValue(characterOptionProvider.isStatOptionEnabled(model, baseStat, specialtyOption));
        booleanModel.addChangeListener(new IChangeListener() {
          @Override
          public void changeOccurred() {
            if (booleanModel.getValue()) characterOptionProvider.enableStatOption(model, baseStat, specialtyOption);
            else characterOptionProvider.disableStatOption(model, baseStat, specialtyOption);
          }
        });
      }
    }
  }

  private boolean viewFilter(IEquipmentStats equipment) {
    boolean match;
    if (equipment instanceof ArtifactStats) {
      ArtifactStats stats = (ArtifactStats) equipment;
      match = false;
      if (dataProvider.getAttuneTypes(model) != null) for (ArtifactAttuneType type : dataProvider.getAttuneTypes(model))
        if (stats.getAttuneType() == type) match = true;
      if (!match) return false;
    }
    return true;
  }

  private String createEquipmentDescription(IEquipmentItem item, IEquipmentStats equipment) {
    return stringBuilder.createString(item, equipment);
  }

  public Tool addContextTool() {
    return view.addAction();
  }
}

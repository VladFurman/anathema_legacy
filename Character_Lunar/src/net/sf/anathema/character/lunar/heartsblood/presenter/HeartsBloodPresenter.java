package net.sf.anathema.character.lunar.heartsblood.presenter;

import net.sf.anathema.character.generic.framework.additionaltemplate.listening.DedicatedCharacterChangeAdapter;
import net.sf.anathema.character.library.removableentry.presenter.IRemovableEntryListener;
import net.sf.anathema.character.library.removableentry.presenter.IRemovableEntryView;
import net.sf.anathema.character.lunar.heartsblood.view.HeartsBloodView;
import net.sf.anathema.character.lunar.heartsblood.view.IAnimalFormSelectionView;
import net.sf.anathema.framework.presenter.resources.BasicUi;
import net.sf.anathema.interaction.Command;
import net.sf.anathema.lib.control.IIntValueChangedListener;
import net.sf.anathema.lib.control.ObjectValueListener;
import net.sf.anathema.lib.gui.Presenter;
import net.sf.anathema.lib.resources.Resources;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class HeartsBloodPresenter implements Presenter {

  private final IHeartsBloodModel model;
  private final HeartsBloodView view;
  private final Resources resources;
  private final Map<IAnimalForm, IRemovableEntryView> viewsByForm = new HashMap<>();

  public HeartsBloodPresenter(IHeartsBloodModel model, HeartsBloodView view, Resources resources) {
    this.model = model;
    this.view = view;
    this.resources = resources;
  }

  @Override
  public void initPresentation() {
    String animalFormString = resources.getString("Lunar.HeartsBlood.AnimalForm");
    String animalStaminaString = resources.getString("Lunar.HeartsBlood.AnimalStamina");
    String animalDexterityString = resources.getString("Lunar.HeartsBlood.AnimalDexterity");
    String animalStrengthString = resources.getString("Lunar.HeartsBlood.AnimalStrength");
    String animalAppearanceString = resources.getString("Lunar.HeartsBlood.AnimalAppearance");
    BasicUi basicUi = new BasicUi();
    IAnimalFormSelectionView selectionView = view.createAnimalFormSelectionView(basicUi.getAddIcon(), animalFormString,
            animalStrengthString, animalDexterityString, animalStaminaString, animalAppearanceString);
    initSelectionViewListening(selectionView);
    initModelListening(basicUi, selectionView);
    for (IAnimalForm form : model.getEntries()) {
      addAnimalFormView(basicUi, form);
    }
    reset(selectionView);
  }

  private void initSelectionViewListening(IAnimalFormSelectionView selectionView) {
    selectionView.addNameListener(new ObjectValueListener<String>() {
      @Override
      public void valueChanged(String newValue) {
        model.setCurrentName(newValue);
      }
    });
    selectionView.addStrengthListener(new IIntValueChangedListener() {
      @Override
      public void valueChanged(int newValue) {
        model.setCurrentStrength(newValue);
      }
    });
    selectionView.addDexterityListener(new IIntValueChangedListener() {
      @Override
      public void valueChanged(int newValue) {
        model.setCurrentDexterity(newValue);
      }
    });
    selectionView.addStaminaListener(new IIntValueChangedListener() {
      @Override
      public void valueChanged(int newValue) {
        model.setCurrentStamina(newValue);
      }
    });
    selectionView.addAppearanceListener(new IIntValueChangedListener() {
      @Override
      public void valueChanged(int newValue) {
        model.setCurrentAppearance(newValue);
      }
    });
    selectionView.addAddButtonListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        model.commitSelection();
      }
    });
  }

  private void addAnimalFormView(BasicUi basicUi, final IAnimalForm form) {
    IRemovableEntryView formView = view.addEntryView(basicUi.getRemoveIconPath(), null, form.getName() +
            (" (" + form.getStrength() + "/" + form.getDexterity() + "/" + form.getStamina() +
                    "/" + form.getAppearance()) + ")");
    viewsByForm.put(form, formView);
    formView.addButtonListener(new Command() {
      @Override
      public void execute() {
        model.removeEntry(form);
      }
    });
  }

  private void initModelListening(final BasicUi basicUi, final IAnimalFormSelectionView selectionView) {
    model.addModelChangeListener(new IRemovableEntryListener<IAnimalForm>() {
      @Override
      public void entryAdded(IAnimalForm form) {
        addAnimalFormView(basicUi, form);
        reset(selectionView);
      }

      @Override
      public void entryRemoved(IAnimalForm form) {
        IRemovableEntryView removableView = viewsByForm.get(form);
        view.removeEntryView(removableView);
      }

      @Override
      public void entryAllowed(boolean complete) {
        selectionView.setAddButtonEnabled(complete);
      }
    });
    model.addCharacterChangeListener(new DedicatedCharacterChangeAdapter() {
      @Override
      public void experiencedChanged(boolean experienced) {
        for (IAnimalForm form : viewsByForm.keySet()) {
          if (form.isCreationLearned()) {
            IRemovableEntryView formView = viewsByForm.get(form);
            formView.setButtonEnabled(!experienced);
          }
        }
      }
    });
  }

  private void reset(IAnimalFormSelectionView selectionView) {
    selectionView.setName(null);
    selectionView.setStrength(1);
    selectionView.setDexterity(1);
    selectionView.setStamina(1);
    selectionView.setAppearance(1);
    model.setCurrentName(null);
    model.setCurrentStamina(1);
    model.setCurrentDexterity(1);
    model.setCurrentStrength(1);
    model.setCurrentAppearance(1);
  }
}

package net.sf.anathema.character.craft.presenter;

import net.sf.anathema.character.generic.framework.additionaltemplate.listening.DedicatedCharacterChangeAdapter;
import net.sf.anathema.character.library.intvalue.IRemovableTraitView;
import net.sf.anathema.character.library.selection.AbstractStringEntryTraitPresenter;
import net.sf.anathema.character.library.selection.IRemovableStringEntriesView;
import net.sf.anathema.character.library.selection.IStringSelectionView;
import net.sf.anathema.character.library.trait.presenter.TraitPresenter;
import net.sf.anathema.character.library.trait.subtrait.ISubTrait;
import net.sf.anathema.character.library.trait.view.SimpleTraitView;
import net.sf.anathema.framework.presenter.resources.BasicUi;
import net.sf.anathema.interaction.Command;
import net.sf.anathema.lib.gui.Presenter;
import net.sf.anathema.lib.resources.Resources;

public class CraftPresenter extends AbstractStringEntryTraitPresenter<ISubTrait> implements Presenter {

  private final ICraftModel model;
  private final IRemovableStringEntriesView<SimpleTraitView> view;
  private final Resources resources;

  public CraftPresenter(ICraftModel model, IRemovableStringEntriesView<SimpleTraitView> view, Resources resources) {
    super(model, view);
    this.model = model;
    this.view = view;
    this.resources = resources;
  }

  @Override
  public void initPresentation() {
    String labelText = resources.getString("Crafts.SelectionLabel");
    BasicUi basicUi = new BasicUi();
    IStringSelectionView selectionView = view.addSelectionView(labelText, basicUi.getAddIconPath());
    initSelectionViewListening(selectionView);
    initModelListening(basicUi, selectionView);
    for (ISubTrait craft : model.getEntries()) {
      if (!model.isRemovable(craft)) {
        addFixedSubView(basicUi, craft);
      }
      else {
        addSubView(basicUi, craft);
      }
    }
    reset(selectionView);
    initButtons(model.isExperienced());
  }

  @Override
  protected void initModelListening(BasicUi basicUi, IStringSelectionView selectionView) {
    super.initModelListening(basicUi, selectionView);
    model.addCharacterChangeListener(new DedicatedCharacterChangeAdapter() {
      @Override
      public void experiencedChanged(boolean experienced) {
        initButtons(experienced);
      }
    });
  }

  private void initButtons(boolean experienced) {
    for (ISubTrait trait : model.getEntries()) {
      if (!model.isRemovable(trait)) {
        continue;
      }
      IRemovableTraitView< ? > subView = getSubView(trait);
      if (trait.getCreationValue() > 0) {
        subView.setButtonEnabled(!experienced);
      }
    }
  }

  private void addFixedSubView(BasicUi basicUi, ISubTrait craft) {
    IRemovableTraitView< ? > subView = createSubView(basicUi, craft, resources.getString("Craft." + craft.getName()));
    subView.setButtonEnabled(false);
    addSubView(craft, subView);
  }

  @Override
  protected IRemovableTraitView< ? > createSubView(BasicUi basicUi, ISubTrait craft) {
    return createSubView(basicUi, craft, craft.getName());
  }

  private IRemovableTraitView< ? > createSubView(BasicUi basicUi, final ISubTrait craft, String name) {
    IRemovableTraitView< ? > craftView = view.addEntryView(basicUi.getRemoveIconPath(), craft, name);
    craftView.setValue(craft.getCurrentValue());
    new TraitPresenter(craft, craftView).initPresentation();
    craftView.addButtonListener(new Command() {
      @Override
      public void execute() {
        model.removeEntry(craft);
      }
    });
    return craftView;
  }
}

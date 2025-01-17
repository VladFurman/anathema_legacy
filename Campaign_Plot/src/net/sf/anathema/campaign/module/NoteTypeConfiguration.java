package net.sf.anathema.campaign.module;

import net.sf.anathema.campaign.note.model.IBasicItemData;
import net.sf.anathema.campaign.note.persistence.BasicDataItemPersister;
import net.sf.anathema.campaign.note.view.NoteView;
import net.sf.anathema.campaign.persistence.ISeriesPersistenceConstants;
import net.sf.anathema.campaign.presenter.NotePresenter;
import net.sf.anathema.campaign.presenter.TextEditorProperties;
import net.sf.anathema.campaign.view.SwingNoteView;
import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.framework.item.IItemType;
import net.sf.anathema.framework.module.AbstractPersistableItemTypeConfiguration;
import net.sf.anathema.framework.persistence.IRepositoryItemPersister;
import net.sf.anathema.framework.presenter.PlotItemViewFactory;
import net.sf.anathema.framework.presenter.view.IItemTypeViewProperties;
import net.sf.anathema.framework.presenter.view.SimpleItemTypeViewProperties;
import net.sf.anathema.framework.repository.IItem;
import net.sf.anathema.framework.repository.ItemType;
import net.sf.anathema.framework.repository.RepositoryConfiguration;
import net.sf.anathema.framework.view.ItemView;
import net.sf.anathema.initialization.ItemTypeConfiguration;
import net.sf.anathema.initialization.reflections.Weight;
import net.sf.anathema.lib.exception.AnathemaException;
import net.sf.anathema.lib.file.RelativePath;
import net.sf.anathema.lib.registry.IRegistry;
import net.sf.anathema.lib.resources.Resources;

@ItemTypeConfiguration
@Weight(weight = 20)
public class NoteTypeConfiguration extends AbstractPersistableItemTypeConfiguration {

  public static final String NOTE_ITEM_TYPE_ID = "Note";
  public static final IItemType ITEM_TYPE = new ItemType(NOTE_ITEM_TYPE_ID, new RepositoryConfiguration(".not", "Notes/"));

  public NoteTypeConfiguration() {
    super(ITEM_TYPE);
  }

  @Override
  protected IRepositoryItemPersister createPersister(IApplicationModel model) {
    return new BasicDataItemPersister(getItemType(), ISeriesPersistenceConstants.TAG_NOTE_ROOT);
  }

  public final void registerViewFactory(IApplicationModel model, Resources resources) {
    IRegistry<IItemType, PlotItemViewFactory> viewFactoryRegistry = PlotExtension.getExtension(model).getViewFactoryRegistry();
    viewFactoryRegistry.register(getItemType(), createItemViewFactory(resources));
  }

  private PlotItemViewFactory createItemViewFactory(final Resources resources) {
    return new PlotItemViewFactory() {
      @Override
      public ItemView createView(IItem item) throws AnathemaException {
        String printName = item.getDisplayName();
        RelativePath icon = new PlotUI().getNoteIconPath();
        NoteView basicItemView = new SwingNoteView(printName, icon, new TextEditorProperties(resources));
        IBasicItemData basicItem = (IBasicItemData) item.getItemData();
        new NotePresenter(basicItemView, resources, basicItem).initPresentation();
        return basicItemView;
      }
    };
  }

  @Override
  protected IItemTypeViewProperties createItemTypeCreationProperties(IApplicationModel anathemaModel, Resources resources) {
    return new SimpleItemTypeViewProperties(getItemType(), new PlotUI().getNoteIconPath());
  }
}
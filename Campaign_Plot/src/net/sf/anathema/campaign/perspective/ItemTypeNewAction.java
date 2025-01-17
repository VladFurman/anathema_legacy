package net.sf.anathema.campaign.perspective;

import net.sf.anathema.campaign.item.PlotItemManagement;
import net.sf.anathema.campaign.module.PlotExtension;
import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.framework.item.IItemType;
import net.sf.anathema.framework.presenter.action.NewItemCommand;
import net.sf.anathema.framework.presenter.resources.PlatformUI;
import net.sf.anathema.lib.resources.Resources;

import javax.swing.Action;
import javax.swing.Icon;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class ItemTypeNewAction extends AbstractItemAction {

  private final IItemType type;

  public static Action[] createToolActions(IApplicationModel model, Resources resources) {
    List<Action> actions = new ArrayList<>();
    for (IItemType type : collectItemTypes(model)) {
      ItemTypeNewAction action = new ItemTypeNewAction(type, model, resources);
      action.setName(resources.getString("ItemType." + type.getId() + ".PrintName"));
      actions.add(action);
    }
    return actions.toArray(new Action[actions.size()]);
  }

  public static String createToolTip(Resources resources) {
    return resources.getString("AnathemaCore.Tools.New.Name");
  }

  public static Icon getButtonIcon() {
    return new PlatformUI().getNewToolBarIcon();
  }

  public ItemTypeNewAction(IItemType type, IApplicationModel model, Resources resources) {
    super(model, resources);
    this.type = type;
  }

  @Override
  protected void execute(Component parentComponent) {
    IApplicationModel anathemaModel = getAnathemaModel();
    PlotItemManagement receiver = PlotExtension.getItemManagement(anathemaModel);
    NewItemCommand command = new NewItemCommand(type, anathemaModel, getResources(), receiver);
    command.execute();
  }
}
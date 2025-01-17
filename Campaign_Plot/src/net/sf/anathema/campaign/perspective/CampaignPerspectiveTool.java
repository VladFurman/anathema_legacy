package net.sf.anathema.campaign.perspective;

import net.sf.anathema.framework.perspective.ToolBar;
import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.campaign.toolbar.AnathemaSaveAction;
import net.sf.anathema.campaign.toolbar.AnathemaSaveAllAction;
import net.sf.anathema.campaign.toolbar.ControlledPrintAction;
import net.sf.anathema.campaign.toolbar.QuickPrintAction;
import net.sf.anathema.framework.reporting.PrintCommand;
import net.sf.anathema.lib.resources.Resources;

import javax.swing.Action;

public class CampaignPerspectiveTool {

  public void add(Resources resources, IApplicationModel model, ToolBar toolbar) {
    addNewAction(resources, model, toolbar);
    addLoadAction(resources, model, toolbar);
    Action save = AnathemaSaveAction.createToolAction(model, resources);
    Action saveAll = AnathemaSaveAllAction.createToolAction(model, resources);
    Action quickPrint = getPrintAction(resources, model);
    toolbar.addTools(save, saveAll, quickPrint);
  }

  private void addLoadAction(Resources resources, IApplicationModel model, ToolBar toolbar) {
    toolbar.addMenu(ItemTypeLoadAction.getButtonIcon(), ItemTypeLoadAction.createToolActions(model, resources),
            ItemTypeLoadAction.createToolTip(resources));
  }

  private void addNewAction(Resources resources, IApplicationModel model, ToolBar toolbar) {
    toolbar.addMenu(ItemTypeNewAction.getButtonIcon(), ItemTypeNewAction.createToolActions(model, resources),
            ItemTypeNewAction.createToolTip(resources));
  }

  private Action getPrintAction(Resources resources, IApplicationModel model) {
    if (PrintCommand.isAutoOpenSupported()) {
      return QuickPrintAction.createToolAction(model, resources);
    } else {
      return ControlledPrintAction.createToolAction(model, resources);
    }
  }
}
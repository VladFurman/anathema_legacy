package net.sf.anathema.campaign.toolbar;

import net.sf.anathema.campaign.item.PlotItemManagement;
import net.sf.anathema.campaign.item.PlotItemManagementListener;
import net.sf.anathema.campaign.module.PlotExtension;
import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.framework.presenter.resources.PlatformUI;
import net.sf.anathema.framework.reporting.QuickPrintCommand;
import net.sf.anathema.framework.repository.IItem;
import net.sf.anathema.lib.gui.action.SmartAction;
import net.sf.anathema.lib.resources.Resources;

import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import static javax.swing.KeyStroke.getKeyStroke;

public class QuickPrintAction extends AbstractPrintAction {

  public static Action createToolAction(IApplicationModel model, Resources resources) {
    SmartAction action = new QuickPrintAction(model, resources);
    action.setToolTipText(resources.getString("Anathema.Reporting.Menu.QuickPrint.Tooltip"));
    action.setIcon(new PlatformUI().getPDFTaskBarIcon());
    return action;
  }

  private QuickPrintAction(IApplicationModel anathemaModel, Resources resources) {
    super(anathemaModel, resources);
  }

  @Override
  protected KeyStroke createKeyStroke() {
    return getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
  }

  @Override
  protected PlotItemManagementListener createEnablingListener() {
    return new PrintEnabledListener(this, anathemaModel.getReportRegistry());
  }

  @Override
  protected void execute(Component parentComponent) {
    PlotItemManagement itemManagement = PlotExtension.getItemManagement(anathemaModel);
    IItem item = itemManagement.getSelectedItem();
    if (item == null) {
      return;
    }
    FirstReportFinder reportFinder = new FirstReportFinder(anathemaModel);
    new QuickPrintCommand(resources, item, reportFinder).execute();
  }
}
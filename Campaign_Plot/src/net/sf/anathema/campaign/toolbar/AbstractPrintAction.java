package net.sf.anathema.campaign.toolbar;

import net.sf.anathema.campaign.item.PlotItemManagement;
import net.sf.anathema.campaign.item.PlotItemManagementListener;
import net.sf.anathema.campaign.module.PlotExtension;
import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.framework.reporting.Report;
import net.sf.anathema.framework.reporting.ReportException;
import net.sf.anathema.framework.repository.IItem;
import net.sf.anathema.lib.gui.action.SmartAction;
import net.sf.anathema.lib.resources.Resources;
import org.apache.commons.io.IOUtils;

import javax.swing.KeyStroke;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractPrintAction extends SmartAction {
  protected final IApplicationModel anathemaModel;
  protected final Resources resources;

  public AbstractPrintAction(IApplicationModel anathemaModel, Resources resources) {
    this.anathemaModel = anathemaModel;
    this.resources = resources;
    setHotKey();
    startEnablingListener();
  }

  private void startEnablingListener() {
    PlotItemManagementListener listener = createEnablingListener();
    PlotExtension.getItemManagement(anathemaModel);
    PlotItemManagement itemManagement = PlotExtension.getItemManagement(anathemaModel);
    itemManagement.addListener(listener);
    listener.itemSelected(itemManagement.getSelectedItem());
  }

  private void setHotKey() {
    setAcceleratorKey(createKeyStroke());
  }

  protected void performPrint(IItem item, Report selectedReport, Path selectedFile) throws IOException, ReportException {
    OutputStream stream = null;
    try {
      stream = Files.newOutputStream(selectedFile);
      selectedReport.print(item, stream);
    } finally {
      IOUtils.closeQuietly(stream);
    }
  }

  protected abstract KeyStroke createKeyStroke();

  protected abstract PlotItemManagementListener createEnablingListener();
}
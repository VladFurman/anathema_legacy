package net.sf.anathema.campaign.toolbar;

import net.sf.anathema.campaign.item.PlotItemManagement;
import net.sf.anathema.campaign.item.PlotItemManagementListener;
import net.sf.anathema.campaign.module.PlotExtension;
import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.framework.persistence.IRepositoryItemPersister;
import net.sf.anathema.framework.presenter.resources.PlatformUI;
import net.sf.anathema.framework.repository.IItem;
import net.sf.anathema.framework.repository.RepositoryException;
import net.sf.anathema.framework.repository.access.IRepositoryWriteAccess;
import net.sf.anathema.framework.swing.MessageUtilities;
import net.sf.anathema.lib.control.IChangeListener;
import net.sf.anathema.lib.gui.action.SmartAction;
import net.sf.anathema.lib.message.Message;
import net.sf.anathema.lib.resources.Resources;

import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class AnathemaSaveAllAction extends SmartAction {
  private IItem currentItem;
  private final IChangeListener changeListener = new IChangeListener() {
    @Override
    public void changeOccurred() {
      setSaveAllEnabled();
    }
  };

  private class SaveAllEnabledListener implements PlotItemManagementListener {
    @Override
    public void itemAdded(IItem item) {
      setSaveAllEnabled();
    }

    @Override
    public void itemRemoved(IItem item) {
      setSaveAllEnabled();
    }

    @Override
    public void itemSelected(IItem item) {
      if (currentItem != null) {
        currentItem.removeDirtyListener(changeListener);
      }
      AnathemaSaveAllAction.this.currentItem = item;
      if (item != null && item.getItemType().supportsRepository()) {
        item.addDirtyListener(changeListener);
        setSaveAllEnabled();
      }
    }
  }

  private void setSaveAllEnabled() {
    boolean enable = false;
    PlotItemManagement itemManagement = PlotExtension.getItemManagement(model);
    for (IItem item : itemManagement.getAllItems()) {
      if (item.getItemType().supportsRepository()) {
        enable |= item.isDirty();
      }
    }
    setEnabled(enable);
  }

  private IApplicationModel model;
  private Resources resources;

  public static Action createToolAction(IApplicationModel model, Resources resources) {
    SmartAction action = new AnathemaSaveAllAction(model, resources);
    action.setToolTipText(resources.getString("AnathemaPersistence.SaveAllAction.Tooltip"));
    action.setIcon(new PlatformUI().getSaveAllTaskBarIcon());
    return action;
  }

  private AnathemaSaveAllAction(IApplicationModel model, Resources resources) {
    this.model = model;
    SaveAllEnabledListener listener = new SaveAllEnabledListener();
    setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    PlotItemManagement itemManagement = PlotExtension.getItemManagement(model);
    itemManagement.addListener(listener);
    listener.itemAdded(null);
    this.resources = resources;
  }

  @Override
  protected void execute(Component parentComponent) {
    PlotItemManagement itemManagement = PlotExtension.getItemManagement(model);
    for (IItem item : itemManagement.getAllItems()) {
      if (item.getItemType().supportsRepository() && item.isDirty()) {
        parentComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
          IRepositoryWriteAccess writeAccess = model.getRepository().createWriteAccess(item);
          IRepositoryItemPersister persister = model.getPersisterRegistry().get(item.getItemType());
          persister.save(writeAccess, item);
          item.setClean();
        } catch (IOException | RepositoryException e) {
          MessageUtilities
                  .indicateMessage(getClass(), parentComponent, new Message(resources.getString("AnathemaPersistence.SaveAction.Message.Error"), e));
        } finally {
          parentComponent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
      }
    }
  }
}
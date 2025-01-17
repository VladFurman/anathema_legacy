package net.sf.anathema.swing.interaction;

import net.sf.anathema.framework.view.menu.AddToSwingComponent;
import net.sf.anathema.interaction.Command;
import net.sf.anathema.interaction.CommandProxy;
import net.sf.anathema.interaction.Hotkey;
import net.sf.anathema.interaction.ToggleTool;
import net.sf.anathema.lib.exception.NotYetImplementedException;
import net.sf.anathema.lib.file.RelativePath;
import net.sf.anathema.lib.gui.CommandAction;
import net.sf.anathema.lib.gui.action.SmartAction;
import net.sf.anathema.lib.gui.icon.ImageProvider;

import javax.swing.JToggleButton;

public class ToggleActionInteraction implements ToggleTool {

  private final CommandProxy commandProxy = new CommandProxy();
  private final SmartAction action = new CommandAction(commandProxy);
  private final JToggleButton button = new JToggleButton(action);

  @Override
  public void setIcon(RelativePath relativePath) {
    action.setIcon(new ImageProvider().getImageIcon(relativePath));
  }

  @Override
  public void setOverlay(RelativePath relativePath) {
    throw new UnsupportedOperationException("We'll probably never need this.");
  }

  @Override
  public void setTooltip(String key) {
    action.setToolTipText(key);
  }

  @Override
  public void setText(String key) {
    action.setName(key);
  }

  @Override
  public void enable() {
    action.setEnabled(true);
  }

  @Override
  public void disable() {
    action.setEnabled(false);
  }

  @Override
  public void setCommand(Command command) {
    commandProxy.setDelegate(command);
  }

  @Override
  public void setHotkey(Hotkey s) {
    throw new NotYetImplementedException();
  }

  @Override
  public void select() {
    button.setSelected(true);
  }

  @Override
  public void deselect() {
    button.setSelected(false);
  }

  public void addTo(AddToSwingComponent addTo) {
    addTo.add(action);
  }
}
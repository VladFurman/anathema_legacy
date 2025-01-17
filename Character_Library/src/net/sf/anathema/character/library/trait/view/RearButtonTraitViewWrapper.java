package net.sf.anathema.character.library.trait.view;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import net.sf.anathema.character.library.intvalue.IRemovableTraitView;
import net.sf.anathema.interaction.Command;
import net.sf.anathema.lib.gui.CommandAction;
import net.sf.anathema.lib.gui.layout.SwingLayoutUtils;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;

import static net.sf.anathema.lib.gui.layout.LayoutUtils.fillWithoutInsets;

public class RearButtonTraitViewWrapper<K extends ITraitView<?>> extends AbstractTraitViewWrapper<K> implements IRemovableTraitView<K> {

  private final JButton button;
  private JPanel traitViewPanel;
  private JPanel innerViewPanel;
  private Icon buttonIcon;

  public RearButtonTraitViewWrapper(K view, Icon buttonIcon) {
    super(view);
    this.buttonIcon = buttonIcon;
    this.button = new JButton(buttonIcon);
    button.setPreferredSize(new Dimension(buttonIcon.getIconWidth() + 4, buttonIcon.getIconHeight() + 4));
  }

  @Override
  public void addComponents(JPanel panel) {
    this.traitViewPanel = panel;
    this.innerViewPanel = new JPanel(new MigLayout(fillWithoutInsets().wrapAfter(2)));
    getInnerView().addComponents(innerViewPanel);
    panel.add(innerViewPanel, new CC().growX().pushX());
    panel.add(button, SwingLayoutUtils.constraintsForImageButton(button));
  }

  @Override
  public void setButtonEnabled(boolean enabled) {
    button.setEnabled(enabled);
  }

  @Override
  public void addButtonListener(final Command command) {
    CommandAction action = new CommandAction(command, buttonIcon);
    button.setAction(action);
  }

  @Override
  public void delete() {
    getInnerView().delete();
    traitViewPanel.remove(innerViewPanel);
    traitViewPanel.remove(button);
    traitViewPanel.revalidate();
  }
}
package net.sf.anathema.swing.character.perspective;

import net.sf.anathema.character.perspective.model.CharacterIdentifier;
import net.sf.anathema.framework.swing.IView;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.CardLayout;

public class StackView implements IView {

  private final CardLayout stack = new CardLayout();
  private final JPanel viewPanel = new JPanel(stack);

  public void showView(CharacterIdentifier identifier) {
    stack.show(viewPanel, identifier.getId());
  }

  public void addView(CharacterIdentifier identifier, IView view) {
    JComponent component = view.getComponent();
    JScrollPane pane = new JScrollPane(component);
    viewPanel.add(pane, identifier.getId());
    viewPanel.revalidate();
    viewPanel.repaint();
  }

  @Override
  public JComponent getComponent() {
    return viewPanel;
  }
}

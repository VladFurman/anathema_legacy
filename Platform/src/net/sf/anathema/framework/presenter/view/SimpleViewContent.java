package net.sf.anathema.framework.presenter.view;

import net.sf.anathema.framework.view.util.ContentProperties;
import net.sf.anathema.lib.gui.IView;
import net.sf.anathema.lib.gui.swing.IDisposable;

public class SimpleViewContent implements IViewContent {

  private final ContentProperties properties;
  private final IView tabView;

  public SimpleViewContent(ContentProperties properties, IView tabView) {
    this.properties = properties;
    this.tabView = tabView;
  }

  @Override
  public void addTo(MultipleContentView view) {
    view.addView(tabView, properties);
  }

  @Override
  public IDisposable getDisposable() {
    return tabView instanceof IDisposable ? (IDisposable) tabView : null;
  }
}
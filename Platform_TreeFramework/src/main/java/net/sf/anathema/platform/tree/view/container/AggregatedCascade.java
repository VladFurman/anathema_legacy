package net.sf.anathema.platform.tree.view.container;

import com.google.common.collect.Lists;
import net.sf.anathema.framework.ui.RGBColor;
import net.sf.anathema.platform.tree.display.NodeProperties;
import net.sf.anathema.platform.tree.display.draw.ShapeWithPosition;
import net.sf.anathema.platform.tree.view.interaction.PolygonPanel;

import java.util.List;

public class AggregatedCascade implements Cascade {
  private final List<ContainerCascade> cascades = Lists.newArrayList();

  @Override
  public void colorNode(String nodeId, RGBColor fillColor) {
    for (ContainerCascade cascade : cascades) {
      if (cascade.hasNode(nodeId)) {
        cascade.colorNode(nodeId, fillColor);
      }
    }
  }

  @Override
  public void addTo(PolygonPanel panel) {
    for (Cascade cascade : cascades) {
      cascade.addTo(panel);
    }
  }

  @Override
  public void addToggleListener(NodeToggleListener listener) {
    for (Cascade cascade : cascades) {
      cascade.addToggleListener(listener);
    }
  }

  @Override
  public void removeToggleListener(NodeToggleListener listener) {
    for (Cascade cascade : cascades) {
      cascade.removeToggleListener(listener);
    }
  }

  @Override
  public void initNodeNames(NodeProperties properties) {
    for (Cascade cascade : cascades) {
      cascade.initNodeNames(properties);
    }
  }

  @Override
  public void determinePositionFor(String nodeId, ShapeWithPosition control) {
    for (ContainerCascade cascade : cascades) {
      cascade.determinePositionFor(nodeId, control);
    }
  }

  public void add(ContainerCascade cascade) {
    cascades.add(cascade);
  }
}
package net.sf.anathema.cascades.presenter;

import net.sf.anathema.character.main.magic.charm.Charm;
import net.sf.anathema.character.main.magic.charm.requirements.IndirectCharmRequirement;
import net.sf.anathema.framework.ui.RGBColor;
import net.sf.anathema.hero.charms.display.coloring.CharmColoring;
import net.sf.anathema.hero.charms.display.view.CharmView;

public class CascadeColoringStrategy implements CharmColoring {
  private CharmView view;

  public CascadeColoringStrategy(CharmView view) {
    this.view = view;
  }

  @Override
  public void colorCharm(Charm charm) {
    view.colorNode(charm.getId(), RGBColor.White);
  }

  @Override
  public void setPrerequisiteVisuals(IndirectCharmRequirement requirement) {
    view.colorNode(requirement.getStringRepresentation(), RGBColor.White);
  }
}
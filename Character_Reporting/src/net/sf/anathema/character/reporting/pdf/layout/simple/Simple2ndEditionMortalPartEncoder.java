package net.sf.anathema.character.reporting.pdf.layout.simple;

import com.lowagie.text.pdf.BaseFont;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.backgrounds.PdfBackgroundEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.experience.ExperienceBoxContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.IBoxContentEncoder;
import net.sf.anathema.lib.resources.IResources;

public class Simple2ndEditionMortalPartEncoder extends AbstractSecondEditionPartEncoder {

  private SimpleEncodingRegistry registry;

  public Simple2ndEditionMortalPartEncoder(IResources resources, BaseFont baseFont, SimpleEncodingRegistry registry) {
    super(resources, baseFont, 3);
    this.registry = registry;
  }

  @Override
  public IBoxContentEncoder getAnimaEncoder() {
    return new PdfBackgroundEncoder(getResources());
  }

  @Override
  public IBoxContentEncoder getEssenceEncoder() {
    return new ExperienceBoxContentEncoder();
  }

  @Override
  public boolean hasSecondPage() {
    return false;
  }

  @Override
  public IBoxContentEncoder getGreatCurseEncoder() {
    return registry.getLinguisticsEncoder(); //No Great Curse for Mortals
  }
}

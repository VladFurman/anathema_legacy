package net.sf.anathema.character.equipment.impl.reporting.rendering.arsenal;

import net.sf.anathema.character.equipment.impl.reporting.content.WeaponryContent;
import net.sf.anathema.hero.sheet.pdf.content.BasicContent;
import net.sf.anathema.hero.sheet.pdf.encoder.EncoderIds;
import net.sf.anathema.hero.sheet.pdf.encoder.boxes.GlobalEncoderFactory;
import net.sf.anathema.hero.sheet.pdf.encoder.boxes.RegisteredEncoderFactory;
import net.sf.anathema.hero.sheet.pdf.encoder.general.box.ContentEncoder;
import net.sf.anathema.lib.resources.Resources;

@RegisteredEncoderFactory
public class ArsenalEncoderFactory extends GlobalEncoderFactory {

  public ArsenalEncoderFactory() {
    super(EncoderIds.ARSENAL);
    setPreferredHeight(new PreferredWeaponryHeight());
  }

  @Override
  public ContentEncoder create(Resources resources, BasicContent content) {
    return new WeaponryEncoder(WeaponryContent.class);
  }
}

package net.sf.anathema.character.equipment.impl.reporting.rendering.arsenal;

import com.itextpdf.text.DocumentException;
import net.sf.anathema.hero.sheet.pdf.session.ReportSession;
import net.sf.anathema.hero.sheet.pdf.content.SubBoxContent;
import net.sf.anathema.hero.sheet.pdf.encoder.extent.Bounds;
import net.sf.anathema.hero.sheet.pdf.encoder.general.box.AbstractContentEncoder;
import net.sf.anathema.hero.sheet.pdf.encoder.graphics.SheetGraphics;

public class WeaponryEncoder<C extends SubBoxContent> extends AbstractContentEncoder<C> {

  private final WeaponryTableEncoder tableEncoder;

  public WeaponryEncoder(Class<C> contentClass) {
    super(contentClass);
    this.tableEncoder = new WeaponryTableEncoder(contentClass);
  }

  @Override
  public void encode(SheetGraphics graphics, ReportSession session, Bounds bounds) throws DocumentException {
    tableEncoder.encodeTable(graphics, session, bounds);
  }
}

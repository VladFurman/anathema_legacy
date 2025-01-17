package net.sf.anathema.cards.data;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import net.sf.anathema.cards.layout.ICardReportResourceProvider;
import net.sf.anathema.character.generic.framework.magic.MagicDisplayLabeler;
import net.sf.anathema.character.generic.framework.magic.stringbuilder.ScreenDisplayInfoStringBuilder;
import net.sf.anathema.character.generic.framework.magic.stringbuilder.source.MagicSourceStringBuilder;
import net.sf.anathema.character.generic.impl.magic.Cost;
import net.sf.anathema.character.generic.impl.magic.HealthCost;
import net.sf.anathema.character.generic.magic.IMagic;
import net.sf.anathema.character.generic.magic.description.MagicDescription;
import net.sf.anathema.lib.resources.Resources;

public abstract class AbstractMagicCardData implements ICardData {

	private IMagic magic;
	private Resources resources;
	private MagicDescription description;
	private ICardReportResourceProvider resourceProvider;
	
	public AbstractMagicCardData(IMagic magic, MagicDescription description,
			ICardReportResourceProvider resourceProvider, Resources resources) {
		this.magic = magic;
		this.resources = resources;
		this.resourceProvider = resourceProvider;
		this.description = description;
	}
	
	protected Resources getResources() {
		return resources;
	}
	
	protected ICardReportResourceProvider getResourceProvider() {
		return resourceProvider;
	}
	
	@Override
	public String getTitle() {
      return new MagicDisplayLabeler(getResources()).getLabelForMagic(magic);
    }

	@Override
	public String getSource() {
		String source = new MagicSourceStringBuilder<>(resources).createSourceString(magic);
		source = source.replaceAll(resources.getString("CardsReport.MoEP.Long"),
								   resources.getString("CardsReport.MoEP.Short"));
		return source;
	}
	
	protected Phrase getCostPhrase(boolean semicolon) {
		String cost = new ScreenDisplayInfoStringBuilder(resources).createCostString(magic);
		return new Phrase(cost + (semicolon ? ": " : ""), resourceProvider.getBoldFont());
	}
	
	protected boolean hasDescription() {
		return !description.isEmpty();
	}
	
	protected void addDescriptionPhrases(Paragraph phrases) {
		for (String string : description.getParagraphs()) {
	    	phrases.add(new Paragraph(string, resourceProvider.getNormalFont()));
	    }
	}
	
	protected boolean hasCost(IMagic magic) {
		return magic.getTemporaryCost().getEssenceCost() != Cost.NULL_COST ||
			   magic.getTemporaryCost().getWillpowerCost() != Cost.NULL_COST ||
			   magic.getTemporaryCost().getHealthCost() != HealthCost.NULL_HEALTH_COST ||
			   magic.getTemporaryCost().getXPCost() != Cost.NULL_COST;
	}
	
	@Override
    public boolean wantsNewPage() {
		return false;
	}
}

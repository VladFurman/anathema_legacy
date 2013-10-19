package net.sf.anathema.character.main.magic.charm.prerequisite.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.anathema.character.main.magic.charm.Charm;
import net.sf.anathema.character.main.magic.charm.CharmImpl;
import net.sf.anathema.character.main.magic.charm.ICharmLearnArbitrator;
import net.sf.anathema.character.main.magic.charm.ICharmLearnableArbitrator;
import net.sf.anathema.character.main.magic.charm.prerequisite.CharmLearnPrerequisite;
import net.sf.anathema.character.main.magic.charm.prerequisite.DirectCharmLearnPrerequisite;

import com.google.common.base.Preconditions;

public class SimpleCharmLearnPrerequisite implements DirectCharmLearnPrerequisite, CharmLearnPrerequisite {
	private final String prerequisiteId;
	private Charm prerequisite;
	
	public SimpleCharmLearnPrerequisite(String charm) {
		this.prerequisiteId = charm;
	}
	
	public SimpleCharmLearnPrerequisite(Charm charm) {
		this.prerequisite = charm;
		this.prerequisiteId = charm.getId();
	}

	@Override
	public Charm[] getDirectPredecessors() {
		return new Charm[] { prerequisite };
	}

	@Override
	public boolean isSatisfied(ICharmLearnArbitrator arbitrator) {
		return arbitrator.isLearned(prerequisite);
	}
	
	@Override
	public boolean isAutoSatisfiable(ICharmLearnableArbitrator arbitrator) {
		return arbitrator.isLearnable(prerequisite);
	}
	
	@Override
	public Charm[] getLearnPrerequisites(ICharmLearnArbitrator arbitrator) {
		Set<Charm> prerequisiteCharms = new HashSet<>();
	    prerequisiteCharms.addAll(prerequisite.getLearnPrerequisitesCharms(arbitrator));
	    prerequisiteCharms.add(prerequisite);
	    return prerequisiteCharms.toArray(new Charm[0]);
	}

	@Override
	public void link(Map<String, CharmImpl> charmsById) {
		if (prerequisite != null) {
			return;
		}
		prerequisite = charmsById.get(prerequisiteId);
		Preconditions.checkNotNull(prerequisite, "Parent Charm " + prerequisiteId + " not defined" );
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SimpleCharmLearnPrerequisite) {
			SimpleCharmLearnPrerequisite prerequisite = (SimpleCharmLearnPrerequisite) obj;
			return prerequisite.prerequisite.equals(prerequisite);
		}
		return false;
	}
}
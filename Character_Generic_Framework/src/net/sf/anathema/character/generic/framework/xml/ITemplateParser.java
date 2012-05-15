package net.sf.anathema.character.generic.framework.xml;

import net.sf.anathema.lib.exception.PersistenceException;
import org.dom4j.Element;

public interface ITemplateParser<T> {

  T parseTemplate(Element element) throws PersistenceException;
}
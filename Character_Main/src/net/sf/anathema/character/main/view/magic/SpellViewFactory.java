package net.sf.anathema.character.main.view.magic;

import net.sf.anathema.character.main.type.ICharacterType;
import net.sf.anathema.character.main.framework.RegisteredCharacterView;
import net.sf.anathema.character.main.view.SubViewFactory;

@RegisteredCharacterView(ISpellView.class)
public class SpellViewFactory implements SubViewFactory {
  @Override
  public <T> T create(ICharacterType type) {
    return (T) new SpellView();
  }
}
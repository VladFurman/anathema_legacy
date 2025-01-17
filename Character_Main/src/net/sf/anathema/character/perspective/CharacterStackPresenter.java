package net.sf.anathema.character.perspective;

import net.sf.anathema.character.perspective.model.CharacterIdentifier;
import net.sf.anathema.character.perspective.model.ItemSystemModel;
import net.sf.anathema.framework.repository.IItem;

import java.util.ArrayList;
import java.util.List;

public class CharacterStackPresenter {
  private final List<CharacterIdentifier> knownCharacters = new ArrayList<>();
  private final ItemSystemModel model;
  private final CharacterStackBridge bridge;

  public CharacterStackPresenter(CharacterStackBridge bridge, ItemSystemModel model) {
    this.bridge = bridge;
    this.model = model;
  }

  public void showCharacter(CharacterIdentifier identifier) {
    if (!knownCharacters.contains(identifier)) {
      addViewForCharacter(identifier);
      knownCharacters.add(identifier);
    }
    bridge.showCharacterView(identifier);
    model.setCurrentCharacter(identifier);
  }

  public void addViewForCharacter(CharacterIdentifier identifier) {
    IItem item = model.loadItem(identifier);
    bridge.addViewForCharacter(identifier, item);
  }
}

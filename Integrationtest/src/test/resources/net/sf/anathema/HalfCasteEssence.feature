@Integration
Feature: Half-Caste and God-Blooded pay extra for Essence

  Scenario: Essence 2 costs extra at character creation
    Given a new God-Blooded of any kind
    When I set her Essence to 2
    Then she has spent 12 bonus points

  Scenario: Essence 3 costs extra at character creation
    Given a new God-Blooded of any kind
    When I set her Essence to 3
    Then she has spent 27 bonus points

  Scenario Outline: Raising Essence during play costs extra
    Given a new <type> using rules for <template>
    And her current Essence is <currentEssence>
    And she is experienced
    When I set her Essence to <increasedEssence>
    Then she has spent <xpCost> experience points

  Examples:
    | type     | template          | currentEssence | increasedEssence | xpCost |
    | Lunar    | HalfCasteLunar    | 1              | 2                | 14     |
    | Lunar    | HalfCasteLunar    | 2              | 3                | 28     |
    | Solar    | HalfCasteSolar    | 1              | 2                | 12     |
    | Abyssal  | HalfCasteAbyssal  | 1              | 2                | 12     |
    | Sidereal | HalfCasteSidereal | 1              | 2                | 14     |
    | Ghost    | GhostBlooded      | 1              | 2                | 18     |
    | Spirit   | GodBlooded        | 1              | 2                | 0      |
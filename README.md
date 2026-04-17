# ⚔️ SC2002 Turn-Based Combat Arena

A command-line turn-based combat arena game built in Java, designed with **Object-Oriented Design Principles (OODP)** and **SOLID principles**.

Developed for **SC2002 Object-Oriented Design & Programming** at Nanyang Technological University (NTU), AY2025/26 Semester 2.

---

## 📖 Game Overview

A player selects a character class, picks 2 items, and fights through waves of enemies using actions, items, and status effects. The game proceeds in rounds with turn order determined by each combatant's speed stat.

### Game Flow

1. **Setup** — Choose a class (Warrior / Wizard), pick 2 items, select difficulty
2. **Battle** — Take turns attacking, defending, using items, or casting special skills
3. **Victory** — Defeat all enemies including backup spawns
4. **Defeat** — Player HP reaches 0

### Player Classes

| Class | HP | ATK | DEF | SPD | Special Skill |
|-------|-----|-----|-----|-----|---------------|
| **Warrior** | 260 | 40 | 20 | 30 | **Shield Bash** — Deal damage + stun for 2 turns |
| **Wizard** | 200 | 50 | 10 | 20 | **Arcane Blast** — Deal damage to all enemies, +10 ATK per kill |

### Enemy Types

| Enemy | HP | ATK | DEF | SPD |
|-------|-----|-----|-----|-----|
| **Goblin** | 55 | 35 | 15 | 25 |
| **Wolf** | 40 | 45 | 5 | 35 |

### Difficulty Levels

| Level | Difficulty | Initial Spawn | Backup Spawn |
|-------|-----------|---------------|--------------|
| 1 | Easy | 3 Goblins | — |
| 2 | Medium | 1 Goblin + 1 Wolf | 2 Wolves |
| 3 | Hard | 2 Goblins | 1 Goblin + 2 Wolves |

### Items (choose 2 at game start, duplicates allowed)

| Item | Effect |
|------|--------|
| **Potion** | Heal 100 HP (capped at max HP) |
| **Power Stone** | Trigger special skill once without affecting cooldown |
| **Smoke Bomb** | Enemy attacks deal 0 damage for 2 player actions |

---

## 🏗️ Architecture

The system follows **Boundary–Control–Entity (BCE)** layered architecture.

```
sc2002/
├── GameMain.java                          # Entry point
├── ui/                                    # Boundary Layer
│   ├── GameUI.java                        # UI interface (DIP)
│   └── ConsoleGameUI.java                 # CLI implementation
├── engine/                                # Control Layer
│   └── CombatEngine.java                  # Core game loop + BattleResult
├── action/                                # Entity — Actions
│   ├── Actions.java                       # Interface
│   ├── BasicAttack.java
│   ├── Defend.java
│   ├── UseItem.java
│   ├── SpecialSkill.java                  # Abstract (Template Method)
│   ├── ShieldBashAction.java              # Warrior skill
│   ├── ArcaneBlastAction.java             # Wizard skill
│   └── TargetType.java                    # Enum
├── entity/
│   ├── combatant/                         # Entity — Combatants
│   │   ├── Combatant.java                 # Abstract base class
│   │   ├── Player.java                    # Abstract player
│   │   ├── Enemy.java                     # Abstract enemy
│   │   ├── Warrior.java
│   │   ├── Wizard.java
│   │   ├── Goblin.java
│   │   ├── Wolf.java
│   │   └── statuseffects/                 # Entity — Status Effects
│   │       ├── StatusEffect.java          # Interface (event-based)
│   │       ├── StunEffect.java
│   │       ├── DefendBuff.java
│   │       ├── SmokeBombEffect.java
│   │       └── ArcaneBlastBuff.java
│   └── items/                             # Entity — Items
│       ├── Item.java                      # Interface
│       ├── Potion.java
│       ├── PowerStone.java
│       └── SmokeBomb.java
├── strategy/                              # Strategy Interfaces
│   ├── TurnOrderStrategy.java             # Interface
│   ├── SpeedBasedTurnOrder.java
│   ├── EnemyActionStrategy.java           # Interface
│   └── BasicAttackStrategy.java
└── level/                                 # Level Management
    ├── Level.java
    └── LevelFactory.java
```

---

## 🔧 SOLID Principles

| Principle | Application |
|-----------|-------------|
| **SRP** | Each class has one responsibility: `CombatEngine` manages rounds, `ConsoleGameUI` handles I/O, each effect/action is a separate class |
| **OCP** | New `Actions`, `StatusEffect`, or `Item` types added by implementing interfaces — no modification to `CombatEngine` |
| **LSP** | `Warrior`, `Wizard`, `Goblin`, `Wolf` all substitutable as `Combatant` in `CombatEngine`'s `List<Combatant>` |
| **ISP** | Focused interfaces: `Actions` (3 methods), `Item` (3 methods), `StatusEffect` (event-based with defaults) |
| **DIP** | `CombatEngine` depends on `TurnOrderStrategy` interface (injected via constructor); `Enemy` depends on `EnemyActionStrategy`; UI uses `GameUI` interface |

### Design Patterns

| Pattern | Where | Purpose |
|---------|-------|---------|
| **Strategy** | `TurnOrderStrategy`, `EnemyActionStrategy` | Pluggable turn ordering and enemy AI algorithms |
| **Factory** | `LevelFactory` | Creates `Level` configurations based on difficulty |
| **Template Method** | `SpecialSkill` → `ShieldBashAction` / `ArcaneBlastAction` | Shared cooldown logic with class-specific `performSkillEffect()` |

### Key Design Features

- **Event-based StatusEffect system** — Effects use `onOwnerAction()`, `onTurnSkipped()`, `getDamageReceivedMultiplier()` instead of simple round ticking, enabling precise control over when effects expire
- **Modifier pattern** — `Combatant.getAttack()` and `getDefense()` dynamically sum modifiers from all active status effects, avoiding direct stat mutation
- **TargetType enum** — Clean targeting system (`Single_enemy`, `All_enemies`, `Self`, `Dependent`) eliminates string matching
- **Snapshot pattern** — `CombatEngine` snapshots effects before each action to prevent newly-applied effects from being consumed on the same turn

---

## 🎮 Gameplay Preview

```
=== Welcome to RPG Combat Game ===

Select your class:
1. Warrior (High HP & Defense)
2. Wizard (High Attack & Magic)
Choice (1-2): 1

Enter your name: Hero

Select difficulty (1=Easy, 2=Medium, 3=Hard): 1

--- Round 1 ---
Hero (Player): HP 260/260 | ATK 40 | DEF 20
1) Goblin A: HP 55/55
2) Goblin B: HP 55/55
3) Goblin C: HP 55/55

Hero's turn!
1. Basic Attack
2. Defend
3. Special Skill (Shield Bash)
4. Use Item
Choose action (1-4):
```

---

## 👥 Team Members

| Name | Matriculation No. |
|------|-------------------|
Kaewchaijaroenkit Chayapol | U2520021C |
Lau Jun Lit Boris | U2522435C |
Joe Ong Teng Kiat | U2522073C |
He Zhiqing | U2523695K |


---

## 📝 Acknowledgements

Developed for SC2002 Object-Oriented Design & Programming, School of Computer Science and Engineering, Nanyang Technological University, AY2025/26 Semester 2.

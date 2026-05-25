![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![JavaFX](https://img.shields.io/badge/JavaFX-GUI-blue?style=flat-square)
![OOP](https://img.shields.io/badge/Paradigm-OOP-green?style=flat-square)
![MVC](https://img.shields.io/badge/Architecture-MVC-purple?style=flat-square)
![Git](https://img.shields.io/badge/Version%20Control-Git-red?style=flat-square)

# 🎮 DooR DasH: Scare vs Laugh Touchdown

A **fully playable strategy board game** built in Java with JavaFX, inspired by the *Monsters Inc.* universe. Two monsters race across a 100-cell board collecting energy from children's doors — the first to reach Boo's Door with **≥ 1000 energy** wins.

Built across **3 progressive milestones** covering OOP data structures, a complete game engine, and a full JavaFX GUI following the MVC architectural pattern.

> Team project — Programming II course, Spring 2026 | German International University

---

## 🏗️ Architecture & Engineering

The project is organized into **7 packages** with strict separation of concerns:

```
game.engine
├── cards/          → Card class hierarchy & effects
├── cells/          → Cell class hierarchy & board interactions
├── monsters/       → Monster type system with polymorphic behavior
├── dataloader/     → CSV-driven entity loading pipeline
├── exceptions/     → Custom exception hierarchy
├── interfaces/     → CanisterModifier interface
└── (root)          → Board, Game engine, Constants, Role enum
```

### Monster System — Polymorphism in Practice

All 4 monster types extend the abstract `Monster` class and override `move()`, `setEnergy()`, and `executePowerupEffect()` to produce completely different gameplay from the same turn loop:

| Type | Passive | Powerup |
|---|---|---|
| **Dasher** | Moves at 2× dice roll | Momentum Rush: 3× speed for 3 turns |
| **Dynamo** | Doubles all energy changes (gains & losses) | Screech Freeze: skips opponent's next turn |
| **MultiTasker** | Moves at ½ dice roll; +200 on all energy changes | Focus Mode: normal speed for 2 turns |
| **Schemer** | +10 on every energy change | Chain Attack: steals from ALL monsters at once |

### Cell System — Strategy Layer

| Cell Type | Effect |
|---|---|
| **Door Cells** (50 total) | Energy gain/loss based on role match; team-wide effect; one-time activation |
| **Conveyor Belts** (5) | Transports monster forward |
| **Contamination Socks** (5) | Transports backward + 100 energy penalty (shieldable) |
| **Card Cells** (10) | Draws from shuffled deck and executes effect |
| **Monster Cells** (6) | Free powerup (role match) or energy swap (role mismatch) |

### Card Engine

25 cards across 5 types, expanded by rarity value at runtime and reshuffled when the deck runs out:

- **SwapperCard** — swap positions if behind
- **EnergyStealCard** — steal 50/100/150 energy (shield-blockable)
- **StartOverCard** — send player or opponent back to cell 0
- **ShieldCard** — block next negative energy effect for landing monster's team
- **ConfusionCard** — swap both monsters' roles for 2–3 turns

### Board Engine

- Zigzag 10×10 grid with `indexToRowCol()` handling alternating left-to-right / right-to-left row traversal
- `moveMonster()` handles full movement including transport chains, collision detection, confusion decrements, and position sync via `updateMonsterPositions()`
- `InvalidMoveException` thrown and turn retried when final position collides with opponent

### Exception Hierarchy

```
Exception
└── GameActionException (abstract)
    ├── InvalidMoveException
    ├── InvalidTurnException
    └── OutOfEnergyException
IOException
└── InvalidCSVFormat
```

### Data Pipeline

All game entities are loaded from CSV files at startup via `DataLoader`, fully decoupled from game logic — enabling easy configuration of monsters, cells, and cards without touching engine code.

---

## 🖥️ JavaFX GUI (Milestone 3)

Built following the **MVC architectural pattern**:

- **Model** — the full game engine (Milestone 2) runs independently of the UI
- **View** — dynamic JavaFX scenes reflect all engine state changes in real time
- **Controller** — translates player input into engine calls; handles all exceptions gracefully without terminating the game

**GUI covers:**
- Role selection screen and game instructions
- Full 100-cell board with visible cell types, door energy values, and cell indices
- Per-turn indicators: dice result, drawn card name & effect, freeze skip notification
- Per-monster panel: name, original role, current role (confusion indicator), type, energy, position, active status effects
- Activated/exhausted door highlighting and stationed monster identity on Monster Cells
- Energy change animations and shield-block indicators
- Game Over / Win screen with final energy totals and return to menu

---

## 💻 Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Core game logic & OOP architecture |
| JavaFX | GUI rendering (no Swing) |
| Git & GitHub | Version control & team collaboration |

---

## 👥 Team

| Name | GitHub |
|---|---|
| Omar Shaker | [@Omar-Shaker-Elbana](https://github.com/Omar-Shaker-Elbana) |
| Mark Fahim | [@mark1234720](https://github.com/mark1234720) |
| Ahmed Roshdy | [@AhmedMohammedRo](https://github.com/AhmedMohammedRo) |
| Karl Hany | [@karlhany222-spec](https://github.com/karlhany222-spec) |

---

*Educational project inspired by the Monsters Inc. universe.*

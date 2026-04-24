package game.engine;

public final class Constants {

    /* =====================================================
       =============== 1. BOARD STRUCTURE ==================
       ===================================================== */

    public static final int BOARD_SIZE = 100;

    public static final int BOARD_ROWS = 10;
    public static final int BOARD_COLS = 10;

    public static final int STARTING_POSITION = 0;
    public static final int WINNING_POSITION = 99;

    /* =====================================================
       ============ 2. SPECIAL CELL POSITIONS ==============
       ===================================================== */

    public static final int[] MONSTER_CELL_INDICES = {2, 18, 34, 54, 82, 88};

    public static final int[] CONVEYOR_CELL_INDICES = {6, 22, 44, 52, 66};

    public static final int[] SOCK_CELL_INDICES = {32, 42, 74, 84, 98};

    public static final int[] CARD_CELL_INDICES = {4, 12, 28, 36, 48, 56, 60, 76, 86, 90};

    /* =====================================================
       =============== 3. GAME OBJECTIVES ==================
       ===================================================== */

    public static final int WINNING_ENERGY = 1000;
    public static final int MIN_ENERGY = 0;

    /* =====================================================
       ================== 5. CELL EFFECTS ==================
       ===================================================== */

    public static final int SLIP_PENALTY = 100;

    /* =====================================================
       =============== 6. MONSTER ABILITIES =================
       ===================================================== */

    // Multitasker passive bonus
    public static final int MULTITASKER_BONUS = 200;

    // Schemer stolen bonus
    public static final int SCHEMER_STEAL = 10;

    /* =====================================================
       ================= 7. POWER & ECONOMY =================
       ===================================================== */

    // Cost to activate a power-up (in turn)
    public static final int POWERUP_COST = 500;

}

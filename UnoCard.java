public class UnoCard {
    private final Color color;
    private final Value value;
    public UnoCard(Color color, Value value) {
        this.color = color;
        this.value = value;
    }
    public Color getColor() {
        return color;
    }
    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return color + "_" + value;
    }
    enum Color {
        RED, BLUE, YELLOW, GREEN, WILD;
        private static final Color[] colors = Color.values();
        public static Color getColor(int i) {
            return Color.colors[i];
        }
    }
    enum Value {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, DRAW_TOW, SKIP, REVERSE, WILD, WILD_FOUR;
        private static final Value[] values = Value.values();
        public static Value getValue(int i) {
            return Value.values()[i];
        }
    }
}
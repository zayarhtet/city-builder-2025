package model;

public enum CellItem {
    RESIDENTIAL ('R',1), SERVICE_INDUSTRIAL('S',1), GENERAL('#',1), SERVICE_BUILDING('B',2),
    H_ROAD('*',1),
    V_ROAD('$',1),
    JUNCTION_ROAD('@', 1),
    TRANSMISSION_LINE('-',1);
    public final char representation;
    public final int tiles;
    CellItem(char rep, int size) { representation = rep; tiles= size; }
}

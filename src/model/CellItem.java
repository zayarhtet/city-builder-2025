package model;

public enum CellItem {
    RESIDENTIAL ('R',1), SERVICE_INDUSTRIAL('S',1), GENERAL('#',1),

    POLICE_DEPARTMENT('P',2),
    STADIUM('T',2),
    H_ROAD('*',1),
    V_ROAD('$',1),
    JUNCTION_ROAD('@', 1),
    TRANSMISSION_LINE('-',1),
    R_CAR('c', 1),
    D_CAR('d', 1),
    DEL_OPT('d',0);
    public final char representation;
    public final int tiles;
    CellItem(char rep, int size) { representation = rep; tiles= size; }
}

package com.coffee.citybuilder.model;

public enum CellItem {
    RESIDENTIAL (30,1,1), SERVICE_INDUSTRIAL(50,1,2), GENERAL(0,1),
    POLICE_DEPARTMENT(80,2,2),
    STADIUM(100,2,2),
    H_ROAD(5,1),
    V_ROAD(5,1),
    JUNCTION_ROAD(5, 1),
    TRANSMISSION_LINE(10,1),
    POWER_PLANT(80, 2),
    R_CAR(0, 1),
    D_CAR(0, 1),
    DEL_OPT(0,0),
    DISASTER(0,0);
    public final int price;
    public final int tiles;
    public final int electricityDemand;
    CellItem(int cost, int size) { price = cost; tiles = size; electricityDemand = 0; }
    CellItem(int cost, int size, int elecDemand) { price = cost; tiles= size; electricityDemand = elecDemand; }
}

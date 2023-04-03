package model;

import persistence.Database;

import java.sql.Array;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

public class City {

    private final int row, col;
    private CellItem [][] cells;
    private Database database;
    private List<Position> roads = new ArrayList<>();
    private List<Building> buildings = new ArrayList<> ();
    private List<Position> transmissionLines = new ArrayList<>();
    private List<Zone> zones = new ArrayList<>();

    public City() {
        this(22, 33);
    }

    public City(int row, int col) {
        this.row = row; this.col = col;

        cells = new CellItem[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                cells[i][j] = CellItem.GENERAL;
            }
        }
    }
    public City(String id) {
        this.row = 100; this.col = 100;
        // USE Database class and obtain the data for this id
        // This is the constructor, so you have to initiate the private attribute with the help of database

    }

    public void buildRoad(Position p, CellItem ct) {
        cells[p.y][p.x] = ct;
        roads.add(new Position(p));
        // Create a Road class and handle the fee and maintenance
    }

    public void demolish(Position p){
        CellItem ct = cells[p.y][p.x];
        switch (ct){
            case H_ROAD:
            case V_ROAD:
            case JUNCTION_ROAD:
                deleteRoad(p); break;
            case POLICE_DEPARTMENT:
            case STADIUM:
                deleteBuilding(p); break;
            case GENERAL:
            default:
                break;
        }
    }

    public void deleteRoad(Position p){
        cells[p.y][p.x] = CellItem.GENERAL;
        roads.remove(p);
    }

    public void deleteTransmissionLine(Position p){
        transmissionLines.remove(p);
    }

    public void deleteBuilding(Position p){


        for(Building b : buildings){
            boolean partOf = true; // check
            // buildings[y][x].getBuildingSize()
            // 1 -> (x,y)
            // get top left pos
            // 2 -> (x,y) (x+1,y) (x,y+1) (x+1,y+1)
        }
    }

    public CellItem getCellItem(int row, int col) { return cells[row][col]; }
    public int getColumnCount() { return col; }
    public int getRowCount() { return row; }
    public List<Position> getRoadList() { return new ArrayList<>(roads); }
    public List<Building> getBuildingList() { return new ArrayList<>(buildings); }
    public boolean isOccupied(Position p) {
        // check if the parameter position is General field or not
        return false;
    }

}

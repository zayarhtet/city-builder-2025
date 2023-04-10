package model;

import persistence.Database;

import java.sql.Array;
import java.sql.DatabaseMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class City {
    private final String    id;
    private String          username;
    private final String    createdDate;
    private String          lastModifiedDate;
    private final int       row = 22,
                            col = 33;
    private CellItem [][]   cells;
    private List<Position>  roads = new ArrayList<>();
    private List<Building>  buildings = new ArrayList<> ();
    private List<Position>  transmissionLines = new ArrayList<>();
    private List<Zone>      zones = new ArrayList<>();

    public City(String username) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.lastModifiedDate = this.createdDate = formatter.format(new Date());

        cells = new CellItem[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                cells[i][j] = CellItem.GENERAL;
            }
        }
    }

    public void buildRoad(Position p, CellItem ct) {
        cells[p.y][p.x] = ct;
        roads.add(new Position(p));
        // Create a Road class and handle the fee and maintenance
    }

    public void constructBuilding(Position p,CellItem c){
        int radius = c.tiles-1;
        //System.out.println(p.y+" "+p.x);
        int offset = 2; // offset for InGameButtonPanel
        boolean isInBound = p.x < col-offset-1 && p.y < row-offset-1;
        if(!isInBound) return;

        boolean isFree = !( isOccupied(p) || isOccupied(new Position(p.x+1,p.y)) ||
                              isOccupied(new Position(p.x,p.y+1)) ||
                              isOccupied(new Position(p.x+1,p.y+1)) );
        if(!isFree) return;

        cells[p.y][p.x]                = c;
        cells[p.y+radius][p.x]         = c;
        cells[p.y][p.x+radius]         = c;
        cells[p.y+radius][p.x+radius]  = c;

        // Building Object Creation
        ArrayList<Position> locations = new ArrayList<>();
        locations.add(p);
        if(radius == 1){ // 2 tile building
            locations.add(new Position(p.x+1,p.y));
            locations.add(new Position(p.x,p.y+1));
            locations.add(new Position(p.x+1,p.y+1));
        }

        buildings.add(new Building(locations));
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
        setModifiedDate();
    }

    public void deleteRoad(Position p){
        cells[p.y][p.x] = CellItem.GENERAL;
        roads.remove(p);
    }

    public void deleteTransmissionLine(Position p){
        transmissionLines.remove(p);
    }

    public void deleteBuilding(Position p){
        int ind = -1;
        for(int i=0; i<buildings.size(); i++){
            if(buildings.get(i).location.contains(p)){
                ind = i;
                break;
            }
        }
        if(ind == -1) return;
        for(Position p1 : buildings.get(ind).location){
            cells[p1.y][p1.x] = CellItem.GENERAL;
        }
        buildings.remove(ind);
    }

    public CellItem getCellItem(int row, int col) { return cells[row][col]; }
    public int getColumnCount() { return col; }
    public int getRowCount() { return row; }
    public List<Position> getRoadList() { return new ArrayList<>(roads); }
    public List<Building> getBuildingList() { return new ArrayList<>(buildings); }
    public boolean isOccupied(Position p) { return cells[p.y][p.x] != CellItem.GENERAL; }
    public String getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getCreatedDate() { return this.createdDate; }
    public String getModifiedDate() { return this.lastModifiedDate; }
    public void setModifiedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.lastModifiedDate = formatter.format(new Date());
    }

    public boolean isRoad(int row, int col) {
        if (row >= this.row || col >= this.col) return false;
        return cells[row][col] == CellItem.H_ROAD || cells[row][col] == CellItem.JUNCTION_ROAD;
    }

    public boolean isVRoad(int row, int col) {
        if (row >= this.row || col >= this.col) return false;
        return cells[row][col] == CellItem.V_ROAD || cells[row][col] == CellItem.JUNCTION_ROAD;
    }
}

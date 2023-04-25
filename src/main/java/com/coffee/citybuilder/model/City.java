package com.coffee.citybuilder.model;

import java.text.SimpleDateFormat;
import java.util.*;

import com.coffee.citybuilder.model.budget.Bank;
import com.coffee.citybuilder.model.building.Building;
import com.coffee.citybuilder.model.building.PoliceDepartment;
import com.coffee.citybuilder.model.building.PowerPlant;
import com.coffee.citybuilder.model.building.Stadium;
import com.coffee.citybuilder.model.zone.ResidentialZone;
import com.coffee.citybuilder.model.zone.ServiceIndustrialZone;
import com.coffee.citybuilder.model.zone.Zone;

import static com.coffee.citybuilder.resource.Constant.Initial_Population;
import static com.coffee.citybuilder.resource.Constant.ROAD_COST;

public class City {
    private final String    id;
    private String          username;
    private final String    createdDate;
    private String          lastModifiedDate;
    private final int       row = 22,
                            col = 33;
    private Bank            bank;
    private CellItem [][]   cells;
    private List<Position>  roads = new ArrayList<>();
    private List<Building>  buildings = new ArrayList<> ();
    private List<Position>  transmissionLines = new ArrayList<>();
    private List<Zone>      zones = new ArrayList<>();
    private DateTime        datetime;
    private int             population = 0;
    private int             employedCount = 0;

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
        this.bank = new Bank();
        this.datetime = new DateTime();
        Random rand = new Random();
        assignZone(new Position(rand.nextInt(col), rand.nextInt(row)), CellItem.RESIDENTIAL);
        assignZone(new Position(rand.nextInt(col), rand.nextInt(row)), CellItem.RESIDENTIAL);
    }

    public void buildRoad(Position p, CellItem ct) {
        if (bank.cost("Road", ROAD_COST)) {
            cells[p.y][p.x] = ct;
        }
    }

    public void assignZone(Position p, CellItem ct) {
        if (isOccupied(p)) return;
        if(!bank.cost(ct.toString(), ct.price)) return;
        cells[p.y][p.x] = ct;
        Zone rz;
        switch (ct) {
            case RESIDENTIAL:
                rz = new ResidentialZone(p);
                this.population += rz.getPopulation();
                zones.add(rz);
                break;
            case SERVICE_INDUSTRIAL:
                rz = new ServiceIndustrialZone(p);
                this.employedCount += rz.getPopulation();
                zones.add(rz);
                break;
        }
    }

    public void constructBuilding(Position p,CellItem c){
        int radius = c.tiles-1;
        int offset = 2; // offset for InGameButtonPanel
        boolean isInBound = p.x < col-offset-1 && p.y < row-offset-1;
        if(!isInBound) return;

        boolean isFree = !( isOccupied(p) || isOccupied(new Position(p.x+1,p.y)) ||
                              isOccupied(new Position(p.x,p.y+1)) ||
                              isOccupied(new Position(p.x+1,p.y+1)) );
        if(!isFree) return;

        if (!bank.cost(c.toString(), c.price)) return;

        cells[p.y][p.x]                = c;
        cells[p.y+radius][p.x]         = c;
        cells[p.y][p.x+radius]         = c;
        cells[p.y+radius][p.x+radius]  = c;

        // Building Object Creation
        List<Position> locations = new ArrayList<>();
        locations.add(new Position(p.y,p.x));
        if(radius == 1){ // 2 tile building
            locations.add(new Position(p.y+1,p.x));
            locations.add(new Position(p.y,p.x+1));
            locations.add(new Position(p.y+1,p.x+1));
        }
        switch (c) {
            case POLICE_DEPARTMENT: buildings.add(new PoliceDepartment(locations)); break;
            case POWER_PLANT: buildings.add(new PowerPlant(locations)); break;
            case STADIUM: buildings.add(new Stadium(locations)); break;
        }
    }

    public void demolish(Position p){
        CellItem ct = cells[p.y][p.x];
        switch (ct){
            case RESIDENTIAL:
            case SERVICE_INDUSTRIAL:
                deleteZone(p); break;
            case H_ROAD:
            case V_ROAD:
            case JUNCTION_ROAD:
                deleteRoad(p); break;
            case POWER_PLANT:
            case POLICE_DEPARTMENT:
            case STADIUM:
                deleteBuilding(p); break;
            case GENERAL:
            default:
                break;
        }
        setModifiedDate();
    }

    public void deleteZone(Position p) {
        CellItem ct = cells[p.y][p.x];
        cells[p.y][p.x] = CellItem.GENERAL;
        Iterator iter = zones.iterator();
        while(iter.hasNext()) {
            Zone z = (Zone) iter.next();
            if (z.isAt(p)) {
                switch (z.getCt()) {
                    case RESIDENTIAL -> this.population -= z.getPopulation();
                    case SERVICE_INDUSTRIAL -> this.employedCount -= z.getPopulation();
                }
                iter.remove();
                break;
            }
        }
        bank.earn("Demolish "+ ct.toString(), reimbursement(ct.price));
    }

    public void deleteRoad(Position p){
        CellItem ct = cells[p.y][p.x];
        cells[p.y][p.x] = CellItem.GENERAL;
        roads.remove(p);
        bank.earn("Demolish Road", reimbursement(ct.price));
    }

    public void deleteTransmissionLine(Position p){
        CellItem ct = cells[p.y][p.x];
        cells[p.y][p.x] = CellItem.GENERAL;
        transmissionLines.remove(p);
        bank.earn("Demolish Transmission Line", reimbursement(ct.price));
    }

    public void deleteBuilding(Position p){
        Position d = new Position(p.y,p.x);
        int ind = -1;
        for(int i=0; i<buildings.size(); i++){
            if(buildings.get(i).contains(d)){
                ind = i;
                break;
            }
        }
        if(ind == -1) return;
        List<Position> bldgLocation = buildings.get(ind).getLocation();
        for(Position p1 : bldgLocation){
            cells[p1.x][p1.y] = CellItem.GENERAL;
        }
        CellItem ct = buildings.get(ind).getCt();
        buildings.remove(ind);
        bank.earn("Demolish "+ ct.toString(), reimbursement(ct.price));
    }

    public Disaster spawnDisaster(){
        Random r = new Random();
        int i = r.nextInt(1);
        return Disaster.values()[i];
    }
    public static int reimbursement(int price) {
        return (int) (price*0.5);
    }
    public int getPopulation() { return this.population; }
    public int getEmployedCount() { return this.employedCount; }
    public int getUnemployedCount() {
        int unEmployed = this.population - this.employedCount;
        return unEmployed <= 0? 0 : unEmployed;
    }
    public int getVacancyCount() {
        int vacant = this.employedCount - this.population;
        return vacant <= 0? 0: vacant;
    }
    public int getSatisfaction() {
        // calculate based on unemployed count, electricity-access count, police, stadium access count, budget
        return 100;
    }
    public String getDateTime() {
        return datetime.getDate();
    }
    public void timeGone() {
        datetime.timeMove();
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
    public int getBudget() { return this.bank.getBudget(); }
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

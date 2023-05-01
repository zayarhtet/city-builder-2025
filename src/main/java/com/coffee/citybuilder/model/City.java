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

import javax.sound.midi.SysexMessage;

import static com.coffee.citybuilder.resource.Constant.Initial_Population;
import static com.coffee.citybuilder.resource.Constant.ROAD_COST;

public class City {
    private final String id;
    private String username;
    private final String createdDate;
    private String lastModifiedDate;
    private final int row = 22,
            col = 33;
    private Bank bank;
    private CellItem[][] cells;
    private List<Position> roads = new ArrayList<>();
    private List<Building> buildings = new ArrayList<>();
    private List<Position> transmissionLines = new ArrayList<>();
    private List<Zone> zones = new ArrayList<>();
    private DateTime datetime;
    private int population = 0;
    private int employedCount = 0;
    private int safetyAccess = 0;
    private int relaxAccess = 0;

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
            refreshConnection();
        }
    }

    public void buildTransmissionLine(Position p, CellItem ct) {
        if (bank.cost("Transmission Line", ROAD_COST)) {
            cells[p.y][p.x] = ct;
            supplyElectricity();
        }
        refreshConnection();
    }

    public void assignZone(Position p, CellItem ct) {
        if (isOccupied(p)) return;
        if (!bank.cost(ct.toString(), ct.price)) return;
        cells[p.y][p.x] = ct;
        //System.out.println(isConnected(p,new Position(0,0)));
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
        refreshConnection();
        supplyElectricity();
    }

    public void constructBuilding(Position p, CellItem c) {
        int radius = c.tiles - 1;
        int offset = 2; // offset for InGameButtonPanel
        boolean isInBound = p.x < col - offset - 1 && p.y < row - offset - 1;
        if (!isInBound) return;

        boolean isFree = !(isOccupied(p) || isOccupied(new Position(p.x + 1, p.y)) ||
                isOccupied(new Position(p.x, p.y + 1)) ||
                isOccupied(new Position(p.x + 1, p.y + 1)));
        if (!isFree) return;

        if (!bank.cost(c.toString(), c.price)) return;

        cells[p.y][p.x] = c;
        cells[p.y + radius][p.x] = c;
        cells[p.y][p.x + radius] = c;
        cells[p.y + radius][p.x + radius] = c;

        // Building Object Creation
        List<Position> locations = new ArrayList<>();
        locations.add(new Position(p.y, p.x));
        if (radius == 1) { // 2 tile building
            locations.add(new Position(p.y + 1, p.x));
            locations.add(new Position(p.y, p.x + 1));
            locations.add(new Position(p.y + 1, p.x + 1));
        }
        switch (c) {
            case POLICE_DEPARTMENT:
                buildings.add(new PoliceDepartment(locations));
                break;
            case POWER_PLANT:
                buildings.add(new PowerPlant(locations));
                break;
            case STADIUM:
                buildings.add(new Stadium(locations));
                break;
        }
        supplyElectricity();
    }

    public void demolish(Position p) {
        CellItem ct = cells[p.y][p.x];
        switch (ct) {
            case RESIDENTIAL:
            case SERVICE_INDUSTRIAL:
                deleteZone(p);
                break;
            case H_ROAD:
            case V_ROAD:
            case JUNCTION_ROAD:
                deleteRoad(p);
                break;
            case POWER_PLANT:
            case POLICE_DEPARTMENT:
            case STADIUM:
                deleteBuilding(p);
                break;
            case J_TL:
            case H_TL:
            case V_TL:
            case TRANSMISSION_LINE:
                deleteTransmissionLine(p);
                break;
            case GENERAL:
            default:
                break;
        }
        refreshConnection();
        supplyElectricity();
        setModifiedDate();
    }

    public void deleteZone(Position p) {
        CellItem ct = cells[p.y][p.x];
        cells[p.y][p.x] = CellItem.GENERAL;
        Iterator iter = zones.iterator();
        while (iter.hasNext()) {
            Zone z = (Zone) iter.next();
            if (z.isAt(p)) {
                switch (z.getCt()) {
                    case RESIDENTIAL : this.population -= z.getPopulation(); break;
                    case SERVICE_INDUSTRIAL : this.employedCount -= z.getPopulation(); break;
                }
                iter.remove();
                break;
            }
        }
        bank.earn("Demolish " + ct.toString(), reimbursement(ct.price));
    }

    public void deleteRoad(Position p) {
        CellItem ct = cells[p.y][p.x];
        cells[p.y][p.x] = CellItem.GENERAL;
        roads.remove(p);
        bank.earn("Demolish Road", reimbursement(ct.price));
    }

    public void deleteTransmissionLine(Position p) {
        CellItem ct = cells[p.y][p.x];
        cells[p.y][p.x] = CellItem.GENERAL;
        transmissionLines.remove(p);
        bank.earn("Demolish Transmission Line", reimbursement(ct.price));
    }

    public void deleteBuilding(Position p) {
        Position d = new Position(p.y, p.x);
        int ind = -1;
        for (int i = 0; i < buildings.size(); i++) {
            if (buildings.get(i).contains(d)) {
                ind = i;
                break;
            }
        }
        if (ind == -1) return;
        List<Position> bldgLocation = buildings.get(ind).getLocation();
        for (Position p1 : bldgLocation) {
            cells[p1.x][p1.y] = CellItem.GENERAL;
        }
        CellItem ct = buildings.get(ind).getCt();
        buildings.remove(ind);
        bank.earn("Demolish " + ct.toString(), reimbursement(ct.price));
    }

    private void refreshConnection() {

        for (Zone a : zones) {
            int availableWorkers = 0;
            if (a.getCt() != CellItem.SERVICE_INDUSTRIAL) continue;
            for (Zone b : zones) {
                if (b.getCt() != CellItem.RESIDENTIAL) continue;

                if( isConnected(a.getLocation(), b.getLocation())){
                    b.setCanWork(true);
                    availableWorkers += b.getPopulation();
                }

            }
            if(a instanceof ServiceIndustrialZone){
//                ServiceIndustrialZone sz = (ServiceIndustrialZone) a;
                ( (ServiceIndustrialZone) a).increaseWorkers(availableWorkers);
            }

        }

        for (Zone z : zones) {
            Position p = z.getLocation();
            //System.out.println(p.y + "," + p.x + " " + z.getCanWork());
        }

        calculateEmployee();
    }

    private boolean isConnected(Position start, Position end) {

        if (cells[start.y][start.x] == cells[end.y][end.x])
            return false;
        if (cells[start.y][start.x] != CellItem.RESIDENTIAL && cells[start.y][start.x] != CellItem.SERVICE_INDUSTRIAL)
            return false;
        if (cells[end.y][end.x] != CellItem.RESIDENTIAL && cells[end.y][end.x] != CellItem.SERVICE_INDUSTRIAL)
            return false;

        boolean[][] visited = new boolean[row][col];
        visited[start.y][start.x] = true;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{start.y, start.x});

        while (!queue.isEmpty()) {
            int[] curr = queue.remove();
            int y = curr[0];
            int x = curr[1];

            if (Math.abs(y - end.y) + Math.abs(x - end.x) == 1) {
                return true;
            }

            // check adjacent cells

            if (y > 0 && isVRoad(y - 1, x) && !visited[y - 1][x]) {
                visited[y - 1][x] = true;
                queue.add(new int[]{y - 1, x});
            }
            if (y < col - 1 && isVRoad(y + 1, x) && !visited[y + 1][x]) {
                visited[y + 1][x] = true;
                queue.add(new int[]{y + 1, x});
            }
            if (x > 0 && isRoad(y, x - 1) && !visited[y][x - 1]) {
                visited[y][x - 1] = true;
                queue.add(new int[]{y, x - 1});
            }
            if (x < row - 1 && isRoad(y, x + 1) && !visited[y][x + 1]) {
                visited[y][x + 1] = true;
                queue.add(new int[]{y, x + 1});
            }
        }

        return false;
    }

    private boolean canTransmit(Position start, Position end) {
        boolean[][] visited = new boolean[row][col];
        visited[start.x][start.y] = true;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{start.x, start.y}); // flip 1

        while (!queue.isEmpty()) {
            int[] curr = queue.remove();
            int y = curr[0];
            int x = curr[1];

            Position check = new Position(end);

            if(getCellItem(y,x) == CellItem.RESIDENTIAL || getCellItem(y,x) == CellItem.SERVICE_INDUSTRIAL){
                check.x = end.y;
                check.y = end.x;
            }

            if (Math.abs(y - check.x) + Math.abs(x - check.y) <= 1 ||
                    (Math.abs(y - end.x) == 1 && Math.abs(x - end.y) == 1)
            ) { return true; }


            if (y > 0 && isConductor(y - 1, x) && !visited[y - 1][x]) {
                visited[y - 1][x] = true;
                queue.add(new int[]{y - 1, x});
            }
            if (y < row - 1 && isConductor(y + 1, x) && !visited[y + 1][x]) {
                visited[y + 1][x] = true;
                queue.add(new int[]{y + 1, x});
            }
            if (x > 0 && isConductor(y, x - 1) && !visited[y][x - 1]) {
                visited[y][x - 1] = true;
                queue.add(new int[]{y, x - 1});
            }
            if (x < col - 1 && isConductor(y, x + 1) && !visited[y][x + 1]) {
                visited[y][x + 1] = true;
                queue.add(new int[]{y, x + 1});
            }

            if(y > 0 && x > 0 && isConductor(y-1,x-1) && !visited[y-1][x-1]){
                visited[y-1][x-1] = true;
                queue.add(new int[]{y-1,x-1});
            }
            if(y < row - 1 && x > 0 && isConductor(y+1,x-1) && !visited[y+1][x-1]){
                visited[y+1][x-1] = true;
                queue.add(new int[]{y+1,x-1});
            }
            if(y > 0 && x < col - 1 && isConductor(y-1,x+1) && !visited[y-1][x+1]){
                visited[y-1][x+1] = true;
                queue.add(new int[]{y-1,x+1});
            }
            if(y < row - 1 && x < col - 1 && isConductor(y+1,x+1) && !visited[y+1][x+1]){
                visited[y+1][x+1] = true;
                queue.add(new int[]{y+1,x+1});
            }

        }

        return false;
    }

    private void supplyElectricity() {

        for (Building b : buildings) {
            b.setHasElectricity(false);
        }

        for (Zone z : zones) {
            z.setHasElectricity(false);
        }

        for (Building p : buildings) {
            if (p.getCt() != CellItem.POWER_PLANT)
                continue;
            p.resetQuota();

            for (Building b : buildings) {
                if (!b.isHasElectricity()) {

                    if (!canTransmit(p.topLeft(), b.topLeft())) {
                        continue;
                    }
                    if (p.canShare(b.getCt().electricityDemand)) {
                        p.Share(b.getCt().electricityDemand);
                        b.setHasElectricity(true);
                        //System.out.println(b.getCt() + " has Electricity "+b.getCt().electricityDemand);
                    }
                }

            }

            for (Zone z : zones) {
                if (!z.isHasElectricity()) {

                    if (!canTransmit(p.topLeft(), z.getLocation())) {
                        continue;
                    }
                    if (p.canShare(z.getCt().electricityDemand)) {
                        p.Share(z.getCt().electricityDemand);
                        z.setHasElectricity(true);
                        //System.out.println(z.getCt() + " has Electricity "+z.getCt().electricityDemand);
                        //System.out.println(z.getCt() + " has Electricity "+z.getCt().electricityDemand);
                    }

                }
            }
        }

    }

    public Disaster spawnDisaster() {
        Random r = new Random();
        int i = r.nextInt(1);
        return Disaster.values()[i];
    }

    public static int reimbursement(int price) {
        return (int) (price * 0.5);
    }

    private void calculateEmployee() {
        this.employedCount = 0;
        for (Zone z : zones) {
            if (z.getCanWork()) {
                employedCount += z.getPopulation();
            }
        }
    }

    private int distance(Position a, Position b) {
        double d = Math.sqrt((a.y - b.y) * (a.y - b.y) + (a.x - b.x) * (a.x - b.x));
        return (int) Math.ceil(d);
    }

    private boolean withinRadius(Position p, int radius, CellItem ct) {

        Position rows = new Position(Math.max(p.y - radius, 0), Math.min(p.y + radius, row));
        Position columns = new Position(Math.max(p.x - radius, 0), Math.min(p.x + radius, col));

        for (int r = rows.x; r <= rows.y; r++) {
            for (int c = columns.x; c <= columns.y; c++) {
                if (getCellItem(r, c) == ct && distance(new Position(r, c), p) <= radius) {
                    return true;
                }
            }
        }
        return false;
    }

    private void calculateServiceAccess() {
        this.safetyAccess = this.relaxAccess = 0;

        for (Zone z : zones) {
            if (z.getCt() == CellItem.RESIDENTIAL) {
                if (withinRadius(z.getLocation(), PoliceDepartment.radius, CellItem.POLICE_DEPARTMENT)) {
                    safetyAccess += z.getPopulation();
                }
                //relaxAccess No radius?
            }
        }
    }

    public int getPopulation() {
        return this.population;
    }

    public int getEmployedCount() {
        return this.employedCount;
    }

    public int getUnemployedCount() {
        int unEmployed = this.population - this.employedCount;
        return unEmployed <= 0 ? 0 : unEmployed;
    }

    public int getVacancyCount() {
        int vacant = this.employedCount - this.population;
        return vacant <= 0 ? 0 : vacant;
    }

    public int getSatisfaction() {
        // calculate based on unemployed count, electricity-access count, police, stadium access count, budget
        //satisfaction = (getEmployedCount() * 0.3) + (electricityScore * 0.2) +
        //               (safetyAccess * 0.2) + (relaxAccess * 0.2) +
        //               (getBudget() * 0.1);
        return 100;
    }

    public String getDateTime() {
        return datetime.getDate();
    }

    public void timeGone() {
        datetime.timeMove();
        setModifiedDate();
        if (datetime.isYearEnd()) {
            collectTax();
            payPension();
            spendMaintenanceFee();
            datetime.doneYearEnd();
        }
    }
    private void collectTax() {
        bank.earn("Taxation from Industries", (int)(employedCount*0.2));
    }
    private void payPension() {
        int pensionerCount = new Random().nextInt((int)(population*0.1));
        bank.cost("Pension", pensionerCount*3 );
    }
    private void spendMaintenanceFee() {
        bank.cost("Building Maintenance", buildings.size()*1);
        bank.cost("Road Maintenance", (int)(roads.size()*0.5));
        bank.cost("Transmission Line Maintenance", (int) (transmissionLines.size() * 0.5));
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
    public Bank getBank() { return new Bank(this.bank); }
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

    private boolean isConductor(int row, int col) {
        if (row >= this.row || col >= this.col) return false;
        return !(isRoad(row, col) || isVRoad(row, col) || cells[row][col] == CellItem.GENERAL);
    }
}

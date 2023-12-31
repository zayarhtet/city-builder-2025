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
import com.coffee.citybuilder.model.zone.ZoneInfo;

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
    private DateTime datetime = new DateTime();
    private int population = 0;
    private int employedCount = 0;
    private int safetyAccess = 0;
    private int relaxAccess = 0;
    private int electricityPopulation = 0;
    private int electricityCount = 0;
    private int vacantCount = 0;
    private int residentialZoneCount = 0;
    private int serviceIndustrialZoneCount = 0;
    private int pensionerCount = 0;

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
        assignZone(new Position(rand.nextInt(col-2), rand.nextInt(row-2)), CellItem.RESIDENTIAL);
        assignZone(new Position(rand.nextInt(col-2), rand.nextInt(row-2)), CellItem.RESIDENTIAL);
    }

    /**
     * Build road
     * @param p position to build
     * @param ct CellItem type
     */
    public void buildRoad(Position p, CellItem ct) {
        if (bank.cost("Road", ROAD_COST)) {
            cells[p.y][p.x] = ct;
            roads.add(new Position(p));
            refreshConnection();
        }
    }

    /**
     * Build transmission line
     * @param p position
     * @param ct CellItem
     */
    public void buildTransmissionLine(Position p, CellItem ct) {
        if (bank.cost("Transmission Line", ROAD_COST)) {
            cells[p.y][p.x] = ct;
            supplyElectricity();
        }
        refreshConnection();
    }

    /**
     * Build Zone
     * @param p position
     * @param ct CellItem
     */
    public void assignZone(Position p, CellItem ct) {
        if (isOccupied(p)) return;
        if (!bank.cost(ct.toString(), ct.price)) return;
        cells[p.y][p.x] = ct;
        Zone rz;
        switch (ct) {
            case RESIDENTIAL:
                rz = new ResidentialZone(p);
                this.population += rz.getPopulation();
                this.residentialZoneCount++;
                zones.add(rz);
                break;
            case SERVICE_INDUSTRIAL:
                rz = new ServiceIndustrialZone(p);
                zones.add(rz);
                this.serviceIndustrialZoneCount++;
                break;
        }
        refreshConnection();
        supplyElectricity();
        calculateServiceAccess();
    }

    /**
     * Construct Building
     * @param p position
     * @param c CellItem
     */
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
        calculateServiceAccess();
    }

    /**
     * Delete Anything on the map
     * @param p position to delete
     */
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
        calculateServiceAccess();
        setModifiedDate();
    }

    /**
     * Delete Zone
     * @param p Position to delete
     */
    public void deleteZone(Position p) {
        CellItem ct = cells[p.y][p.x];
        cells[p.y][p.x] = CellItem.GENERAL;
        Iterator iter = zones.iterator();
        while (iter.hasNext()) {
            Zone z = (Zone) iter.next();
            if (z.isAt(p)) {
                switch (z.getCt()) {
                    case RESIDENTIAL : this.population -= z.getPopulation(); this.residentialZoneCount--; break;
                    case SERVICE_INDUSTRIAL : this.vacantCount -= z.getWorkerCapacity(); this.employedCount -= z.getPopulation(); this.serviceIndustrialZoneCount--; break;
                }
                iter.remove();
                break;
            }
        }
        bank.earn("Demolish " + ct.toString(), reimbursement(ct.price));
    }

    /**
     * Delete Road
     * @param p Position to delete
     */
    public void deleteRoad(Position p) {
        CellItem ct = cells[p.y][p.x];
        for (Zone z : zones) {
            if (isConnected(p, z.getLocation())) { return; }
        }
        cells[p.y][p.x] = CellItem.GENERAL;
        roads.remove(p);
        bank.earn("Demolish Road", reimbursement(ct.price));
    }

    /**
     * Delete transmission line
     * @param p Position to delete
     */
    public void deleteTransmissionLine(Position p) {
        CellItem ct = cells[p.y][p.x];
        cells[p.y][p.x] = CellItem.GENERAL;
        transmissionLines.remove(p);
        bank.earn("Demolish Transmission Line", reimbursement(ct.price));
    }

    /**
     * Delete Building
     * @param p Position to delete
     */
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

    /**
     * Refresh the connection between zones (work and residential)
     */
    private void refreshConnection() {
        employedCount = 0;
        vacantCount = 0;
        for (Zone a : zones) {
            int availableWorkers = 0; boolean had = false;
            if (a.getCt() != CellItem.SERVICE_INDUSTRIAL) continue;
            for (Zone b : zones) {
                if (b.getCt() != CellItem.RESIDENTIAL) continue;

                if( isConnected(a.getLocation(), b.getLocation())){
                    had = true;
                    b.setCanWork(true);
                    availableWorkers += b.getPopulation();
                }
            }
            if (had) {
                a.increaseWorkers(availableWorkers);
                employedCount += a.getPopulation();
            } else {
                a.decreaseWorkers();
            }
            vacantCount += a.getWorkerCapacity();
        }
        employedCount = Math.min(employedCount, population);
        for(Zone a: zones) {
            if (a.getCt() != CellItem.RESIDENTIAL) continue;
            a.setCanWork(false);
            for (Zone b : zones) {
                if(b.getCt() != CellItem.SERVICE_INDUSTRIAL) continue;
                if (isConnected(a.getLocation(), b.getLocation())) {
                    a.setCanWork(true); break;
                }
            }
        }
    }

    /**
     * Check if two position are connected by Road with Breadth First Search
     * @param start Start Position
     * @param end End Position
     * @return boolean
     */
    private boolean isConnected(Position start, Position end) {
        CellItem ctStart = cells[start.y][start.x];
        CellItem ctEnd = cells[end.y][end.x];
        List<CellItem> cts = new ArrayList<>(); cts.add(CellItem.H_ROAD); cts.add(CellItem.V_ROAD); cts.add(CellItem.JUNCTION_ROAD);
        cts.add(CellItem.RESIDENTIAL); cts.add(CellItem.SERVICE_INDUSTRIAL);
        if (!cts.contains(ctStart) || !cts.contains(ctEnd)) return false;

        boolean[][] visited = new boolean[row][col];
        visited[start.y][start.x] = true;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{start.y, start.x});

        while (!queue.isEmpty()) {
            int[] curr = queue.remove();
            int y = curr[0];
            int x = curr[1];

            if (Math.abs(y - end.y) + Math.abs(x - end.x) == 1) { return true; }

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

    /**
     * Check if the building is connected with transmission line
     * @param start Start Position
     * @param end End Position
     * @return boolean
     */
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

    /**
     * Refresh the connection between Residential and PowerPlant
     */
    private void supplyElectricity() {

        for (Building b : buildings) { b.setHasElectricity(false); }

        for (Zone z : zones) { z.setHasElectricity(false); }

        electricityPopulation = 0; electricityCount = 0;

        for (Building p : buildings) {
            if (p.getCt() != CellItem.POWER_PLANT) continue;
            p.resetQuota();
            for (Building b : buildings) {
                if (!b.isHasElectricity()) {
                    if (!canTransmit(p.topLeft(), b.topLeft())) { continue; }
                    if (p.canShare(b.getCt().electricityDemand)) {
                        p.Share(b.getCt().electricityDemand);
                        b.setHasElectricity(true);
                        electricityCount++;
                    }
                }
            }

            for (Zone z : zones) {
                if (!z.isHasElectricity()) {
                    if (!canTransmit(p.topLeft(), z.getLocation())) continue;
                    if (p.canShare(z.getCt().electricityDemand)) {
                        p.Share(z.getCt().electricityDemand);
                        z.setHasElectricity(true);
                        if (z.getCt() == CellItem.RESIDENTIAL) {
                            electricityPopulation += z.getPopulation();
                        }
                    }
                }
            }
        }
    }

    /**
     * Spawn Disaster
     * @return Disaster Object
     */
    public Disaster spawnDisaster() {
        Random r = new Random();
        int i = r.nextInt(1);
        return Disaster.values()[i];
    }

    /**
     * Distance between 2 Position
     * @param a Position 1
     * @param b Position 2
     * @return int - distance
     */
    private int distance(Position a, Position b) {
        double d = Math.sqrt((a.y - b.y) * (a.y - b.y) + (a.x - b.x) * (a.x - b.x));
        return (int) Math.ceil(d);
    }

    /**
     * Check if any given CellItem is within the given radius with the position as a Centre.
     * @param p Center
     * @param radius Radius
     * @param ct Target CellItem
     * @return boolean
     */
    private boolean withinRadius(Position p, int radius, CellItem ct) {
        Position rows = new Position(Math.max(p.y - radius, 0), Math.min(p.y + radius, row));
        Position columns = new Position(Math.max(p.x - radius, 0), Math.min(p.x + radius, col));

        for (int r = rows.x; r < rows.y; r++) {
            for (int c = columns.x; c < columns.y; c++) {
                if (getCellItem(r, c) == ct) { return true; }
            }
        }
        return false;
    }

    /**
     * Refresh the connection between Residential and Service Building
     */
    private void calculateServiceAccess() {
        this.safetyAccess = this.relaxAccess = 0;

        for (Zone z : zones) {
            if (z.getCt() == CellItem.RESIDENTIAL) {
                if (withinRadius(z.getLocation(), PoliceDepartment.radius, CellItem.POLICE_DEPARTMENT)) {
                    safetyAccess += z.getPopulation();
                    z.setHasPolice(true);
                }
                if (withinRadius(z.getLocation(), PoliceDepartment.radius, CellItem.STADIUM)) {
                    relaxAccess += z.getPopulation();
                    z.setHasStadium(true);
                }
            }
        }
    }

    /**
     * Calculate the satisfaction
     * @return double
     */
    public double getSatisfaction() {
        double policePercentage = (double) safetyAccess / population * 100;
        double relaxationPercentage = (double) relaxAccess / population * 100;
        double electricityPercentage = (double) electricityPopulation / population * 100;
        double employmentPercentage = (double) employedCount / population * 100;
        double joblessPercentage = (double) getUnemployedCount() / population * 100;
        double budgetPercentage = (double) bank.getBudget() / 1000 * 100;
        double satisfaction = (policePercentage * 0.15) + (relaxationPercentage * 0.1)
                + (electricityPercentage * 0.15) + (employmentPercentage * 0.3) + (joblessPercentage * 0.2)
                + (budgetPercentage * 0.1);

        return (double) Math.round(satisfaction * 100) / 100;
    }

    /**
     * Get the zone information
     * @param p Zone Position
     * @return ZoneInfo object
     */
    public ZoneInfo getZoneInfo(Position p) {
        // search p inside zones and obtain Zone object
        // calculate the following values
        Zone b = null;
        for (Zone a : zones) {
            if (a.getLocation().equals(p)) b = a;
        }

        int dist = 100;
        for (Zone c : zones) {
            if (c.getCt() == CellItem.SERVICE_INDUSTRIAL) {
                dist = Math.min(dist, distance(b.getLocation(), c.getLocation()));
            }
        }

        double populationWeight = 0.10;
        double policeCoverageWeight = 0.20;
        double stadiumAccessWeight = 0.15;
        double electricityAccessWeight = 0.15;
        double distanceToWorkWeight = 0.20;
        double pensionerCountWeight = 0.10;
        double employedWeight = 0.10;

        // Normalize attribute values to a percentage scale (0-100)
        double populationNormalized = normalizeValue(b.getPopulation(), 0, 1000000);
        double distanceToWorkNormalized = normalizeValue(dist, 0, 100);
        double pensionerCountNormalized = normalizeValue(b.getPensionerCount(), 0, 100);

        // Calculate attribute scores
        double populationScore = populationNormalized * populationWeight;
        double policeCoverageScore = b.isHasPolice() ? 100 * policeCoverageWeight : 0;
        double stadiumAccessScore = b.isHasStadium() ? 100 * stadiumAccessWeight : 0;
        double electricityAccessScore = b.isHasElectricity() ? 100 * electricityAccessWeight : 0;
        double distanceToWorkScore = distanceToWorkNormalized * distanceToWorkWeight;
        double pensionerCountScore = pensionerCountNormalized * pensionerCountWeight;
        double employedScore = b.getCanWork() ? 100 * employedWeight : 0;

        // Calculate overall satisfaction score
        double satisfactionScore = populationScore + policeCoverageScore + stadiumAccessScore + electricityAccessScore
                - (b.getCanWork()? distanceToWorkScore: 0) + pensionerCountScore + employedScore;


        return new ZoneInfo(b.getPopulation(), b.getPensionerCount(),satisfactionScore,b.getCanWork()? b.getPopulation(): 0,b.isHasPolice(),b.isHasStadium(), b.isHasElectricity());
    }

    /**
     * Simplify the value
     * @param value Value
     * @param min Minimum
     * @param max Maximum
     * @return double
     */
    private double normalizeValue(int value, int min, int max) {
        // Normalize the value to a percentage scale
        return ((double) (value - min) / (max - min)) * 100;
    }

    /**
     * Time spent
     */
    public void timeGone() {
        datetime.timeMove();
        setModifiedDate();
        if (datetime.isYearEnd()) {
            pensionerCount = 0;
            population = 0;
            for(Zone z: zones) {
                z.increaseAge();
                pensionerCount += z.getPensionerCount();
                population += z.getPopulation();
            }
            collectTax();
            payPension();
            spendMaintenanceFee();
            refreshConnection();
            datetime.doneYearEnd();
        }
    }

    /**
     * Collect tax
     */
    private void collectTax() {
        bank.earn("Taxation from Industries", (int)(employedCount*0.2));
    }

    /**
     * Pay Pension
     */
    private void payPension() {
        bank.cost("Pension", pensionerCount*3 );
    }

    /**
     * Pay Maintenance fee
     */
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
    public int getTotalBuilding() { return buildings.size(); }
    public int getElectricityCount() { return electricityCount; }
    public int getResidentialZoneCount() { return residentialZoneCount; }
    public int getServiceIndustrialZoneCount() { return serviceIndustrialZoneCount; }
    public int getTotalRoad() { return roads.size(); }
    public String getDateTime() { return datetime.getDate(); }
    public int getElectricityPopulation() { return electricityPopulation; }
    public int getPopulation() { return this.population; }
    public static int reimbursement(int price) { return (int) (price * 0.5); }
    public int getEmployedCount() { return this.employedCount; }
    public List<Zone> getZones() { return this.zones; }
    public int getUnemployedCount() {
        int unEmployed = this.population - this.employedCount;
        return unEmployed <= 0 ? 0 : unEmployed;
    }
    public int getVacancyCount() {
        int vacant = this.vacantCount - this.employedCount;
        return vacant <= 0 ? 0 : vacant;
    }
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

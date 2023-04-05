package view;

import jdk.jfr.Event;
import model.Building;
import model.CellItem;
import model.City;
import model.Position;
import resource.ResourceLoader;
import view.component.EventModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.util.Map.entry;

public class CityMap extends JPanel implements MouseMotionListener, MouseListener {
    private JFrame                  mainFrame;
    private int                     tile_size = 30;
    private int                     speed = 1;
    private int                     vehicleFrameRate = 3;
    private int [][]                vehiclesRow;
    private int [][]                vehiclesCol;
    private final City              city;
    private Map<CellItem, Image>    graphics = Map.ofEntries(
            entry(CellItem.GENERAL, ResourceLoader.loadImage("resource/grass.png")),
            entry(CellItem.H_ROAD, ResourceLoader.loadImage("resource/h-road.png")),
            entry(CellItem.V_ROAD, ResourceLoader.loadImage("resource/v-road.png")),
            entry(CellItem.JUNCTION_ROAD, ResourceLoader.loadImage("resource/bot-right-road.png")),
            entry(CellItem.DEL_OPT, ResourceLoader.loadImage("resource/delete.png")),
            entry(CellItem.POLICE_DEPARTMENT, ResourceLoader.loadImage("resource/ps.png")),
            entry(CellItem.R_CAR, ResourceLoader.loadImage("resource/right-car.png")),
            entry(CellItem.D_CAR, ResourceLoader.loadImage("resource/down-car.png"))
            // you can add many graphic as you want
    );
    public CityMap(JFrame frame) throws IOException {
        this(frame, "");
    }
    public CityMap(JFrame frame, String id) throws IOException {
        mainFrame = frame;

        this.city = id.length() == 0? new City() : new City(id);

        addMouseMotionListener(this);
        addMouseListener(this);
        setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D gr = (Graphics2D) g;
        int w = city.getColumnCount();
        int h = city.getRowCount();
        g.clearRect(0,0,w*tile_size,h*tile_size);
        List<Position> roads = city.getRoadList();

        // base Zone rendering
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Image img = null;
                CellItem ci = city.getCellItem(i, j);
                switch (ci) {
                    case GENERAL:
                        img = graphics.get(CellItem.GENERAL); break;
                    case H_ROAD:
                        img = graphics.get(CellItem.H_ROAD); break;
                    case V_ROAD:
                        img = graphics.get(CellItem.V_ROAD); break;
                    case JUNCTION_ROAD:
                        img = graphics.get(CellItem.JUNCTION_ROAD); break;
                    case TRANSMISSION_LINE:
                        img = graphics.get(CellItem.TRANSMISSION_LINE); break;
                }
                if (img == null) continue;
                gr.drawImage(img, j*tile_size, i*tile_size, tile_size, tile_size, null );
            }
        }

        // buildings rendering
        List<Building> buildings = city.getBuildingList();
        Iterator<Building> iter = buildings.iterator();
        while(iter.hasNext()) {
            // draw building
            Position p = iter.next().topLeft();
            CellItem c = city.getCellItem(p.y,p.x);
            Image img = graphics.get(c);
            if(img == null) continue;
            gr.drawImage(img, p.x*tile_size, p.y*tile_size, c.tiles*tile_size, c.tiles*tile_size, null );
        }

        // animation and event
        if (!EventModel.isFree()) {
            EventModel em = EventModel.getEventModelInstance();
            CellItem ct = em.getCellItem(); Position p = em.getPosition();
            // After implementing isOccupied() method, then comment out this.
            if (city.isOccupied(p)) return;
            gr.drawImage(graphics.get(ct), p.x*tile_size, p.y*tile_size,ct.tiles*tile_size , ct.tiles*tile_size , null);
        }

        if (!EventModel.isGenuinelyFree()) return;

        // implement vehicle rendering // row
        renderVehicles(gr);



    }

    public void renderVehicles(Graphics2D gr) {
        for (int col = 0; col < vehiclesCol.length; col++) {
            for (int j = 0; j < vehiclesCol[col].length; j++) {
                int row = vehiclesCol[col][j];
//                vehiclesCol[col][j] = row +3 >= city.getRowCount()*tile_size? 0 : row+3;
                vehiclesCol[col][j] = calculateVehicleMovement2(row, col);

                if (!city.isRoad((row+tile_size)/tile_size,col) || !city.isRoad(row/tile_size,col)) {
                    vehiclesCol[col][j] = ((vehiclesCol[col][j]/tile_size)*tile_size)+tile_size >= city.getRowCount()*tile_size? 0: (vehiclesCol[col][j]/tile_size)*tile_size + tile_size;
                    continue;
                }
                gr.drawImage(graphics.get(CellItem.D_CAR), col*tile_size, row, tile_size, tile_size, null);
            }
        }
        for (int row = 0; row < vehiclesRow.length; row++) {
            for (int j = 0; j < vehiclesRow[row].length; j++) {
                int col = vehiclesRow[row][j];
                vehiclesRow[row][j] = calculateVehicleMovement(col, row);

                if (!city.isRoad(row,(col+tile_size)/tile_size) || !city.isRoad(row,col/tile_size)){
                    vehiclesRow[row][j] = ((vehiclesRow[row][j]/tile_size)*tile_size) + tile_size >= city.getColumnCount()*tile_size? 0: ((vehiclesRow[row][j]/tile_size)*tile_size) + tile_size;
                    continue;
                }
//                vehiclesRow[row][j] = calculateVehicleMovement(col, row);
//                vehiclesRow[row][j] =  col +3 >= city.getColumnCount()*tile_size? 0 : col+3;
                gr.drawImage(graphics.get(CellItem.R_CAR), col, row*tile_size, tile_size, tile_size, null);
            }
        }
    }

    private int calculateVehicleMovement(int original, int rowCars) {
        int newValue = original+vehicleFrameRate;
        if ((newValue+tile_size)/tile_size >= city.getColumnCount()) return 0;
        int cars[] = vehiclesCol[(newValue+tile_size)/tile_size];

        int carsTranspose[] = vehiclesRow[rowCars];
        for (int i = 0; i < carsTranspose.length; i++) {
            int existedCar = carsTranspose[i];
            if (original == existedCar) continue;
            if (isTwoTileTouching(newValue, rowCars*tile_size, existedCar, rowCars*tile_size)) return original;
        }
        for (int i = 0; i< cars.length; i++) {
            if(isTwoTileTouching(newValue, rowCars*tile_size, ((newValue+tile_size)/tile_size)*tile_size, cars[i]))  {
                return original;
            }
        }
        if (newValue >= city.getColumnCount()*tile_size) return 0;
        else return newValue;
    }
    private int calculateVehicleMovement2(int original, int rowCars) {
        int newValue = original+vehicleFrameRate;
        if ((newValue+tile_size)/tile_size >= city.getRowCount()) return 0;
        int cars[] = vehiclesRow[(newValue+tile_size)/tile_size];

        int carsTranspose[] = vehiclesCol[rowCars];
        for (int i = 0; i < carsTranspose.length; i++) {
            int existedCar = carsTranspose[i];
            if (original == existedCar) continue;
            if (isTwoTileTouching(newValue, rowCars*tile_size, existedCar, rowCars*tile_size)) return original;
        }
        for (int i = 0; i< cars.length; i++) {
            if(isTwoTileTouching(newValue, rowCars*tile_size, ((newValue+tile_size)/tile_size)*tile_size, cars[i]))  {
                return original;
            }
        }
        if (newValue >= city.getColumnCount()*tile_size) return 0;
        else return newValue;
    }
    private boolean isTwoTileTouching(int firstX, int firstY, int secondX, int secondY) {
        return firstX < secondX+tile_size && firstX+tile_size > secondX && firstY < secondY+tile_size && firstY+tile_size > secondY;
    }

    public void initiateRandomVehicles(int vehicleCount) {
        vehiclesRow = new int[city.getRowCount()][vehicleCount];
        vehiclesCol = new int[city.getColumnCount()][vehicleCount];
        Random rand = new Random();
        for (int i = 0, row = city.getRowCount(); i < row; i++) {
            for (int j = 0; j < vehicleCount; j++) {
                vehiclesRow[i][j] = rand.nextInt(city.getColumnCount())*tile_size;
            }
        }
        for (int i = 0, col = city.getColumnCount(); i < col; i++) {
            for (int j = 0; j < vehicleCount; j++) {
                vehiclesCol[i][j] = rand.nextInt(city.getRowCount())*tile_size;
            }
        }
    }

    // MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (EventModel.isFree()) return;
        EventModel em = EventModel.getEventModelInstance();
        // check if the target is occupied
        // check what needs to be built with em.getCellItem() and with Switch case and call respective methods from city.
        switch (em.getCellItem()){
            case V_ROAD:
            case JUNCTION_ROAD:
            case H_ROAD:
                city.buildRoad(em.getPosition(), em.getCellItem()); break;
            case STADIUM:
            case POLICE_DEPARTMENT:
                city.constructBuilding(em.getPosition(),em.getCellItem()); break;
            case DEL_OPT:
                city.demolish(em.getPosition()); break;
            default:
                break;
        }

//        EventModel.DeleteInstance();
//        initiateRandomVehicles(2);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {
        EventModel.enableModel();
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        EventModel.disableModel();
        repaint();
    }

    // MouseMotionListener
    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if (EventModel.isFree()) return;

        int x = e.getX(), y = e.getY();
        EventModel em = EventModel.getEventModelInstance();
        em.savePosition(new Position(x/tile_size,y/tile_size));
        repaint();
    }

}

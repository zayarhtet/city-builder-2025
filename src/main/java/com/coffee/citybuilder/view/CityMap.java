package com.coffee.citybuilder.view;

import javax.swing.*;
import javax.swing.Timer;

import com.coffee.citybuilder.model.*;
import com.coffee.citybuilder.model.building.Building;
import com.coffee.citybuilder.resource.ResourceLoader;
import com.coffee.citybuilder.view.component.EventModel;
import com.coffee.citybuilder.view.component.Vehicle;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Map;

public class CityMap extends JPanel implements MouseMotionListener, MouseListener {
    private JFrame                  mainFrame;
    private int                     tile_size = 30;
    private int                     vehicleFrameRate = 1;
    private Vehicle[]               vehiclesRow;
    private Vehicle[]               vehiclesCol;

    private City                    city;
    private Map<CellItem, Image>    graphics = Map.ofEntries(
            Map.entry(CellItem.GENERAL, ResourceLoader.loadImage("resource/grass.png")),
            Map.entry(CellItem.H_ROAD, ResourceLoader.loadImage("resource/h-road.png")),
            Map.entry(CellItem.V_ROAD, ResourceLoader.loadImage("resource/v-road.png")),
            Map.entry(CellItem.JUNCTION_ROAD, ResourceLoader.loadImage("resource/bot-right-road.png")),
            Map.entry(CellItem.DEL_OPT, ResourceLoader.loadImage("resource/delete.png")),
            Map.entry(CellItem.POLICE_DEPARTMENT, ResourceLoader.loadImage("resource/pd.png")),
            Map.entry(CellItem.R_CAR, ResourceLoader.loadImage("resource/right-car.png")),
            Map.entry(CellItem.D_CAR, ResourceLoader.loadImage("resource/down-car.png")),

            Map.entry(CellItem.L_CAR, ResourceLoader.loadImage("resource/left-car.png")),
            Map.entry(CellItem.T_CAR, ResourceLoader.loadImage("resource/top-car.png")),
            Map.entry(CellItem.POWER_PLANT, ResourceLoader.loadImage("resource/pp.png")),
            Map.entry(CellItem.STADIUM, ResourceLoader.loadImage("resource/stadium-2.png")),
            Map.entry(CellItem.RESIDENTIAL, ResourceLoader.loadImage("resource/residential-2.png")),
            Map.entry(CellItem.SERVICE_INDUSTRIAL, ResourceLoader.loadImage("resource/factory-2.png")),
            Map.entry(CellItem.TRANSMISSION_LINE, ResourceLoader.loadImage("resource/Trans.png"))
//            entry(CellItem.POWER_PLANT, ResourceLoader.loadImage("resource/powerplant-21.png")),
            // you can add many graphic as you want
    );

    private Animator anim = new Animator();
    private int FPS = 45;
    private Timer timer;

    public CityMap(JFrame frame) throws IOException {
        mainFrame = frame;

        addMouseMotionListener(this);
        addMouseListener(this);
        setVisible(false);
        timer = new Timer(1000 / FPS, new FrameListener());
        timer.start();
    }
    public void setCity(City city) {
        this.city = city;
        repaint();
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
                    case RESIDENTIAL:
                        img = graphics.get(CellItem.RESIDENTIAL); break;
                    case SERVICE_INDUSTRIAL:
                        img = graphics.get(CellItem.SERVICE_INDUSTRIAL); break;
                    case H_ROAD:
                        img = graphics.get(CellItem.H_ROAD); break;
                    case V_ROAD:
                        img = graphics.get(CellItem.V_ROAD); break;
                    case JUNCTION_ROAD:
                        img = graphics.get(CellItem.JUNCTION_ROAD); break;
                    case H_TL:
                        img = graphics.get(CellItem.H_TL); break;
                    case V_TL:
                        img = graphics.get(CellItem.V_TL); break;
                    case J_TL:
                        img = graphics.get(CellItem.J_TL); break;
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
            Position p = iter.next().topLeft();
            if(p.x < 0 || p.y <0) break;
            CellItem c = city.getCellItem(p.x,p.y);
            Image img = graphics.get(c);
            if(img == null) {
                continue;
            }
            gr.drawImage(img, p.y*tile_size, p.x*tile_size , c.tiles*tile_size, c.tiles*tile_size, null );
        }

        anim.Animate(gr);
        // animation and event
        if (!EventModel.isFree()) {
            EventModel em = EventModel.getEventModelInstance();
            CellItem ct = em.getCellItem(); Position p = em.getPosition();
            if (p == null) return;
            if (city.isOccupied(p)) return;
            gr.drawImage(graphics.get(ct), p.x*tile_size, p.y*tile_size,ct.tiles*tile_size , ct.tiles*tile_size , null);
        }

        if (!EventModel.isGenuinelyFree()) return;

        renderVehicles(gr);
    }

    /*************************************** Vehicles Starts ****************************************/
    public void setVehicleFrameRate(int i) { this.vehicleFrameRate = i; }
    public void renderVehicles(Graphics2D gr) {
        for (int col = 0; col < vehiclesCol.length; col++) {
            Vehicle v = vehiclesCol[col];
            if (!city.isVRoad(v.getValue()/tile_size, col) || !city.isVRoad((v.getValue()+tile_size)/tile_size, col)) {
                vehiclesCol[col].setValue(calculateIgnoreRoad(v, city.getRowCount()*tile_size));
                continue;
            }
            vehiclesCol[col].setValue(calculateVehicleMovement(v, col, city.getRowCount(), vehiclesRow, v.isGoRightOrDown()));
            if (v.isGoRightOrDown()) gr.drawImage(graphics.get(CellItem.D_CAR), col*tile_size, v.getValue(), tile_size, tile_size, null);
            else gr.drawImage(graphics.get(CellItem.T_CAR), col*tile_size, v.getValue(), tile_size, tile_size, null);
        }
        for (int row = 0; row < vehiclesRow.length; row++) {
            Vehicle v = vehiclesRow[row];
            if (!city.isRoad(row, v.getValue()/tile_size) || !city.isRoad(row, (v.getValue()+tile_size)/tile_size)) {
                vehiclesRow[row].setValue(calculateIgnoreRoad(v, city.getColumnCount()*tile_size));
                continue;
            }
            vehiclesRow[row].setValue(calculateVehicleMovement(v, row, city.getColumnCount(), vehiclesCol, v.isGoRightOrDown()));
            if (v.isGoRightOrDown()) gr.drawImage(graphics.get(CellItem.R_CAR), v.getValue(), row*tile_size, tile_size, tile_size, null);
            else gr.drawImage(graphics.get(CellItem.L_CAR), v.getValue(), row*tile_size, tile_size, tile_size, null);
        }
    }
    private int calculateIgnoreRoad(Vehicle original, int upperBound) {
        int newValue;
        if (original.isGoRightOrDown()) {
            newValue = ((original.getValue()/tile_size)*tile_size)+tile_size;
            if (newValue >= upperBound) {
                original.setGoRightOrDown();
                return calculateIgnoreRoad(original, upperBound);
            }
        } else {
            newValue = ((original.getValue()/tile_size)*tile_size)-1;
            if (newValue <= 0) {
                original.setGoRightOrDown();
                return calculateIgnoreRoad(original, upperBound);
            }
        }
        return newValue;
    }
    private int calculateVehicleMovement(Vehicle v, int fixedAxisIndex, int upperBound, Vehicle[] transposeAisle, boolean isGoRightOrDown) {
        int newValue;
        if (isGoRightOrDown) {
            newValue = v.getValue() + vehicleFrameRate;
            if ((newValue+tile_size)/tile_size >= upperBound) {
                v.setGoRightOrDown();
                return calculateVehicleMovement(v, fixedAxisIndex, upperBound, transposeAisle, v.isGoRightOrDown());
            }
            Vehicle cars = transposeAisle[(newValue+tile_size)/tile_size];
             if(isTwoTileTouching(newValue, fixedAxisIndex*tile_size, ((newValue+tile_size)/tile_size)*tile_size, cars.getValue())) return v.getValue();
        } else {
            newValue = v.getValue() - vehicleFrameRate;
            if (newValue <= 0) {
                v.setGoRightOrDown();
                return calculateVehicleMovement(v, fixedAxisIndex, upperBound, transposeAisle, v.isGoRightOrDown());
            }
            Vehicle cars = transposeAisle[newValue/tile_size];
            if(isTwoTileTouching(newValue, fixedAxisIndex*tile_size, ((newValue)/tile_size)*tile_size, cars.getValue())) return v.getValue();
        }
        return newValue;
    }
    private boolean isTwoTileTouching(int firstX, int firstY, int secondX, int secondY) {
        return firstX < secondX+tile_size && firstX+tile_size > secondX && firstY < secondY+tile_size && firstY+tile_size > secondY;
    }
    public void initiateRandomVehicles(int vehicleCount) {
        vehiclesRow = new Vehicle[city.getRowCount()];
        vehiclesCol = new Vehicle[city.getColumnCount()];
        Random rand = new Random();
        for (int i = 0; i < vehiclesRow.length; i++) {
            vehiclesRow[i] = new Vehicle(rand.nextInt(city.getColumnCount())*tile_size);
        }
        for (int i = 0; i < vehiclesCol.length; i++) {
            vehiclesCol[i] = new Vehicle(rand.nextInt(city.getRowCount())*tile_size);
        }
    }
    /*************************************** Vehicles Ends ****************************************/

    void initDisaster(EventModel em){
        Disaster d = city.spawnDisaster();
        Random r = new Random();
        List<Building> bs = city.getBuildingList();
        int ind = r.nextInt(bs.size());
        Position ds = bs.get(ind).topLeft();
        anim.SetUp(d,ds);
    }


    // MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (EventModel.isFree()) return;
        EventModel em = EventModel.getEventModelInstance();
        // check if the target is occupied
        // check what needs to be built with em.getCellItem() and with Switch case and call respective methods from city.
        switch (em.getCellItem()){
            case RESIDENTIAL:
            case SERVICE_INDUSTRIAL:
                city.assignZone(em.getPosition(), em.getCellItem()); break;
            case V_ROAD:
            case JUNCTION_ROAD:
            case H_ROAD:
                city.buildRoad(em.getPosition(), em.getCellItem()); break;
            case STADIUM:
            case POLICE_DEPARTMENT:
            case POWER_PLANT:
                city.constructBuilding(em.getPosition(),em.getCellItem()); break;
            case J_TL:
            case H_TL:
            case V_TL:
            case TRANSMISSION_LINE:
                city.buildTransmissionLine(em.getPosition(),em.getCellItem()); break;
            case DEL_OPT:
                city.demolish(em.getPosition()); break;
            case DISASTER:
                if(!anim.isAnimating()){
                    initDisaster(em); break;
                }
            default:
                break;
        }
        city.setModifiedDate();
        initiateRandomVehicles(1);
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {
        EventModel.enableModel();
        //repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        EventModel.disableModel();
        //repaint();
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
        //repaint();
    }

    class FrameListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }

}

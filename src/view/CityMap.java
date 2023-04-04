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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class CityMap extends JPanel implements MouseMotionListener, MouseListener {
    private JFrame                  mainFrame;
    private int                     tile_size = 30;
    private Map<CellItem, Image>    graphics = Map.ofEntries(
            entry(CellItem.GENERAL, ResourceLoader.loadImage("resource/grass.png")),
            entry(CellItem.H_ROAD, ResourceLoader.loadImage("resource/h-road.png")),
            entry(CellItem.V_ROAD, ResourceLoader.loadImage("resource/v-road.png")),
            entry(CellItem.JUNCTION_ROAD, ResourceLoader.loadImage("resource/bot-right-road.png")),
            entry(CellItem.DEL_OPT, ResourceLoader.loadImage("resource/delete.png")),
            entry(CellItem.POLICE_DEPARTMENT, ResourceLoader.loadImage("resource/ps.png"))
            // you can add many graphic as you want
    );
    private final City              city;
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

package view.component;

import model.CellItem;
import model.Position;

import javax.swing.*;

public class EventModel {
    private static EventModel em = null;
    private Position position;
    private CellItem cell;
    private static boolean isFree = true;
    private static boolean isEnabled = true;
    private JButton btn = null;

    private EventModel(Position saved, CellItem cell) {
        this.cell = cell;
        this.position = saved;
    }
    private EventModel(CellItem cell) {
        this.cell = cell;
    }

    public void savePosition(Position p) {
        em.position = p;
    }
    public Position getPosition() { return em.position; }
    public CellItem getCellItem() { return em.cell; }


    public static EventModel getEventModelInstance() {
        return em;
    }
    public static EventModel EventModelInstance(CellItem cell) {
        if (!isEnabled) return null;
        if (isFree()) em = new EventModel(cell);
        isFree = false;
        return em;
    }
    public static EventModel EventModelInstance(Position saved, CellItem cell) {
        if (!isEnabled) return null;
        if (isFree()) em = new EventModel(saved, cell);
        isFree = false;
        return em;
    }
    public static void DeleteInstance() {
        isFree = true;
        em = null;
        isEnabled = true;
    }
    public static boolean isFree() {
        return isFree;
    }
    public static void disableModel() {
        if (em == null) return;
        isFree = true;
        isEnabled = false;
    }
    public static void enableModel() {
        if (em == null) return;
        isFree = false;
        isEnabled = true;
    }
    public static boolean isModelDisabled() {
        return !isEnabled;
    }
}

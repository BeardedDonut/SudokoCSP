package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by navid on 12/21/17.
 */
class RegionMap {
    private static int regionCounter = 0;
    //TODO this class should represents a region map
    private List<Cell> cells;

    private int regionId;

    public RegionMap(ArrayList<Cell> cells) {
        this.cells = cells;
        this.regionId = ++regionCounter;
    }

    public RegionMap() {
        this.cells = new ArrayList<Cell>();
        this.regionId = ++regionCounter;
    }

    public void addCell(Cell c) {
        cells.add(c);
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public int getRegionId() {
        return regionId;
    }
}

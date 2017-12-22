package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by navid on 12/21/17.
 */
class RegionMap {

    // <editor-fold desc="properties">

    /**
     * Region counter - Increments by instantiation
     */
    private static int regionCounter = 0;

    /**
     * Cells which are in this region
     */
    private List<Cell> cells;

    /**
     * The region Id
     */
    private int regionId;

    // </editor-fold>


    // <editor-fold desc="Constructors">

    /**
     * Constructor
     * @param cells: List of {@link Cell} cells that should be in the region
     */
    public RegionMap(ArrayList<Cell> cells) {
        this.cells = cells;
        this.regionId = ++regionCounter;
    }

    /**
     * Constructor
     */
    public RegionMap() {
        this.cells = new ArrayList<Cell>();
        this.regionId = ++regionCounter;
    }

    // </editor-fold>


    // <editor-fold desc="util functions">

    /**
     * This method adds a given cell to the cells List
     * @param c: {@link Cell} Given cell
     */
    public void addCell(Cell c) {
        cells.add(c);
    }

    // </editor-fold>


    //<editor-fold desc="getters and setters">
    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public int getRegionId() {
        return regionId;
    }
    //</editor-fold>
}

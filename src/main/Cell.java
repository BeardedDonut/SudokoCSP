package main;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by navid on 12/21/17.
 */

/**
 * This Class represents each cell in the Sudoku game board
 */
public class Cell {

    /**
     * This field identifies each cell which is
     * corresponding to each cell position
     */
    private int id;

    /**
     * This collection represents a collection of available
     * domains that can be assigned to this cell
     */
    private List<Object> domain;

    /**
     * Current assigned value of the cell
     */
    private Object currentValue;

    /**
     * Constructor
     *
     * @param id
     * @param value
     */
    public Cell(int id, Object value) {
        this.id = id;
        domain = new LinkedList<Object>();
        if (value == null) {
            for (int i = 0; i < 9; i++) {
                domain.add(i);
            }
        }

        this.currentValue = value;
    }

    // <editor-fold desc="getter and setters and toString">
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Object> getDomain() {
        return domain;
    }

    public void setDomain(List<Object> domain) {
        this.domain = domain;
    }

    public Object getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Object currentValue) {
        this.currentValue = currentValue;
    }

    @Override
    public String toString() {
        StringBuilder x = new StringBuilder();
        x.append("id:\t" + id + "\tvalue:\t" + currentValue);
        return x.toString();
    }
    // </editor-fold>
}

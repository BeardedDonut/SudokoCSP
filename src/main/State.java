package main;

/**
 * Created by navid on 12/21/17.
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class represents the state of the game board
 */
public class State {

    /**
     * Current cell assignments
     */
    private Map<Cell, Object> assignments = null;

    /**
     * current available domains for each cell
     */
    private Map<Cell, List<Object>> domains = null;

    /**
     * Constructor
     */
    public State() {
        assignments = new HashMap<Cell, Object>();
        domains = new HashMap<Cell, List<Object>>();
    }

    /**
     * This method assigns a new value to a cell and returns
     * the new state.
     *
     * @param cell:  cell which the value be assigned to
     * @param value: value of that cell
     * @return returns a new {@code State} after the assignment
     */
    public State assign(Cell cell, Object value) {
        State newState = new State();

        newState.setAssignments(new HashMap<Cell, Object>(assignments));
        newState.getAssignments().put(cell, value);

        newState.setDomains(new HashMap<Cell, List<Object>>(domains));

        List<Object> varDomain = new LinkedList<Object>();
        varDomain.add(value);
        newState.domains.put(cell, varDomain);

        return newState;
    }

    /**
     * Return the values in the domain of a given cell of this state
     *
     * @param cell: The given cell
     * @return: list of values of the given {@param cell}
     */
    public List<Object> getDomainValues(Cell cell) {
        List<Object> values = this.domains.get(cell);

        if (values != null) {
            return values;
        }

        return cell.getDomain();
    }

    // <editor-fold desc="getters and setters and toString">
    public Map<Cell, Object> getAssignments() {
        return assignments;
    }

    public void setAssignments(Map<Cell, Object> assignments) {
        this.assignments = assignments;
    }

    public Map<Cell, List<Object>> getDomains() {
        return domains;
    }

    public void setDomains(Map<Cell, List<Object>> domains) {
        this.domains = domains;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Cell c : assignments.keySet()) {
            s.append(c.toString() + "|=" + assignments.get(c) + "\n");
        }

        s.append("\n");

        for (Cell c : domains.keySet()) {
            s.append(c.toString() + "|=" + domains.get(c) + "\n");
        }

        return s.toString();
    }
    // </editor-fold>
}

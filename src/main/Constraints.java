package main;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by navid on 12/21/17.
 */
public class Constraints {

    /**
     * A human readable expression of this constraint
     */
    private String description;

    /**
     * This fields shows the constraint type of the this constraint
     */
    private ConstraintType myConstraintType;

    /**
     * This field represents cell which are under the influence of t
     * his constraint.
     */
    private List<Cell> cells;

    /**
     * Constructor
     *
     * @param description: a human readable description of this constraint
     * @param cst:         Constraint Type indicator
     */
    public Constraints(String description, ConstraintType cst) {
        cells = new LinkedList<Cell>();
        this.description = description;
        this.myConstraintType = cst;
    }

    public boolean satisfied(State state, final Integer boardSize) {
        boolean[] visited = new boolean[boardSize + 1];

        for (Cell cell : cells) {
            Integer value = (Integer) state.getAssignments().get(cell);

            if (value == null || visited[value]) {
                return false;
            }

            visited[value] = true;
        }

        return true;
    }

    public boolean consistent(State state, final Integer boardSize) {
        boolean[] visited = new boolean[boardSize + 1];
        boolean[] free = new boolean[boardSize + 1];
        int numValues = 0;

        for (Cell cell : cells) {
            for (Object value : state.getDomainValues(cell)) {
                if (!free[(Integer) value]) {
                    numValues++;
                    free[(Integer) value] = true;
                }
            }

            Integer value = (Integer) state.getAssignments().get(cell);
            if (value != null) {
                if (visited[value])
                    return false;
                visited[value] = true;
            }
        }

        if (cells.size() > numValues)
            return false;
        return true;
    }

    // <editor-fold desc = "getter and setters and toString">
    @Override
    public String toString() {
        StringBuilder x = new StringBuilder();

        x.append("Description:\n\t" + description + "\n");

        for (Cell c : cells) {
            x.append(c.toString() + "\n");
        }

        return x.toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ConstraintType getMyConstraintType() {
        return myConstraintType;
    }

    public void setMyConstraintType(ConstraintType myConstraintType) {
        this.myConstraintType = myConstraintType;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    // </editor-fold>
}

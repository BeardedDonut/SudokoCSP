package main;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by navid on 12/21/17.
 */
class Constraint {

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
    private List<Cell> retlatedCells;

    /**
     * Constructor
     *
     * @param description: a human readable description of this constraint
     * @param cst:         Constraint Type indicator
     */
    public Constraint(String description, ConstraintType cst) {
        retlatedCells = new LinkedList<Cell>();
        this.description = description;
        this.myConstraintType = cst;
    }

    public boolean satisfied(State state, final Integer boardSize) {
        boolean[] visited = new boolean[boardSize + 1];

        for (Cell cell : retlatedCells) {
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

        for (Cell cell : retlatedCells) {
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

        if (retlatedCells.size() > numValues)
            return false;
        return true;
    }

    public boolean isRelatedTo(int cellId) {
        for (Cell c : this.retlatedCells) {
            if (cellId == c.getId()) {
                return true;
            }
        }

        return false;
    }

    public boolean isRelatedTo(Cell cell) {
        return this.isRelatedTo(cell.getId());
    }

    // <editor-fold desc = "getter and setters and toString">
    @Override
    public String toString() {
        StringBuilder x = new StringBuilder();

        x.append("Description:\n\t" + description + "\n");

        for (Cell c : retlatedCells) {
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

    public List<Cell> getRetlatedCells() {
        return retlatedCells;
    }

    public void setRetlatedCells(List<Cell> retlatedCells) {
        this.retlatedCells = retlatedCells;
    }

    // </editor-fold>
}

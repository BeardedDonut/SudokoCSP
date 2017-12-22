package main;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by navid on 12/21/17.
 */
class Constraint {
    // <editor-fold desc="properties">
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
    private List<Cell> relatedCells;
    // </editor-fold>


    // <editor-fold desc="constructors">

    /**
     * Constructor
     *
     * @param description: a human readable description of this constraint
     * @param cst:         Constraint Type indicator
     */
    public Constraint(String description, ConstraintType cst) {
        relatedCells = new LinkedList<Cell>();
        this.description = description;
        this.myConstraintType = cst;
    }

    // </editor-fold>s


    // <editor-fold desc="util methods">

    /**
     * This method checks whether this constraint is satisfied
     * in the given state or not.
     * @param state: Given state
     * @param boardSize: Given boardSize
     */
    public boolean satisfied(State state, final Integer boardSize) {
        boolean[] visited = new boolean[boardSize + 1];

        for (Cell cell : relatedCells) {
            Integer value = (Integer) state.getAssignments().get(cell);

            if (value == null || visited[value]) {
                return false;
            }

            visited[value] = true;
        }

        return true;
    }

    /**
     * This method checks whether the state is consistent or not
     * @param state: Given state
     * @param boardSize: Given boardSize
     * @return whether state is consistent or not
     */
    public boolean consistent(State state, final Integer boardSize) {
        boolean[] visited = new boolean[boardSize + 1];
        boolean[] free = new boolean[boardSize + 1];
        int numValues = 0;

        for (Cell cell : relatedCells) {
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

        if (relatedCells.size() > numValues)
            return false;
        return true;
    }

    /**
     * This method verifies whether a cell is affected by this constraint or not!
     * @param cellId: Given cellId of the cell
     */
    public boolean isRelatedTo(int cellId) {
        for (Cell c : this.relatedCells) {
            if (cellId == c.getId()) {
                return true;
            }
        }

        return false;
    }

    /**
     * This method verifies whether a cell is affected by this constraint or not!
     * @param cell: Given Cell
     */
    public boolean isRelatedTo(Cell cell) {
        return this.isRelatedTo(cell.getId());
    }

    // </editor-fold>


    // <editor-fold desc = "getter and setters and toString">
    @Override
    public String toString() {
        StringBuilder x = new StringBuilder();

        x.append("Description:\n\t" + description + "\n");

        for (Cell c : relatedCells) {
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

    public List<Cell> getRelatedCells() {
        return relatedCells;
    }

    public void setRelatedCells(List<Cell> relatedCells) {
        this.relatedCells = relatedCells;
    }

    // </editor-fold>
}

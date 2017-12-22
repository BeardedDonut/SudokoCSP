package main;

import java.util.*;

/**
 * Created by navid on 12/21/17.
 */
public class Sudoku {

    // <editor-fold desc="properties">

    /**
     * Size of the Sudoku board, commonly is 9
     */
    private Integer boardSize;
    /**
     * List of cells
     */
    private List<Cell> cells;
    /**
     * List of all Constraint rules instances
     */
    private List<Constraint> constraintRules;
    /**
     * Initial state of the Sudoku board
     */
    private State base;
    /**
     * A map whch relate each cell with it's constraints
     */
    private Map<Cell, List<Constraint>> cellCsRules;

    /**
     * A map that maps each region Id to it's region map
     */
    private Map<Integer, RegionMap> regions;

    /**
     * A number which measures the number of expanded nodes
     */
    private int nodes;
    /**
     * Variable used to measure the execution time.
     */
    private double takenTime;

    // </editor-fold>


    // <editor-fold desc="Constructor">

    /**
     * Constructot
     *
     * @param size:   size of the map
     * @param regMap: given region map
     */
    public Sudoku(int size) {
        this.boardSize = size;
        this.base = new State();

        constraintRules = new ArrayList<Constraint>();
        this.regions = new HashMap<Integer, RegionMap>();
        cells = new ArrayList<Cell>();

        nodes = 0;
        takenTime = 0.00f;

        int numberOfCells = boardSize * boardSize;
        for (int i = 0; i < numberOfCells; i++) {
            cells.add(new Cell(i, null));
        }
        System.out.println("Ha!");
        generateConstraints();
    }


    // </editor-fold>


    // <editor-fold desc="util functions">

    /**
     * This method returns the corresponding region map of
     * the given region id.
     * @param regId: Region Id
     * @return: corresponding RegionMap
     */
    public RegionMap getRegion(Integer regId) {

        if(this.regions.get(regId) == null) {
            RegionMap temp = new RegionMap();
            this.regions.put(regId, temp);
            return temp;
        }

        return this.regions.get(regId);

    }

    /**
     * This method generate Row and Column Constraints
     */
    public void generateConstraints() {

        // ROW constraints
        for (int row = 0; row < boardSize; row++) {
            Constraint rule = new Constraint("All Diffrerent Constriant", ConstraintType.ROW);
            for (int col = 0; col < boardSize; col++) {
                rule.getRelatedCells().add(cells.get(row * boardSize + col));
            }
            this.constraintRules.add(rule);
        }

        // COLUMN constraints
        for (int col = 0; col < boardSize; col++) {
            Constraint rule = new Constraint("All Diffrerent Constriant", ConstraintType.COLUMN);
            for (int row = 0; row < boardSize; row++) {
                rule.getRelatedCells().add(cells.get(row * boardSize + col));
            }
            this.constraintRules.add(rule);
        }
    }

    /**
     * This method generates Regional Constraints
     */
    public void generateRegionConstraints(){
        //TODO create regions instead of sub squares
        for(Integer regId: regions.keySet()) {
            Constraint cst = new Constraint("All Different Constraint in Region",ConstraintType.REGION);
            List<Cell> affectedCells = regions.get(regId).getCells();
            for(Cell afCell: affectedCells) {
                cst.getRelatedCells().add(afCell);
            }
            this.constraintRules.add(cst);
        }

    }

    /**
     * This method verifies if the a sudoku is solved
     * provided it's state
     *
     * @param state: given state
     * @return
     */
    public boolean isSudokuSolved(State state) {

        if (cells.size() > state.getAssignments().size()) {
            return false;
        }
        for (Constraint c : constraintRules) {
            if (!c.satisfied(state, this.boardSize)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method returns a list of cells in a given state
     * which they are not assigned with a value.
     *
     * @param state: Given State
     * @return
     */
    public List<Cell> getAllUnassignedCells(State state) {
        List<Cell> list = new LinkedList<Cell>();
        for (Cell c : cells) {
            if (state.getAssignments().get(c) == null) {
                list.add(c);
            }
        }
        return list;
    }

    /**
     * This method returns an unassigned cell in the given state.
     * This method does not benefit from any heuristics.
     * @param state: Given state
     * @return : First unassigned cell it founds
     */
    public Cell getUnassignedCell(State state) {
        for (Cell c : cells) {
            if (state.getAssignments().get(c) == null) {
                return c;
            }
        }
        return null;
    }

    /**
     * This method finds an unassigned cell
     * as the Most Constrained Variable(Smaller domain)
     * in a given state.
     *
     * @param state: Given state
     * @return : {@link Cell} cell with smallest domain in the given state
     */
    public Cell getUnassignedCellWithHeuristicMCV(State state) {
        int min = Integer.MAX_VALUE;
        Cell minCell = null;

        if (cells.size() == state.getAssignments().size()) {
            return null;        /* if they are all assigned then return null! */
        }

        for (Cell c : cells) {
            if (state.getAssignments().get(c) == null) {
                int valuesSize = state.getDomainValues(c).size();

                if (valuesSize < min) {
                    min = valuesSize;
                    minCell = c;
                }
            }
        }

        return minCell;
    }

    /**
     * This method finds an unassigned cell in the given
     * state as the Most Constraining Variable among all
     * cells found by the Most Constrained Variable Heuristic.
     *
     * @param state: Given State
     * @return : {@link Cell} fitted Cell
     */
    public Cell getUnassignedCellWithHeuristicMCVAndMinimumConstrainingVariable(State state) {
        List<Cell> cellsList;

        if (this.cells.size() == state.getAssignments().size()) {
            return null;
        }

        /*
            A sorted map(sorted by key values) of Most constrained cells
            -- Most Constrained Variable Heuristic
         */
        TreeMap<Integer, List<Cell>> sortedMCVTree = new TreeMap<Integer, List<Cell>>();

        /*
            Finding all Most Constrained cells of the given
            state and store them in a map
            -- Most Constrained Variable Heuristic
        */
        for (Cell c : this.cells) {
            if (state.getAssignments().get(c) == null) {
                int valuesSize = state.getDomainValues(c).size();

                if (sortedMCVTree.containsKey(valuesSize)) {
                    sortedMCVTree.get(valuesSize).add(c);
                } else {
                    cellsList = new LinkedList<Cell>();
                    cellsList.add(c);
                    sortedMCVTree.put(valuesSize, cellsList);
                }
            }
        }

        /*
         * Applying Most Constraining Variable Heuristic...
         */
        cellsList = sortedMCVTree.get(sortedMCVTree.firstKey());
        int maxConstrainingDegree = Integer.MIN_VALUE;
        int currentConstrainingDegree = 0;
        Cell fitCell = null;

        if (cellsList.size() == 1) {
            return cellsList.get(0);
        }

        for (int i = 0; i < cellsList.size(); i++) {
            Cell currentCell = cellsList.get(i);

            for (Constraint cst : this.constraintRules) {
                if (cst.getRelatedCells().contains(currentCell)) {
                    currentConstrainingDegree++;
                }
            }

            if (currentConstrainingDegree > maxConstrainingDegree) {
                maxConstrainingDegree = currentConstrainingDegree;
                fitCell = currentCell;
            }

        }

        return fitCell;
    }

    /**
     * This method finds the best value in a given state
     * to assign for the given Cell(Variable) using the
     * Least Constraining Value heuristic.
     *
     * @param state:     Given state
     * @param domain:    Domains in which we need to find best fit value for the cell
     * @param givenCell: Given cell
     * @return: Least Constraining Value to assign
     */
    public Object findBestValueUsingLCV(State state, List<Object> domain, Cell givenCell) {
        List<Cell> unassignedCells = getAllUnassignedCells(state);
        int min = Integer.MAX_VALUE;
        Object bestValue = null;
        int count = 0;

        for (Object domainVal : domain) {
            count = 0;

            for (Cell currentCell : unassignedCells) {
                if (currentCell.getId() == givenCell.getId()) {
                    continue;
                }
                if (state.getDomains().get(currentCell).contains(domainVal) &&
                        isRelated(currentCell, givenCell)) {
                    count++;
                }
            }

            if (count < min) {
                min = count;
                bestValue = domainVal;
            }
        }

        return bestValue;
    }

    /**
     * This is Forward checking algorithm.
     * This method applies forward checking on a given cell in the given state/
     * @param state: Given state
     * @param givenCell: Given cell
     * @return: a state which shows how forward checking impacts the state
     */
    public State forwardChecking(State state, Cell givenCell) {
        List<Constraint> relatedConstraintsOfCell = this.findRelatedRules(givenCell);
        Object assignedValue = state.getAssignments().get(givenCell);

        for (Constraint cst : relatedConstraintsOfCell) {
            for (Cell c : cst.getRelatedCells()) {
                if (c == givenCell) {
                    continue;
                }

                List<Object> values = state.getDomainValues(c);
                if (values.contains(assignedValue)) {
                    values = new LinkedList<Object>(values);
                    values.remove(assignedValue);

                    state.getDomains().put(c, values);
                    if (state.getAssignments().get(c) == null) {
                        if (values.size() == 1) {
                            state = state.assign(c, values.get(0));
                        } else if (values.size() == 0) {
                            continue;
                        }
                    }
                }
            }
        }
        return state;
    }

    /**
     * This methods checks whether given state is consistent or not.
     *
     * @param state: Given sate
     * @return: indicates consistency of the given state
     */
    public boolean isStateConsistent(State state) {
        for (Constraint cst : this.constraintRules) {
            if (!cst.consistent(state, this.boardSize)) {
                return false;
            }
        }
        return true;
    }

    /**
     * BackTracking search algorithm
     * This method initializes primary variables and then calls the {@link #recursiveBacktrackSearch(State, int)}
     * to do the job based on the given type;
     * @param type: type of which algorithm is needed
     * @return: returns the final state
     * type 0 = pure backtracking without any heuristics
     * type 1 = backtracking with Forward Checking enabled
     * type 2 = backtracking with Forward Checking and applied 3 heuristic which are listed below:
     * {Most Constrained Variable, Most Constraining Variable, Least Constraining Value}
     */
    public State backtrackSearchInit(int type) {
        this.nodes = 0;
        long tStart = System.nanoTime();
        State startState = this.base;
        State endState = recursiveBacktrackSearch(startState, type);
        long tEnd = System.nanoTime();
        this.takenTime = (double) (tEnd - tStart) / 1000000000.0f;
        return endState;
    }

    /**
     * This is the recursive backtracking algorithm!
     * @param state: given state
     * @param type: type of algorithm watch this link for more info {@link #backtrackSearchInit(int)}
     */
    public State recursiveBacktrackSearch(State state, int type) {
        nodes++;
        if (isSudokuSolved(state)) {
            return state;
        }

        Cell cell;
        if (type == 2) {
            cell = getUnassignedCellWithHeuristicMCVAndMinimumConstrainingVariable(state);
        } else if (type == 1){
            cell = getUnassignedCellWithHeuristicMCV(state);
        } else {
            cell = getUnassignedCell(state);
        }

        if (cell == null) {
            return null;
        }

        List<Object> values = state.getDomainValues(cell);
        if (type == 2) {
            Object value = findBestValueUsingLCV(state, values, cell);
            values.remove(value);
            values.add(0, value);
        }

        for (Object value : values) {
            State a2 = state.assign(cell, value);

            if (type > 0) {
                a2 = forwardChecking(a2, cell);
            }

            if (!isStateConsistent(a2)) {
                continue;
            }
            a2 = recursiveBacktrackSearch(a2, type);
            if (a2 != null) {
                return a2;
            }
        }
        return null;
    }

    /**
     * This method returns the constraints that affect the given cell.
     *
     * @param cell: A given cell.
     */
    public List<Constraint> findRelatedRules(Cell cell) {
        if (this.cellCsRules != null) {
            return cellCsRules.get(cell);
        }

        this.cellCsRules = new HashMap<Cell, List<Constraint>>();

        for (Constraint cst : this.constraintRules) {
            for (Cell c : cst.getRelatedCells()) {
                if (cellCsRules.containsKey(c)) {
                    cellCsRules.get(c).add(cst);
                } else {
                    List<Constraint> myRulesList = new LinkedList<Constraint>();
                    myRulesList.add(cst);
                    cellCsRules.put(c, myRulesList);
                }
            }
        }

        return cellCsRules.get(cell);
    }

    /**
     * This function returns true if there is at least
     * one constraint between these two cells.
     *
     * @param c1: first {@link Cell} cell
     * @param c2: Second {@link Cell} cell
     * @return: indicates whether c2 and c1 inter related or not
     */
    public boolean isRelated(Cell c1, Cell c2) {

        for (Constraint cst : this.constraintRules) {
            if (cst.isRelatedTo(c1) && cst.isRelatedTo(c2)) {
                return true;
            }
        }

        return false;
    }


    // </editor-fold>


    // <editor-fold desc="getters and setters">
    public Integer getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(Integer boardSize) {
        this.boardSize = boardSize;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Constraint> getConstraintRules() {
        return constraintRules;
    }

    public void setConstraintRules(List<Constraint> constraintRules) {
        this.constraintRules = constraintRules;
    }

    public State getBase() {
        return base;
    }

    public void setBase(State base) {
        this.base = base;
    }

    public Map<Cell, List<Constraint>> getCellCsRules() {
        return cellCsRules;
    }

    public void setCellCsRules(Map<Cell, List<Constraint>> cellCsRules) {
        this.cellCsRules = cellCsRules;
    }

    public int getNodes() {
        return nodes;
    }

    public void setNodes(int nodes) {
        this.nodes = nodes;
    }

    public double getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(double takenTime) {
        this.takenTime = takenTime;
    }

    // </editor-fold>
}

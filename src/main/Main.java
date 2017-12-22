package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

/**
 * Created by navid on 12/21/17.
 */
public class Main {

    public static void main(String... args) {
        Sudoku sudo, old_sudo = null;
        int runs=1;
        try {
            for (int type = 0; type < 3; type++) {
                BufferedReader in = new BufferedReader(new FileReader("./data/test.txt"));
                String line;
                int board=0;
                while( (line = in.readLine()) != null){
                    for (int run = 0; run < runs; run++) {
                        if(run==0){
                            int size = new Integer(line);
                            sudo = new Sudoku(size, null);
                            int cell_id, cell_num;
                            String row;
                            for (int x = 0; x < sudo.getBoardSize(); x++) {
                                row=in.readLine();
                                for (int y = 0; y < sudo.getBoardSize(); y++) {
                                    cell_id = x * 9 + y;
                                    if(row.charAt(y) != 'x'){
                                        cell_num = Character.getNumericValue(row.charAt(y));
                                        Cell cell = sudo.getCells().get(cell_id);
                                        cell.setCurrentValue(cell_num);
                                        cell.setDomain(new LinkedList<Object>());
                                        sudo.getCells().set(cell_id,cell);

                                        sudo.setBase(sudo.getBase().assign(cell, cell_num));
                                        if(type>0){
                                            sudo.setBase(sudo.forwardChecking(sudo.getBase(), cell));
                                        }                    }
                                }
                            }
                            old_sudo = sudo;
                        }else
                            sudo = old_sudo;

                        State solution = sudo.backtrackSearchInit(type);
                        if (solution == null){
                            System.out.println("BAD\tb"+board+"\tt"+type+"\tr"+(run+1)+"\tnodes "+sudo.getNodes()+"\ttime "+sudo.getTakenTime());
                            continue;
                        }
                        System.out.println("OK\tb"+board+"\tt"+type+"\tr"+(run+1)+"\tnodes "+sudo.getNodes()+"\ttime "+sudo.getTakenTime());
                        System.out.println(solution.printStateBoard(sudo.getBoardSize(), sudo.getCells()));
                    }
                    board++;
                    //System.out.println();
                }
                in.close();
            }
        }catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Error");
            //System.exit(1);
        }
    }

}

package src;

import szte.mi.Move;

import java.util.ArrayList;
import java.util.Scanner;

public class Player {


    int size=8;
    int [] directionX= {-1, 0, 1, -1, 1, -1, 0, 1};
    int [] directionY= {-1, -1, -1, 0, 0, 1, 1, 1};


    private Field[][] board;

    //number of moves
    private int moves;

    //Black, White and Free Fields
    public enum Field{
        B,W,FREE}

    //Whos turn is it
    private Field turn;



    //condtructor for the player, black has always the first turn
    public Player(){
        initBoard(size);
        this.turn=Field.B;
        this.moves=0;
    }

    //initialize the othello board
    public Field[][] initBoard(int size){
        board = new Field[size][size];
        for(int y=0; y<board.length;y++) {
            for (int x = 0; x < board[y].length; x++) {
                if(y==3&&x==3 || y==4&&x==4) board[y][x] = Field.W;
                else if (y==3&&x==4 || y==4&&x==3) board[y][x] = Field.B;
                else board[y][x] = Field.FREE;
            }
        }
        printBoard();
        return board;
    }


    public boolean isGameOver(){
        if(playerAllowedToMove(Field.B)==false && playerAllowedToMove(Field.W)==false)
            return true;
        else return false;
    }


    //prints the board
    public void printBoard(){
        System.out.println("  0   1   2   3   4   5   6   7  ");
        for (int i=0; i<size; i++) {
            System.out.println("---------------------------------");
            System.out.print("| ");
            for (int j=0; j<size; j++)
                System.out.print(getSymbol(board[i][j])+" | ");
                System.out.println(i);
        }
        System.out.println("---------------------------------");
        }




    //return the String for the Symbols on the board fro printing on console
    private String getSymbol(Field field) {

        if (field == Field.B)
            return "B";
        else if (field == Field.W)
            return "W";
        else
            return " ";
    }


    //counts all symbols from one specific type of field
    //e.g- how many black or white stones are on the board
    private int countSymbols(Field f){
        int count=0;
        for(int y=0; y<size;y++) {
            for (int x = 0; x < board[y].length; x++) {
                if(board[y][x] == f) count++;
            }
        }
        return count;
    }




    //flip the symbol from black to white and the other way around in specific direction
    public void changeSymbol(Field f, int x, int y, int direction) {
        if (needToFlip(x, y, f, direction)) {
            x += this.directionX[direction];
            y += this.directionY[direction];
            if(!(x==-1||y==-1)&&!(x == 8 || y == 8)){
                while (board[y][x] != f) {
                    board[y][x] = f;
                    this.moves++;
                    x += this.directionX[direction];
                    y += this.directionY[direction];
                    if((x==-1||y==-1 || x == 8 || y == 8)) break;
                }
            }
        }
    }

    //gives the other symbol if its black or white. otherwise it return free
    public Field oppositeSymbol(Field f){
        if (f == Field.B)
            return Field.W;
        else if (f == Field.W)
            return Field.B;
        else return Field.FREE;
    }

    //checks if the field in given direction need to be flipped
    public boolean needToFlip(int x, int y, Field f, int direction) {
        boolean check = false;
        for (int i = 0; i < size; i++) {
            x += this.directionX[direction];
            y += this.directionY[direction];
            if (x == -1 || y == -1 || x == 8 || y == 8) return false;
            if (board[y][x] == oppositeSymbol(f)) {
                check = true;
            }
            else if (board[y][x] == f){
                if(check) return true;
                else return false;

            }
            else return false;
        }
        return false;
    }

    //is the player allowed to move? if not pass or lost
    public boolean playerAllowedToMove(Field f){
        if(turn==f) {
            for (int y = 1; y < size - 1; y++)
                for (int x = 1; x < size - 1; x++)
                    if (moveValid(f, x, y)) {
                        return true;
                    }
        }
        return false;
    }


    //is this move allowed?
    public boolean moveValid(Field f, int x, int y ){
        if(board[y][x] == Field.FREE){
            for(int i=0;i<size;i++)
                if(needToFlip(x, y, f ,i)) {
                    return true;
                }
        }
        return false;
    }

    public ArrayList<Move> allValidMoves(Field f) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for(int x=0; x<size; x++) {
            for(int y=0; y<size; y++) {
                if(moveValid(f,x,y)) {
                    Move aMove = new Move(x,y);
                    possibleMoves.add(aMove);
                }
            }
        }
        return possibleMoves;
    }

    public void movesToString(ArrayList<Move> move){
        System.out.println("All your valid moves:");
        for(Move m : move){
            String string= " ("+m.x+","+m.y+") ";
            System.out.print(string);
        }
        System.out.println();

    }



    public boolean rightPosition(int x, int y) {
        if(x<0 || x>7 || y<0 || y>7) return false;
        return true;
    }



    public void makeMove(Field f, Move move){
        if(turn==f) {
                if (moveValid(f, move.x, move.y)) {
                    board[move.y][move.x] = f;
                    this.turn = changePlayer(f);
                    for (int i = 0; i < size; i++) {
                        changeSymbol(f, move.x, move.y, i);
                    }
                    printBoard();
                } else System.out.println("Move is not valid!");
        }
        else System.out.println("It is not your turn!");
    }

    public void getInput() {
        boolean check = true;
        Scanner sc = new Scanner(System.in);
        int x, y;
        Move move = null;
        while (check) {
            System.out.println(turn + "'s Turn");
            ArrayList<Move> list= allValidMoves(turn);
            movesToString(list);
            System.out.println("Enter your next valid move: x and y-axis values 0 to 7 divided by a comma (e.g 3,4)");
            try {
                String[] input = sc.nextLine().split(",");
                x = Integer.parseInt(input[0]);
                y = Integer.parseInt(input[1]);
                move = new Move(x, y);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input!");
            }
                if(rightPosition(move.x,move.y)){
                    if (playerAllowedToMove(turn)) {
                        System.out.println("The Move is:" + move.x + "," + move.y);
                        makeMove(turn, move);
                    } else if (board[move.y][move.x] != Field.FREE) {
                        System.out.println("This field is not free. Choose another one.");
                    } else {
                        System.out.println("You have no valid moves, you have to pass. It's " + oppositeSymbol(turn) + " turn now.");
                        turn = changePlayer(turn);
                        if (isGameOver()) {
                            check = false;
                            showWinner();
                        }
                    }

                }
                else System.out.println("The numbers both have to be between 0 and 7. Try it again!");


            }
        }


        public void showWinner () {
            System.out.println(countSymbols(Field.B) + ": black Stones and " + countSymbols(Field.W) + " white Stones.");
            if (countSymbols(Field.B) > countSymbols(Field.W)) System.out.println("Black won!");
            else if (countSymbols(Field.W) > countSymbols(Field.B)) System.out.println("White won!");
            else System.out.println("Draw!");
        }


        public Field changePlayer (Field f){
            if (f == Field.B) return Field.W;
            else return Field.B;
        }


    }

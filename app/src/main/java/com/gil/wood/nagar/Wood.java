package com.gil.wood.nagar;

import java.util.ArrayList;

import static com.gil.wood.nagar.main.round;

/**
 * Created by Gil on 28/11/2017.
 */

public class Wood {
    public double origSize;
    public double remainingSize;
    public ArrayList<Piece> piecesList;
    public boolean isMissing;
    public double missingSize;
    public boolean efficient;

    //Constants:

    public static final double CUTTING_WASTE = 0.3;
    public static final double LOW_EFFICIENCY_BOTTOM_BOUND1 = 15;
    public static final double LOW_EFFICIENCY_BOTTOM_BOUND2 = 17;
    public static final double LOW_EFFICIENCY_BOTTOM_BOUND3 = 20;
    public static final double LOW_EFFICIENCY_TOP_BOUND = 40;
    public static final double EFFICIENCY_ORIG_SIZE_STEP1 = 40;
    public static final double EFFICIENCY_ORIG_SIZE_STEP2 = 60;
    public static final double EFFICIENCY_ORIG_SIZE_STEP3 = 140;
    public static final double GOOD_RELATION = 0.15;
    public static final double BAD_RELATION = 0.33;
    public static final double BAD_FOR_LAST_RELATION = 0.5;


    public Wood(double origSize) {
        this.origSize = origSize;
        this.remainingSize = origSize;
        this.piecesList = new ArrayList<>();
        this.isMissing = false;
        this.missingSize = 0;
        this.efficient = false;
    }

    public void add(double pieceSize) {
        piecesList.add(new Piece(pieceSize));
        this.remainingSize = round(this.remainingSize - pieceSize - CUTTING_WASTE);
        if (this.remainingSize <0) this.remainingSize = 0;
        this.efficient = !isLowEfficiency();
    }

    public double removeLastAdded() {
        double lastAddedSize = piecesList.remove(piecesList.size()-1).size;
        this.remainingSize = round(this.remainingSize + lastAddedSize + CUTTING_WASTE);
        if (this.remainingSize <0) this.remainingSize = 0;
        this.efficient = !isLowEfficiency();
        return lastAddedSize;
    }

    public void addMissing(double pieceSize) {
        piecesList.add(new Piece(pieceSize));
        if (this.isMissing == false)this.missingSize = - CUTTING_WASTE;
        this.isMissing = true;
        this.missingSize = round(this.missingSize + pieceSize + CUTTING_WASTE);
    }

    public boolean isEmpty() {
        return piecesList.isEmpty();
    }


    public String toString() {
        String piecesString = "";
        String result;
        for (Piece currentPiece : this.piecesList) {
            piecesString = piecesString + ", " + String.valueOf(currentPiece.size);
        }
        if (piecesString.length() > 0) piecesString = piecesString.substring(1);

        if (this.isMissing) {
            result = "~~~~~~~~~~~" + System.lineSeparator() + "Missing Wood!!!"  +  System.lineSeparator() +  System.lineSeparator() +  "maximal size = "+ this.missingSize + System.lineSeparator() +
                    "pieces: " + piecesString + System.lineSeparator()  + "~~~~~~~~~~~" ;
        } else {
            result = "original size = " + origSize + System.lineSeparator() +
                    "remaining size = " + remainingSize + System.lineSeparator() +
                    "pieces: " + piecesString + System.lineSeparator() + System.lineSeparator();
        }


        return result;
    }



    /********************************************************/
    /***********singular optimizations functions*************/
    /********************************************************/

    public boolean isLowEfficiencyForSingularOptim() {
        return ((origSize > EFFICIENCY_ORIG_SIZE_STEP2) && ((remainingSize / origSize) > BAD_RELATION));
    }
    public boolean isLowEfficiencyLastForSingularOptim() {
        return ((origSize > EFFICIENCY_ORIG_SIZE_STEP2) && ((remainingSize / origSize) > BAD_FOR_LAST_RELATION));
    }
    public boolean isGoodEfficiencyForSingularOptim() {
        return ((origSize > EFFICIENCY_ORIG_SIZE_STEP2) && ((remainingSize / origSize) < GOOD_RELATION));
    }
    public double getDeficiency() {
        return (remainingSize / origSize);
    }


    /********************************************************/
    /*****greedy efficiency optimizations functions**********/
    /********************************************************/


    public boolean isBaseSizeAndLowEfficiency() {
        return (origSize > EFFICIENCY_ORIG_SIZE_STEP3) && (remainingSize > LOW_EFFICIENCY_BOTTOM_BOUND3) && (remainingSize < LOW_EFFICIENCY_TOP_BOUND);
    }

    public boolean isLowEfficiency() {
        if ((origSize <= EFFICIENCY_ORIG_SIZE_STEP1) && (remainingSize > LOW_EFFICIENCY_BOTTOM_BOUND1) ) return true;
        if ((origSize <= EFFICIENCY_ORIG_SIZE_STEP2) && (remainingSize > LOW_EFFICIENCY_BOTTOM_BOUND2) ) return true;
        if ((origSize <= EFFICIENCY_ORIG_SIZE_STEP3) && (remainingSize > LOW_EFFICIENCY_BOTTOM_BOUND3) && (remainingSize < LOW_EFFICIENCY_TOP_BOUND) ) return true;
        return false;

    }
    public void lastWood() {
        efficient = true;
    }
    public boolean isWoodEfficient() {
        return efficient;
    }



}

package com.gil.wood.nagar;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.*;

import static com.gil.wood.nagar.Wood.CUTTING_WASTE;


/**
 * Created by Gil on 28/11/2017.
 */

public class main {
    public static final double NULL_VALUE = 0;

    public static void main(String[] args) {
        System.out.println("hello");
        test();
    }


    /********************************************************/
    /*********************STATIC FUNCTIONS*******************/
    /********************************************************/

    public static int getNoNullLength(double[] array) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != NULL_VALUE)
                ++count;
        }
        return count;
    }


    /**
     * sorts an array in a descending order
     *
     * @param array
     */
    public static void sortArrayDes(double[] array) {

        for (int i = 0; i < array.length; i++)
            array[i] = -array[i];
        Arrays.sort(array);
        for (int i = 0; i < array.length; i++)
            array[i] = -array[i];
        for (int i = 0; i < array.length; i++)
            System.out.print(array[i]);

        return;
    }

    public static double round(double value) {
        int places = 1;
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * removes an inventory wood from an inventory wood sizes array
     *
     * @param availableWoodList all available inventory wood (sizes)
     * @param badValue          size of a problematic inventory wood to be removed
     * @return a copy of the inventory wood sizes array, without the problem element
     */
    public static double[] removeElementFromArrayByValue(double[] availableWoodList, double badValue) {
        if (badValue == 0) return availableWoodList;
        double[] newWoodList = new double[availableWoodList.length - 1];
        int index = 0;
        while (index < availableWoodList.length && availableWoodList[index] != badValue) {
            newWoodList[index] = availableWoodList[index];
            index += 1;
        }
        for (int i = index; i + 1 < availableWoodList.length; i++) {
            newWoodList[i] = availableWoodList[i + 1];
        }
        return newWoodList;
    }


    /********************************************************/
    /********************BRAIN FUNCTIONS*********************/
    /********************************************************/


    /**
     * assigns demanded pieces to a current inventory wood greedily
     * when greedy Efficiency is activated:
     * will avoid using short inventory pieces that grant low efficiency
     * will split the division in the larger (close to base size) inventory pieces so the leftover is efficient.
     *
     * @param remainingDemandPieces all pieces that are needed to complete the project
     * @param remainingSize         the size of the current inventory wood
     * @param greedyEfficiency      activates greedy efficiency
     * @return wood type that contains the division to pieces
     */
    public static Wood divideWood(double[] remainingDemandPieces, double remainingSize, boolean greedyEfficiency) { //remaining size is orig size at start

        sortArrayDes(remainingDemandPieces);
        Wood currentWood = new Wood(remainingSize);
        int AvailablePiecesAmount = getNoNullLength(remainingDemandPieces);
        for (int i = 0; i < AvailablePiecesAmount; i++) {
            if (remainingDemandPieces[i] <= remainingSize) {
                currentWood.add(remainingDemandPieces[i]);
                remainingSize = round(remainingSize - remainingDemandPieces[i] - CUTTING_WASTE);
                System.out.println("the preior value of piece is: " + remainingDemandPieces[i]);
                remainingDemandPieces[i] = NULL_VALUE;
                System.out.println("the value of piece is now: " + remainingDemandPieces[i]);
            }
        }

        sortArrayDes(remainingDemandPieces);
        if (remainingDemandPieces[0] == NULL_VALUE) currentWood.lastWood();
        System.out.println("finished dividing a wood");
        System.out.println(currentWood.toString());

        //greedy efficiency:
        if (greedyEfficiency) {
            if (!currentWood.isWoodEfficient()) { // (normal inventory piece) return resources back to demand list, discard division.
                int i = remainingDemandPieces.length - 1;
                for (Piece currentPiece : currentWood.piecesList) {
                    remainingDemandPieces[i] = (currentPiece.size);
                    i--;
                }
                currentWood.piecesList.clear();
            }

            else if (currentWood.isBaseSizeAndLowEfficiency()){ // (base inventory piece) return only last resource back to demand list.
                int i = remainingDemandPieces.length - 1;
                remainingDemandPieces[i] = currentWood.removeLastAdded();
                System.out.println(remainingDemandPieces[i]);
                System.out.println(currentWood.toString());
            }
        }
        sortArrayDes(remainingDemandPieces);
        return currentWood;
    }


    /**
     * assigns all remaining demanded pieces to one special missing-wood record,
     * used when inventory is empty
     *
     * @param remainingDemandPieces all pieces that are needed to complete the project
     * @return wood type that contains the division to pieces
     */
    public static Wood divideMissingWood(double[] remainingDemandPieces) {

        sortArrayDes(remainingDemandPieces);
        Wood currentWood = new Wood(0);
        int AvailablePiecesAmount = getNoNullLength(remainingDemandPieces);
        for (int i = 0; i < AvailablePiecesAmount; i++) {
            currentWood.addMissing(remainingDemandPieces[i]);
            remainingDemandPieces[i] = NULL_VALUE;

        }
        System.out.println("finished dividing a wood");
        System.out.println(currentWood.toString());
        return currentWood;
    }


    /**
     * assigns all demanded pieces to the entire inventory wood greedily
     *
     * @param remainingDemandPieces all pieces (sizes) that are needed to complete the project
     * @param invArray              all available inventory wood (sizes)
     * @param greedyEfficiency      activates greedy efficiency
     * @return an optimal wood division result list
     */
    public static ArrayList<Wood> divideAllWoods(double[] remainingDemandPieces, double[] invArray, boolean greedyEfficiency) {
        System.out.println("non null length of inventory wood list: " + getNoNullLength(invArray));
        ArrayList<Wood> resultWoodList = new ArrayList<Wood>();
        int index = 0;
        while (getNoNullLength(invArray) > 0 && remainingDemandPieces.length > 0 && remainingDemandPieces[0] != NULL_VALUE && index < getNoNullLength(invArray)) {
            System.out.println("trying to add wood number: " + index);
            Wood resultWood = divideWood(remainingDemandPieces, invArray[index], greedyEfficiency);
            if (!resultWood.isEmpty())
                resultWoodList.add(resultWood);
            index += 1;
        }
        if (remainingDemandPieces.length > 0 && remainingDemandPieces[0] != NULL_VALUE) { //add Missing
            resultWoodList.add(divideMissingWood(remainingDemandPieces));
        }


        System.out.println("finished dividing all woods");
        return resultWoodList;
    }

    public static boolean isWoodListContainMissing(ArrayList<Wood> WoodList) {
        return ((!WoodList.isEmpty()) && (WoodList.get(WoodList.size() - 1)).isMissing);
    }


    public static String toStringWoodList(ArrayList<Wood> WoodList) {
        String result = "";
        if (!WoodList.isEmpty()) {
            for (Wood currentWood : WoodList) {
                result = result + currentWood.toString();
            }
        }
        return result;
    }



    /**
     * performs the greedy division with the singular optimization
     * in case that greedyEfficiency is activated - runs optimizations that may be "inventory resources costly" but with a long range efficiency
     *
     * @param invArray all available inventory wood (sizes)
     * @param demArray all pieces (sizes) that are needed to complete the project
     * @param greedyEfficiency activates greedy Efficiency optimization that allows only efficient divisions
     * @return a string that represent the optimal division
     */
    public static String GreedyResults(double[] invArray, double[] demArray, boolean greedyEfficiency) {
        Arrays.sort(invArray);
        ArrayList<Wood> woodList = divideAllWoods(demArray.clone(), invArray, false);
        woodList = singularOptimizationWoodList(woodList, invArray, demArray.clone());
        if (isWoodListContainsInefficient(woodList) && greedyEfficiency){
            ArrayList<Wood> woodListGreedyEf = divideAllWoods(demArray.clone(), invArray, true);
            if (!isWoodListContainMissing(woodListGreedyEf)) woodList = woodListGreedyEf;
        }
        return toStringWoodList(woodList);
    }


    /********************************************************/
    /********************OPTIMIZATIONS***********************/
    /********************************************************/


    /**
     * performs a singular optimization, it is useful for small projects where the greedy algorithm
     * tries to get rid of a smaller piece of inventory wood first, regardless of total efficiency.
     * by eliminating that inefficient inventory wood from the calculation - more efficient result may appear
     *
     * @param WoodList          division wood list
     * @param AvailableWoodList all available initial inventory wood (sizes)
     * @param PiecesList        all pieces that are needed to complete the project     *
     * @return an optimal wood division result list
     */
    public static ArrayList<Wood> singularOptimizationWoodList(ArrayList<Wood> WoodList, double[] AvailableWoodList, double[] PiecesList) {
        if (isWoodListContainMissing(WoodList) || WoodList.size() < 2 || !isProblemValue(WoodList))
            return WoodList;
        double[] newAvailableWoodList = removeElementFromArrayByValue(AvailableWoodList, findProblemValue(WoodList));
        ArrayList<Wood> newWoodList = divideAllWoods(PiecesList, newAvailableWoodList, false);
        if ((newWoodList.size() < WoodList.size()) && ((newWoodList.get(newWoodList.size() - 1)).isGoodEfficiencyForSingularOptim()))
            return newWoodList;
        return WoodList;
    }

    /**
     * decides whether a singular optimization may be possible
     * criteria: a division of a wood is not efficient and the last wood to be divided is extremely not efficient
     *
     * @param WoodList division wood list
     * @return true if optimization is possible
     */
    public static boolean isProblemValue(ArrayList<Wood> WoodList) {
        boolean middleBad = false;
        boolean endBad;
        for (Wood currentWood : WoodList) {
            if (currentWood.isLowEfficiencyForSingularOptim()) {
                middleBad = true;
                break;
            }
        }
        endBad = (WoodList.get(WoodList.size() - 1)).isLowEfficiencyLastForSingularOptim();
        return middleBad && endBad;
    }


    /**
     * finds a bad inventory wood division for a singular optimization
     * criteria: a division of a wood is not efficient and the last wood to be divided is extremely not efficient
     *
     * @param WoodList division wood list
     * @return the size of the inefficient wood
     */
    public static double findProblemValue(ArrayList<Wood> WoodList) {
        double problemValue = 0;
        for (Wood currentWood : WoodList) {
            if (currentWood.isLowEfficiencyForSingularOptim()) {
                problemValue = currentWood.origSize;
                break;
            }
        }
        return problemValue;
    }


    /**
     * checks the entire solution for inefficient wood divisions for future greedy efficiency optimizations
     *
     * @param WoodList resault wood division list
     * @return will current list may improve after optimization
     */
    public static boolean isWoodListContainsInefficient(ArrayList<Wood> WoodList) {
        boolean res = false;
        if ((!WoodList.isEmpty()) && !(WoodList.get(WoodList.size() - 1)).isMissing) {
            for (Wood currentWood : WoodList) {
                if (!currentWood.isWoodEfficient() || currentWood.isBaseSizeAndLowEfficiency()) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }


    /********************************************************/
    /*************************TEST***************************/
    /********************************************************/


    //generate inventory for tests
    public static double[] getFictionalInventoryArray() {
        return new double[]{220, 220,220,220};
    }

    //generate demand for tests
    public static double[] getFictionalDemandArray() {
        return new double[]{62, 62, 62, 62};
    }

    public static void test() {
        double[] invArray = getFictionalInventoryArray();
        double[] demArray = getFictionalDemandArray();
        System.out.println(GreedyResults(invArray, demArray, true));
    }
}

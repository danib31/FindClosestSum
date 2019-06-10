package com.solidus;
import java.io.*;
import java.util.ArrayList;

public class FindItems {

    public static void main(String[] args) throws IOException{

        //================================================================================
        // Initialization
        //================================================================================

        File file = new File(args[0]);
        BufferedReader br = new BufferedReader(new FileReader(file));

        ArrayList<String> itemNames = new ArrayList<String>();
        ArrayList<Integer> itemPrices = new ArrayList<Integer>();
        PricesResult result;

        String st;
        while ((st = br.readLine()) != null){
            itemNames.add(st.split(",")[0]);
            itemPrices.add(Integer.parseInt(st.split(",")[1].replaceAll("\\s+","")));
        }

        //================================================================================
        // End Initialization -> Call Function -> Print Result
        //================================================================================

        if(args.length > 2 && args[2].equals("triple")){
            result = findTriple(itemNames, itemPrices, Integer.parseInt(args[1]));
            result.isTriple = true;
        }
        else{
            result = findPair(itemNames, itemPrices, Integer.parseInt(args[1]), 0, itemPrices.size()-1);
            result.isTriple = false;
        }
        printResult(result);

    }

    static PricesResult findPair(ArrayList<String> itemNames, ArrayList<Integer> itemPrices, int target, int start, int end){

        //================================================================================
        // Initialization
        //================================================================================

        double minDiff = Double.POSITIVE_INFINITY;
        int indexL = -1;
        int indexR = -1;

        PricesResult result = new PricesResult();
        result.sum = 0;

        int len = itemPrices.size();
        if(len < 2) return result;
        int left = start;
        int right = end;
        int diff;

        //================================================================================
        // End Initialization -> Scan from both edges of array
        //================================================================================

        while(left < right){
            diff = target - itemPrices.get(left) - itemPrices.get(right);
            if(diff == 0){
                indexL = left;
                indexR = right;
                break;
            }
            if(diff < 0){
                right--;
            }
            else{
                if(diff < minDiff) {
                    minDiff = diff;
                    indexL	= left;
                    indexR = right;
                }
                left++;
            }
        }

        //================================================================================
        // End Scan -> Update result
        //================================================================================

        if(indexL >= 0){
            result.priceA = itemPrices.get(indexL);
            result.priceB = itemPrices.get(indexR);
            result.nameA = itemNames.get(indexL);
            result.nameB = itemNames.get(indexR);
            result.sum = result.priceA + result.priceB;
        }
        else{
            result.sum = 0;
        }
        return result;

    }

    static PricesResult findTriple(ArrayList<String> itemNames, ArrayList<Integer> itemPrices, int target){

        //================================================================================
        // Initialization
        //================================================================================

        PricesResult currResult = new PricesResult();
        PricesResult bestResult = new PricesResult();
        bestResult.sum = 0;
        int len = itemNames.size();
        Integer currPrice;
        double minDiff = Double.POSITIVE_INFINITY;
        double currDiff;

        //================================================================================
        // End Initialization -> Iterate from start and scan for additional pair
        //================================================================================

        for(int i=0;i<len-2;i++){
            currPrice = itemPrices.get(i);
            currResult = findPair(itemNames, itemPrices, target - currPrice, i + 1, len-1);
            if(currResult.sum > 0){
                currDiff = target - currResult.sum - currPrice;
                if(currDiff >= 0 && currDiff < minDiff){
                    minDiff = currDiff;
                    bestResult.sum = currResult.sum + currPrice;
                    bestResult.nameA = itemNames.get(i);
                    bestResult.nameB = currResult.nameA;
                    bestResult.nameC = currResult.nameB;
                    bestResult.priceA = currPrice;
                    bestResult.priceB = currResult.priceA;
                    bestResult.priceC = currResult.priceB;
                    bestResult.isTriple = true;
                }
                if(currDiff == 0) break;
            }
        }
        return bestResult;
    }

    static void printResult (PricesResult result){
        if(result.sum == 0){
            System.out.println("Not Possible");
            return;
        }
        if(result.isTriple==true){
            System.out.println(result.nameA + " " + result.priceA + ", " + result.nameB + " " + result.priceB + ", " + result.nameC + " " + result.priceC);
        }
        else{
            System.out.println(result.nameA + " " + result.priceA + ", " + result.nameB + " " + result.priceB);
        }
    }

    private static class PricesResult {
        private int sum;
        private boolean isTriple;
        private int priceA;
        private int priceB;
        private int priceC;
        private String nameA;
        private String nameB;
        private String nameC;
    }
}

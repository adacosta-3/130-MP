package org.example.cs130mpfinal;

import java.util.*;
import javax.swing.JOptionPane;

/**
 * The QuineMcCluskeyCalculator class provides functionality for simplifying Boolean functions
 * using the Quine-McCluskey (Tabular) Method.
 *
 * @author Arianne Jayne Acosta
 * @author Christian Jesse Bonifacio
 * @version 1.0.0
 * @since 2024-03-20
 */
public class QuineMcCluskeyCalculator {

    /**
     * Comparator for comparing terms based on the number of ones in their binary form.
     */
    private class OnesComparator implements Comparator<Term>{
        /**
         * Compares two terms based on their number of ones.
         * @param a First term to compare.
         * @param b Second term to compare.
         * @return 0 if same number of ones, positive int if a > b, negative int if a < b
         */
        @Override
        public int compare (Term a, Term b) {
            return a.getNumOnes() - b.getNumOnes();
        }
    }

    /** Array of terms to store terms necessary for solution. */
    private Term[] terms;

    /** List storing minterms entered by user. */
    private ArrayList<Integer> minterms;

    /** Maximum length possible for solution. */
    private int maxLength;

    /** List array containing solutions accumulated throughout the program. */
    private ArrayList<String>[] solution;

    /** List containing prime implicants accumulated throughout the program. */
    private ArrayList<String> primeImplicants;

    /** List storing every term necessary for the second stage of solving. */
    private ArrayList<Term> finalTerms;

    /** List of lists storing terms gathered from the first step of solving. */
    public ArrayList<ArrayList<Term>[]> firstStep;

    /** List of Hash sets storing checked terms gathered from the first step of solving. */
    public ArrayList<HashSet<String>> checkedFirstStep;

    /** List storing simplified terms after using Petrick's method. */
    public ArrayList<String> simplified;

    /**
     * Constructor for initializing the Quine-McCluskey calculator.
     * @param mintermsStr A valid string containing the minterms to be solved.
     */
    public QuineMcCluskeyCalculator(String mintermsStr) {

        int[] minterms = convertString(mintermsStr);

        Arrays.sort(minterms);

        maxLength = Integer.toBinaryString(minterms[minterms.length - 1]).length();

        this.minterms = new ArrayList<>();
        primeImplicants = new ArrayList<String>();
        firstStep = new ArrayList<ArrayList<Term>[]>();
        checkedFirstStep = new ArrayList<HashSet<String>>();
        simplified = new ArrayList<String>();

        Term[] temp = new Term[minterms.length];
        int k = 0; // Index in temp array.
        for (int i = 0; i < minterms.length; i++) {
            temp[k++] = new Term(minterms[i], maxLength);
            this.minterms.add(minterms[i]);
        }

        terms = new Term[k];
        for (int i = 0; i < k; i++) {
            terms[i] = temp[i];
        }

        Arrays.sort(terms, new OnesComparator());
    }

    /**
     * Converts the minterms string input and checks if valid.
     * @param s A valid string containing the minterms to be solved.
     * @return Integer array with minterms parsed from string input.
     */
    private int[] convertString(String s) {
        s = s.replace(", ",",");
        s = s.replace(",", " ");

        if (s.trim().equals("")) {
            return new int[] {};
        }

        String[] a = s.trim().split(" +");
        int[] t = new int[a.length]; // Array of minterms.

        for (int i = 0; i < t.length; i++) {
            try {
                int temp = Integer.parseInt(a[i]);
                t[i] = temp;
            } catch (Exception e) {
                if (!s.matches("[\\d,\\s]+"))
                {
                    QMCController window = new QMCController();
                    window.showAlert("Your input must only contain integers and be comma or space delimited\ne.g. 1,2,3; 1 2 3; 1, 2, 3\nYour input: " + s, "Invalid Input");
                    return new int[] {};
                }

            }
        }

        HashSet<Integer> dup = new HashSet<>();
        for (int i = 0; i < t.length; i++) {
            if (dup.contains(t[i])) {
                QMCController window = new QMCController();
                window.showAlert("List of minterms must not have duplicates.","Invalid Input");
                break;
            }
            dup.add(t[i]);
        }

        return t;
    }

    /**
     * Groups an array of terms based on their number of ones.
     * @param terms Array of terms to be grouped.
     * @return Array of array lists of terms where each element represents a group of terms with the same number of ones.
     */
    private ArrayList<Term>[] group(Term[] terms) {

        ArrayList<Term>[] groups = new ArrayList[terms[terms.length - 1].getNumOnes() + 1];

        for (int i = 0; i < groups.length; i++) {
            groups[i] = new ArrayList<>();
        }

        for (int i = 0; i < terms.length; i++) {
            int k = terms[i].getNumOnes();
            groups[k].add(terms[i]);
        }

        return groups;
    }

    /**
     * First stage of solution using Quine-McCluskey method.
     * Main solver method of the class to be called.
     */
    public void solve(){
        ArrayList<Term> unchecked = new ArrayList<>();

        ArrayList<Term>[] list = group(this.terms);

        ArrayList<Term>[] result;

        firstStep.add(list);

        boolean insert = true;

        do {
            HashSet<String> checked= new HashSet<>();

            result = new ArrayList[list.length - 1];

            ArrayList<String> temp;
            insert = false;

            for (int i = 0; i < list.length - 1; i++){
                result[i] = new ArrayList<>();
                temp = new ArrayList<>();

                for (int j = 0; j < list[i].size(); j++){
                    for (int k = 0; k < list[i + 1].size(); k++){
                        if (checkValidity(list[i].get(j), list[i + 1].get(k))) {
                            checked.add(list[i].get(j).getString());
                            checked.add(list[i+1].get(k).getString());

                            Term n = new Term(list[i].get(j), list[i+1].get(k));

                            if (!temp.contains(n.getString())) {
                                result[i].add(n);
                                insert = true;
                            }
                            temp.add(n.getString());

                        }
                    }
                }
            }

            if (insert) {
                for (int i = 0; i < list.length; i++) {
                    for (int j = 0; j < list[i].size(); j++) {
                        if (!checked.contains(list[i].get(j).getString())) {
                            unchecked.add(list[i].get(j));
                        }
                    }
                }
                list = result;

                firstStep.add(list);
                checkedFirstStep.add(checked);
            }
        } while (insert && list.length > 1);

        finalTerms = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list[i].size(); j++) {
                finalTerms.add(list[i].get(j));
            }
        }
        for (int i = 0; i < unchecked.size(); i++) {
            finalTerms.add(unchecked.get(i));
        }

        solveSecond();
    }

    /**
     * Second stage of solution using Quine-McCluskey method.
     * Recursively calls itself if there are still remaining minterms.
     */
    public void solveSecond(){
        if (!identifyPrimeImplicants()) {
            if (!rowDominance()) {
                if (!columnDominance()) {
                    simplify();
                    return;
                }
            }
        }

        if (minterms.size() != 0)
            solveSecond();
        else {
            solution = new ArrayList[1];
            solution[0] = primeImplicants;
        }
    }

    /**
     * Check if two terms are already valid for grouping.
     * @param term1 The first term to be checked.
     * @param term2 The second term to be checked.
     * @return True if grouping is possible, else false if not.
     */
    boolean checkValidity (Term term1, Term term2) {
        if (term1.getString().length() != term2.getString().length())
            return false;

        int k = 0;
        for (int i = 0; i < term1.getString().length(); i++) {
            if (term1.getString().charAt(i) == '-' && term2.getString().charAt(i) != '-')
                return false;
            else if (term1.getString().charAt(i) != '-' && term2.getString().charAt(i) == '-')
                return false;
            else if (term1.getString().charAt(i) != term2.getString().charAt(i))
                k++;
        }

        if (k != 1)
            return false;
        else
            return true;
    }

    /**
     * Check if two terms have all their numbers present in another term.
     * @param term1 The first term to be the basis of checking.
     * @param term2 The second term to be checked for presence of numbers in term1.
     * @return True if term1 contains all its numbers present in term2, else false.
     */
    boolean contains(Term term1, Term term2) {
        if (term1.getMintermNumbers().size() <= term2.getMintermNumbers().size()) {
            return false;
        }

        ArrayList<Integer> a = term1.getMintermNumbers();
        ArrayList<Integer> b = term2.getMintermNumbers();

        if (a.containsAll(b))
            return true;
        else
            return false;
    }

    /**
     * Simplifies the solution done on the object using Petrick's method.
     */
    void simplify(){
        HashSet<String>[] temp = new HashSet[minterms.size()];

        //Construct temp array containing sets of associated characters for minterms in finalTerms
        for (int i = 0; i < minterms.size(); i++) {
            temp[i] = new HashSet<>();
            for (int j = 0; j < finalTerms.size(); j++) {
                if (finalTerms.get(j).getMintermNumbers().contains(minterms.get(i))) {
                    char t = (char) ('a' + j);
                    simplified.add(t + ": " + finalTerms.get(j).getString());
                    temp[i].add(t + "");
                }
            }
        }

        HashSet<String> finalResult = multiply(temp, 0);

        int min = -1;
        int count = 0;
        for (Iterator<String> t = finalResult.iterator(); t.hasNext();) {
            String m = t.next();
            if (min == -1 || m.length() < min) {
                min = m.length();
                count = 1;
            } else if (min == m.length()) {
                count++;
            }
        }

        solution = new ArrayList[count];
        int k = 0;
        for (Iterator<String> t = finalResult.iterator(); t.hasNext();) {
            String c = t.next();
            if (c.length() == min) {
                solution[k] = new ArrayList<>();
                for (int i = 0; i < c.length(); i++) {
                    solution[k].add(finalTerms.get((int) c.charAt(i) - 'a').getString());
                }
                for (int i = 0; i < primeImplicants.size(); i++) {
                    solution[k].add(primeImplicants.get(i));
                }
                k++;
            }
        }
    }

    /**
     * Multiplies elements from sets at indices adjacent to each other in the Hash set array
     * and recursively computes for the product.
     * @param p Array of Hash sets containing elements to be multiplied.
     * @param k Index pointing to the first set to be multiplied.
     * @return A Hash set resulting from the multiplication of adjacent sets in a Hash set array.
     */
    HashSet<String> multiply(HashSet<String>[] p, int k){
        if (k >= p.length - 1)
            return p[k];

        HashSet<String> s = new HashSet<>();

        for (Iterator<String> t = p[k].iterator(); t.hasNext();) {
            String temp2 = t.next();
            for (Iterator<String> g = p[k + 1].iterator(); g.hasNext();) {
                String temp3 = g.next();
                s.add(mix(temp2, temp3));
            }
        }
        p[k + 1] = s;
        return multiply(p, k + 1);
    }

    /**
     * Mixes terms and simplifies those that are duplicated with respect to properties of boolean expressions.
     * @param str1 The first string to be multiplied.
     * @param str2 The second string to be multiplied.
     * @return A string containing the simplified boolean product of both input strings.
     */
    String mix (String str1, String str2){
        HashSet<Character> r = new HashSet<>();

        for (int i = 0; i < str1.length(); i++)
            r.add(str1.charAt(i));

        for (int i = 0; i < str2.length(); i++)
            r.add(str2.charAt(i));

        StringBuilder result = new StringBuilder();
        for (Iterator<Character> i = r.iterator(); i.hasNext();)
            result.append(i.next());

        return result.toString();
    }

    /**
     * Identify prime implicants, add them to primeImplicants array list, and remove from minterms and finalTerms array lists.
     * @return True if prime implicants are identified, else false.
     */
    private boolean identifyPrimeImplicants(){
        ArrayList<Integer>[] columns = new ArrayList[minterms.size()];

        for (int i = 0; i < minterms.size(); i++) {
            columns[i] = new ArrayList();
            for (int j = 0; j < finalTerms.size(); j++) {
                if (finalTerms.get(j).getMintermNumbers().contains(minterms.get(i))) {
                    columns[i].add(j);
                }
            }
        }
        boolean isPrimeImplicant = false;

        for (int i = 0; i < minterms.size(); i++) {
            if (columns[i].size() == 1) {
                isPrimeImplicant = true;

                ArrayList<Integer> del = finalTerms.get(columns[i].get(0)).getMintermNumbers();

                for (int j = 0; j < minterms.size(); j++) {
                    if (del.contains(minterms.get(j))) {
                        minterms.remove(j);
                        j--;
                    }
                }

                primeImplicants.add(finalTerms.get(columns[i].get(0)).getString());
                finalTerms.remove(columns[i].get(0).intValue());
                break;
            }
        }
        return isPrimeImplicant;
    }

    /**
     * Identify dominating columns and remove them from the minterms and finalTerms array lists.
     * @return True if there are dominating columns and were identified and removed, else false.
     */
    private boolean columnDominance(){
        boolean flag = false;

        ArrayList<ArrayList<Integer>> columns = new ArrayList<>();

        for (int i = 0; i < minterms.size(); i++){
            columns.add(new ArrayList<Integer>());
            for (int j = 0; j < finalTerms.size(); j++){
                if (finalTerms.get(j).getMintermNumbers().contains(minterms.get(i)))
                    columns.get(i).add(j);
            }
        }

        for (int i = 0; i < columns.size(); i++) {
            for (int j = i + 1; j < columns.size(); j++) {
                if (columns.get(j).containsAll(columns.get(i)) && columns.get(j).size() > columns.get(i).size()) {
                    columns.remove(j);
                    minterms.remove(j);
                    j--;
                    flag = true;
                } else if (columns.get(i).containsAll(columns.get(j)) && columns.get(i).size() > columns.get(j).size()) {
                    columns.remove(i);
                    minterms.remove(i);
                    i--;
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * Identify dominating rows and remove them from the minterms and finalTerms array lists.
     * @return True if there are dominating rows and were identified and removed, else false.
     */
    private boolean rowDominance(){
        boolean flag = false;

        for (int i = 0; i < finalTerms.size() - 1; i++) {
            for (int j = i + 1; j < finalTerms.size(); j++) {
                if (contains(finalTerms.get(i), finalTerms.get(j))) {
                    finalTerms.remove(j);
                    j--;
                    flag = true;
                } else if (contains(finalTerms.get(j), finalTerms.get(i))) {
                    finalTerms.remove(i);
                    i--;
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    String toStandardForm(String s) {
        StringBuilder r = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '-') {
                continue;
            }

            else if (s.charAt(i) == '1') {
                r.append((char) ('A' + i));
            }

            else {
                r.append((char) ('A' + i));
                r.append('\'');
            }
        }

        if (r.toString().length() == 0) {
            r.append("1");
        }
        return r.toString();
    }

    /**
     * Build a String for the final resulting solutions to be presented to the user.
     * @param variables List of variables that contains corresponding variable names.
     * @return The String build-up of the final resulting solutions.
     */
    public String printResults(String[] variables) {
        StringBuilder printedAnswer = new StringBuilder();
        for (int i = 0; i < solution.length; i++) {

            if (solution.length == 1)
                printedAnswer.append("Solution:").append("\n");
            else
                printedAnswer.append("Solution #").append(i+1).append(":").append("\n");

            StringBuilder finalAnswer = new StringBuilder();
            for (int j = 0; j < solution[i].size(); j++) {
                finalAnswer.append(toStandardForm(solution[i].get(j)));
                if (j != solution[i].size() - 1) {
                    finalAnswer.append(" + ");
                }
            }

            for (int j = 0; j < finalAnswer.toString().length(); j++){
                if(finalAnswer.charAt(j) == 'A')
                    printedAnswer.append(variables[0]);
                else if (finalAnswer.charAt(j) == 'B')
                    printedAnswer.append(variables[1]);
                else if (finalAnswer.charAt(j) == 'C')
                    printedAnswer.append(variables[2]);
                else if (finalAnswer.charAt(j) == 'D')
                    printedAnswer.append(variables[3]);
                else if (finalAnswer.charAt(j) == 'E')
                    printedAnswer.append(variables[4]);
                else if (finalAnswer.charAt(j) == 'F')
                    printedAnswer.append(variables[5]);
                else if (finalAnswer.charAt(j) == 'G')
                    printedAnswer.append(variables[6]);
                else if (finalAnswer.charAt(j) == 'H')
                    printedAnswer.append(variables[7]);
                else if (finalAnswer.charAt(j) == 'I')
                    printedAnswer.append(variables[8]);
                else if (finalAnswer.charAt(j) == 'J')
                    printedAnswer.append(variables[9]);
                else
                    printedAnswer.append(finalAnswer.toString().charAt(j));
            }
            printedAnswer.append("\n\n");
        }
        return printedAnswer.toString();
    }
}

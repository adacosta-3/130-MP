import java.util.*;

/**
 * The Term class encapsulates the representation and manipulation of boolean function terms,
 * facilitating the simplification process in the Quine-McCluskey algorithm.
 *
 * @author Arianne Jayne Acosta
 * @author Christian Jesse Bonifacio
 * @version 1.0
 * @since 2024-03-20
 */
public class Term {
    /**
     * Represents the binary form of a boolean function term.
     */
    private String term;

    /**
     * Stores the count of '1's in the binary representation of the term.
     */
    private int ones;

    /**
     * Stores the minterm numbers associated with the term.
     */
    private ArrayList<Integer> mintermNumbers;

    /**
     * Constructs a Term object from an integer minterm value.
     *
     * @param value The integer value of the minterm.
     * @param length The length of the binary string, used for leading zero padding.
     */
    public Term(int value, int length) {
        // Convert minterm to binary string
        String binary = Integer.toBinaryString(value);

        // Left-pad zeroes if binary string length does not match desired length
        StringBuffer temp = new StringBuffer(binary);
        while (temp.length() != length) {
            temp.insert(0, 0);
        }
        this.term = temp.toString();

        // Initialize array list to store minterm numbers
        mintermNumbers = new ArrayList<Integer>();
        mintermNumbers.add(value);

        // Count the number of ones in the binary representation
        ones = 0;
        for (int i = 0; i < term.length(); i++) {
            if(term.charAt(i) == '1')
                ones++;
        }
    }

    /**
     * Constructs a Term object from two grouped terms.
     *
     * @param term1 The first term to be grouped.
     * @param term2 The second term to be grouped.
     */
    public Term(Term term1, Term term2) {
        // Create a new term by comparing and replacing non-matching characters
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < term1.getString().length(); i++) {
            if (term1.getString().charAt(i) != term2.getString().charAt(i))
                temp.append("-");
            else
                temp.append(term1.getString().charAt(i));
        }
        this.term = temp.toString();

        // Count the number of ones in the new term
        ones = 0;
        for (int i = 0; i < term.length(); i++) {
            if (this.term.charAt(i) == '1')
                ones++;
        }

        // Combine minterm numbers of both terms
        mintermNumbers = new ArrayList<Integer>();
        mintermNumbers.addAll(term1.getMintermNumbers());
        mintermNumbers.addAll(term2.getMintermNumbers());
    }

    /**
     * Retrieves the binary representation of the term.
     *
     * @return A string representing the binary value of the term.
     */
    String getString() {
        return term;
    }

    /**
     * Retrieves the list of minterm numbers associated with the term.
     *
     * @return An ArrayList containing the minterm numbers.
     */
    ArrayList<Integer> getMintermNumbers(){
        return mintermNumbers;
    }

    /**
     * Retrieves the count of '1's in the binary representation of the term.
     *
     * @return The number of '1's in the term.
     */
    int getNumOnes(){
        return ones;
    }
}


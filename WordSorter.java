import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * This program reads a text file, extracts all words,
 * sorts them alphabetically, and writes them to an output file.
 * Includes random input generation and binary search.
 *
 * @author  Yoma Ozoh
 * @version 1.0
 * @since   2026-11-13
 */
public final class WordSorter {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private WordSorter() {
    }

    /**
     * Main method to run the menu, file reading, sorting, and searching.
     *
     * @param args Command line arguments
     */
    public static void main(final String[] args) {
        // Create scanner to read user keyboard input
        final Scanner userInput = new Scanner(System.in);
        // Set input file name
        final String inputFileName = "input.txt";
        // Set output file name
        final String outputFileName = "output.txt";


        // Ask user for choice
        System.out.print("Enter your choice (1: existing input.txt)"
        + "or (2: generate random input.txt): ");

        // Read user input
        final String choiceStr = userInput.nextLine();
        // Store user numeric choice
        int choice = 0;

        // Try converting text input into an integer
        try {
            // Convert string to integer
            choice = Integer.parseInt(choiceStr);
        } catch (NumberFormatException e) {
            // Print error if user did not enter a number
            System.out.println("Invalid input. Please enter 1 or 2.");
            // Close scanner
            userInput.close();
            // Stop program
            return;
        }

        // Check if number isn't 1 or 2
        if (choice < 1 || choice > 2) {
            System.out.println("Invalid choice. Must be 1 or 2.");
            // Close scanner
            userInput.close();
            // Stop program
            return;
        }

        // Check if user chose option 2
        if (choice == 2) {
            // Generate random words into input file
            generateInputFile(inputFileName);
        }

        // Read and clean words from input file into an array
        final String[] extractedWords = extractWords(inputFileName);
        // Check if no words were found
        if (extractedWords.length == 0) {
            // Print missing file or empty file message
            System.out.println("No words found or file missing.");
            // Close scanner
            userInput.close();
            // Stop program
            return;
        }

        // Sort extracted words from A to Z
        final String[] sortedWords = sortWordsAlphabetically(extractedWords);

        // Save sorted words to output file
        writeWordsToFile(sortedWords, outputFileName);
        // Display sorted words in terminal so user sees what is in the list
        System.out.print("Sorted words: ");
        // for loop through sorted array and print each word
        for (int i = 0; i < sortedWords.length; i++) {
            // Print current word followed by a space
            System.out.print(sortedWords[i] + " ");
        }
        System.out.println();
        // Ask user for a word to search
        System.out.print("Enter a word to search for in the sorted list: ");
        // Read word and make lowercase
        final String target = userInput.nextLine().toLowerCase();

        // Check if user didn't input anything
        if (target.isEmpty()) {
            System.out.println("Invalid input: no word entered.");
        } else {
            // Search sorted array for target word
            final int foundIndex = binarySearchWord(sortedWords, target);
            // Check if word was found
            if (foundIndex != -1) {
                // Print index where word was found
                System.out.println("Word '" + target
                        + "' found at index " + foundIndex + ".");
            } else {
                // Print message if word is not in list
                System.out.println("Word '" + target + "' was not found.");
            }
        }

        // Close scanner before ending program
        userInput.close();
    }

    /**
     * Generates a random input.txt file with sample words.
     *
     * @param fileName The name of the file to create
     */
    public static void generateInputFile(final String fileName) {
        // Array of sample words to pick from
        final String[] wordBank = {
            "zebra", "apple", "mango", "banana", "cherry",
            "grape", "orange", "kiwi", "peach", "lemon"
        };
        // Create random number generator
        final Random rand = new Random();
        // Set total number of random words to generate
        final int wordCount = 8;

        // Try opening file to write words
        try {
            // Open file writer
            final PrintWriter writer = new PrintWriter(new File(fileName));
            // Loop 8 times to write 8 words
            for (int i = 0; i < wordCount; i++) {
                // Pick random index from wordBank
                final int randomIndex = rand.nextInt(wordBank.length);
                // Write random word followed by a space
                writer.print(wordBank[randomIndex] + " ");
            }
            // Add final newline
            writer.println();
            // Save and close file
            writer.close();
        } catch (FileNotFoundException e) {
            // Print error if file cannot be created
            System.out.println("Error creating " + fileName);
        }
    }

    /**
     * Reads a text file and extracts all cleaned words into an array.
     *
     * @param fileName The file to read from
     * @return Array of extracted lowercase words
     */
    public static String[] extractWords(final String fileName) {
        // Create dynamic list to hold extracted words
        final ArrayList<String> wordList = new ArrayList<>();

        // Try opening input file to read
        try {
            // Open file scanner
            final Scanner fileScanner = new Scanner(new File(fileName));
            // Loop while more words exist in file
            while (fileScanner.hasNext()) {
                // Read next word from file
                final String rawWord = fileScanner.next();
                // Strip punctuation and convert to lowercase
                final String cleanWord =
                        rawWord.replaceAll("[^a-zA-Z]", "").toLowerCase();
                // Check if cleaned word still has letters
                if (!cleanWord.isEmpty()) {
                    // Add cleaned word to list
                    wordList.add(cleanWord);
                }
            }
            // Close file scanner
            fileScanner.close();
        } catch (FileNotFoundException e) {
            // Print error if file is missing
            System.out.println("Error: " + fileName + " was not found.");
        }

        // Convert ArrayList into standard String array and return
        return wordList.toArray(new String[0]);
    }

    /**
     * Sorts an array of strings alphabetically using Bubble Sort.
     *
     * @param wordsArray The unsorted array of words
     * @return Alphabetically sorted array of words
     */
    public static String[] sortWordsAlphabetically(
            final String[] wordsArray) {
        // Get total number of words in array
        final int n = wordsArray.length;
        // Outer loop for each sorting pass
        for (int i = 0; i < n - 1; i++) {
            // Inner loop to compare adjacent words
            for (int j = 0; j < n - i - 1; j++) {
                // Check if current word comes after next word
                if (wordsArray[j].compareTo(wordsArray[j + 1]) > 0) {
                    // Store current word temporarily
                    final String temp = wordsArray[j];
                    // Move next word to current spot
                    wordsArray[j] = wordsArray[j + 1];
                    // Place temporary word into next spot
                    wordsArray[j + 1] = temp;
                }
            }
        }
        // Return sorted array
        return wordsArray;
    }

    /**
     * Searches for a target word in a sorted array using binary search.
     *
     * @param sortedArray The alphabetically sorted array
     * @param targetWord The word to search for
     * @return Index of the word if found, or -1 if not found
     */
    public static int binarySearchWord(final String[] sortedArray,
            final String targetWord) {
        // Set starting search index
        int low = 0;
        // Set ending search index
        int high = sortedArray.length - 1;

        // Loop while search range is valid
        while (low <= high) {
            // Find middle index
            final int mid = (low + high) / 2;
            // Compare target word to middle word
            final int comparison = targetWord.compareTo(sortedArray[mid]);

            // Check if middle word matches target
            if (comparison == 0) {
                // Return matching index
                return mid;
            } else if (comparison > 0) {
                // Narrow search to right half
                low = mid + 1;
            } else {
                // Narrow search to left half
                high = mid - 1;
            }
        }
        // Return -1 if word is not found
        return -1;
    }

    /**
     * Writes an array of words to an output text file, one per line.
     *
     * @param sortedArray The sorted words to write
     * @param outputFileName The destination file name
     */
    public static void writeWordsToFile(final String[] sortedArray,
            final String outputFileName) {
        // Try opening output file to write
        try {
            // Open file writer
            final PrintWriter writer =
                    new PrintWriter(new File(outputFileName));
            // Loop through every sorted word
            for (int i = 0; i < sortedArray.length; i++) {
                // Write current word on its own line
                writer.println(sortedArray[i]);
            }
            // Save and close file
            writer.close();
        } catch (FileNotFoundException e) {
            // Print error if file cannot be written
            System.out.println("Error writing to " + outputFileName);
        }
    }
}

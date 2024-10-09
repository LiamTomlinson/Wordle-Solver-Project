package project20280.wordlefiles.resources;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import project20280.interfaces.Entry;
import project20280.priorityqueue.HeapPriorityQueue;

public class HuffmanTree {

    static class HuffmanNode implements Comparable<HuffmanNode> {
        char character;
        int frequency;
        HuffmanNode left, right;

        public HuffmanNode(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;

        }

        public HuffmanNode(HuffmanNode left, HuffmanNode right) {
            this.left = left;
            this.right = right;
            this.frequency = left.frequency + right.frequency;
        }

        @Override
        public int compareTo(HuffmanNode o) {
            return this.frequency - o.frequency;
        }
    }

    public static HuffmanNode buildHuffmanTree(Map<Character, Integer> frequencies) {
        HeapPriorityQueue<Integer, HuffmanNode> pq = new HeapPriorityQueue<>(Comparator.comparingInt((Integer frequency) -> frequency));

        // Create leaf nodes for each character and add them to the priority queue
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            pq.insert(entry.getValue(), new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // Construct the Huffman tree
        while (pq.size() > 1) {
            HuffmanNode left = pq.removeMin().getValue();
            HuffmanNode right = pq.removeMin().getValue();
            HuffmanNode parent = new HuffmanNode(left, right);
            pq.insert(parent.frequency, parent);
        }

        // Return the root of the Huffman tree
        return pq.min().getValue();
    }

    public static Map<Character, Integer> countCharacterFrequencies(String fileName) throws IOException {
        Map<Character, Integer> frequencies = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
                }
            }
        }
        return frequencies;

    }





    public static void printFrequencies(Map<Character, Integer> frequencies) {
            System.out.println("Character frequencies:");
            for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
    }

    public static void printHuffmanTree(HuffmanNode root) {
            printHuffmanTree(root, 0);
    }

    private static void printHuffmanTree(HuffmanNode node, int depth) {
            if (node != null) {
                for (int i = 0; i < depth; i++) {
                    System.out.print("  ");
                }
                if (node.character != '\0') {
                    System.out.println(node.character + " (" + node.frequency + ")");
                } else {
                    System.out.println("* (" + node.frequency + ")");
                }
                printHuffmanTree(node.left, depth + 1);
                printHuffmanTree(node.right, depth + 1);
            }
    }

    public static Map<Character, String> buildEncodingMap(HuffmanNode root) {
        Map<Character, String> encodingMap = new HashMap<>();
        buildEncodingMap(root, "", encodingMap);
        return encodingMap;
    }

    private static void buildEncodingMap(HuffmanNode node, String encoding, Map<Character, String> encodingMap) {
        if (node != null) {
            if (node.character != '\0') { // If it's a leaf node
                encodingMap.put(node.character, encoding);
            } else { // If it's an internal node
                buildEncodingMap(node.left, encoding + "0", encodingMap);
                buildEncodingMap(node.right, encoding + "1", encodingMap);
            }
        }
    }

    public static void encodeFile(String inputFileName, String outputFileName, Map<Character, String> encodingMap) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            int c;
            while ((c = reader.read()) != -1) {
                char character = (char) c;
                String encoding = encodingMap.get(character);
                if (encoding != null) {
                    writer.write(encoding);
                }
                else if (c == '\n'){

                }
                else {

                }
            }
        }
        System.out.println("done");
    }

    public static void printBitsSaved(String inputFileName, String encodedFileName, Map<Character, String> encodingMap) throws IOException {
        // Calculate the original size of the file in bits
        float originalSize = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            int c;
            while ((c = reader.read()) != -1) {
                char character = (char) c;
                if (encodingMap.containsKey(character)) {
                    originalSize += 8; // Each character is represented by 8 bits
                }
            }
        }

        // Calculate the size of the compressed file in bits
        float compressedSize = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(encodedFileName))) {
            int c;
            while ((c = reader.read()) != -1) {
                compressedSize++; // Each character is a bit in the encoded file
            }
        }

        // Calculate the amount of bits saved
        float bitsSaved = originalSize - compressedSize;

        // Print the comparison and amount of bits saved
        System.out.println("Original file size: " + originalSize + " bits");
        System.out.println("Compressed file size: " + compressedSize + " bits");
        System.out.println("Bits saved by Huffman compression: " + bitsSaved + " bits");
        float ratio = compressedSize / originalSize;
        System.out.println("Compression ratio: " + ratio);
    }

    public static void findShortestAndLongestWordsByFrequency(String dictionaryFileName, Map<Character, Integer> frequencies) throws IOException {
        String shortestWord = null;
        String longestWord = null;
        int shortestFrequency = Integer.MAX_VALUE;
        int longestFrequency = Integer.MIN_VALUE;

        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFileName))) {
            String word;

            // Read each word from the dictionary file
            while ((word = reader.readLine()) != null) {
                int totalFrequency = 0;

                // Calculate the total frequency of the letters in the word
                for (char character : word.toCharArray()) {
                    totalFrequency += frequencies.getOrDefault(character, 0);
                }

                // Update the shortest word and frequency if necessary
                if (totalFrequency < shortestFrequency) {
                    shortestFrequency = totalFrequency;
                    shortestWord = word;
                }

                // Update the longest word and frequency if necessary
                if (totalFrequency > longestFrequency) {
                    longestFrequency = totalFrequency;
                    longestWord = word;
                }
            }
        }

        // Print the results
        System.out.println("Shortest word by frequency: " + shortestWord + " (frequency: " + shortestFrequency + ")");
        System.out.println("Longest word by frequency: " + longestWord + " (frequency: " + longestFrequency + ")");
    }


//D:\Java\data-structures-LiamTomlinson\src\project20280\wordlefiles\resources\dictionary.txt
public static void main(String[] args) {
    try {
        String dictionaryFileName = "D:\\Java\\data-structures-LiamTomlinson\\src\\project20280\\wordlefiles\\resources\\dictionary.txt";
        Map<Character, Integer> frequencies = countCharacterFrequencies(dictionaryFileName);

        // Build the Huffman tree and encoding map
        HuffmanNode root = buildHuffmanTree(frequencies);
        printHuffmanTree(root);
        Map<Character, String> encodingMap = buildEncodingMap(root);


        // Encode the file
        encodeFile(dictionaryFileName, "encoded.txt", encodingMap);

        // Find and print the shortest and longest words by frequency
        findShortestAndLongestWordsByFrequency(dictionaryFileName, frequencies);

        // Print bits saved
        printBitsSaved(dictionaryFileName, "encoded.txt", encodingMap);


    } catch (IOException e) {
        e.printStackTrace();
    }
}




}

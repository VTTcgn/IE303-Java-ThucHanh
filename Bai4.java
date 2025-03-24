import java.io.*;
import java.util.*;

public class Bai4 {
    private HashMap<String, Integer> wordCounts = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> transitionCounts = new HashMap<>();

    public void trainModel(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        String previousWord = null;

        while ((line = reader.readLine()) != null) {
            String[] words = line.toLowerCase().split("\\s+");
            for (String word : words) {
                // Cập nhật wordCounts
                if (!wordCounts.containsKey(word)) {
                    wordCounts.put(word, 1);
                } else {
                    wordCounts.put(word, wordCounts.get(word) + 1);
                }
                
                // Cập nhật transitionCounts
                if (previousWord != null) {
                    if (!transitionCounts.containsKey(previousWord)) {
                        transitionCounts.put(previousWord, new HashMap<>());
                    }
                    HashMap<String, Integer> innerMap = transitionCounts.get(previousWord);
                    
                    if (!innerMap.containsKey(word)) {
                        innerMap.put(word, 1);
                    } else {
                        innerMap.put(word, innerMap.get(word) + 1);
                    }
                }
                previousWord = word;
            }
        }
        reader.close();
    }

    // Sinh văn bản dựa trên từ đầu tiên
    public String generateText(String seed) {
        StringBuilder result = new StringBuilder(seed);
        String currentWord = seed;
        int maxWords = 5;

        for (int i = 1; i < maxWords; i++) {
            if (!transitionCounts.containsKey(currentWord)) break;
            HashMap<String, Integer> nextWords = transitionCounts.get(currentWord);
            currentWord = getMostLikelyNextWord(nextWords);
            if (currentWord == null) break;
            result.append(" ").append(currentWord);
        }
        return result.toString();
    }

    // Chọn từ có xác suất cao nhất
    private String getMostLikelyNextWord(HashMap<String, Integer> nextWords) {
        String bestWord = null;
        int maxCount = -1;
        
        for (Map.Entry<String, Integer> entry : nextWords.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                bestWord = entry.getKey();
            }
        }
        return bestWord;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập một từ bất kỳ: ");
        String inputWord = scanner.nextLine().toLowerCase();
        scanner.close();
        
        Bai4 generator = new Bai4();
        generator.trainModel("UIT-ViOCD.txt"); 
        
        String result = generator.generateText(inputWord);
        System.out.println("Generated text: " + result);
    }
}

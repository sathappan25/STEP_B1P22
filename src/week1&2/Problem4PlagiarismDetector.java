import java.util.*;

public class Problem4PlagiarismDetector {
    private final int n;
    private final Map<String, Set<String>> ngramIndex = new HashMap<>();

    public Problem4PlagiarismDetector(int n) {
        this.n = n;
    }

    public void indexDocument(String docId, List<String> words) {
        for (int i = 0; i + n <= words.size(); i++) {
            String gram = String.join(" ", words.subList(i, i + n));
            ngramIndex.computeIfAbsent(gram, k -> new HashSet<>()).add(docId);
        }
    }

    public SimilarityResult analyze(String docId, List<String> words) {
        Map<String, Integer> matches = new HashMap<>();
        int total = Math.max(1, words.size() - n + 1);
        for (int i = 0; i + n <= words.size(); i++) {
            String gram = String.join(" ", words.subList(i, i + n));
            Set<String> docs = ngramIndex.get(gram);
            if (docs == null) continue;
            for (String d : docs) {
                if (d.equals(docId)) continue;
                matches.merge(d, 1, Integer::sum);
            }
        }
        String bestDoc = null;
        double bestScore = 0;
        for (Map.Entry<String, Integer> e : matches.entrySet()) {
            double sim = 100.0 * e.getValue() / total;
            if (sim > bestScore) {
                bestScore = sim;
                bestDoc = e.getKey();
            }
        }
        return new SimilarityResult(bestDoc, bestScore, matches);
    }

    public record SimilarityResult(String mostSimilarDoc, double similarityPct, Map<String, Integer> matchCounts) {}

    // Demo
    public static void main(String[] args) {
        Problem4PlagiarismDetector detector = new Problem4PlagiarismDetector(5);
        detector.indexDocument("essay_089", List.of("a", "b", "c", "d", "e", "f"));
        detector.indexDocument("essay_092", List.of("a", "b", "c", "d", "e", "x", "y"));
        SimilarityResult r = detector.analyze("essay_123", List.of("a", "b", "c", "d", "e", "g"));
        System.out.println(r);
    }
}
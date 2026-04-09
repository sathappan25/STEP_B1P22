import java.util.*;

public class Problem7Autocomplete {
    private final Map<String, Integer> frequencies = new HashMap<>();
    private final TrieNode root = new TrieNode();
    private final int suggestionsLimit;

    public Problem7Autocomplete(int suggestionsLimit) {
        this.suggestionsLimit = suggestionsLimit;
    }

    public void recordQuery(String query) {
        frequencies.merge(query, 1, Integer::sum);
        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isTerminal = true;
    }

    public List<String> suggest(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return List.of();
        }
        List<String> results = new ArrayList<>();
        dfs(node, new StringBuilder(prefix), results);
        results.sort(Comparator.comparingInt((String q) -> frequencies.getOrDefault(q, 0)).reversed());
        return results.size() > suggestionsLimit ? results.subList(0, suggestionsLimit) : results;
    }

    private void dfs(TrieNode node, StringBuilder path, List<String> results) {
        if (node.isTerminal) results.add(path.toString());
        for (Map.Entry<Character, TrieNode> e : node.children.entrySet()) {
            path.append(e.getKey());
            dfs(e.getValue(), path, results);
            path.deleteCharAt(path.length() - 1);
        }
    }

    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isTerminal;
    }

    // Demo
    public static void main(String[] args) {
        Problem7Autocomplete ac = new Problem7Autocomplete(3);
        ac.recordQuery("java tutorial");
        ac.recordQuery("javascript");
        ac.recordQuery("java download");
        System.out.println(ac.suggest("jav"));
    }
}
package seedu.saveit.logic.parser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.swing.text.html.Option;

/**
 * Stores mapping of prefixes to their respective arguments.
 * Each key may be associated with multiple argument values.
 * Values for a given key are stored in a list, and the insertion ordering is maintained.
 * Keys are unique, but the list of argument values may contain duplicate argument values, i.e. the same argument value
 * can be inserted multiple times for the same prefix.
 */
public class ArgumentMultimap {

    /** Prefixes mapped to their respective arguments**/
    private final Map<Prefix, List<String>> argMultimap = new HashMap<>();

    /**
     * Associates the specified argument value with {@code prefix} key in this map.
     * If the map previously contained a mapping for the key, the new value is appended to the list of existing values.
     *
     * @param prefix   Prefix key with which the specified argument value is to be associated
     * @param argValue Argument value to be associated with the specified prefix key
     */
    public void put(Prefix prefix, String argValue) {
        List<String> argValues = getAllValues(prefix);
        argValues.add(argValue);
        argMultimap.put(prefix, argValues);
    }

    /**
     * Returns the last value of {@code prefix}.
     */
    public Optional<String> getValue(Prefix prefix) {
        List<String> values = getAllValues(prefix);
        return values.isEmpty() ? Optional.empty() : Optional.of(values.get(values.size() - 1));
    }

    /**
     * Returns all values of {@code prefix}.
     * If the prefix does not exist or has no values, this will return an empty list.
     * Modifying the returned list will not affect the underlying data structure of the ArgumentMultimap.
     */
    public List<String> getAllValues(Prefix prefix) {
        if (filterIdenticalPrefix(prefix).size() == 0) {
            return new ArrayList<>();
        }
        return new ArrayList<>(filterIdenticalPrefix(prefix));
    }

    /**
     * Returns a list String which belongs to the requested prefix
     * @param prefix
     * @return
     */
    private List<String> filterIdenticalPrefix (Prefix prefix) {
        //return a map view of identical keys
        Map<Prefix, List<String>> test = new TreeMap<>(argMultimap.entrySet().stream()
                .filter(item -> item.getKey().equals(prefix))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        //fill the list with each string
        List<String> list = new ArrayList<>();
        test.entrySet().forEach(item -> list.add(item.getValue().get(item.getValue().size() - 1)));
        return list;
    }


//TODO: maybe use comparator instead
//    /**
//     * Returns a list String which belongs to the requested prefix
//     * @param prefix
//     * @return
//     */
//    private List<String> filterIdenticalPrefix (Prefix prefix) {
//        //return a map view of identical keys
//        List<String> list = new ArrayList<>();
//        Map<Prefix, List<String>> orderedPrefix = new TreeMap<>((Prefix o1, Prefix o2)->o1.getPosition()-o2.getPosition());
//        orderedPrefix.putAll(argMultimap.entrySet().stream()
//                .filter(item -> item.getKey().equals(prefix))
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
//        //fill the list with each string
//        orderedPrefix.entrySet().forEach(item -> list.add(item.getValue().get(item.getValue().size() - 1)));
//        return list;
//    }

    /**
     * Attempts to find the {@code Prefix} used as the key with the same position as the caret
     * @param caretPosition
     * @return Prefix if found and null if not
     */
    public Prefix findPrecedingPrefixKey(int caretPosition) {
        Set<Prefix> keySet = argMultimap.keySet();
        List<Prefix> filtered = keySet.stream()
                .filter(prefix -> prefix.getPrefix() != ArgumentTokenizer.END_MARKER)
                .filter(prefix -> (prefix.getPosition() + prefix.getPrefix().length()) <= caretPosition)
                .collect(Collectors.toList());

        filtered.sort(Comparator.comparing(prefix -> prefix.getPosition()));

        if (filtered.size() == 0) {
            return null;
        }
        // Should get the last matched Prefix (as it is the closest to the caret)
        return filtered.get(filtered.size() - 1);
    }

    /**
     * Attempts to find the {@code Prefix} that is positioned after
     * @param currentPrefix
     */
    public Prefix findSucceedingPrefixKey(Prefix currentPrefix) {
        Set<Prefix> keySet = argMultimap.keySet();
        List<Prefix> filtered = keySet.stream()
                .filter(prefix -> currentPrefix.getPosition() <= prefix.getPosition())
                .collect(Collectors.toList());

        filtered.sort(Comparator.comparing(prefix -> prefix.getPosition()));

        if (filtered.size() <= 1) {
            return currentPrefix;
        }
        // Return the second matched Prefix (which would be the succeeding Prefix after currentPrefix)
        // The first matched should be the currentPrefix
        return filtered.get(1);
    }

    /**
     * Returns the preamble (text before the first valid prefix). Trims any leading/trailing spaces.
     */
    public String getPreamble() {
        return getValue(new Prefix(ArgumentTokenizer.START_MARKER)).orElse("");
    }
}

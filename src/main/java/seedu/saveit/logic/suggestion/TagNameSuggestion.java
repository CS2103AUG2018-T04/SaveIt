package seedu.saveit.logic.suggestion;

import java.util.*;
import java.util.stream.Collectors;

import seedu.saveit.commons.util.StringUtil;
import seedu.saveit.logic.parser.ArgumentTokenizer;
import seedu.saveit.logic.parser.Prefix;
import seedu.saveit.model.Model;

/**
 * The suggestion component which stores and provides tag name key words
 */
public class TagNameSuggestion implements Suggestion {

    private static final String TAG_SUCCESS = "Existing Tag selected";

    private Model model;
    private String argument;
    private Prefix startPrefix;
    private Prefix endPrefix;

    public TagNameSuggestion(Model model, String argument, Prefix startPrefix, Prefix endPrefix) {
        this.model = model;
        this.argument = argument;
        this.startPrefix = startPrefix;
        this.endPrefix = endPrefix;
    }

    /**
     * Compares and match the keywords.
     */
    @Override
    public SuggestionResult evaluate() {
        List<String> tags = model.getCurrentTagSet()
                .stream()
                .filter(tagName -> StringUtil.partialMatchFromStart(tagName, argument))
                .collect(Collectors.toList());
        tags.sort(String.CASE_INSENSITIVE_ORDER);

        LinkedList<SuggestionValue> values = new LinkedList<>();
        for (String tag : tags) {
            values.add(new SuggestionValue(tag, tag));
        }

        int startPosition = startPrefix.getPosition() + startPrefix.getPrefix().length();
        int endPosition = endPrefix.getPrefix() == ArgumentTokenizer.END_MARKER
                ? endPrefix.getPosition() : endPrefix.getPosition() - 1;

        return new SuggestionResult(values, TAG_SUCCESS, startPosition, endPosition);
    }
}

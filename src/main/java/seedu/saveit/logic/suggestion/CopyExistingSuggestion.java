package seedu.saveit.logic.suggestion;

import static seedu.saveit.logic.parser.CliSyntax.PREFIX_DESCRIPTION_STRING;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT_STRING;

import java.util.Arrays;
import java.util.LinkedList;

import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.parser.ArgumentTokenizer;
import seedu.saveit.logic.parser.Prefix;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;

/**
 * Prompts the user with a suggestion to copy and paste the existing text value
 *  in any field of the object that is being edited
 *  e.g. {@code Description} of {@code Issue}
 *  e.g. {@code Remark} of {@code Solution}
 */
public class CopyExistingSuggestion implements Suggestion {

    private static final String COPY_EXISTING_PROMPT = "Copy Existing...";
    private static final String COPY_EXISTING_SUCCESS = "Existing value copied.";

    private Model model;
    private Index index;
    private Prefix startPrefix;
    private Prefix endPrefix;

    public CopyExistingSuggestion(Model model, Index index, Prefix startPrefix, Prefix endPrefix) {
        this.model = model;
        this.index = index;
        this.startPrefix = startPrefix;
        this.endPrefix = endPrefix;
    }

    @Override
    public SuggestionResult evaluate() {
        String result = getValueFromIdentifier(startPrefix.getPrefix());
        SuggestionValue value = new SuggestionValue(COPY_EXISTING_PROMPT, result);
        LinkedList<SuggestionValue> values = new LinkedList<>(Arrays.asList(value));
        int startPosition = startPrefix.getPosition() + startPrefix.getPrefix().length();
        int endPosition = endPrefix.getPrefix() == ArgumentTokenizer.END_MARKER
                ? endPrefix.getPosition() : endPrefix.getPosition() - 1;

        return new SuggestionResult(values, COPY_EXISTING_SUCCESS, startPosition, endPosition);
    }

    /**
     * Get value based on identifier
     */
    private String getValueFromIdentifier(String identifier) {
        Issue issue = model.getFilteredAndSortedIssueList().get(index.getZeroBased());

        switch (identifier) {

        case PREFIX_DESCRIPTION_STRING:
            return issue.getDescription().value;

        case PREFIX_STATEMENT_STRING:
            return issue.getStatement().issue;

        default:
            return "";
        }
    }
}

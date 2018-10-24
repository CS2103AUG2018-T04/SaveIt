package seedu.address.model.issue;

import java.util.Comparator;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Issue;

/**
 * Create respective Comparator based on the command inputted.
 */
public class IssueSort {
    private static final String FREQUENCY_SORT = "freq";
    private static final String CHRONOLOGICAL_SORT = "chro";
    private static final String TAG_SORT = "tag";
    private final Comparator<Issue> comparator;

    public IssueSort(String sort_type) throws ParseException {
        switch (sort_type) {
        case FREQUENCY_SORT:
            this.comparator = new IssueFreqSort();
            break;
        case CHRONOLOGICAL_SORT:
            this.comparator = new IssueChroSort();
            break;
        case TAG_SORT:
            this.comparator = new IssueTagSort();
            break;
        default:
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }
    }

    public Comparator<Issue> getComparator() {
        return this.comparator;
    }

}
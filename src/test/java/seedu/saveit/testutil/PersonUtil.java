package seedu.saveit.testutil;

import static seedu.saveit.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.saveit.logic.commands.AddCommand;
import seedu.saveit.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.saveit.model.issue.Issue;
import seedu.saveit.model.tag.Tag;

/**
 * A utility class for Issue.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code issue}.
     */
    public static String getAddCommand(Issue issue) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(issue);
    }

    /**
     * Returns the part of command string for the given {@code issue}'s details.
     */
    public static String getPersonDetails(Issue issue) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + issue.getName().issue + " ");
        sb.append(PREFIX_PHONE + issue.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + issue.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + issue.getAddress().value + " ");
        issue.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.issue).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}

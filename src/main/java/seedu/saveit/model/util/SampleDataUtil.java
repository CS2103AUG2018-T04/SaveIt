package seedu.saveit.model.util;

import static seedu.saveit.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_SOLUTION_LINK;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_STATEMENT;
import static seedu.saveit.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.saveit.model.Issue;
import seedu.saveit.model.ReadOnlySaveIt;
import seedu.saveit.model.SaveIt;
import seedu.saveit.model.issue.Description;
import seedu.saveit.model.issue.IssueStatement;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;
import seedu.saveit.model.issue.solution.Remark;
import seedu.saveit.model.issue.solution.SolutionLink;

/**
 * Contains utility methods for populating {@code SaveIt} with sample data.
 */
public class SampleDataUtil {

    public static Issue[] getSampleIssues() {
        String[] solutionArrF = {"https://www.reddit.com/", "yes"};
        String[] solutionArrG = {"https://docs.oracle.com/javase/7/docs/api/overview-summary.html", "remark"};
        String[] solutionArrH = {"https://www.google.com.sg/", "newRemark"};
        return new Issue[]{
            new Issue(new IssueStatement("Java NullPointer"), new Description("cannot find object"),
                getSolutionList(new Solution(new SolutionLink("https://stackoverflow.com/"),
                        new Remark("remark"))), getTagSet("solved")),
            new Issue(new IssueStatement("StackOverflow"), new Description("Cannot run"),
                    getSolutionList(
                            new Solution(new SolutionLink("https://ivle.nus.edu.sg/v1/workspace.aspx"),
                                    new Remark("newRemark")),
                            new Solution(new SolutionLink("https://www.wikipedia.org/"),
                                    new Remark("remark"))),
                    getTagSet("newBug", "notSolved")),
            new Issue(new IssueStatement("ArrayIndexOutOfBounds"), new Description("invalid input"),
                    getSolutionList(new Solution(new SolutionLink("https://www.zhihu.com/"),
                            new Remark("solutionIsHere"))),
                    getTagSet("notSolved")),
            new Issue(new IssueStatement("ClassNotFoundException"), new Description("WrongPackage"),
                    getSolutionList(new Solution(new SolutionLink("https://stackoverflow.com/"),
                            new Remark("This solution is quite simple."))),
                    getTagSet("urgent")),
            new Issue(new IssueStatement("ExceptionNotHandled"), new Description("Mistake"),
                    getSolutionList(
                            new Solution(new SolutionLink("https://www.reddit.com/"),
                                    new Remark("Not sure about this one.")),
                            new Solution(new SolutionLink(
                                    "https://docs.oracle.com/javase/7/docs/api/overview-summary.html"),
                                    new Remark("Ask prof tmr."))),
                    getTagSet("solved")),
            new Issue(new IssueStatement("UnknownBug"), new Description("Unknown"),
                    getSolutionList(new Solution(new SolutionLink("https://www.google.com.sg/"),
                            new Remark("Ask John to solve."))),
                    getTagSet("Dead"))
        };
    }

    public static ReadOnlySaveIt getSampleSaveIt() {
        SaveIt sampleAb = new SaveIt();
        for (Issue sampleIssue : getSampleIssues()) {
            sampleAb.addIssue(sampleIssue);
        }
        return sampleAb;
    }

    /**
     * Returns a solution set containing the list of strings given.
     */
    public static List<Solution> getSolutionList(Solution... solutions) {
        return Arrays.asList(solutions);
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
            .map(Tag::new)
            .collect(Collectors.toSet());
    }

    /**
     * Returns true if the commandWord should follow with index otherwise false.
     */
    public static boolean isCommandWordNeedIndex(String commandWord) {
        String[] commandWordNotNeedIndex = {
            "addtag", "delete", "edit", "retrieve", "select", "setprimary", "at", "d", "e", "rv", "s", "sp"};
        return Arrays.stream(commandWordNotNeedIndex).parallel().anyMatch(commandWord::equals);
    }

    /**
     * check if find the parameter
     * @param inputCheck every two consecutive characters.
     * @return true if parameter, otherwise false
     */
    public static boolean isPrefixParameter(String inputCheck) {
        return inputCheck.equals(PREFIX_STATEMENT.toString()) || inputCheck.equals(PREFIX_SOLUTION_LINK.toString())
            || inputCheck.equals(PREFIX_REMARK.toString()) || inputCheck.equals(PREFIX_DESCRIPTION.toString())
            || inputCheck.equals(PREFIX_TAG.toString()) || inputCheck.equals(PREFIX_NEW_TAG.toString());
    }
}

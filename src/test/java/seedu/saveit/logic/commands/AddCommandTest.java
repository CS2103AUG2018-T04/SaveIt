package seedu.saveit.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.saveit.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_DESCRIPTION_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_SOLUTION_C;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_SOLUTION_JAVA;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_SOLUTION_STACKOVERFLOW;
import static seedu.saveit.logic.commands.CommandTestUtil.VALID_STATEMENT_C;
import static seedu.saveit.testutil.TypicalDirectories.ROOT_LEVEL;
import static seedu.saveit.testutil.TypicalDirectories.getCustomizedIssueLevel;
import static seedu.saveit.testutil.TypicalDirectories.getCustomizedSolutionLevel;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_FIRST_ISSUE;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_SECOND_SOLUTION;
import static seedu.saveit.testutil.TypicalIndexes.INDEX_THIRD_SOLUTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.saveit.commons.core.directory.Directory;
import seedu.saveit.commons.core.index.Index;
import seedu.saveit.logic.CommandHistory;
import seedu.saveit.logic.commands.exceptions.CommandException;
import seedu.saveit.model.Issue;
import seedu.saveit.model.Model;
import seedu.saveit.model.ReadOnlySaveIt;
import seedu.saveit.model.SaveIt;
import seedu.saveit.model.issue.IssueSort;
import seedu.saveit.model.issue.Solution;
import seedu.saveit.model.issue.Tag;
import seedu.saveit.testutil.IssueBuilder;

public class AddCommandTest {
    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullIssue_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    //=========== Add Issue Test ===================================================================================

    @Test
    public void execute_solutionAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingIssueAdded modelStub = new ModelStubAcceptingIssueAdded();
        Issue validIssue = new IssueBuilder().build();

        CommandResult commandResult = new AddCommand(validIssue).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_ISSUE_SUCCESS, validIssue), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validIssue), modelStub.issuesAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateIssue_throwsCommandException() throws Exception {
        Issue validIssue = new IssueBuilder().build();
        AddCommand addCommand = new AddCommand(validIssue);
        ModelStub modelStub = new ModelStubWithIssue(validIssue);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_ISSUE);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void execute_issueLevelAddIssue_throwsCommandException() throws Exception {
        Issue validIssue = new IssueBuilder().build();
        AddCommand addCommand = new AddCommand(validIssue);
        ModelStub modelStub = new ModelStubAcceptingIssueAdded();
        Directory issueLevel = getCustomizedIssueLevel(INDEX_FIRST_ISSUE);
        modelStub.resetDirectory(issueLevel);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_WRONG_DIRECTORY);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void execute_solutionLevelAddIssue_throwsCommandException() throws Exception {
        Issue validIssue = new IssueBuilder().build();
        AddCommand addCommand = new AddCommand(validIssue);
        ModelStub modelStub = new ModelStubAcceptingIssueAdded();
        Directory solutionLevel = getCustomizedSolutionLevel(INDEX_FIRST_ISSUE, INDEX_THIRD_SOLUTION);
        modelStub.resetDirectory(solutionLevel);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_WRONG_DIRECTORY);
        addCommand.execute(modelStub, commandHistory);
    }

    //=========== Add Solution test ===================================================================================

    @Test
    public void execute_issueAcceptedByModel_issueLevelAddSuccessful() throws Exception {
        ModelStubAcceptingSolutionAdded modelStub = new ModelStubAcceptingSolutionAdded(new IssueBuilder().build());
        Directory issueLevel = getCustomizedIssueLevel(INDEX_FIRST_ISSUE);
        modelStub.resetDirectory(issueLevel);

        Issue validIssue = new IssueBuilder().withDummyStatement().withDummyDescription()
                .withSolutions(VALID_SOLUTION_STACKOVERFLOW).build();
        Issue expectedIssue = new IssueBuilder().withSolutions(VALID_SOLUTION_STACKOVERFLOW).build();
        CommandResult commandResult = new AddCommand(validIssue).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SOLUTION_SUCCESS,
                new Solution(VALID_SOLUTION_STACKOVERFLOW)), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(expectedIssue), modelStub.issuesAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_issueAcceptedByModel_solutionLevelAddSuccessful() throws Exception {
        ModelStubAcceptingSolutionAdded modelStub = new ModelStubAcceptingSolutionAdded(new IssueBuilder()
                .withSolutions(VALID_SOLUTION_JAVA, VALID_SOLUTION_C).build());
        Directory issueLevel = getCustomizedSolutionLevel(INDEX_FIRST_ISSUE, INDEX_SECOND_SOLUTION);
        modelStub.resetDirectory(issueLevel);

        Issue validIssue = new IssueBuilder().withDummyStatement().withDummyDescription()
                .withSolutions(VALID_SOLUTION_STACKOVERFLOW).build();
        Issue expectedIssue = new IssueBuilder()
                .withSolutions(VALID_SOLUTION_JAVA, VALID_SOLUTION_C, VALID_SOLUTION_STACKOVERFLOW).build();
        CommandResult commandResult = new AddCommand(validIssue).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SOLUTION_SUCCESS,
                new Solution(VALID_SOLUTION_STACKOVERFLOW)), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(expectedIssue), modelStub.issuesAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateSolution_throwsCommandException() throws Exception {
        ModelStubAcceptingSolutionAdded modelStub = new ModelStubAcceptingSolutionAdded(new IssueBuilder()
                .withSolutions(VALID_SOLUTION_JAVA, VALID_SOLUTION_C).build());
        Directory issueLevel = getCustomizedIssueLevel(INDEX_FIRST_ISSUE);
        modelStub.resetDirectory(issueLevel);

        Issue validIssue = new IssueBuilder().withDummyStatement().withDummyDescription()
                .withSolutions(VALID_SOLUTION_JAVA).build();
        AddCommand addCommand = new AddCommand(validIssue);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_SOLUTION);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void execute_rootLevelAddSolution_throwsCommandException() throws Exception {
        ModelStubAcceptingSolutionAdded modelStub = new ModelStubAcceptingSolutionAdded(new IssueBuilder().build());
        modelStub.resetDirectory(ROOT_LEVEL);

        Issue validIssue = new IssueBuilder().withDummyStatement().withDummyDescription()
                .withSolutions(VALID_SOLUTION_JAVA).build();
        AddCommand addCommand = new AddCommand(validIssue);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_FAILED_ISSUE);
        addCommand.execute(modelStub, commandHistory);
    }

    //=========== Add Issue then Solution test ========================================================================

    @Test
    public void execute_issueAndSolutionAcceptedByModel_issueLevelAddSuccessful() throws Exception {
        ModelStubAcceptingIssueAndSolutionAdded modelStub = new ModelStubAcceptingIssueAndSolutionAdded();
        Issue validIssue = new IssueBuilder().withStatement(VALID_STATEMENT_C)
                .withDescription(VALID_DESCRIPTION_C).build();

        CommandResult commandResult = new AddCommand(validIssue).execute(modelStub, commandHistory);
        assertEquals(String.format(AddCommand.MESSAGE_ISSUE_SUCCESS, validIssue), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validIssue), modelStub.issuesAdded);

        Directory issueLevel = getCustomizedIssueLevel(INDEX_FIRST_ISSUE);
        modelStub.resetDirectory(issueLevel);
        validIssue = new IssueBuilder().withDummyStatement().withDummyDescription()
                .withSolutions(VALID_SOLUTION_STACKOVERFLOW).build();
        Issue expectedIssue = new IssueBuilder().withStatement(VALID_STATEMENT_C)
                .withDescription(VALID_DESCRIPTION_C).withSolutions(VALID_SOLUTION_STACKOVERFLOW).build();
        commandResult = new AddCommand(validIssue).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SOLUTION_SUCCESS,
                new Solution(VALID_SOLUTION_STACKOVERFLOW)), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(expectedIssue), modelStub.issuesAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    //=========== Equality test =======================================================================================

    @Test
    public void equals() {
        Issue alice = new IssueBuilder().withStatement("Alice").build();
        Issue bob = new IssueBuilder().withStatement("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different issue -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing;
     */
    private class ModelStub implements Model {

        @Override
        public void addIssue(Issue issue) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlySaveIt newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetDirectory(Directory currentDirectory) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Directory getCurrentDirectory() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlySaveIt getSaveIt() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasIssue(Issue issue) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasSolution(Index index, Solution solution) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteIssue(Issue target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addSolution(Index index, Solution solution) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateIssue(Issue target, Issue editedIssue) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Issue> getFilteredIssueList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Solution> getFilteredSolutionList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Issue> getFilteredAndSortedIssueList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void filterIssues(Predicate<Issue> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredIssueList(Predicate<Issue> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortIssues(IssueSort sortType) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredAndSortedIssueList(Comparator<Issue> sortType) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addTag(Index index, Set<Tag> tagList) {
            throw new AssertionError("This method should not be called.");
        }
        public TreeSet<String> getCurrentTagSet() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public TreeSet<String> getCurrentIssueStatementSet() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoSaveIt() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoSaveIt() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoSaveIt() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoSaveIt() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitSaveIt() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean refactorTag(Tag oldTag, Tag newTag) {
            return false;
        }
    }

    /**
     * A Model stub that contains a single issue and a directory;
     */
    private class ModelStubWithIssue extends ModelStub {

        private final Directory directory = ROOT_LEVEL;
        private final Issue issue;

        ModelStubWithIssue(Issue issue) {
            requireNonNull(issue);
            this.issue = issue;
        }

        @Override
        public boolean hasIssue(Issue issue) {
            requireNonNull(issue);
            return this.issue.isSameIssue(issue);
        }

        @Override
        public Directory getCurrentDirectory() {
            return directory;
        }
    }

    /**
     * A Model stub that always accept the issue being added.
     */
    private class ModelStubAcceptingIssueAdded extends ModelStub {

        final ArrayList<Issue> issuesAdded = new ArrayList<>();
        Directory directory = ROOT_LEVEL;

        @Override
        public boolean hasIssue(Issue issue) {
            requireNonNull(issue);
            return issuesAdded.stream().anyMatch(issue::isSameIssue);
        }

        @Override
        public void addIssue(Issue issue) {
            requireNonNull(issue);
            issuesAdded.add(issue);
        }

        @Override
        public void commitSaveIt() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlySaveIt getSaveIt() {
            return new SaveIt();
        }

        @Override
        public void resetDirectory(Directory currentDirectory) {
            this.directory = currentDirectory;
        }

        @Override
        public Directory getCurrentDirectory() {
            return directory;
        }
    }

    /**
     * A Model stub that always accept the solution being added.
     */
    private class ModelStubAcceptingSolutionAdded extends ModelStub {

        final ArrayList<Issue> issuesAdded = new ArrayList<>();
        Directory directory = ROOT_LEVEL;

        public ModelStubAcceptingSolutionAdded(Issue issue) {
            issuesAdded.add(issue);
        }

        public ModelStubAcceptingSolutionAdded() { }

        @Override
        public boolean hasIssue(Issue issue) {
            requireNonNull(issue);
            return issuesAdded.stream().anyMatch(issue::isSameIssue);
        }

        @Override
        public boolean hasSolution(Index index, Solution solution) {
            requireAllNonNull(index, solution);
            return issuesAdded.get(index.getZeroBased()).getSolutions().contains(solution);
        }

        @Override
        public void addSolution(Index index, Solution solution) {
            requireAllNonNull(index, solution);

            Issue issueToEdit = issuesAdded.get(index.getZeroBased());
            List<Solution> solutionsToUpdate = new ArrayList<>(issueToEdit.getSolutions());
            solutionsToUpdate.add(solution);
            Issue updateIssue = new Issue(issueToEdit.getStatement(), issueToEdit.getDescription(),
                    solutionsToUpdate, issueToEdit.getTags(), issueToEdit.getFrequency());
            issuesAdded.set(index.getZeroBased(), updateIssue);
        }

        @Override
        public void commitSaveIt() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlySaveIt getSaveIt() {
            return new SaveIt();
        }

        @Override
        public void resetDirectory(Directory currentDirectory) {
            this.directory = currentDirectory;
        }

        @Override
        public Directory getCurrentDirectory() {
            return directory;
        }
    }

    private class ModelStubAcceptingIssueAndSolutionAdded extends ModelStubAcceptingSolutionAdded {

        @Override
        public void addIssue(Issue issue) {
            issuesAdded.add(issue);
        }
    }
}

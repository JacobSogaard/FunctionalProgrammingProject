package com.example.tournamentandroid;

import com.example.tournamentandroid.models.ITournament;

import org.assertj.core.util.diff.Delta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.quicktheories.core.DetatchedRandomnessSource;
import org.quicktheories.core.Gen;

import enums.TournamentType;
import models.Game;
import models.Player;
import models.Team;
import models.Tournament;

import static enums.TournamentType.SINGLEELIMINATION;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.Generate.characters;
import static org.quicktheories.generators.SourceDSL.arbitrary;
import static org.quicktheories.generators.SourceDSL.integers;
import static org.quicktheories.generators.SourceDSL.lists;
import static org.quicktheories.generators.SourceDSL.longs;
import static org.quicktheories.generators.SourceDSL.strings;
import org.junit.Test;
import org.quicktheories.core.RandomnessSource;
import org.quicktheories.core.stateful.Command;
import org.quicktheories.core.stateful.Sequential;
import org.quicktheories.generators.Generate;
import org.quicktheories.impl.Constraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class QuickCheckerTest {

    private final TournamentType TYPE = TournamentType.SINGLEELIMINATION;
    Random r = new Random();
    private Object Team;

    @Test
    public void addingTwoPositiveIntegersAlwaysGivesAPositiveInteger(){
        qt()
                .forAll(integers().allPositive()
                        , integers().allPositive())
                .check((i,j) -> i + j > 0);
    }


    /**
     * Must be a better way to do this
     */
    @Test
    public void createTournamentHasTeams() {

            qt()
                    .forAll(integers().between(1,1000))
                    .check((i) -> {

                        ArrayList<Team> arr=new ArrayList<Team>(Collections.nCopies(i, new Team("t", new Player("1", 0), new Player("2",0))));

                        Tournament tournament = new Tournament(TYPE, arr);
                        return !tournament.getAllTeams().isEmpty();
                    });


/*
        qt()
                .forAll(lists().of(teams()).ofSize(r.nextInt())) //Add random integer stuff to ofSize()
                .check((l) -> {
                    Tournament t = new Tournament(TournamentType.SINGLEELIMINATION, l);
                    return t.getAllTeams().size() > 0;
                });

 */


    }

    //foreach integer, generate teams at that amount - test
    @Test
    public void concludeMatchRemovesTeam(){
        qt()
                .forAll(integers().between(4, 32)) //Add random integer stuff to ofSize()
                .check((i) -> {
                    ArrayList<Team> arr=new ArrayList<Team>(Collections.nCopies(i, new Team("t", new Player("1", 0), new Player("2",0))));
                    Tournament t = new Tournament(TournamentType.SINGLEELIMINATION, arr);
                    t.start();
                    int osize = t.getAllTeams().size(); // Original size
                    Game g = t.getNextMatch();
                    g.setMatchWinner(Objects.requireNonNull(g.getTeam1()), t.getCurrentRound());
                    t.concludeMatch(g); //Conclude match
                    int newSize = t.getAllTeams().size();
                    return newSize == osize-1 && newSize < i && newSize > 0; //new size should be 1 less than before
                });
    }

    @Test
    public void tournamentDoneWhenOneTeamLeftRandomTeams(){
        qt()
                .forAll(lists().of(teams()).ofSize(20)) //Add random integer stuff to ofSize()
                .check((l) -> {
                    Tournament t = new Tournament(TournamentType.SINGLEELIMINATION, l);
                    t.start();

                    while (t.getAllTeams().size() > 1) {
                        Game g = t.getNextMatch();
                        g.setMatchWinner(Objects.requireNonNull(g.getTeam1()), t.getCurrentRound());
                        t.concludeMatch(g);
                    }
                    return t.isDone() && t.getAllTeams().size() == 1;
                });
    }

    @Test
    public void tournamentDoneWhenOneTeamLeftRandomListLength(){
        qt()
                .forAll(integers().between(19,20)) //Add random integer stuff to ofSize()
                .check((i) -> {
                    ArrayList<Team> arr=new ArrayList<Team>(Collections.nCopies(i, new Team("t", new Player("1", 0), new Player("2",0))));
                    Tournament t = new Tournament(TournamentType.SINGLEELIMINATION, arr);
                    t.start();

                    while (t.getAllTeams().size() > 1) {
                        Game g = t.getNextMatch();
                        g.setMatchWinner(Objects.requireNonNull(g.getTeam1()), t.getCurrentRound());
                        t.concludeMatch(g);

                    }
                    if (t.getAllTeams().size() == 1) {
                        return t.isDone();
                    }
                    return !t.isDone();
                });
    }

    @Test
    public void numberOfRoundsTestFixedSize(){
        //This does not seem to work :(

                    qt()
                            .forAll(lists().of(teams()).ofSize(50)) //do so it generates more than one size
                            .check((l) -> {
                                Tournament t = new Tournament(TournamentType.SINGLEELIMINATION, l);
                                t.start();
                                if (t.getAllTeams().size() > 32) {
                                    return t.getMaxRounds() == 0;
                                } else {
                                    return t.getMaxRounds() == log2(t.getAllTeams().size()); //fails at teams size == 1
                                }
                    });


    }

    //Everything above 32 should return 0
    @Test
    public void numberOfRoundsTestFixedTeams(){
        qt()
                .forAll(integers().between(4,1000))
                .check((i) -> {
                    ArrayList<Team> arr=new ArrayList<Team>(Collections.nCopies(i, new Team("t", new Player("1", 0), new Player("2",0))));
                    Tournament t = new Tournament(TournamentType.SINGLEELIMINATION, arr);
                    t.start();

                    if (t.getAllTeams().size() > 32) {
                        return t.getMaxRounds() == 0;
                    } else {
                        return t.getMaxRounds() == log2(t.getAllTeams().size()); //fails at teams size == 1
                    }
                });

    }



    public static int log2(int x)
    {
        return (int) Math.ceil(Math.log(x) / Math.log(2));
    }

    /*
    private Tournament tournament(){
        return new Tournament(TournamentType.SINGLEELIMINATION, lists().of(teams()).ofSize(20));
    }
     */

    @Test
    public void checkTeamsGenerator(){
        qt()
                .withTestingTime(5, TimeUnit.SECONDS)
                .forAll(teams())
                .check((i) -> {
                    //System.out.println(i.getTeamName() + " ; " + i.getPlayer1().getName() + " ; " + i.getPlayer2().getName());
                    return i.getPlayer2().getName().length() > 1;
                });
    }

    @Test
    public void checkTeamnameGenerator(){
        qt()
                .forAll(teamnames())
                .check((i) -> {
                    System.out.println(i);
                    return i.length() <= 10 && i.length() > 0;
                    });
    }

    @Test
    public void checkPlayernameGenerator(){
        qt()
                .forAll(playernames())
                .check((i) -> {
                    System.out.println(i);
                    return i.length() <= 20  && i.length() > 0;
                });
    }

    /**
     * PRINT THE OUTPUT OF THESE AND VERIFY THEM!
     * @return
     */
    private Gen<Team> teams() {
        return teamnames().zip(players(), players(),
                Team::new);
    }

    private Gen<Player> players(){
        return playernames().zip(wongames(),
                Player::new);
    }

    private Gen<Integer> wongames() {
        return integers().from(0).upToAndIncluding(1004856);
    }

    private Gen<String> playernames(){
        return strings().basicLatinAlphabet().ofLengthBetween(1, 20);
    }

    private Gen<String> teamnames(){
        return strings().basicLatinAlphabet().ofLengthBetween(1, 10);
    }

    @Test
    public void sequential() {

        Gen<List<Commands>> commandSequences = lists().of(arbitrary()
                .enumValuesWithNoOrder(Commands.class))
                .ofSizeBetween(1, 100);

        Gen<List<CommandsList>> commandSequencesList = lists().of(arbitrary()
                .enumValuesWithNoOrder(CommandsList.class))
                .ofSizeBetween(1, 50);

        //All of the following 3 state-machines work, the IDE however does not like having multiple. So just remove the comment on the one you want to use.
/*
        qt()
                .forAll(lists().of(teams()).ofSize(20), commandSequences)
                .checkAssert((initialState, commands) -> Sequential.modelCheck(initialState.size(), commands, l -> new TournamentMock(initialState, initialState.size()), ITournament::get));

        qt()
                .forAll(integers().between(4,33), commandSequences)
                .checkAssert((initialState, commands) -> Sequential.modelCheck(initialState, commands, l -> new TournamentMock(l), ITournament::get));
        }

*/
        qt()
                .forAll(lists().of(integers().allPositive()).ofSizeBetween(1,100000), commandSequencesList)
                .checkAssert((initialState, commands) -> Sequential.modelCheck(initialState.size(), commands, l -> new ArrayList<Integer>(Collections.nCopies(l, 0)), ArrayList<Integer>::size));
        }


}

enum Commands implements Command<ITournament, Integer> {
    ConcludeMatch() {
        @Override
        public void run(ITournament sut) {
            Game g = sut.getNextMatch();
            g.setMatchWinner(g.getTeam1(), sut.getCurrentRound());
            sut.concludeMatch(Objects.requireNonNull(g));
        }

        @Override
        public Integer nextState(Integer state) {
            System.out.println(state);
            return state - 1;
        }
    },

    GET() {
        @Override
        public void run(ITournament sut) {
            sut.get();
        }

        @Override
        public Integer nextState(Integer state) {
            System.out.println(state);
            return state;
        }
    },
}

class TournamentMock implements ITournament {

    Integer n = 0;
    private List<Team> turL;
    private Tournament tur;


    TournamentMock(List<Team> turL, Integer size) {
        n = size;

        this.turL = turL;
        this.tur = new Tournament(TournamentType.SINGLEELIMINATION, turL);
        this.tur.start();
    }

    TournamentMock(Integer size) {
        this.n = size;
        ArrayList<Team> arr=new ArrayList<Team>(Collections.nCopies(size, new Team("t", new Player("1", 0), new Player("2",0))));
        this.tur = new Tournament(TournamentType.SINGLEELIMINATION, arr);
        tur.start();

    }

    @NotNull
    @Override
    public List<Team> getAllTeams() {
        return tur.getAllTeams();
    }

    @Override
    public void setRandom(boolean bool) {

    }

    @Override
    public void setCupLimit(int limit) {

    }

    @Override
    public int getCupLimit() {
        return 0;
    }

    @Nullable
    @Override
    public List<Game> getConcludedMatches() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public int getCurrentRound() {
        return 0;
    }

    @Override
    public int getMaxRounds() {
        return 0;
    }

    @Override
    public boolean hasRound(int round) {
        return false;
    }

    @NotNull
    @Override
    public List<Game> getMathcesForRound(int round) {
        return null;
    }

    @Nullable
    @Override
    public Boolean isDone() {
        return null;
    }

    @Nullable
    @Override
    public Game getNextMatch() {
        return tur.getNextMatch();
    }

    @Override
    public void concludeMatch(@NotNull Game match) {
        tur.concludeMatch(match);
        n = tur.getAllTeams().size();
    }

    @Override
    public void setWinner(@Nullable Team team) {

    }

    @Override
    public int get() {
        return n;
    }
}


enum CommandsList implements Command<ArrayList<Integer>, Integer> {

    ADD() {
        @Override
        public void run(ArrayList<Integer> sut) {
            Random r = new Random();
            sut.add(r.nextInt());
        }

        @Override
        public Integer nextState(Integer state) {
            return state + 1;
        }
    },

    REMOVE() {
        @Override
        public void run(ArrayList<Integer> sut) {
            Random r = new Random();
            if (sut.size() != 0) {
                sut.remove(r.nextInt(sut.size()));
            }
        }

        @Override
        public Integer nextState(Integer state) {
            if (state != 0) {
                return state - 1;
            }
            return 0;
        }
    },

    SIZE() {
        @Override
        public void run(ArrayList<Integer> sut) {
            sut.size();
        }

        @Override
        public Integer nextState(Integer state) {
            return state;
        }
    },

    CLEAR() {
        @Override
        public void run(ArrayList<Integer> sut) {
            sut.clear();
        }

        @Override
        public Integer nextState(Integer state) {
            return 0;
        }
    },
}

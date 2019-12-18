package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.ConsensusVote;
import main.Fragment;
import main.FragmentBuilder;

public class ConsensusVoteTest {

	@Test
	public void testConsensusVote() {
    	
    	FragmentBuilder[] tab = new FragmentBuilder[8];
    	
    	tab[0] = new FragmentBuilder(new Fragment("taagaaaaataa-"));
    	tab[1] = new FragmentBuilder(new Fragment("taa-aaaaataa-"));
    	tab[2] = new FragmentBuilder(new Fragment("tca-aagaataa-"));
    	tab[3] = new FragmentBuilder(new Fragment("tca-aagaataa-"));
    	tab[4] = new FragmentBuilder(new Fragment("tcacaagaacaa-"));
    	tab[5] = new FragmentBuilder(new Fragment("acagaagaa-aa-"));
    	tab[6] = new FragmentBuilder(new Fragment("acagaagaa----"));
    	tab[7] = new FragmentBuilder(new Fragment("aaaaaaaaaaaac"));
    	
    	assertEquals(new Fragment("tcagaagaataac"), ConsensusVote.consensusVote(tab));
	}
	
	@Test
	public void test2() {
		FragmentBuilder[] fbArray = GapPropagatorTest.getExample2();
		assertEquals(new Fragment("cccccacggttcaggaaggggttccaaaaa"), ConsensusVote.consensusVote(fbArray));
	}
	
}

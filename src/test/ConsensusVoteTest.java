package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.ConsensusVote;
import main.Fragment;

public class ConsensusVoteTest {

	@Test
	public void testConsensusVote() {
    	
    	Fragment[] tab = new Fragment[8];
    	
    	tab[0] = new Fragment("taagaaaaataa-");
    	tab[1] = new Fragment("taa-aaaaataa-");
    	tab[2] = new Fragment("tca-aagaataa-");
    	tab[3] = new Fragment("tca-aagaataa-");
    	tab[4] = new Fragment("tcacaagaacaa-");
    	tab[5] = new Fragment("acagaagaa-aa-");
    	tab[6] = new Fragment("acagaagaa----");
    	tab[7] = new Fragment("aaaaaaaaaaaac");
    	
    	assertEquals(new Fragment("tcagaagaataac"), ConsensusVote.consensusVote(tab));
	}

}

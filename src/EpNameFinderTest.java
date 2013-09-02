import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.Before;


public class EpNameFinderTest {

	EpNameFinder ef1;
	
	@Before
	public void setUp() throws IOException {
		ef1 = new EpNameFinder();
	}
	
	@Test
	public void testSize() throws IOException {
		ef1.run("Top Gear", 1);
		assertTrue(ef1.getSize() != 0);
		
		ef1.run("How I Met Your Mother", 1);
		assertTrue(ef1.getSize() != 0);
		
		ef1.run("Breaking Bad", 1);
		assertTrue(ef1.getSize() != 0);
		
		ef1.run("Suits", 1);
		assertTrue(ef1.getSize() != 0);
		
		ef1.run("Game of Thrones", 1);
		assertTrue(ef1.getSize() != 0);
		
		ef1.run("Family Guy", 1);
		assertTrue(ef1.getSize() != 0);
		
		ef1.run("The Big Bang Theory", 1);
		assertTrue(ef1.getSize() != 0);
	}
	
	@Test
	public void testCaseSensitive() throws IOException {

		ef1 = new EpNameFinder();

		ef1.run("Family Guy", 5);
		String firstRun = ef1.getEpisodeName(5);

		ef1.run("FAMILY GUY", 5);
		String secondRun = ef1.getEpisodeName(5);

		ef1.run("family guy", 5);
		String thirdRun = ef1.getEpisodeName(5);

		ef1.run("fAmIlYgUy", 5);
		String fourthRun = ef1.getEpisodeName(5);
		
		assertTrue(firstRun.equals(secondRun) && firstRun.equals(thirdRun) && firstRun.equals(fourthRun) );

	}
	
	@Test
	public void testTopGear() throws IOException {
		ef1.run("Top Gear", 16);
		
		//Episode 4 is at index 5, hence the 'weird' test code.
		assertEquals(ef1.getEpisodeName(3), "Series 16, Episode 4");
		
		ef1.run("Top Gear", 2);
		assertEquals(ef1.getEpisodeName(0), "Series 2, Episode 1");
		
		ef1.run("Top Gear", 11);
		assertEquals(ef1.getEpisodeName(5), "Series 11, Episode 6");
		
		ef1.run("Top Gear", 3);
		assertEquals(ef1.getEpisodeName(1), "Series 3, Episode 2");
	}

}

package org.xandercat.ofe.searchutility.candidate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Properties;

public class CSVCandidateSourceTest {

	private File getTempFile(int rows) throws Exception {
		File file = File.createTempFile("abc", "def");
		file.deleteOnExit();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (int i=0; i<rows; i++) {
			String s = String.valueOf(i);
			writer.write(s + ",Hello There " + s + ",this is a test,my short desc " + s + ",This is my full description " + s + "\n");
		}
		writer.close();
		return file;
	}
	
	@Test
	public void testCSVCandidateSourceGeneric() throws Exception {
		CSVCandidateSource<MapCandidate> source = new CSVCandidateSource<MapCandidate>();
		Properties properties = new Properties();
		File csvFile = getTempFile(2);
		properties.put("file", csvFile.getAbsolutePath());
		properties.put("fields", "id,field1,field2,shortDescription,fullDescription");
		source.initialize(properties, MapCandidate.class);
		Collection<MapCandidate> candidates = source.getCandidates();
		assertEquals(2, candidates.size());
		MapCandidate[] mc = new MapCandidate[2];
		mc = candidates.toArray(mc); 
		assertEquals("0", mc[0].get("id"));
		assertEquals("this is a test", mc[0].get("field2"));
		assertEquals("my short desc 0", mc[0].get("shortDescription"));
		assertEquals("1", mc[1].get("id"));
		assertEquals("This is my full description 1", mc[1].get("fullDescription"));
		assertEquals("0", mc[0].getUniqueId());
		assertEquals("my short desc 1", mc[1].getShortDescription());
	}
}

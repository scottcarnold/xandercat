package org.xandercat.ofe.searchutility.candidate;

import org.junit.Test;
import org.xandercat.ofe.Candidate;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Properties;

public class CSVCandidateSourceTest {

	public static class TestClass implements Candidate {
	
		private String string1;
		private Integer integer1;
		private int int1;
		private Boolean boolean1;
		private boolean primitiveBoolean1;
		
		public TestClass() {
		}
		public TestClass(String string1, Integer integer1, int int1, Boolean boolean1, boolean primitiveBoolean1) {
			this.string1 = string1;
			this.integer1 = integer1;
			this.int1 = int1;
			this.boolean1 = boolean1;
			this.primitiveBoolean1 = primitiveBoolean1;
		}
		public String getString1() {
			return string1;
		}
		public void setString1(String string1) {
			this.string1 = string1;
		}
		public Integer getInteger1() {
			return integer1;
		}
		public void setInteger1(Integer integer1) {
			this.integer1 = integer1;
		}
		public int getInt1() {
			return int1;
		}
		public void setInt1(int int1) {
			this.int1 = int1;
		}
		public Boolean getBoolean1() {
			return boolean1;
		}
		public void setBoolean1(Boolean boolean1) {
			this.boolean1 = boolean1;
		}
		public boolean isPrimitiveBoolean1() {
			return primitiveBoolean1;
		}
		public void setPrimitiveBoolean1(boolean primitiveBoolean1) {
			this.primitiveBoolean1 = primitiveBoolean1;
		}
		@Override
		public String getUniqueId() {
			return string1;
		}
		@Override
		public String getShortDescription() {
			return string1;
		}
		@Override
		public String getFullDescription() {
			return toString();
		}
	}
	
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
	
	private File getTempFileTestClass(int rows) throws Exception {
		File file = File.createTempFile("bcd", "efg");
		file.deleteOnExit();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (int i=0; i<rows; i++) {
			String s = String.valueOf(i);
			writer.write(s + "," + s + "," + s + ",true,true\n");
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
	
	@Test
	public void testCSVCandidateSourceCustomClass() throws Exception {
		CSVCandidateSource<TestClass> source = new CSVCandidateSource<TestClass>();
		Properties properties = new Properties();
		File csvFile = getTempFileTestClass(2);
		properties.put("file", csvFile.getAbsolutePath());
		properties.put("fields", "string1,integer1,int1,boolean1,primitiveBoolean1");
		source.initialize(properties, TestClass.class);
		Collection<TestClass> candidates = source.getCandidates();
		assertEquals(2, candidates.size());
		TestClass[] tc = new TestClass[2];
		tc = candidates.toArray(tc);
		assertEquals("0",tc[0].getString1());
		assertEquals(Integer.valueOf(0),tc[0].getInteger1());
		assertEquals(0,tc[0].getInt1());
		assertEquals(Boolean.TRUE,tc[0].getBoolean1());
		assertEquals(true,tc[0].isPrimitiveBoolean1());
	}
}

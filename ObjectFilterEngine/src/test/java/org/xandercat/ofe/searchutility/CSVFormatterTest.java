package org.xandercat.ofe.searchutility;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;

public class CSVFormatterTest {

	public static class TestClass {
	
		private String string1;
		private Integer integer1;
		private Boolean boolean1;
		
		public TestClass() {
		}
		public TestClass(String string1, Integer integer1, Boolean boolean1) {
			this.string1 = string1;
			this.integer1 = integer1;
			this.boolean1 = boolean1;
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
		public Boolean getBoolean1() {
			return boolean1;
		}
		public void setBoolean1(Boolean boolean1) {
			this.boolean1 = boolean1;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((boolean1 == null) ? 0 : boolean1.hashCode());
			result = prime * result + ((integer1 == null) ? 0 : integer1.hashCode());
			result = prime * result + ((string1 == null) ? 0 : string1.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestClass other = (TestClass) obj;
			if (boolean1 == null) {
				if (other.boolean1 != null)
					return false;
			} else if (!boolean1.equals(other.boolean1))
				return false;
			if (integer1 == null) {
				if (other.integer1 != null)
					return false;
			} else if (!integer1.equals(other.integer1))
				return false;
			if (string1 == null) {
				if (other.string1 != null)
					return false;
			} else if (!string1.equals(other.string1))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "TestClass [string1=" + string1 + ", integer1=" + integer1 + ", boolean1=" + boolean1 + "]";
		}
	}
	
	@Test
	public void testFormatAndParse() throws Exception {
		CSVFormatter<TestClass> formatter = new CSVFormatter<TestClass>(TestClass.class)
			.fields("string1","integer1","boolean1");
		TestClass tc = new TestClass("string", Integer.valueOf(34), Boolean.TRUE);
		String csvString = formatter.format(tc);
		assertEquals("string,34,true", csvString);
		TestClass tc2 = formatter.parse(csvString);
		assertEquals(tc, tc2);
	}
	
	@Test
	public void testFormatAndParseWithEscapes() throws Exception {
		CSVFormatter<TestClass> formatter = new CSVFormatter<TestClass>(TestClass.class)
			.fields("string1","integer1","boolean1");
		TestClass tc = new TestClass("Doe, John", Integer.valueOf(34), Boolean.TRUE);
		String csvString = formatter.format(tc);
		assertEquals("\"Doe, John\",34,true", csvString);
		TestClass tc2 = formatter.parse(csvString);
		assertEquals(tc, tc2);	
		tc.setString1("Provolone, Angelo \"Snaps\"");
		csvString = formatter.format(tc);
		assertEquals("\"Provolone, Angelo \"\"Snaps\"\"\",34,true", csvString);
		tc2 = formatter.parse(csvString);
		assertEquals(tc, tc2);
	}
	
	@Test
	public void testForceAsString() throws Exception {
		CSVFormatter<TestClass> formatter = new CSVFormatter<TestClass>(TestClass.class)
			.fields("string1","integer1","boolean1");
		formatter.setForceAsStringFields(Collections.singleton("string1"));
		TestClass tc = new TestClass("string", Integer.valueOf(34), Boolean.TRUE);
		String csvString = formatter.format(tc);
		assertEquals("\"=\"\"string\"\"\",34,true", csvString);	
	}
}

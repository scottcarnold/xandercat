package org.xandercat.ofe.searchutility;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CSVFormatterTest {

	public static class TestClassWithNesting {
		
		private String string1;
		private TestClass testClass1;
		
		public TestClassWithNesting() {
		}
		public TestClassWithNesting(String string1, TestClass testClass1) {
			this.string1 = string1;
			this.testClass1 = testClass1;
		}
		public String getString1() {
			return string1;
		}
		public void setString1(String string1) {
			this.string1 = string1;
		}
		public TestClass getTestClass1() {
			return testClass1;
		}
		public void setTestClass1(TestClass testClass1) {
			this.testClass1 = testClass1;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((string1 == null) ? 0 : string1.hashCode());
			result = prime * result + ((testClass1 == null) ? 0 : testClass1.hashCode());
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
			TestClassWithNesting other = (TestClassWithNesting) obj;
			if (string1 == null) {
				if (other.string1 != null)
					return false;
			} else if (!string1.equals(other.string1))
				return false;
			if (testClass1 == null) {
				if (other.testClass1 != null)
					return false;
			} else if (!testClass1.equals(other.testClass1))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "TestClassWithNesting [string1=" + string1 + ", testClass1=" + testClass1 + "]";
		}
	}
	
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
	public void testFormatAndParseWithEmptyValues() throws Exception {
		CSVFormatter<TestClass> formatter = new CSVFormatter<TestClass>(TestClass.class)
				.fields("string1","integer1","boolean1");
			TestClass tc = new TestClass("", null, Boolean.TRUE);
			String csvString = formatter.format(tc);
			assertEquals(",,true", csvString);
			TestClass tc2 = formatter.parse(csvString);
			assertEquals(tc, tc2);		
			tc = new TestClass("one", null, null);
			csvString = formatter.format(tc);
			assertEquals("one,,", csvString);
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
	
	@Test
	public void testFormatAndParseWithNesting() throws Exception {
		CSVFormatter<TestClassWithNesting> formatter = new CSVFormatter<TestClassWithNesting>(TestClassWithNesting.class);
		formatter.setFields(new String[] { "string1", "testClass1.string1", "testClass1.integer1", "testClass1.boolean1" });
		TestClassWithNesting tc = new TestClassWithNesting("string1", new TestClass("nestedstring1", Integer.valueOf(12), Boolean.TRUE));
		String csvString = formatter.format(tc);
		assertEquals("string1,nestedstring1,12,true", csvString);
		TestClassWithNesting tc2 = formatter.parse(csvString);
		assertEquals(tc, tc2);
	}
	
	@Test
	public void testFormatAndParseWithMap() throws Exception {
		CSVFormatter<Map<String, Object>> formatter = CSVFormatter.genericFormatter();
		formatter.setFields(new String[] {"int1","integer1","string1"}, new Class[] {Integer.TYPE,Integer.class,String.class});
		Map<String,Object> tc = new HashMap<String,Object>();
		tc.put("int1", 1);  // should get autoboxed as Integer
		tc.put("integer1", Integer.valueOf(2));
		tc.put("string1", "maptest");
		String csvString = formatter.format(tc);
		assertEquals("1,2,maptest", csvString);
		Map<String,Object> tc2 = formatter.parse(csvString);
		assertEquals(3, tc2.size());
		assertEquals(Integer.valueOf(1), tc2.get("int1"));
		assertEquals(Integer.valueOf(2), tc2.get("integer1"));
		assertEquals("maptest", tc2.get("string1"));
	}
}

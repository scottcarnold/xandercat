package org.xandercat.ofe;

import org.junit.Test;
import org.xandercat.ofe.filter.IntegerFilter;
import org.xandercat.ofe.filter.NumericMatchStyle;
import org.xandercat.ofe.filter.StringFilter;
import org.xandercat.ofe.filter.StringMatchStyle;

import static org.junit.Assert.*;

import java.util.SortedSet;

public class ObjectFilterEngineTest {

	private static class NestedTestClass {
		
		private String string1;

		@SuppressWarnings("unused")
		public NestedTestClass() {
		}
		public NestedTestClass(String string1) {
			this.string1 = string1;
		}
		@SuppressWarnings("unused")
		public String getString1() {
			return string1;
		}
		@SuppressWarnings("unused")
		public void setString1(String string1) {
			this.string1 = string1;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
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
			NestedTestClass other = (NestedTestClass) obj;
			if (string1 == null) {
				if (other.string1 != null)
					return false;
			} else if (!string1.equals(other.string1))
				return false;
			return true;
		}
	}
	
	private static class TestClass implements Candidate {
		
		private Integer integer1;
		private int int1;
		private NestedTestClass nestedTestClass;
		
		@SuppressWarnings("unused")
		public TestClass() {
		}
		public TestClass(Integer integer1, int int1) {
			this.integer1 = integer1;
			this.int1 = int1;
		}
		public TestClass(Integer integer1, int int1, NestedTestClass nestedTestClass) {
			this(integer1, int1);
			this.nestedTestClass = nestedTestClass;
		}
		@SuppressWarnings("unused")
		public Integer getInteger1() {
			return integer1;
		}
		@SuppressWarnings("unused")
		public void setInteger1(Integer integer1) {
			this.integer1 = integer1;
		}
		public int getInt1() {
			return int1;
		}
		@SuppressWarnings("unused")
		public void setInt1(int int1) {
			this.int1 = int1;
		}
		@SuppressWarnings("unused")
		public NestedTestClass getNestedTestClass() {
			return nestedTestClass;
		}
		@SuppressWarnings("unused")
		public void setNestedTestClass(NestedTestClass nestedTestClass) {
			this.nestedTestClass = nestedTestClass;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + int1;
			result = prime * result + ((integer1 == null) ? 0 : integer1.hashCode());
			result = prime * result + ((nestedTestClass == null) ? 0 : nestedTestClass.hashCode());
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
			if (int1 != other.int1)
				return false;
			if (integer1 == null) {
				if (other.integer1 != null)
					return false;
			} else if (!integer1.equals(other.integer1))
				return false;
			if (nestedTestClass == null) {
				if (other.nestedTestClass != null)
					return false;
			} else if (!nestedTestClass.equals(other.nestedTestClass))
				return false;
			return true;
		}
		@Override
		public String getUniqueId() {
			return String.valueOf(int1);
		}
		@Override
		public String getShortDescription() {
			return getUniqueId();
		}
		@Override
		public String getFullDescription() {
			return "Object with ID " + getUniqueId();
		}
	}
	
	@Test
	public void testPrimitivesWithWrapperFilters() throws Exception {
		ObjectFilterEngine<TestClass> ofe = new ObjectFilterEngine<TestClass>();
		ofe.addFilter("integer1", new IntegerFilter(NumericMatchStyle.GREATER_THAN, Integer.valueOf(0)));
		ofe.addFilter("int1", new IntegerFilter(NumericMatchStyle.GREATER_THAN, Integer.valueOf(2)).required());
		TestClass tc1 = new TestClass(Integer.valueOf(3), 6);
		TestClass tc2 = new TestClass(Integer.valueOf(1), 2);
		ofe.addCandidate(tc1);
		ofe.addCandidate(tc2);
		SortedSet<ScoredCandidate<TestClass>> scoredCandidates = ofe.getScoredCandidates();
		assertEquals(1, scoredCandidates.size());
		assertEquals(6, scoredCandidates.first().getCandidate().getInt1());
	}
	
	@Test
	public void testFilteringOnNestedFields() throws Exception {
		ObjectFilterEngine<TestClass> ofe = new ObjectFilterEngine<TestClass>();
		ofe.addFilter("integer1", new IntegerFilter(NumericMatchStyle.GREATER_THAN, Integer.valueOf(0)));
		ofe.addFilter("int1", new IntegerFilter(NumericMatchStyle.GREATER_THAN, Integer.valueOf(2)).required());
		ofe.addFilter("nestedTestClass.string1", new StringFilter(StringMatchStyle.EQUALS, "nt").required());
		TestClass tc1 = new TestClass(Integer.valueOf(3), 6, new NestedTestClass("nt"));
		TestClass tc2 = new TestClass(Integer.valueOf(4), 8, new NestedTestClass("rx"));
		ofe.addCandidate(tc1);
		ofe.addCandidate(tc2);
		SortedSet<ScoredCandidate<TestClass>> scoredCandidates = ofe.getScoredCandidates();
		assertEquals(1, scoredCandidates.size());
		assertEquals(6, scoredCandidates.first().getCandidate().getInt1());
	}
}

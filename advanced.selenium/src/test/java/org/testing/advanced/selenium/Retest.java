package org.testing.advanced.selenium;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class Retest {
  @Test
  public void retest() {
	  System.out.println("kale");
  }
  @BeforeMethod
  public void beforeMethod() {
	  System.out.println("Manoj");
  }

}

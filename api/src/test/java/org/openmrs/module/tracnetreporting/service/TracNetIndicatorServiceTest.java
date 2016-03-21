package org.openmrs.module.tracnetreporting.service;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;


public class TracNetIndicatorServiceTest extends BaseModuleContextSensitiveTest {
	
	@Test
	public void testIfServiceExists() {
		Assert.assertNotNull(Context.getService(TracNetIndicatorService.class));
	}
}

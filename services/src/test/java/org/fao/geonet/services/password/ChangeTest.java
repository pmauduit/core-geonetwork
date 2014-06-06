package org.fao.geonet.services.password;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import jeeves.server.context.ServiceContext;

import org.fao.geonet.domain.Pair;
import org.fao.geonet.domain.Profile;
import org.fao.geonet.domain.User;
import org.fao.geonet.exceptions.BadParameterEx;
import org.fao.geonet.exceptions.MissingParameterEx;
import org.fao.geonet.exceptions.UserNotFoundEx;
import org.fao.geonet.repository.UserRepository;
import org.fao.geonet.services.AbstractServiceIntegrationTest;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test suite for the Change controller, which allows users to change their passwords.
 * 
 * @author pmauduit
 *
 */
public class ChangeTest extends AbstractServiceIntegrationTest {

	private final Change changeController = new Change();
	@Autowired
	private UserRepository userRepo;

	private ServiceContext context;
	
	@Before
	public void setUp() throws Exception {
		context = createServiceContext();
	}
	
	@Test
	public void unknownUserchangeTest() throws Exception {
		Element params = createParams(Pair.write("username", "notexistinguser"),
				Pair.write("password", "fakepassword"),
				Pair.write("changeKey", "invalidKey"));
		try {
			changeController.exec(params, context);
		} catch (Throwable e) {
			assertTrue(e instanceof UserNotFoundEx);
		}
	}

	@Test
	public void missingParameterChangeTest() throws Exception {
		Element params = createParams(Pair.write("notExpected", "notExpectedParameter"));
		try {
			changeController.exec(params, context);
		} catch (Throwable e) {
			assertTrue(e instanceof MissingParameterEx);
		}
	}
	
	@Test
	public void legitUserInvalidKeyChangeTest() throws Exception {
		// Creates a registereduser willing to change its password
		// using the controller.
		assertTrue(userRepo != null);
		User legitUser = new User().setName("legitchange testuser")
				.setOrganisation("GeoNetwork")
				.setProfile(Profile.RegisteredUser)
				.setSurname("testuser")
				.setUsername("testuser");
		legitUser.getEmailAddresses().add("testuser@localhost");
		userRepo.saveAndFlush(legitUser);

		Element params = createParams(Pair.write("username", "testuser"),
				Pair.write("password", "fakepassword"),
				Pair.write("changeKey", "invalidKey"));
		
		try {
			changeController.exec(params, context);
		} catch (Throwable e) {
			assertTrue(e instanceof BadParameterEx);
		}
	}

	@Test
	public void legitUserChangeTest() throws Exception {
		// Creates a registereduser willing to change its password
		// using the controller.
		assertTrue(userRepo != null);
		User legitUser = new User().setName("legitchange testuser")
				.setOrganisation("GeoNetwork")
				.setProfile(Profile.RegisteredUser)
				.setSurname("testuser")
				.setUsername("testuser");
		legitUser.getEmailAddresses().add("testuser@localhost");
		userRepo.saveAndFlush(legitUser);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String todaysDate = sdf.format(new Date());
		
	    
	    
		Element params = createParams(Pair.write("username", "testuser"),
				Pair.write("password", "fakepassword"),
				Pair.write("changeKey", todaysDate));
		
			Element ret = changeController.exec(params, context);
			assertTrue(ret.getName().equals("response"));
	}
	
}

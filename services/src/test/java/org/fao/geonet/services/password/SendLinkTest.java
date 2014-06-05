package org.fao.geonet.services.password;

import static org.junit.Assert.assertTrue;

import java.util.List;

import jeeves.server.context.ServiceContext;

import org.fao.geonet.domain.Pair;
import org.fao.geonet.domain.Profile;
import org.fao.geonet.domain.User;
import org.fao.geonet.exceptions.UserNotFoundEx;
import org.fao.geonet.repository.UserRepository;
import org.fao.geonet.services.AbstractServiceIntegrationTest;
import org.fao.geonet.utils.Xml;
import org.jdom.Element;
import org.junit.Test;


/**
 * Test suite for the "sendlink" controller.
 * 
 * SendLink allows a user to retrieve its account in case of forgotten password, under certain conditions:
 * - The user has to exist (obviously)
 * - The user should be of profile RegisteredUser
 * 
 * @author pmauduit
 */
public class SendLinkTest extends AbstractServiceIntegrationTest {

	final SendLink slController = new SendLink();

	/**
	 *  Tests the controller passing a unknown user ;
	 * @throws Exception
	 */
	@Test
	public void unknownUsersendLinkTest() throws Exception {
		ServiceContext context = createServiceContext();
	    Element params = createParams(Pair.write("username", "notexisting"));
	    try {
	    	slController.exec(params, context);
	    } catch (Throwable e) {
	    	assertTrue(e instanceof UserNotFoundEx);
	    }
	}
	
	/** 
	 * Tests on a user who has a profile too high ;
	 * (only registered users can ask for a password renewal)
	 *
	 * @throws Exception
	 */
	@Test
	public void tooHighProfileUserSendLinkTest() throws Exception {
		ServiceContext context = createServiceContext();
		Element params = createParams(Pair.write("username", "admin"));
		try {
			slController.exec(params, context);
		} catch (Throwable e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	/**
	 *  Tests on a registered user, should complete successfully.
	 * @throws Exception
	 */
	@Test
	public void registeredUserSendLinkTest() throws Exception {
		ServiceContext context = createServiceContext();
		UserRepository userRepository = context.getBean(UserRepository.class);

		User testUser = new User().setName("sendlink testuser")
				.setOrganisation("GeoNetwork")
				.setProfile(Profile.RegisteredUser)
				.setSurname("testuser")
				.setUsername("sendlinktestuser");
		
		testUser.getEmailAddresses().add("sendlink.testuser@localhost");

		userRepository.saveAndFlush(testUser);
		
		
		Element params = createParams(Pair.write("username", "sendlinktestuser"));
		Element ret = slController.exec(params, context);

		assertTrue(ret.getName().equals("response"));
	}
}

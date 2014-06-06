package org.fao.geonet.services.user;

import static org.junit.Assert.assertTrue;
import jeeves.server.UserSession;
import jeeves.server.context.ServiceContext;

import org.fao.geonet.domain.Address;
import org.fao.geonet.domain.Group;
import org.fao.geonet.domain.ISODate;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.domain.MetadataStatus;
import org.fao.geonet.domain.Pair;
import org.fao.geonet.domain.Profile;
import org.fao.geonet.domain.User;
import org.fao.geonet.domain.UserGroup;
import org.fao.geonet.repository.GroupRepository;
import org.fao.geonet.repository.MetadataRepository;
import org.fao.geonet.repository.MetadataStatusRepository;
import org.fao.geonet.repository.StatusValueRepository;
import org.fao.geonet.repository.UserGroupRepository;
import org.fao.geonet.repository.UserRepository;
import org.fao.geonet.services.AbstractServiceIntegrationTest;
import org.fao.geonet.utils.Xml;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Tests the "delete user" controller.
 *
 * User: pmauduit
 */
public class UserRemoveIntegrationTest  extends AbstractServiceIntegrationTest {

	private final Remove userRemoveController = new Remove();
	private ServiceContext context ;
	
	@Autowired
	private UserGroupRepository userGroupRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private MetadataRepository metadataRepository;
	
	@Autowired
	private MetadataStatusRepository metadataStatusRepository;
	
	@Autowired
	private StatusValueRepository statusValueRepository;
	
	@Before
	public void setUp() throws Exception {
		context = createServiceContext();		
	}
	
	/**
	 * Tests the user self removal, which should raise an IllegalArgumentException.
	 * 
	 * @throws Exception
	 */
	@Test
	public void removeSelfUserTest() throws Exception {
		Element params = createParams(Pair.write("id", "1"));

		// Faking the administrator user
		this.loginAsAdmin(context);
		try {
			userRemoveController.exec(params, context);
		} catch (Throwable e) {
			assertTrue (e instanceof IllegalArgumentException);
		}	
	}
	
	/**
	 * Tests the user removal, connected as a low-profile user.
	 * Should raise an IllegalArgumentException (only Administrator's and UserAdmin's users can delete users).
	 * 
	 * @throws Exception
	 */
	@Test
	public void lowPrivilegesRemoveUserTest() throws Exception {
		Element params = createParams(Pair.write("id", "42"));

		// Prepares a user
		User loggedUser = new User().setName("UserRemovalTest")
				.setOrganisation("geonetwork")
				.setUsername("userremovaltest");
		
		loggedUser.getEmailAddresses().add("user@localhost");

		Profile[] lowLevelProfs =  { Profile.Editor, Profile.RegisteredUser, Profile.Reviewer, Profile.Guest };
		for (Profile p: lowLevelProfs) {
			loggedUser.setProfile(p);
			_userRepo.saveAndFlush(loggedUser);
			this.loginAs(loggedUser, context);
			try {
				userRemoveController.exec(params, context);
			} catch (Throwable e) {
				assertTrue (e instanceof IllegalArgumentException);
			}				
		}
	}

	/**
	 * Tests the User removal controller onto a non-existing user.
	 * Should raise an EmptyResultDataAccessException.
	 *
	 * @throws Exception
	 */
	@Test
	public void removeNonExistingUserTest() throws Exception {
		Element params = createParams(Pair.write("id", "42"));

		loginAsAdmin(context);
		
		try {
			userRemoveController.exec(params, context);
		} catch (Throwable e) {
			System.out.println(e);
			assertTrue(e instanceof EmptyResultDataAccessException);
		}
	}

	/**
	 * Tests to remove a user not member of a group we are managing as UserAdmin.
	 * Should raise a IllegalParameterException.
	 * @throws Exception
	 */
	@Test
	public void removeUserByUserAdminWithNoGroupPrivilegeTest() throws Exception {

		// Prepares a UserAdmin user to connect as
		User loggedUser = new User().setName("UserRemovalTest")
				.setOrganisation("geonetwork")
				.setUsername("userremovaltest");
		
		loggedUser.getEmailAddresses().add("user@localhost");
		loggedUser.setProfile(Profile.UserAdmin);

		// creates a user to remove
		User toBeRemoved = new User().setName("ToBe Removed")
				.setOrganisation("geonetwork")
				.setUsername("toberemoved");
		
		toBeRemoved.getEmailAddresses().add("remove-me@localhost");
		_userRepo.saveAndFlush(toBeRemoved);

		Element params = createParams(Pair.write("id", toBeRemoved.getId()));

		Group newGroup = new Group().setDescription("yet another group").setEmail("newgroup@localhost").setName("anothergroup");
		groupRepository.saveAndFlush(newGroup);
		userGroupRepository.saveAndFlush(new UserGroup().setUser(toBeRemoved).setGroup(newGroup).setProfile(Profile.Editor));

		loginAs(loggedUser, context);

		try {
			userRemoveController.exec(params, context);
		} catch (Throwable e) {
			// "java.lang.IllegalArgumentException: You don't have rights to delete this user because the user is not part of your group"
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	/**
	 * Tests to remove a user who is also a metadata owner.
	 * Should raise a IllegalParameterException.
	 * @throws Exception
	 */
	@Test
	public void removeMetadataOwnerUserTest() throws Exception {
		// creates a user to remove
		User toBeRemoved = new User().setName("ToBe Removed")
				.setOrganisation("geonetwork")
				.setUsername("toberemoved");
		
		toBeRemoved.getEmailAddresses().add("remove-me@localhost");
		_userRepo.saveAndFlush(toBeRemoved);
		
		// Associates the first MD to the created user
		Metadata md = new Metadata().setData("<dummymd />")
				.setUuid("12345-654321-55555-92345");
		md.getSourceInfo().setOwner(toBeRemoved.getId()).setSourceId("GeoNetwork autogenerated - testing framework");
		md.getDataInfo().setSchemaId("dummy-schema");
		
		metadataRepository.saveAndFlush(md);
		
		Element params = createParams(Pair.write("id", toBeRemoved.getId()));

		loginAsAdmin(context);

		try {
			userRemoveController.exec(params, context);
		} catch (Throwable e) {
			// "java.lang.IllegalArgumentException: Cannot delete a user that is also a metadata owner"
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	/**
	 * Tests to remove a user who also generated a status.
	 * Should raise a IllegalParameterException.
	 * @throws Exception
	 */
	@Test
	public void removeMetadataUserWithStatusTest() throws Exception {
		// creates a user to remove
		User toBeRemoved = new User().setName("ToBe Removed")
				.setOrganisation("geonetwork")
				.setUsername("toberemoved");
		
		toBeRemoved.getEmailAddresses().add("remove-me@localhost");
		_userRepo.saveAndFlush(toBeRemoved);
		
		// Creates a new MD
		Metadata md = new Metadata().setData("<dummymd />")
				.setUuid("12345-654321-55555-92345");
		md.getSourceInfo().setOwner(1).setSourceId("GeoNetwork autogenerated - testing framework");
		md.getDataInfo().setSchemaId("dummy-schema");
		metadataRepository.saveAndFlush(md);
		
		// Create a new MetadataStatus related to the user to be removed
		MetadataStatus st = new MetadataStatus();
		st.setStatusValue(statusValueRepository.findOneByName("approved"));
		st.getId().setMetadataId(md.getId()).setUserId(toBeRemoved.getId()).setChangeDate(new ISODate());
		st.setChangeMessage("this has been approved by JUnit.");
		metadataStatusRepository.saveAndFlush(st);
		
		
		Element params = createParams(Pair.write("id", toBeRemoved.getId()));

		loginAsAdmin(context);

		try {
			userRemoveController.exec(params, context);
		} catch (Throwable e) {
			// "java.lang.IllegalArgumentException: Cannot delete a user that has set a metadata status"
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	/**
	 * Tests to remove a user (who actually can be removed).
	*
	 * @throws Exception
	 */
	@Test
	public void removeUserTest() throws Exception {
		// creates a user to remove
		User toBeRemoved = new User().setName("ToBe Removed")
				.setOrganisation("geonetwork")
				.setUsername("toberemoved");
		
		toBeRemoved.getEmailAddresses().add("remove-me@localhost");
		_userRepo.saveAndFlush(toBeRemoved);

		Element params = createParams(Pair.write("id", toBeRemoved.getId()));
		loginAsAdmin(context);


		Element ret = userRemoveController.exec(params, context);
		assertTrue(ret.getName().equals("response"));
	}
	
}

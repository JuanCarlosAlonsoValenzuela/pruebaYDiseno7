
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.SocialProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SocialProfileServiceTest extends AbstractTest {

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private ActorService			actorService;


	public SocialProfile create(String nick, String name, String profileLink) {
		SocialProfile socialProfile = new SocialProfile();

		socialProfile.setName(name);
		socialProfile.setNick(nick);
		socialProfile.setProfileLink(profileLink);

		return socialProfile;
	}

	@Test
	public void createTest() {

		super.authenticate("customer1");
		this.actorService.loggedAsActor();

		SocialProfile socialProfile = new SocialProfile();
		SocialProfile saved = new SocialProfile();

		socialProfile = this.socialProfileService.create();
		socialProfile.setName("Name");
		socialProfile.setNick("Nick");
		socialProfile.setProfileLink("https://www.youtube.com");

		saved = this.socialProfileService.save(socialProfile);

		Assert.isTrue(this.socialProfileService.findAll().contains(saved));
		super.authenticate(null);

	}

}

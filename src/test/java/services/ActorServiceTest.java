
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private TutorialService			tutorialService;


	// TEST---------------------------------------------------------------------

	/*
	 * public void loggedAsActor() { UserAccount userAccount; userAccount =
	 * LoginService.getPrincipal();
	 * Assert.isTrue(userAccount.getAuthorities().size() > 0); }
	 */

	@Test
	public void testNumberOfActors() {
		Collection<Actor> all;
		super.authenticate("customer1");
		all = this.actorService.findAll();
		Assert.isTrue(all.size() == 17);
		super.authenticate(null);
	}

	@Test
	public void testActorByUsername() {
		Actor actor = new Actor();
		super.authenticate("customer1");
		actor = this.actorService.getActorByUsername("customer1");
		Assert.isTrue(actor.getName().equals("Paco"));
		super.authenticate(null);
	}

	@Test
	public void testShowTutorials() {
		super.authenticate("customer1");
		Assert.isTrue(this.actorService.showTutorials().size() > 0);
		super.authenticate(null);
	}

	@Test
	public void testNumberOfBoxes() {
		Actor actor = new Actor();
		super.authenticate("customer1");
		actor = this.actorService.getActorByUsername("customer1");
		Assert.isTrue(this.actorService.getlistOfBoxes(actor).size() == 4);
		super.authenticate(null);
	}

	@Test
	public void testUpdateActorSpam() {
		Actor actor = new Actor();
		super.authenticate("customer1");
		actor = this.actorService.getActorByUsername("customer1");
		this.configurationService.isActorSuspicious(actor);
		Assert.isTrue(actor.getHasSpam() == false);
		super.authenticate(null);
	}
}

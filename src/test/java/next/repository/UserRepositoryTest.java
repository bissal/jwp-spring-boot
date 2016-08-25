package next.repository;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import core.test.IntegrationTest;
import next.model.User;

public class UserRepositoryTest extends IntegrationTest {
	@Autowired
	private UserRepository userRepository;

	@Test
	public void crud() {
		User user = new User("userId", "password", "name", "m@il");
		userRepository.save(user);
	}

}

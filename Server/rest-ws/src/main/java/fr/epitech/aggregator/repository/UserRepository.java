package fr.epitech.aggregator.repository;

import java.util.List;
import fr.epitech.aggregator.model.User;

public interface UserRepository {
	List<User> getAll();	
	User get(long id);
	void create(User user);
	void delete(User user);
	void update(User user);
}

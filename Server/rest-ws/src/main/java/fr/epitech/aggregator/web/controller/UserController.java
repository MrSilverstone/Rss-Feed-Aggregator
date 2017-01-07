package fr.epitech.aggregator.web.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.epitech.aggregator.model.User;
import fr.epitech.aggregator.repository.UserRepository;

@RestController
@RequestMapping(value="/api")
public class UserController {
	@Resource
	private UserRepository userRepository;
	
	@RequestMapping(value="/user/{id}", method=RequestMethod.GET)
	public User getUser(@PathVariable long id){
		return userRepository.get(id);
	}
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	public List<User> getUsers(){
		return userRepository.getAll();
	}
}

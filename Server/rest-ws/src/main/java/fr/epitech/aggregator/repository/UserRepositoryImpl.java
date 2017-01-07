package fr.epitech.aggregator.repository;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.epitech.aggregator.model.User;

@Repository
public class UserRepositoryImpl implements UserRepository {
	@Resource JdbcTemplate jt;
	
	@Override
	public User get(long id) {
		String GET_USER_BY_ID = "select * from USER where USER_ID=?";
		return jt.queryForObject(GET_USER_BY_ID, BeanPropertyRowMapper.newInstance(User.class), id);	
	}

	@Override
	public List<User> getAll() {
		String GET_ALL_USERS = "select * from USER";
		return jt.query(GET_ALL_USERS, BeanPropertyRowMapper.newInstance(User.class));
	}

	@Override
	public void create(User user) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void delete(User user) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		
	}
}

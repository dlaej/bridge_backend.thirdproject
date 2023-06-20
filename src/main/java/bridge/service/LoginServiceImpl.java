package bridge.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import bridge.dto.LoginDto;
import bridge.dto.UserDto;
import bridge.mapper.LoginMapper;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginMapper loginMapper;
	// 해시값 설정
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDto login(LoginDto loginDto) throws Exception {
		return loginMapper.login(loginDto);
	}

	@Override
	public int registUser(UserDto userDto) throws Exception {
		// 패스워드를 암호화 해서 새로 저장
		userDto.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
		return loginMapper.registUser(userDto);
	}

	// String username, String password, boolean enabled,
	// boolean accountNonExpired, boolean credentialsNonExpired,
	// boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto userDto = loginMapper.selectUserByUserId(username);
		if (userDto == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(userDto.getUserId(), userDto.getUserPassword(), true, true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getloginDto(UserDto userDto) {
		return loginMapper.getloginDto(userDto);
	}

	// 외부 로그인
	@Override
	public UserDto passInformation(UserDto userDto) throws Exception {
		return loginMapper.passInformation(userDto);
	}

	@Override
	public int userIdCheck(String userIdCheck) throws Exception {
		int result = loginMapper.userIdCheck(userIdCheck);
		return result;
	}

	@Override
	public String findId(String email) {
		// TODO Auto-generated method stub
		return loginMapper.findId(email);
	}

	@Override
	public void findPassword(String email,String password) {
		// TODO Auto-generated method stub
		UserDto userDto =  loginMapper.findPassword(email);
		userDto.setUserPassword(passwordEncoder.encode(password));
		loginMapper.updatePassword(userDto);		

	}
}

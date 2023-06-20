package bridge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import bridge.service.EmailService;
import lombok.extern.slf4j.Slf4j;

/**
 * 이메일 컨트롤러
 */

@Slf4j
@RestController
public class EmailController {
	
	@Autowired
      EmailService emailService;

    @PostMapping("/emailConfirm/{email}")
    public String emailConfirm(@PathVariable("email") String email) throws Exception {
    	log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+email);
      String confirm = emailService.sendSimpleMessage(email);
      return confirm;
    }
}
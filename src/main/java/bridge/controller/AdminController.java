package bridge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import bridge.dto.ReportDto;
import bridge.dto.UserDto;
import bridge.mapper.BridgeMapper;
import bridge.service.BridgeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Slf4j
public class AdminController {

	@Autowired
	private BridgeService bridgeService;

	@Autowired
	private BridgeMapper bridgeMapper;

	@ApiOperation(value = "신고 목록 조회")
	@GetMapping("/api/openReportList")
	public ResponseEntity<List<ReportDto>> openReportList() throws Exception {
		List<ReportDto> list = bridgeService.openReportList();
		if (list == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}
	}
	/* 신고 목록을 조회하는 기능입니다.
	1. ApiOperation
	Swagger란 Rest Api를 편리하게 문서화해주는 프레임워크입니다. 
	Swagger가 제공하는 어노테이션 중 각각의 API에 대한 설명을 달아 놓기 위해 ApiOperation 어노테이션을 사용하였습니다. 
 
	2. @GetMapping 
	@RequestMapping은 http 메서드 요청을 처리할 수 있는 어노테이션입니다.
	메서드 종류로 get, post, put, delete 등이 있습니다.
	그 중 get 요청은 클라이언트에서 서버로 정보를 요청하기 위해 사용되는 http 메소드입니다.
	신고 목록 조회 기능은 단순히 서버로 데이터를 요청하는 기능이기 때문에 GetMapping을 사용했습니다. 

	3. ResponseEntity
	@see bridge.controller.AdminController#selectReportCount()

	*/

	@ApiOperation(value = "특정 신고 목록 조회")
	@GetMapping("/api/openReportDetail/{reportIdx}")
	public ResponseEntity<ReportDto> openReportDetail(@PathVariable("reportIdx") int reportIdx) throws Exception {
		ReportDto reportDto = bridgeService.openReportDetail(reportIdx);
		if (reportDto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(reportDto);
		}
	}

	@ApiOperation(value = "유저 신고 카운트 조회")

	@GetMapping("/api/reportCount/{userId}")
	public ResponseEntity<Object> selectReportCount(@PathVariable("userId") String userId) throws Exception {
		try {
			int a = bridgeMapper.selectReportCount(userId);
			return ResponseEntity.status(HttpStatus.OK).body(a);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
		}
	}

	@ApiOperation(value = "신고 처리")

	@PutMapping("/api/handleReport/{userId}")
	public ResponseEntity<Object> handleReport(@PathVariable("userId") String userId, @RequestBody UserDto userDto)
			throws Exception {
		try {
			userDto.setUserId(userId);
			bridgeService.handleReport(userDto);
			return ResponseEntity.status(HttpStatus.OK).body(1);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
		}
	}

}

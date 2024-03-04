package board.board.controller;

import board.board.dto.BoardDto;
import board.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @Tag : Swagger(또는 OpenAPI) 스펙을 따르는 API 문서를 생성할 때 사용되는 Spring 어노테이션
// API 작업(operation) 또는 컨트롤러 클래스에 적용, API 작업이나 컨트롤러 클래스가 어떤 범주에 속하는지를 지정
// 사용자 관리와 관련된 API 작업은 Users 태그로 그룹화할 수 있다

@Tag(name = "게시판", description = "게시판 관련 API")
@RestController
public class RestBoardApiController {
	
	@Autowired
	private BoardService boardService;

	@Operation(summary = "게시판 목록 조회", description = "게시판 목록이 조회됩니다.")
		// summary 작업에 대한 간단 요약 설명
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})

	// 스웨거란?
	//  RESTful 웹 서비스를 설계, 빌드, 문서화하는 데 사용되는 강력한 프레임워크
	// 사용법?
	// localhost:8088/swagger-ui/ 로 접근한다
	// 자동으로 @Tag를 읽어 RESTful api를 읽어온다. (DTO도 다 읽어온다)

	@RequestMapping(value="/api/board", method=RequestMethod.GET)
	public List<BoardDto> openBoardList() throws Exception{
		return boardService.selectBoardList();
	}

	@Operation(summary = "게시판 작성", description = "게시판을 작성합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})

	@RequestMapping(value="/api/board/write", method=RequestMethod.POST)
	// 스웨거는 URL로 공유가 된다
	public void insertBoard(@RequestBody BoardDto board) throws Exception{
		boardService.insertBoard(board, null);
	}

	@Operation(summary = "게시판 상세조회", description = "게시판을 상세하게 조회합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})
	@RequestMapping(value="/api/board/{boardIdx}", method=RequestMethod.GET)
	@Cacheable(key = "#boardIdx", value ="BOARD")
	public BoardDto openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception{
		
		return boardService.selectBoardDetail(boardIdx);
	}

	@Operation(summary = "게시판 수정", description = "게시판을 수정합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})
	@RequestMapping(value="/api/board/{boardIdx}", method=RequestMethod.PUT)
	@CacheEvict(key = "#boardIdx", value ="BOARD")
	public String updateBoard(@RequestBody BoardDto board) throws Exception{
		boardService.updateBoard(board);
		return "redirect:/board";
	}

	@Operation(summary = "게시판 삭제", description = "게시판을 삭제합니다.")
	// @ApiResponses : Swagger(또는 OpenAPI)를 사용하여 API의 응답에 대한 설명을 지정하는 데 사용되는 Spring 어노테이션
	// @ApiOperation 어노테이션과 함께 사용되어 API 작업에 대한 설명과 함께 응답 코드와 메시지를 문서화
	// 여기서는 @Operation를 사용하였다
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST"),
			@ApiResponse(responseCode = "404", description = "NOT FOUND"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
	})
	@RequestMapping(value="/api/board/{boardIdx}", method=RequestMethod.DELETE)
	@CacheEvict(key = "#boardIdx", value ="BOARD")
	public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
		boardService.deleteBoard(boardIdx);
		return "redirect:/board";
	}
}

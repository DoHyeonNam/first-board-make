package board.board.controller;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;
import board.board.mapstruct.BoardDtoReqMapStruct;
import board.board.mapstruct.BoardFileDtoReqMapStruct;
import board.board.mapstruct.BoardFileDtoResMapStruct;
import board.board.service.JpaBoardService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class JpaBoardController {

	@Autowired
	private JpaBoardService jpaBoardService;

	@Autowired
	private board.common.FileUtils fileUtils;
	
	@Autowired
	BoardFileDtoResMapStruct boardFileDtoResMapStruct;

	@Autowired
	BoardDtoReqMapStruct boardDtoReqMapStruct;

	@Autowired
	BoardFileDtoReqMapStruct boardFileDtoReqMapStruct;


	// 핸들러 메소드 (웹 프레임워크에서 요청을 처리하는 메서드) 는 HTTP GET 요청에만 응답
	//ModelAndView 객체를 반환할 때는 ModelMap 을 이용하여 키 (뷰의 이름) 값 (데이터)
	// --> 그리고 컨트롤러가 생성한 모든 모델 데이터와 뷰 정보를 함께 반환할 수 있습니다.
	@RequestMapping(value="/jpa/board", method=RequestMethod.GET)
	public ModelAndView openBoardList(ModelMap model) throws Exception{
		ModelAndView mv = new ModelAndView("/board/jpaBoardList");

		List<BoardEntity> list = jpaBoardService.selectBoardList();
		mv.addObject("list", list);

		return mv;
	}

	@RequestMapping(value="/jpa/board/write", method=RequestMethod.GET)
	public String openBoardWrite() throws Exception{
		return "/board/jpaBoardWrite";
	}


	@RequestMapping(value="/jpa/board/write", method={RequestMethod.POST})
	public String writeBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		List<BoardFileEntity> list = fileUtils.parseFileInfo(multipartHttpServletRequest);
		System.out.println("list : " + list);
		jpaBoardService.saveBoard(board, list);
		return "redirect:/jpa/board";
	}


	@RequestMapping(value="/jpa/board/{boardIdx}", method=RequestMethod.GET)
	public ModelAndView openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception{
			// URL 에서 경로 변수인 boardIdx 를 추출하고 jpaBoardService 를 통해 해당 boardIdx 에 해당하는 게시글의 상세 정보를 가져온다
		ModelAndView mv = new ModelAndView("/board/jpaBoardDetail");
			// 가져온 정보를 ModelAndView 에 추가하고 해당 뷰로 반환한다

		BoardEntity board = jpaBoardService.selectBoardDetail(boardIdx);
		mv.addObject("board", board);

		return mv;
	}

	/**
	 // 직렬화,역직렬화 문제 발생 문제 발생 -> 객체를 캐시에 저장하고 다시 가져올 때 발생할 수 있음
	 		객체는 직렬화되어 저장되고 필요할 때 역직렬화 되어 다시 메모리에 로드된다

	 	❓직렬화 문제 : 객체가 직렬화되어 캐시에 저장될 때 해당 객체의필드와 메서드만이 저장된다
	 		객체의 메서드 중 직렬화 할 수 없는 메서드가 있다면 해당 메서드는 저장되지 않을 수 있다
	 		-> 캐시로부터 객체를 가져올 때 필요한 정보가 누락될 수 있다
	 	❓ 역직렬화 문제 : 객체가 캐시로부터 역직렬화될 때 클래스의 변경 또는 클래스 버전의 충돌로 인해 역직렬화 과정에서 예외가 발생할 수 있다
	 		객체를 저장할 때 사용된 클래스 버전과 객체를 가져올 때 사용되는 클래스 버전이 다를 경우 역직렬화 과정에서 호환성 문제가 발생할 수 있다

	 @RequestMapping(value="/jpa/board/{boardIdx}", method=RequestMethod.GET)
	 //  @Cacheable(key = "#boardIdx", value ="BOARD")
	 	// 캐시를 사용하면 이에 저장된 결과를 사용하여 성능을 향상시킨다
	 public ModelAndView openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception{
	 ModelAndView mv = new ModelAndView("/board/jpaBoardDetail");

	 BoardEntity board = jpaBoardService.selectBoardDetail(boardIdx);
	 	// selectBoardDetail 이 BoardResDto 타입 --> 메서드를 ModelAndView 가 아니라 다른 타입으로 바꿔야 할 것 같음
	 mv.addObject("board", board);

	 return mv;
	 //     return jpaBoardService.selectBoardDetail(boardIdx);
	 }

	 **/


	/**
	 * MapStruct 추가하고 수정 시 기존 게시판의 파일 Null로 바뀜
	 */
//	@RequestMapping(value="/jpa/board/edit/{boardIdx}", method=RequestMethod.PUT)
//	@CachePut(key = "#boardIdx", value ="BOARD")
//	public String updateBoard(@RequestParam String title, @RequestParam String contents, @PathVariable("boardIdx") int boardIdx, @RequestParam("files") MultipartFile file) throws Exception{
//		List<BoardFileEntity> list = fileUtils.parseFileInfo(file);
//		BoardEntity board = jpaBoardService.selectBoardDetail(boardIdx);
//
//		jpaBoardService.updateBoard(board, list, contents, title);
//
//		return "redirect:/jpa/board";
//	}

	@RequestMapping(value="/jpa/board/edit/{boardIdx}", method=RequestMethod.POST)
	public String updateBoard(@PathVariable("boardIdx") int boardIdx,
							  @RequestParam String title,
							  @RequestParam String contents,
							  MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		List<BoardFileEntity> list = fileUtils.parseFileInfo(multipartHttpServletRequest);
		BoardEntity board = jpaBoardService.selectBoardDetail(boardIdx);

		jpaBoardService.updateBoard(board, list, contents, title);

		return "redirect:/jpa/board";
	}
/**
* @PathVariable : URL 경로에서 변수 값을 추출하는 데 사용된다
*  => 처음에 위의 방법을 이해를 못해서 제대로 적용을 못함. URL에 파라미터로 넘어오는 값이 PUT으로 넘어온다
*  => EX) localhost:8088/jpa/board/edit/{대충 이런식으로}
*
* @RequestParam :  HTTP 요청의 쿼리 매개변수(query parameter)를 처리하는 데 사용된다
*  =>  EX) localhost:8088/jpa/board/edit/{대충 이런식으로}?title=땡땡땡&contents=땅땅땅
*
 */

//	@RequestMapping(value="/jpa/board/delete/{boardIdx}", method=RequestMethod.POST)
//	public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
//		List<BoardFileEntity> fileList = jpaBoardService.selectBoardFileList(boardIdx);
//		List<String> filePathList = new ArrayList<>();
//		for(BoardFileEntity boardFile : fileList){
//			String filePath = boardFile.getStoredFilePath();
//			String storedFilePath = Paths.get("").toAbsolutePath() +"\\"+ filePath;
//			filePathList.add(storedFilePath);
//		}
//		fileUtils.removeFile(filePathList);
//		jpaBoardService.deleteBoard(boardIdx);
//		return "redirect:/jpa/board";
//	}


@RequestMapping(value="/jpa/board/delete/{boardIdx}", method=RequestMethod.POST)
public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
//		jpaBoardService.deleteBoardFile(boardIdx);
	jpaBoardService.deleteBoard(boardIdx);
	return "redirect:/jpa/board";
}

	@RequestMapping(value="/jpa/board/deleteFiles", method=RequestMethod.POST)
	public String deleteFiles(int idx, int boardIdx) throws Exception{
		jpaBoardService.deleteBoardFile(boardIdx, idx);
		return "redirect:/jpa/board/"+boardIdx;
	}

	@RequestMapping(value="/jpa/board/file", method=RequestMethod.GET)
	public void downloadBoardFile(int boardIdx, int idx, HttpServletResponse response) throws Exception{
		BoardFileEntity file = jpaBoardService.selectBoardFileInformation(boardIdx, idx);

		byte[] files = FileUtils.readFileToByteArray(new File(file.getStoredFilePath()));

		response.setContentType("application/octet-stream");
		response.setContentLength(files.length);
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(file.getOriginalFileName(),"UTF-8")+"\";");
		response.setHeader("Content-Transfer-Encoding", "binary");

		response.getOutputStream().write(files);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	
	@RequestMapping(value="/jpa/board/file", method = RequestMethod.POST)
	public String uploadBoardFile(Integer boardIdx, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		BoardEntity board = jpaBoardService.selectBoardDetail(boardIdx);
		List<BoardFileEntity> existingfilelist = jpaBoardService.selectBoardFileList(boardIdx);
		List<BoardFileEntity> uploadfilelist = fileUtils.parseFileInfo(multipartHttpServletRequest);
		List<BoardFileEntity> boardFileList = new ArrayList<>();
		boardFileList.addAll(existingfilelist);
		boardFileList.addAll(uploadfilelist);
		board.setFileList(boardFileList);
		jpaBoardService.saveBoard(board, boardFileList);

		return "redirect:/jpa/board/";
	}

	@RequestMapping(value="/jpa/board/search", method=RequestMethod.GET)
	public String searchBoard(@RequestParam String searchKeyword, Model model) throws Exception{

		List<BoardEntity> list = jpaBoardService.searchingBoardByTitle(searchKeyword);

		model.addAttribute("list", list);

		return "/board/jpaBoardList";
	}
}
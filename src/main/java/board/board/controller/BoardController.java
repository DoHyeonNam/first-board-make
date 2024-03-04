package board.board.controller;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.service.BoardService;
import board.common.FileUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;

	@Autowired
	private FileUtils fileUtils;

	@RequestMapping("/board/openBoardList.do")
	public ModelAndView openBoardList() throws Exception{
		ModelAndView mv = new ModelAndView("/board/boardList");

		List<BoardDto> list = boardService.selectBoardList();
		mv.addObject("list", list);
		if (log.isDebugEnabled()){
			log.debug(" openBoardList() 호출");
		}
		if (log.isInfoEnabled()){
			log.info(" 인포에서 openBoardList() 호출");
		}
		return mv;
		// 동적템플릿엔진(JSP 등등)을 사용해서 화면을 전달하기에 스웨거 사용이 안된다.
	}

	@RequestMapping("/board/openBoardWrite.do")
	public String openBoardWrite() throws Exception{
		return "/board/boardWrite";
	}

//	@RequestMapping("/board/insertBoard.do")
//	public String insertBoard(BoardDto board, @RequestParam("file") MultipartFile file) throws Exception{
//		List<BoardFileDto> list = fileUtils.parseFileInfo(file);
//		boardService.insertBoard(board, list);
//		return "redirect:/board/openBoardList.do";
//	}

	@RequestMapping("/board/openBoardDetail.do")
	public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception{
		ModelAndView mv = new ModelAndView("/board/boardDetail");

		BoardDto board = boardService.selectBoardDetail(boardIdx);
		mv.addObject("board", board);

		return mv;
	}

	@RequestMapping("/board/updateBoard.do")
	public String updateBoard(BoardDto board) throws Exception{
		boardService.updateBoard(board);
		return "redirect:/board/openBoardList.do";
	}

	@RequestMapping("/board/deleteBoard.do")
	public String deleteBoard(int boardIdx) throws Exception{
		boardService.deleteBoard(boardIdx);
		return "redirect:/board/openBoardList.do";
	}

	@RequestMapping("/board/downloadBoardFile.do")
	public void downloadBoardFile(@RequestParam int idx, @RequestParam int boardIdx, HttpServletResponse response) throws Exception{
		BoardFileDto boardFile = boardService.selectBoardFileInformation(idx, boardIdx);
		if(ObjectUtils.isEmpty(boardFile) == false) {
			String fileName = boardFile.getOriginalFileName();

			byte[] files = org.apache.commons.io.FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));

			response.setContentType("application/octet-stream");
			response.setContentLength(files.length);
			response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8")+"\";");
			response.setHeader("Content-Transfer-Encoding", "binary");

			response.getOutputStream().write(files);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
}

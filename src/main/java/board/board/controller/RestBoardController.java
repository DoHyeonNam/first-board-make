package board.board.controller;

import board.board.entity.BoardFileEntity;
import board.board.service.RestBoardService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import java.io.File;

import jakarta.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class RestBoardController {

	@Autowired
	private RestBoardService restBoardService;

	@Autowired
	private board.common.FileUtils fileUtils;

	@RequestMapping(value="/board", method=RequestMethod.GET)
	public ModelAndView openBoardList() throws Exception{
		ModelAndView mv = new ModelAndView("/board/restBoardList");

		List<BoardDto> list = restBoardService.selectBoardList();
		mv.addObject("list", list);

		return mv;
	}

	@RequestMapping(value="/board/write", method=RequestMethod.GET)
	public String openBoardWrite() throws Exception{
		return "/board/restBoardWrite";
	}

//	@RequestMapping(value="/board/write", method=RequestMethod.POST)
//	public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
//		List<BoardFileEntity> list = fileUtils.parseFileInfo(multipartHttpServletRequest);
//		restBoardService.insertBoard2(board, list);
//
//		return "redirect:/board";
//	}

	@RequestMapping(value="/board/{boardIdx}", method=RequestMethod.GET)
	public ModelAndView openBoardDetail(@PathVariable("boardIdx") int boardIdx, ModelMap model) throws Exception{
		ModelAndView mv = new ModelAndView("/board/restBoardDetail");

		BoardDto board = restBoardService.selectBoardDetail(boardIdx);
		mv.addObject("board", board);

		return mv;
	}

	@RequestMapping(value="/board/{boardIdx}", method=RequestMethod.PUT)
	public String updateBoard(BoardDto board) throws Exception{
		restBoardService.updateBoard(board);
		return "redirect:/board";
	}

	@RequestMapping(value="/board/{boardIdx}", method=RequestMethod.DELETE)
	public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
		restBoardService.deleteBoard(boardIdx);
		return "redirect:/board";
	}

	@RequestMapping(value="/board/file", method=RequestMethod.GET)
	public void downloadBoardFile(@RequestParam int idx, @RequestParam int boardIdx, HttpServletResponse response) throws Exception{
		BoardFileDto boardFile = restBoardService.selectBoardFileInformation(idx, boardIdx);
		if(ObjectUtils.isEmpty(boardFile) == false) {
			String fileName = boardFile.getOriginalFileName();

			byte[] files = FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));

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

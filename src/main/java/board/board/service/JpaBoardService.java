package board.board.service;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface JpaBoardService {

	List<BoardEntity> selectBoardList() throws Exception;

	void saveBoard(BoardEntity board, List<BoardFileEntity> list) throws Exception;

//	void saveBoardWithFile1(BoardEntity board, MultipartFile file) throws Exception;
	void saveBoardWithFile2(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;


	BoardEntity selectBoardDetail(int boardIdx) throws Exception;

	void deleteBoard(int boardIdx);

	BoardFileEntity selectBoardFileInformation(int boardIdx, int idx) throws Exception;

	List<BoardEntity> searchingBoardByTitle(String title) throws Exception;

	List<BoardFileEntity> selectBoardFileList(int boardIdx);

//	void deleteBoardFile(int fileId);
	List<BoardFileEntity> selectBoardFile(int fileId);

	void updateBoard(BoardEntity board, List<BoardFileEntity> list, String contents, String title) throws Exception;

	void deleteBoardFile(int boardIdx, int idx); //

}

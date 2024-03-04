package board.board.service;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.entity.BoardFileEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RestBoardService {
	
	List<BoardDto> selectBoardList() throws Exception;
	
	void insertBoard(BoardDto board, List<BoardFileDto> list) throws Exception;

	void insertBoard2(BoardDto board, List<BoardFileEntity> list) throws Exception;

	BoardDto selectBoardDetail(int boardIdx) throws Exception;

	void updateBoard(BoardDto board) throws Exception;

	void deleteBoard(int boardIdx) throws Exception;

	BoardFileDto selectBoardFileInformation(int idx, int boardIdx) throws Exception; 
}

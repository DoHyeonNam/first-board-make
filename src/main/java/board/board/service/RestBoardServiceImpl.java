package board.board.service;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.entity.BoardFileEntity;
import board.board.mapper.BoardMapper;
import board.common.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class RestBoardServiceImpl implements RestBoardService{
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BoardMapper boardMapper;

	@Autowired
	private FileUtils fileUtils;
	
	@Override
	public List<BoardDto> selectBoardList() throws Exception {
		return boardMapper.selectBoardList();
	}
	
	@Override
	public void insertBoard(BoardDto board, List<BoardFileDto> list) throws Exception {


			boardMapper.insertBoard(board);

			int boardIdx = board.getBoardIdx();

			list.stream().forEach(BoardFileDto -> BoardFileDto.setBoardIdx(boardIdx));

			log.debug("보드파일 객체 상태는 : " + list);

			if (CollectionUtils.isEmpty(list) == false) {
				boardMapper.insertBoardFileList(list);
			}




	}

	@Override
	public void insertBoard2(BoardDto board, List<BoardFileEntity> list) throws Exception {

	}

	@Override
	public BoardDto selectBoardDetail(int boardIdx) throws Exception{
		BoardDto board = boardMapper.selectBoardDetail(boardIdx);
		List<BoardFileDto> fileList = boardMapper.selectBoardFileList(boardIdx);
		board.setFileList(fileList);
		
		boardMapper.updateHitCount(boardIdx);
		
		return board;
	}
	
	@Override
	public void updateBoard(BoardDto board) throws Exception {
		boardMapper.updateBoard(board);
	}

	@Override
	public void deleteBoard(int boardIdx) throws Exception {
		boardMapper.deleteBoard(boardIdx);
	}
	
	@Override
	public BoardFileDto selectBoardFileInformation(int idx, int boardIdx) throws Exception {
		return boardMapper.selectBoardFileInformation(idx, boardIdx);
	}
}	


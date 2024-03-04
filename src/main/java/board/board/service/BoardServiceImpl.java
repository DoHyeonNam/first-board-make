package board.board.service;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.entity.BoardFileEntity;
import board.board.mapper.BoardMapper;
import board.common.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

// import org.springframework.dao.DataAccessException; : 추상클래스

@Service
public class BoardServiceImpl implements BoardService{
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BoardMapper boardMapper;

	@Autowired
	private FileUtils fileUtils;

	@Override
	public List<BoardDto> selectBoardList() throws Exception {
		// 🚩SMTP 확인용 로직
//		try {
//			List<BoardDto> boardDtoList = boardMapper.selectBoardList();
//			throw  new DataAccessResourceFailureException("연결에러");
//		}catch (DataAccessException e){
//			log.error("데이터베이스 에러 발생 즉시 복구바랍니다");
//			// 강제로 에러 만들어서 SMTP 확인하기
//		}
//		return null;

		// 🚩정상로직
		List<BoardDto> boardlist = null;
		try {
			boardlist = boardMapper.selectBoardList();
			return boardlist;
		}catch (BadSqlGrammarException e){
			log.error(e.getMessage());
			throw e;
		}
	}
	
	@Override
	public void insertBoard(BoardDto board, List<BoardFileDto> list) throws Exception {


			boardMapper.insertBoard(board);
				// sql-board.xml 의 insertBoard 로 게시글 정보 (BoardDto) 를 데이터베이스에 저장한다 (제목 내용 등)
			int boardIdx = board.getBoardIdx();

			list.stream().forEach(BoardFileDto -> BoardFileDto.setBoardIdx(boardIdx));
				// 첨부된 각 파일 (BoardFileDto) 의 게시판번호 (boardIdx) 속성을 새로 생성된 게시글의 인덱스로 설정한다
			log.debug("보드파일 객체 상태는 : " + list);
			// 첨부된 파일 정보가 제대로 설정되었는지 로그로 확인

			if (CollectionUtils.isEmpty(list) == false) {
				boardMapper.insertBoardFileList(list);
				// 파일 정보가 비어있지 않다면 첨부된 파일 정보를 데이터베이스에 저장한다.
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


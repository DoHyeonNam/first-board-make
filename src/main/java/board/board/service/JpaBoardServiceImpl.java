package board.board.service;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;
import board.board.repository.JpaBoardRepository;
import board.common.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JpaBoardServiceImpl implements JpaBoardService{

	@Autowired
	JpaBoardRepository jpaBoardRepository;

	@Autowired
	private FileUtils fileUtils;


	@Override
	public List<BoardEntity> selectBoardList() throws Exception {
		List<BoardEntity> boardEntities = jpaBoardRepository.findAll();
			// 모든 게시판 엔티티를 저장하기 위한 리스트 = boardEntities
		return boardEntities;
	}

	@Override
	public void saveBoard(BoardEntity board, List<BoardFileEntity> list) throws Exception {
		int boardIdx = jpaBoardRepository.getBoardCount()+1;

		if (!list.isEmpty()) {
			board.setFileList(list);
		}
		System.out.println("board:" + board);
		board.setCreatorId("admin");
		board.setBoardIdx(boardIdx);
		jpaBoardRepository.save(board);

	}

	@Override
	public void saveBoardWithFile2(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		List<BoardFileEntity> fileList = fileUtils.parseFileInfo(multipartHttpServletRequest);
		board.setFileList(fileList);
		System.out.println("board : " + board);
		jpaBoardRepository.save(board);
	}

//	@Override
//	public void saveBoardWithFile1(BoardEntity board, MultipartFile file) throws Exception {
//		List<BoardFileEntity> fileList = fileUtils.parseFileInfo(file);
//		board.setFileList(fileList);
//		System.out.println("board : " + board);
//		jpaBoardRepository.save(board);
//	}

	@Override
	public BoardEntity selectBoardDetail(int boardIdx) throws Exception{

		BoardEntity board = jpaBoardRepository.findBoardById(boardIdx);

		board.setHitCnt(board.getHitCnt() + 1);
		jpaBoardRepository.save(board);
//			jpaBoardRepository.flush(); 도 가능
		return board;
	}

	@Override
	public void deleteBoard(int boardIdx) {
		//파일을 삭제하고싶으면 사용, 저장해놓고싶으면 주석처리
		List<BoardFileEntity> fileList = jpaBoardRepository.findAllBoardFile(boardIdx);
		for (BoardFileEntity file : fileList) {
			String filePath = Paths.get(file.getStoredFilePath()).toAbsolutePath().toString();
			File deleteFile = new File(filePath);
			if (deleteFile.exists()) {
				if (deleteFile.delete()) {
					System.out.println("파일 삭제 성공: " + file.getStoredFilePath());
				} else {
					System.out.println("파일 삭제 실패: " + file.getStoredFilePath());
				}
			} else {
				System.out.println("삭제하려는 파일이 존재하지 않습니다: " + file.getStoredFilePath());
			}
		}
		jpaBoardRepository.deleteById(boardIdx);
	}

	@Override
	public BoardFileEntity selectBoardFileInformation(int boardIdx, int idx) throws Exception {
		BoardFileEntity boardFile = jpaBoardRepository.findBoardFile(boardIdx, idx);
		return boardFile;
	}

	@Override
	public List<BoardEntity> searchingBoardByTitle(String title) throws Exception {
		List<BoardEntity> result = jpaBoardRepository.findAllByTitleContainingOrderByCreatedDatetime(title);
		return result;
	}

	@Override
	public List<BoardFileEntity> selectBoardFileList(int boardIdx) {
		List<BoardFileEntity> fileEntityList = jpaBoardRepository.findAllByBoardFile(boardIdx);
		return fileEntityList;
	}


	@Override
	public void deleteBoardFile(int boardIdx, int idx){
		BoardFileEntity file = jpaBoardRepository.findSelectedBoardFile(boardIdx, idx);
		System.out.println(file.getStoredFilePath());
		String filePath = Paths.get(file.getStoredFilePath()).toAbsolutePath().toString();
		System.out.println("파일 절대경로: " + filePath );
		File deleteFile = new File(filePath);
		if (deleteFile.exists()) {
			if (deleteFile.delete()) {
				System.out.println("파일 삭제 성공: " + file.getStoredFilePath());
			} else {
				System.out.println("파일 삭제 실패: " + file.getStoredFilePath());
			}
		} else {
			System.out.println("삭제하려는 파일이 존재하지 않습니다: " + file.getStoredFilePath());
		}
		jpaBoardRepository.deleteBoardEntityByBoardIdx(boardIdx, idx);
	}
	@Override
	public List<BoardFileEntity> selectBoardFile(int fileId) {
		List<BoardFileEntity> file = jpaBoardRepository.selectBoardFile(fileId);
		return file;
	}

	@Override
	public void updateBoard(BoardEntity board, List<BoardFileEntity> newList, String contents, String title) throws Exception {
		List<BoardFileEntity> existingList = board.getFileList();

		// 파일이 추가 또는 변경된 경우에만 업데이트
		if (!newList.isEmpty() && !newList.equals(existingList)) {
			List<BoardFileEntity> nonDuplicateFiles = newList.stream()
					.filter(newFile -> !containsFileWithOriginalFileName(existingList, newFile.getOriginalFileName()))
					.collect(Collectors.toList());
			// stream() : 데이터 소스를 처리할 수 있게 해줌 데이터 추출, 변환, 필터링, 집계 등의 작업
			// containsFileWithOriginalFileName() : 기존 리스트인 existingList에 동일한 파일 이름을 가진 파일이 있는지 확인

			// --> 기존 파일 목록에 중복되지 않는 파일들을 추가
			existingList.addAll(nonDuplicateFiles);
		}

		LocalDateTime currentDateTime = LocalDateTime.now();
		board.setContents(contents);
		board.setTitle(title);
		board.setUpdaterId("admin");
		board.setUpdatedDatetime(currentDateTime);
		jpaBoardRepository.save(board);
	}

	private boolean containsFileWithOriginalFileName(List<BoardFileEntity> fileList, String originalFileName) {
		// fileList에서 originalFileName과 동일한 파일이 존재하는지 확인
		return fileList.stream()
				.anyMatch(file -> file.getOriginalFileName().equals(originalFileName));
	}
}

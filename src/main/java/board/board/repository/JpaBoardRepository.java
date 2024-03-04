package board.board.repository;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaBoardRepository extends JpaRepository<BoardEntity, Integer>{

	@Query("select board from BoardEntity board left outer join fetch board.fileList where board.boardIdx = :boardIdx")
	BoardEntity findBoardById(@Param("boardIdx") int boardIdx);

	@Query("SELECT file FROM BoardFileEntity file WHERE file.boardEntity.boardIdx = :boardIdx AND file.idx = :idx")
	BoardFileEntity findBoardFile(@Param("boardIdx") int boardIdx, @Param("idx") int idx);

	@Query("SELECT file FROM BoardFileEntity file WHERE file.boardEntity.boardIdx = :boardIdx AND file.idx = :idx")
	BoardFileEntity findSelectedBoardFile(@Param("boardIdx") int boardIdx, @Param("idx") int idx);

	@Query("SELECT file FROM BoardFileEntity file WHERE file.idx = :fileId")
	List<BoardFileEntity> selectBoardFile(@Param("fileId") int fileId);

	@Query("SELECT file FROM BoardFileEntity file WHERE file.boardEntity.boardIdx = :boardIdx")
	List<BoardFileEntity> findAllByBoardFile(@Param("boardIdx") int boardIdx);

	@Query("select board from BoardEntity board left join fetch board.fileList where board.title like %:title%")
	List<BoardEntity> findAllByTitleContainingOrderByCreatedDatetime(String title);

	@Query("select COALESCE(MAX(board.boardIdx), 0) from BoardEntity board")
	int getBoardCount();

	@Query("SELECT file FROM BoardFileEntity file WHERE file.boardEntity.boardIdx = :boardIdx")
	List<BoardFileEntity> findAllBoardFile(@Param("boardIdx") int boardIdx);

	@Modifying
	@Transactional
	@Query("DELETE FROM BoardFileEntity file WHERE file.boardEntity.boardIdx = :boardIdx AND file.idx = :idx")
	void deleteBoardEntityByBoardIdx(int boardIdx, int idx);
}


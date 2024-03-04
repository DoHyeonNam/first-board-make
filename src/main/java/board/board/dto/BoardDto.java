package board.board.dto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Tag(name = "BoardDto : 게시글 내용", description = "게시글 내용")
@Data
@RedisHash(value = "BOARD")
public class BoardDto implements Serializable{
	
	private int boardIdx;
	
	private String title;
	
	private String contents;
	
	private int hitCnt;
	
	private String creatorId;
	
	private String createdDatetime;
	
	private String updaterId;
	
	private String updatedDatetime;
	
	private List<BoardFileDto> fileList;
}

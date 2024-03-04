package board.board.entity;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="t_jpa_board") //  JPA 에서 엔티티 클래스와 데이터베이스 테이블 간의 매핑을 정의
@NoArgsConstructor
@Getter
@Setter
public class BoardEntity {
	@Id
	private int boardIdx;

	@Column(nullable=false)
	private String title;

	@Column(nullable=false)
	private String contents;

	@Column(nullable=false)
	private int hitCnt = 0;

	@Column(nullable=false)
	private String creatorId;

	@Column(nullable=false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdDatetime = LocalDateTime.now();

	private String updaterId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime updatedDatetime;

	// cascade = CascadeType.ALL - 부모 엔티티의 상태가 변경되면 해당 변경이 자식 엔티티에도 적용되어야 함.
	// fetch=FetchType.LAZY - EAGER : 즉시로딩 방식, LAZY : 연관된 엔티티들이 필요한 경우에만 로딩되므로 메모리 최적화
	// --> 게시판에 파일을 등록할 수도 있고 등록하지 않을 수도 있기 때문에 LAZY 로 등록하고, 글을 작성할 때 파일을 첨부한 경우에만 로딩된다
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="board_idx")
	private List<BoardFileEntity> fileList;
}

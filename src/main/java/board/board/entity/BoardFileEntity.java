package board.board.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="t_jpa_file")
@NoArgsConstructor
@Getter
@Setter
public class BoardFileEntity {

	// GeneratedValue : PK 값을 자동으로 생성하는 방법 중 하나, 열이 자동 증가(auto-increment)하는 열일 때 사용한다
	// IDENTITY :  자동 증가(auto-increment) 기능을 사용
	// AUTO : JPA가 데이터베이스에 적합한 기본 키 생성 전략을 선택(IDENTITY, SEQUENCE, TABLE 등등)한다
	// AUTO의 장단점 : 데이터베이스에 따라 다르게 동작할 수 있기 때문에 이식성(portability)에 유리, 특정 데이터베이스에서 원하는 전략을 명시적으로 지정하지 못할 수도 있다
	// 위의 AUTO의 단점때문에 파일이 첨부가 안된거, 자동으로 테이블과 시퀀스를 만들려고 해서 만들어둔 테이블로 가질 않았다.
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idx;

	@Column(nullable=false)
	private String originalFileName;
	
	@Column(nullable=false)
	private String storedFilePath;
	
	@Column(nullable=false)
	private long fileSize;

	@Column(nullable=false)
	private String creatorId;
	
	@Column(nullable=false)
	private LocalDateTime createdDatetime = LocalDateTime.now();
	
	private String updaterId;
	
	private LocalDateTime updatedDatetime;

	@ManyToOne( fetch= FetchType.LAZY)
	@JoinColumn(name = "board_idx")
	private BoardEntity boardEntity;
}

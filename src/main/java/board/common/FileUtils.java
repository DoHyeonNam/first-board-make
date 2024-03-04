package board.common;


import board.board.dto.BoardFileReqDto;
import board.board.entity.BoardFileEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


@Component
public class FileUtils {

	public void removeFile(List<String> fileList) {
		for (String storedFilePath : fileList) {
			File files = new File(storedFilePath);
			files.delete();
		}
	}


	public List<BoardFileEntity> parseFileInfo(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {

		if (ObjectUtils.isEmpty(multipartHttpServletRequest)) {
			return null;
		}

		List<BoardFileEntity> fileList = new ArrayList<>();
		
		// 폴더를 만들때 해당 날짜가 없으면 만들고 있으면 바로 넣는다
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd"); //  날짜 포맷 정의
		ZonedDateTime current = ZonedDateTime.now(); // 현재 시간을 가져오는 부분
		String imagesPath = "images"; // 이미지 폴더 상위 경로
		String datePath = current.format(format); // 날짜에 해당하는 하위 경로

		// 이미지 폴더의 절대 경로
		// 상대경로는 default값이 WEB-INF
		// 이전에는 상대경로로 인해 WEB-INF(톰캣 기본경로)로 강제로 임시저장으로 갔지만 지금은 절대경로로 만들 곳을 정확하게 지정해줌
		String absoluteImagesPath = Paths.get(imagesPath).toAbsolutePath().toString();
		// 날짜에 해당하는 하위 경로의 절대 경로
		String absoluteDatePath = Paths.get(absoluteImagesPath, datePath).toString();
		// Path.get(absoluteImagePath, datePath) : 두 경로를 결합하여 새로운 Path 객체를 생성
		// 현재 프로젝트 구조가 프로젝트 밑에 폴더가 하나더있어서 프로젝트 바로 하위폴더에 생성되는 걸 확인할 수 있다.

		// absoluteDatePath : 방금 위에서 만든 절대경로를 새로만들 File에 파라메터값으로 할당한다
		File dateDirectory = new File(absoluteDatePath);
		if (!dateDirectory.exists()) {
			dateDirectory.mkdirs(); // 날짜에 해당하는 하위 경로의 폴더 생성
		}
		
		//  Iterator는 String 객체를 반환하며, 이들 String은 클라이언트가 웹 양식에서 업로드한 각 파일에 대한 이름을 나타낸다
		// MultipartHttpServletRequest에서 받아온 파일 이름을 추출한다
		// 각 파일을 식별할 수 있는 문자열이 포함된다. input태그 필드의 이름(name="?")을 말한다
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

		while (iterator.hasNext()) {
			List<MultipartFile> list = multipartHttpServletRequest.getFiles(iterator.next());
			for (MultipartFile multipartFile : list) {
				if (!multipartFile.isEmpty()) {
					String originalFilename = multipartFile.getOriginalFilename();
					String contentType = multipartFile.getContentType();

					// 파일 확장자 추출
					String fileExtension = StringUtils.getFilenameExtension(originalFilename);

					// 저장될 파일명 생성 (UUID를 사용하여 유니크한 파일명 생성)
					String newFileName = UUID.randomUUID().toString() + "." + fileExtension;

					// 파일 저장 경로 설정
					String storedFilePath = Paths.get(absoluteDatePath, newFileName).toString();

					// 파일 저장
					File storedFile = new File(storedFilePath);
					// transferTo : 자바 2버전은 file, 자바 3 버전에서는 dest라고 되어있음
					// 업로드된 파일을 서버의 파일 시스템에 저장하는 데 사용
					multipartFile.transferTo(storedFile);

					// 파일 정보를 BoardFileEntity에 추가
					BoardFileEntity boardFile = new BoardFileEntity();
					// 업로드된 파일의 크기
					boardFile.setFileSize(multipartFile.getSize());
					// 업로드된 파일의 원본 파일 이름을 설정
					boardFile.setOriginalFileName(originalFilename);
					// 업로드된 파일의 저장된 파일 경로
					boardFile.setStoredFilePath(storedFilePath);
					// 파일을 업로드한 사용자의 ID를 설정
					boardFile.setCreatorId("admin");
					fileList.add(boardFile);
				}
			}
		}
		return fileList;
	}
}

package board.board.mapstruct;

import board.board.dto.BoardReqDto;
import board.board.entity.BoardEntity;
import board.common.mapper.EntityReqMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardReqMapStruct extends EntityReqMapper<BoardEntity, BoardReqDto>
{
    BoardReqMapStruct MAPPER = Mappers.getMapper(BoardReqMapStruct.class);
}

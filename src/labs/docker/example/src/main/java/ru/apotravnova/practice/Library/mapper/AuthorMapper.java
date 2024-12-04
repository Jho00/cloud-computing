package ru.apotravnova.practice.Library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.apotravnova.practice.Library.dto.AuthorDTO;
import ru.apotravnova.practice.Library.model.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    //есть INSTANCE типа AuthorMapper . Это будет «точка входа» в экземпляр после того, как сгенерируем реализацию AuthorMapperImpl.
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    Author toModel(AuthorDTO dto);
    AuthorDTO toDto(Author model);
}

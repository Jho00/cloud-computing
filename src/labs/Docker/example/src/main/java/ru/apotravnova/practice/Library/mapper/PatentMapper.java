package ru.apotravnova.practice.Library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.apotravnova.practice.Library.dto.PatentDTO;
import ru.apotravnova.practice.Library.model.Patent;

//disableSubMappingMethodsGeneration = true - в папке target/generated-sources автоматически НЕ создается PatentMapperImpl
//если =false - то будет автоматическая реализация PatentMapperImpl относительно source и target в @Mapping
@Mapper(disableSubMappingMethodsGeneration = true)
public interface PatentMapper {
    PatentMapper INSTANCE = Mappers.getMapper(PatentMapper.class);

    //source во что, target откуда
    @Mapping(source = "dto.idAuthor", target = "author.id")
    Patent toModel(PatentDTO dto);

    @Mapping(source = "model.author.id", target = "idAuthor")
    PatentDTO toDto(Patent model);
}

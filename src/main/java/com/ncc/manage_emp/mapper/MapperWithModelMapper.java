package com.ncc.manage_emp.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapperWithModelMapper {
    private ModelMapper modelMapper;

    @Autowired
    public MapperWithModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    public <S, T> T mapObject(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }
}

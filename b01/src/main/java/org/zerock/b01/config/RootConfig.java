package org.zerock.b01.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig { //쓰는 이유 : Model Mapper 때문에 -> {원래는 (DTO <-> VO) 인데 }데이터 주고 받을 때 메모리와 데이터 주고 받을 때

    @Bean                            //
    public ModelMapper getMapper() { //model.addAttribute => 메모리에 저장한 걸 모니터에 출력
        //객체 생성
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        return modelMapper;
    }
}

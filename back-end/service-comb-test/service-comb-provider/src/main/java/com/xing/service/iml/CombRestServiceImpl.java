package com.xing.service.iml;


import com.xing.service.CombRestService;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CombRestServiceImpl
 *
 * @author guoxing
 * @date 10/28/2019 2:58 PM
 * @since 2.0.0
 **/
@RestSchema(schemaId = "CombRestServiceImpl") // 服务契约名称 ,如果服务契约名称修改,需要添加service_description.environment=development,否则服务无法启动
@RequestMapping("/")
public class CombRestServiceImpl implements CombRestService {

    /**
     * 首次调用rest
     *
     * @param name
     * @return
     */
    @Override
    @GetMapping("/say")
    public String sayRest(String name) {
        return "hello " + name;
    }
}

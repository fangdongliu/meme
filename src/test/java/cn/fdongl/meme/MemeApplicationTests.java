package cn.fdongl.meme;

import cn.fdongl.meme.pair.controller.PairController;
import cn.fdongl.meme.pair.mapper.PairMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemeApplicationTests {

    @Autowired
    PairMapper pairMapper;

    @Test
    public void contextLoads() {

        Integer i = pairMapper.accept(9,6);

        System.out.println(i);

    }

}


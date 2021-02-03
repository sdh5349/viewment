package com.web.curation.recommend;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FPGrowthTest {


    @Test
    void test() throws Exception{
        //given
        List<List<Long>> dataset = new ArrayList<>();
        dataset.add(Arrays.asList(1L, 2L, 3L, 4L));
        dataset.add(Arrays.asList(1L, 2L, 3L, 5L));
        dataset.add(Arrays.asList(2L, 5L, 6L));
        dataset.add(Arrays.asList(1L, 2L, 4L));
        dataset.add(Arrays.asList(1L, 3L, 6L, 7L));
        //when

        FPGrowth fpGrowth = new FPGrowth(3);
        fpGrowth.setGlobalTree(dataset);
        fpGrowth.mine();

        //then
    }
}

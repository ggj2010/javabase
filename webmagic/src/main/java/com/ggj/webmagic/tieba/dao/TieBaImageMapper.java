package com.ggj.webmagic.tieba.dao;

import com.ggj.webmagic.tieba.bean.TieBaImage;

import java.util.List;

public interface TieBaImageMapper {
    int insertBatch(List<TieBaImage> list);
    Integer selectCountByPageId(String pageId);
}
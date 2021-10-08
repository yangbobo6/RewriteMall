package com.yangbo.seckill.seckill.service;

import com.yangbo.seckill.seckill.dao.GoodsDao;
import com.yangbo.seckill.seckill.domain.Goods;
import com.yangbo.seckill.seckill.domain.SeckillGoods;
import com.yangbo.seckill.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goods) {
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goods.getId());
        //减少商品的数量
        goodsDao.reduceStock(g);
    }
}

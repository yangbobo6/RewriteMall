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

    //查询所有商品的信息
    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    //根据商品id（goodsId）查询商品
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    //减少商品表里面的商品数量  （减少某一张表中的数量  直接update）
    public Boolean reduceStock(GoodsVo goods) {
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goods.getId());
        //减少商品的数量    ?????返回是int？？？？
        int ret = goodsDao.reduceStock(g);
        return ret>0;
    }
}

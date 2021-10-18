package com.yangbo.seckill.seckill.service;

import com.yangbo.seckill.seckill.dao.GoodsDao;
import com.yangbo.seckill.seckill.domain.Goods;
import com.yangbo.seckill.seckill.domain.OrderInfo;
import com.yangbo.seckill.seckill.domain.SeckillOrder;
import com.yangbo.seckill.seckill.domain.SeckillUser;
import com.yangbo.seckill.seckill.redis.RedisService;
import com.yangbo.seckill.seckill.redis.SeckillKey;
import com.yangbo.seckill.seckill.util.MD5Util;
import com.yangbo.seckill.seckill.util.UUIDUtil;
import com.yangbo.seckill.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
public class SeckillService {

    //通常来讲，自己的service里面只引入自己的dao，或者引入别人的service ********
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;

    @Transactional  //原子操作
    public OrderInfo seckill(SeckillUser seckillUser, GoodsVo goods) {
        //减少库存  下订单  写入秒杀订单
        //goods 是秒杀的产品
        boolean success = goodsService.reduceStock(goods);
        if (success){
            //减库存成功   生成订单
            return orderService.createOrder(seckillUser,goods);
        }else {
            //库存没减少，秒杀未成功
            setGoodsOver(goods.getId());
            return null;
        }

    }

    public long getSeckillResult(Long id, long goodsId) {
        //获取秒杀订单
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(id,goodsId);
        if(order!=null){
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){ //秒杀失败
                return -1;
            }else { //在排队中
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver,""+goodsId,true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver,""+goodsId);
    }

    public boolean checkPath(SeckillUser seckillUser, long goodsId, String path) {
        if(seckillUser==null||path==null){
            return false;
        }
        String pathOld = redisService.get(SeckillKey.getSeckillPath,""+seckillUser.getId()+"_"+goodsId,String.class);
        return path.equals(pathOld);
    }

    public String createSeckillPath(SeckillUser seckillUser, long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(SeckillKey.getSeckillPath,""+seckillUser.getId()+"_"+goodsId,str);
        return str;
    }

    //生成验证码
    public BufferedImage createVerifyCode(SeckillUser seckillUser, long goodsId) {
        int width = 80;
        int height = 32;
        //create the images
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        //set the background
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0,0,width-1,height-1);
        //draw the border
        g.setColor(Color.black);
        g.drawRect(0,0,width-1,height-1);
        //create  a random instance to generate the codes
        Random rdm = new Random();
        //make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x,y,0,0);
        }
        //generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0,100,0));
        g.setFont(new Font("Candara",Font.BOLD,24));
        g.drawString(verifyCode,8,24);
        g.dispose();
        //把验证码存到redis里面
        int rnd = calc(verifyCode);
        redisService.set(SeckillKey.getVerifyCode,seckillUser.getId()+""+goodsId,rnd);
        //输出图片
        return image;
    }

    //验证数字验证码是否一致
    public boolean checkVerifyCode(SeckillUser seckillUser, long goodsId, int verifyCode) {
        if(seckillUser==null||goodsId<=0){
            return false;
        }
        Integer codeOld = redisService.get(SeckillKey.getVerifyCode, seckillUser.getId() + "" + goodsId, Integer.class);
        if(codeOld==null||codeOld-verifyCode!=0){
            return false;
        }
        redisService.delete(SeckillKey.getVerifyCode,seckillUser.getId()+""+goodsId);
        return true;
    }

    private int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static char[] ops = new char[]{'+','-','*'};
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+num1+op1+num2+op2+num3;
        return exp;
    }

}

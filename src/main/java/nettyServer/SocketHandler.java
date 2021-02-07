package nettyServer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


import org.apache.log4j.Logger;
import org.joda.time.DateTime;


public class SocketHandler extends ChannelInboundHandlerAdapter {
    public static Logger logger=Logger.getLogger(SocketHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String data=(String)msg;
        logger.info("完整的数据是：  "+data);
        if ("#".equals(data.substring(0,1))){
            String value=data.substring(1,2);
            logger.info("MAC地址是： "+value);
            DateTime now=DateTime.now();
            String my_time=now.toString("yyyy-MM-dd HH:mm:ss");
            /**
             * 存入数据库
             */
            logger.info("入库成功-当前时间是："+my_time+"MAC："+value+"烟感："+0);
        }
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端加入链接："+ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel=ctx.channel();
        channel.closeFuture();
        logger.info("客户端加入链接："+ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel=ctx.channel();
        if (channel.isActive()){
            channel.closeFuture();
        }
    }
}

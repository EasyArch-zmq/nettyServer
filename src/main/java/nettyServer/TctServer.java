package nettyServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

public class TctServer {
    public static Logger LOGGER=Logger.getLogger(TctServer.class);
    public static void main(String[] args) {
        try {
            EventLoopGroup boss=new NioEventLoopGroup();
            EventLoopGroup work=new NioEventLoopGroup();
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(boss,work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new SocketChannelInitializer());
            ChannelFuture future=serverBootstrap.bind(8001).sync();
            LOGGER.info("服务器启动！");
            future.channel().closeFuture().sync();
            LOGGER.info("服务器关闭！");
        }catch (Exception e){
            LOGGER.error("服务器异常："+e.getMessage());
        }

    }
}

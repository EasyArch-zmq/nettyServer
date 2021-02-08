package nettyServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
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
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)     //重用地址
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)// heap buf 's better
                    .childOption(ChannelOption.SO_RCVBUF, 1048576)
                    .childOption(ChannelOption.SO_SNDBUF, 1048576)
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

package nettyServer;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
     //   socketChannel.pipeline().addLast(new HttpRequestDecoder());
        // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
        socketChannel.pipeline().addLast(new HttpResponseEncoder());
        // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
        socketChannel.pipeline().addLast(new HttpRequestDecoder());//有两次FIle操作
        socketChannel.pipeline().addLast(new HttpObjectAggregator(65535));//把上一句的两次File操作聚合在一起
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());//Chunked是一种报文，处理后返回去，报文回去查一下
        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        socketChannel.pipeline().addLast(new StringDecoder());
        socketChannel.pipeline().addLast(new SocketHandler());
    }
}

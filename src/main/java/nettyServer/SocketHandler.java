package nettyServer;



import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;



import java.io.UnsupportedEncodingException;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;



public class SocketHandler extends ChannelInboundHandlerAdapter {
    public static Logger logger=Logger.getLogger(SocketHandler.class);
    private WebSocketServerHandshaker handshaker;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      //  System.out.println("数据是："+msg);
        if (msg instanceof FullHttpRequest) {
            logger.info("this is http quest!");
            String json=handleHttpRequest(ctx, (FullHttpRequest) msg);
            if (json.equals("ok")){
                logger.info("请求升级为长连接成功！");
            }
        }else if (msg instanceof WebSocketFrame) {
            String data = handlerWebSocketFrame(ctx, (TextWebSocketFrame)msg);
            logger.info("完整的数据是：  " + data);
            if ("#".equals(data.substring(0, 1))) {
                String value = data.substring(1, 2);
                logger.info("MAC地址是： " + value);
                DateTime now = DateTime.now();
                String my_time = now.toString("yyyy-MM-dd HH:mm:ss");
                logger.info("入库成功-当前时间是：" + my_time + "MAC：" + value + "烟感：" + 0);
            }
        }
    }
    private String handlerWebSocketFrame(ChannelHandlerContext ctx,
                                         WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame
                    .retain());
        }
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return frame.content().retain().toString();
        }

        String data = ((TextWebSocketFrame) frame).text();
        System.out.println("data是：" + data);
        return data;

    }
    private String handleHttpRequest(ChannelHandlerContext ctx,
                                     FullHttpRequest req) throws InterruptedException, UnsupportedEncodingException {
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {

            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://0.0.0.0:8001" + req.uri(), null, false);
        handshaker = wsFactory.newHandshaker(req);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
        return "ok";
    }
    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, DefaultFullHttpResponse res) {
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static boolean isKeepAlive(FullHttpRequest req) {
        return false;
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


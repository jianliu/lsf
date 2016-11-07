package com.liuj.lsf.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import com.liuj.lsf.Constants;
import com.liuj.lsf.codec.impl.JacksonCodec;
import com.liuj.lsf.msg.BaseMsg;
import com.liuj.lsf.msg.MsgHeader;
import com.liuj.lsf.msg.RequestMsg;
import com.liuj.lsf.msg.ResponseMsg;

import java.io.IOException;

/**
 * encode lsf协议
 * Created by cdliujian1 on 2016/6/15.
 */
public class LSFEncoder extends MessageToByteEncoder {

    private Codec codec = new JacksonCodec();

    private int i = 1;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ByteBuf byteBuf = null;
        if (out == null) {
            out = ctx.alloc().buffer();
        }
        try {
            if (msg instanceof BaseMsg) {
                byteBuf = encodeMsg(ctx, (BaseMsg) msg, out);
            }

        } finally {
            if (byteBuf != null) {
                byteBuf.release();
            }
        }
    }

    private ByteBuf encodeMsg(ChannelHandlerContext ctx, BaseMsg baseMsg, ByteBuf out) throws IOException {
        ByteBuf byteBuf = all(ctx, baseMsg);
        int totalLength = 4 + byteBuf.readableBytes();
        checkCapacity(out, totalLength);
        out.writeBytes(Constants.MAGICCODEBYTE);
        out.writeInt(totalLength);

        out.writeBytes(byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes());

        return byteBuf;
    }

    private void checkCapacity(ByteBuf out, int totalLength) {
        if (out.capacity() < totalLength + 2) {
            out.capacity(totalLength + 2);
        }
    }

    private ByteBuf all(ChannelHandlerContext ctx, BaseMsg baseMsg) throws IOException {
        ByteBuf byteBuf = ctx.alloc().buffer();
        MsgHeader header = baseMsg.getMsgHeader();

        byte[] body = null;
        int bodyLength = 0;
        if (baseMsg instanceof RequestMsg) {
            body = codec.encode(((RequestMsg) baseMsg).getConsumerBean());
            bodyLength = body.length;
        } else if (baseMsg instanceof ResponseMsg) {
            body = codec.encode(((ResponseMsg) baseMsg).getResponse());
            bodyLength = body.length;
        } else if (baseMsg.getMsgBody() != null) {
            bodyLength = baseMsg.getMsgBody().readableBytes();
        }

        header.setLength(bodyLength);
        //codec
        byte[] headerByte = codec.encode(header);
        //header de length
        byteBuf.writeInt(headerByte.length);
        byteBuf.writeBytes(headerByte);

        if (baseMsg instanceof RequestMsg || baseMsg instanceof ResponseMsg) {
            byteBuf.writeBytes(body);
        } else if (baseMsg.getMsgBody() != null) {
            byteBuf.writeBytes(baseMsg.getMsgBody(), baseMsg.getMsgBody().readerIndex(), baseMsg.getMsgBody().readableBytes());
        }
        return byteBuf;
    }

}
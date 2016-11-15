package com.liuj.lsf.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import com.liuj.lsf.Constants;
import com.liuj.lsf.codec.impl.JacksonCodec;
import com.liuj.lsf.config.ConsumerConfig;
import com.liuj.lsf.exceptions.LsfException;
import com.liuj.lsf.msg.BaseMsg;
import com.liuj.lsf.msg.MsgHeader;
import com.liuj.lsf.msg.RequestMsg;
import com.liuj.lsf.msg.ResponseMsg;
import com.liuj.lsf.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * decode lsf协议
 * Created by cdliujian1 on 2016/6/19.
 */
public class LSFDecoder extends ByteToMessageDecoder {

    private final static Logger logger = LoggerFactory.getLogger(LSFDecoder.class);

    private Codec codec = new JacksonCodec();

    //魔数的长度
    private int magicLength = 2;

    //消息体总长度的标识的长度
    private int contentAllLengthByteLength = 4;

    //消息header中header长度标识的长度
    private int contentHeaderLengthByteLength = 4;

    //非消息体的长度 2 + 4
    private int unBodySliceStartLength = 6;

    public LSFDecoder() {
    }

    public LSFDecoder(int magicLength, int contentAllLengthByteLength, int contentHeaderLengthByteLength, int unBodySliceStartLength) {
        this.magicLength = magicLength;
        this.contentAllLengthByteLength = contentAllLengthByteLength;
        this.contentHeaderLengthByteLength = contentHeaderLengthByteLength;
        this.unBodySliceStartLength = unBodySliceStartLength;
    }

    @Override
    protected void decode(
            ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

        if(!checkFrameBytePrefixLength(in)){
            return;
        }

        int contentAllLength = (int) in.getUnsignedInt(in.readerIndex() + magicLength);
        int contentLength = contentAllLength - contentAllLengthByteLength;
        int frameLength = contentLength + contentAllLengthByteLength + magicLength;

        if (frameLength > in.readableBytes()) {
            //数据还不够,返回
            return;
        }

        if (frameLength < unBodySliceStartLength) {
            in.skipBytes(frameLength);
            throw new LsfException("不应该发生的异常,除非in被其他线程操作了");
        }

        BaseMsg msg = decodeBody(in, contentLength);
        if (msg != null) {
            in.readerIndex(frameLength);
            logger.debug("get message success");
            out.add(msg);
        }
    }

    /**
     * 除开magic\总长度外的byteBuf ，解码成消息对象
     * @param in
     * @param contentLength
     * @return
     */
    private BaseMsg decodeBody(ByteBuf in, int contentLength) {
        ByteBuf realBuf = in.slice(unBodySliceStartLength, contentLength);
        //计数器+1
        realBuf.retain();
        //headerlength
        int headerLength = realBuf.readInt();
        ByteBuf headerBuf = realBuf.slice(realBuf.readerIndex(), headerLength);
        byte[] header = new byte[headerLength];
        headerBuf.readBytes(header);
        MsgHeader msgHeader = decodeMessageHeader(header);
        BaseMsg baseMsg = ensureMsg(realBuf, msgHeader, headerLength + contentHeaderLengthByteLength);
        //计算器-1，-1后如果计算器和初始值1相同，则会释放内存
        realBuf.release();
        return baseMsg;
    }

    private boolean checkFrameBytePrefixLength(ByteBuf in){
        if (in.readableBytes() < unBodySliceStartLength) {
            logger.debug("size less than 6:{}", in.readableBytes());
            return false;
        }

        //不会导致readerIndex变更
        short magic = in.getUnsignedByte(in.readerIndex());
        logger.debug("magic:{}", magic);
        return true;
    }

    /**
     * @param in
     * @param msgHeader
     * @param start     headerLength标识占4位 + header内容，即headerLength
     * @return
     */
    private BaseMsg ensureMsg(ByteBuf in, MsgHeader msgHeader, int start) {

        if (msgHeader.getMsgType() == Constants.REQUEST_MSG) {
            RequestMsg msg = new RequestMsg();
            msg.setSendTime(System.currentTimeMillis());
            msg.setMsgBody(in.slice(start, msgHeader.getLength()));
            msg.setMsgHeader(msgHeader);
            int bodyLength = msgHeader.getLength();
            if (bodyLength > 0) {
                byte[] body = new byte[bodyLength];
                msg.getMsgBody().readBytes(body);
                ConsumerConfig consumerBean = codec.decode(body, ConsumerConfig.class);
                msg.setConsumerBean(consumerBean);
            }
            return msg;
        } else if (msgHeader.getMsgType() == Constants.RESPONSE_MSG) {
            ResponseMsg msg = new ResponseMsg();
            msg.setMsgBody(in.slice(start, msgHeader.getLength()));
            msg.setMsgHeader(msgHeader);
            int bodyLength = msgHeader.getLength();
            if (bodyLength > 0) {
                byte[] body = new byte[bodyLength];
                msg.getMsgBody().readBytes(body);
                String targetClz = msgHeader.getClz();
                if(!Constants.NULL_RESULT_CLASS.equals(msg.getMsgHeader().getClz())) {
                    Object response = codec.decode(body, ReflectionUtils.forName(targetClz));
                    msg.setResponse(response);
                }
            }
            return msg;
        }

        throw new LsfException("unknown msgType");
    }

    //codec
    private MsgHeader decodeMessageHeader(byte[] header) {
        return codec.decode(header, MsgHeader.class);
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }
}

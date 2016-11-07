package com.liuj.lsf.codec;

/**
 * Created by cdliujian1 on 2016/6/19.
 * byte  2 :魔数
 * int   contentLength  :消息体总长度
 * int   headerLength  :header长度
 * (int header)        :header内容（成熟的框架header存放的数量有限，一般10个byte不到，readerByte或readInt获取），
 *                      这里decode的时候还是用了codec
 * int   bodyLength    :body长度  bodyLength可以直接存放在header部分，或者直接计算出来，如规定header10个字节
 * (int body)          :body内容
 */
public class LSFProtocol {
}

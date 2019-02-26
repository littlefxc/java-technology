package com.littlefxc.examples.base.thread.pipe;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * 管道输入/输出主要用于线程间的数据传输，传输的媒介是内存。具体实现：
 * <br>面向字节:
 * <ul>
 * <li>PipedWriter</li>
 * <li>PipedReader</li>
 * </ul>
 * <br>面向字符:
 * <ul>
 * <li>PipedOutputStream</li>
 * <li>PipedInputStream</li>
 * </ul>
 *
 * @author fengxuechao
 * @date 2019/2/26
 **/
public class Piped {

    public static void main(String[] args) throws IOException {
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader();
        // 将输出流和输入流进行必要的连接
//        writer.connect(reader);
        reader.connect(writer);
        Thread printThread = new Thread(new Print(reader), "PrintThread");
        printThread.start();
        int receive = 0;
        try {
            while ((receive = System.in.read()) != -1) {
                writer.write(receive);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    static class Print implements Runnable {

        private PipedReader reader;

        public Print(PipedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            int receive = 0;
            try {
                while ((receive = reader.read()) != -1) {
                    System.out.print((char) receive);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package analysis.track.format;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
/**
 * @deprecated
 */
public class TrackRecordReader extends RecordReader<LongWritable, Text> {
    private static final Log LOG = LogFactory.getLog(TrackRecordReader.class);

    private CompressionCodecFactory compressionCodecs = null;
    private long start;
    private long pos;
    private long end;
    private NewLineReader in;
    private int maxLineLength;
    private LongWritable key = null;
    private Text value = null;

    private byte[] separator = "end\n".getBytes();

    public void initialize(InputSplit genericSplit, TaskAttemptContext context)
            throws IOException {
        FileSplit split = (FileSplit) genericSplit;
        Configuration job = context.getConfiguration();
        this.maxLineLength = job.getInt("mapred.linerecordreader.maxlength", 2147483647);
        this.start = split.getStart();
        this.end = (this.start + split.getLength());
        Path file = split.getPath();
        this.compressionCodecs = new CompressionCodecFactory(job);
        CompressionCodec codec = this.compressionCodecs.getCodec(file);

        FileSystem fs = file.getFileSystem(job);
        FSDataInputStream fileIn = fs.open(split.getPath());
        boolean skipFirstLine = false;
        if (codec != null) {
            this.in = new NewLineReader(codec.createInputStream(fileIn), job);
            this.end = 9223372036854775807L;
        } else {
            if (this.start != 0L) {
                skipFirstLine = true;
                this.start -= this.separator.length;

                fileIn.seek(this.start);
            }
            this.in = new NewLineReader(fileIn, job);
        }
        if (skipFirstLine) {
            this.start += this.in.readLine(new Text(), 0, (int) Math.min(2147483647L, this.end - this.start));
        }
        this.pos = this.start;
    }

    public boolean nextKeyValue() throws IOException {
        if (this.key == null) {
            this.key = new LongWritable();
        }
        this.key.set(this.pos);
        if (this.value == null) {
            this.value = new Text();
        }
        int newSize = 0;
        while (this.pos < this.end) {
            newSize = this.in.readLine(this.value, this.maxLineLength,
                    Math.max((int) Math.min(2147483647L, this.end - this.pos), this.maxLineLength));
            if (newSize == 0) {
                break;
            }
            this.pos += newSize;
            if (newSize < this.maxLineLength) {
                break;
            }
            LOG.info("Skipped line of size " + newSize + " at pos " + (this.pos - newSize));
        }
        if (newSize == 0) {
            this.key = null;
            this.value = null;
            return false;
        }
        return true;
    }

    public LongWritable getCurrentKey() {
        return this.key;
    }

    public Text getCurrentValue() {
        return this.value;
    }

    public float getProgress() {
        if (this.start == this.end) {
            return 0.0F;
        }
        return Math.min(1.0F, (float) (this.pos - this.start) / (float) (this.end - this.start));
    }

    public synchronized void close() throws IOException {
        if (this.in != null)
            this.in.close();
    }

    public class NewLineReader {
        private static final int DEFAULT_BUFFER_SIZE = 65536;
        private int bufferSize = 65536;
        private InputStream in;
        private byte[] buffer;
        private int bufferLength = 0;
        private int bufferPosn = 0;

        public NewLineReader(InputStream in) {
            this(in, 65536);
        }

        public NewLineReader(InputStream in, int bufferSize) {
            this.in = in;
            this.bufferSize = bufferSize;
            this.buffer = new byte[this.bufferSize];
        }

        public NewLineReader(InputStream in, Configuration conf) throws IOException {
            this(in, conf.getInt("io.file.buffer.size", 65536));
        }

        public void close() throws IOException {
            this.in.close();
        }

        public int readLine(Text str, int maxLineLength, int maxBytesToConsume) throws IOException {
            str.clear();
            Text record = new Text();
            int txtLength = 0;
            long bytesConsumed = 0L;
            boolean newline = false;
            int sepPosn = 0;
            do {
                if (this.bufferPosn >= this.bufferLength) {
                    this.bufferPosn = 0;
                    this.bufferLength = this.in.read(this.buffer);

                    if (this.bufferLength <= 0) {
                        break;
                    }
                }
                int startPosn = this.bufferPosn;
                for (; this.bufferPosn < this.bufferLength; this.bufferPosn += 1) {
                    if ((sepPosn > 0) && (this.buffer[this.bufferPosn] != this.this$0.separator[sepPosn])) {
                        sepPosn = 0;
                    }

                    if (this.buffer[this.bufferPosn] == this.this$0.separator[sepPosn]) {
                        this.bufferPosn += 1;
                        int i = 0;

                        for (sepPosn++; sepPosn < this.this$0.separator.length; sepPosn++) {
                            if (this.bufferPosn + i >= this.bufferLength) {
                                this.bufferPosn += i - 1;
                                break;
                            }

                            if (this.buffer[(this.bufferPosn + i)] != this.this$0.separator[sepPosn]) {
                                sepPosn = 0;
                                break;
                            }
                            i++;
                        }

                        if (sepPosn == this.this$0.separator.length) {
                            this.bufferPosn += i;
                            newline = true;
                            sepPosn = 0;
                            break;
                        }
                    }
                }
                int readLength = this.bufferPosn - startPosn;
                bytesConsumed += readLength;

                if (readLength > maxLineLength - txtLength) {
                    readLength = maxLineLength - txtLength;
                }
                if (readLength > 0) {
                    record.append(this.buffer, startPosn, readLength);
                    txtLength += readLength;

                    if (newline)
                        str.set(record.getBytes(), 0, record.getLength() - this.this$0.separator.length);
                }
            }
            while ((!newline) && (
                    bytesConsumed < maxBytesToConsume));
            if (bytesConsumed > 2147483647L) {
                throw new IOException("Too many bytes before newline: " + bytesConsumed);
            }

            return (int) bytesConsumed;
        }

        public int readLine(Text str, int maxLineLength) throws IOException {
            return readLine(str, maxLineLength, 2147483647);
        }

        public int readLine(Text str) throws IOException {
            return readLine(str, 2147483647, 2147483647);
        }
    }
}
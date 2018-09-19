package com.vdata.etl.map;

import java.io.File;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.vdata.analysis.utils.DateParserUtils;
import com.vdata.etl.reduce.TrackEtlReducer;
import com.vdata.etl.reduce.TrackSpamReducer;

public class TrackEtlDriver extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		String[] otherArgs = new GenericOptionsParser(getConf(), args)
				.getRemainingArgs();
		if (otherArgs.length < 2) {
			System.out
					.println("Wrong number of arguments: " + otherArgs.length);
			return -1;
		}

		String basehdfsInPath = otherArgs[0];// 存放在hdfs上面的文件路径
		String basehdfsOutPath = otherArgs[1];// hdfs输出路径
		String basecateDicPath = otherArgs[2];// 字典表
		String basespamIpPath = otherArgs[3];// 爬虫的ip

		String date = DateParserUtils.getToday();
		date="20160910";//用于测试
		
		int status=0;
		Configuration conf = getConf();

		
		// 反爬虫job
		Job spamJob = Job.getInstance(conf);

		spamJob.setJarByClass(TrackEtlDriver.class);

		spamJob.setJobName("Spam");
		spamJob.setJar("C:\\commonFiles\\vdata\\MyJob.jar");

		spamJob.setMapOutputKeyClass(Text.class);
		spamJob.setMapOutputValueClass(Text.class);
		spamJob.setOutputKeyClass(NullWritable.class);
		spamJob.setOutputValueClass(Text.class);

		spamJob.setMapperClass(TrackSpamMapper.class);
		spamJob.setReducerClass(TrackSpamReducer.class);

		String spamIpPath = basespamIpPath + "/" + date;
		String dataHdfsInpath = basehdfsInPath +"/"+ date;
		
		System.out.println("******data input******"+dataHdfsInpath);
		System.out.println("******spam out********"+spamIpPath);

		FileInputFormat.addInputPath(spamJob, new Path(dataHdfsInpath));
		FileOutputFormat.setOutputPath(spamJob, new Path(spamIpPath));
		status=spamJob.waitForCompletion(true)?0:1;
		
		System.out.println("---------etl begin-------------");
		
		Job etlJob = Job.getInstance(conf);

		etlJob.setJarByClass(TrackEtlDriver.class);

		etlJob.setJobName("ETL");
		etlJob.setJar("C:\\commonFiles\\vdata\\MyJob.jar");

		etlJob.setMapOutputKeyClass(Text.class);
		etlJob.setMapOutputValueClass(Text.class);
		etlJob.setOutputKeyClass(NullWritable.class);
		etlJob.setOutputValueClass(Text.class);

		etlJob.setMapperClass(TrackEtlMapper.class);
		etlJob.setReducerClass(TrackEtlReducer.class);
		etlJob.setNumReduceTasks(3);//根据数据量大小动态设置
		// 加载字典表
		String cateDicPath = basecateDicPath + "/"+date+"/"
				+ "catedic#cateflag";
		DistributedCache.addCacheFile(new URI(cateDicPath),
				etlJob.getConfiguration());

		// 加载爬虫ip,加目录下的所有文件
		FileSystem fs = FileSystem.get(conf);
		FileStatus[] fileList = fs.listStatus(new Path(spamIpPath));
		for (int i = 0; i < fileList.length; i++) {
			String fileName = fileList[i].getPath().getName();
			if (fileName.equals("_SUCCESS"))
				continue;
			System.out.println("**********spamInputPath***********"
					+ spamIpPath + "/" + fileName);
			String inPath1Link = new Path(spamIpPath + "/" + fileName).toUri()
					.toString() + "#" + "spamIp" + i;
			DistributedCache.addCacheFile(new URI(inPath1Link),
					etlJob.getConfiguration());
		}

		String dataHdfsOutputPath = basehdfsOutPath + "/" + date;
		FileInputFormat.addInputPath(etlJob, new Path(dataHdfsInpath));
		FileOutputFormat.setOutputPath(etlJob, new Path(dataHdfsOutputPath));
		
		status=etlJob.waitForCompletion(true)?0:1;
		System.out.println("***********over***************");
		
		return status;
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("HADOOP_USER_NAME", "root");
		int status = ToolRunner.run(new TrackEtlDriver(), args);
		System.exit(status);

	}

}

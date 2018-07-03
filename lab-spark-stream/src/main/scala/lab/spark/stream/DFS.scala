package lab.spark.stream

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object DFS {
  def main(args: Array[String]):Unit = {
    val conf = new SparkConf().setAppName("hdfs").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val textRDD = sc.textFile("hdfs://bd1:8020/test/data/data.txt")
//    textRDD.reduce((_1,_2)=>{
//      println(_1)
//      println(_2)
//      _2
//    })
    val kvRDD = textRDD.keyBy(line=>{
      val split = line.split(",")
      split(0)+split(2)
    });
    val datas = kvRDD.map(kvs=>{
      (kvs._1,Integer.parseInt(kvs._2.split(",")(4)))
    })
    val sumDatas = datas.groupByKey().map(kv=>{
      var sum=0
      kv._2.foreach(v=>{
        sum+=v
      })
      (kv._1,sum)
    })
//    val order = sumDatas.reduce((a,b)=>{
//      if(a._2>b._2){
//        a
//      }else{
//        b
//      }
//    })
    sumDatas.sortBy(f=>{
      f._2
    }).collect().foreach(f=>{
      println(f)
    })
//    println(order._1,order._2)
//    order.foreach(kv=>{
//      println(kv)
//    })
    
//    kvRDD.foreach(kv=>{
//      println(kv.)
////      println(k.)
//    })
//    kvRDD.groupByKey().reduce((current,nexut)=>{
//      values._2.foreach(line=>{
//        
//      })
//      values._2.
//      var sum = 0
//      current._2.foreach(str=>{
//        sum+=Integer.parseInt(str.split(",")(4));
//      })
//      nexut._2.foreach(str=>{
//        sum+=Integer.parseInt(str.split(",")(4));
//      })
//      println(key)
//      values
//      current
//    });
//    println(kvRDD.groupByKey().count());
    
//    textRDD.foreach(line=>{
//      println(line);
//    })
  }
}
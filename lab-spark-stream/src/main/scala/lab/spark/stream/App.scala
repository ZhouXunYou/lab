package lab.spark.stream

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import java.util.Properties
import org.apache.spark.rdd.RDD
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.{ Map => JavaMap }
object App {

  def main(args: Array[String]) {
    //    val hadoopHomeProperties:java.lang.String = "hadoop.home.dir"
    //    val hadoopHome:java.lang.String  = "E:\\Dev\\hadoop-2.8.1"
    //    val props:Properties = new Properties();
    //    props.put(hadoopHomeProperties, hadoopHome)
    //    System.setProperties(props)
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "bd1:9092,bd2:9092,bd3:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "earliest",
//      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean))
    val conf = new SparkConf().setAppName("A").setMaster("local[3]")
    val streamingContext = new StreamingContext(conf, Seconds(3))
    val topics = Array("kpidata")
    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams))
    stream.foreachRDD(rdd => {
      
      
      
      val om = new ObjectMapper()
      val messages = rdd.map(message=>{
//        val map = 
//        map.put("offset", message.offset())
        var value = message.value()
//        value.replace
//        value = value.substring(0,value.length()-3)+",\n  \"offset\":"+message.offset()+"\n}"
        var map = om.readValue(value, classOf[JavaMap[String,Any]])
        map.put("offset", message.offset())
        map.put("1", 1)
        map
      })
//      messages.reduce((key,value)=>{
//        println(key,value)
//        null
//      })
      messages.foreach(message=>{
        println(message)
      })
//      rdd.foreach(message=>{
//        val value = message.value()
//        println(value)
//      })
//      val data = rdd.map(message => {
//        val value = message.value()
//        println(value)
//        val valueMap = om.readValue(value, classOf[JavaMap[String, Any]])
//        valueMap.put("offset", message.offset());
//      })
    })
    streamingContext.start()
    streamingContext.awaitTermination()
  }

}

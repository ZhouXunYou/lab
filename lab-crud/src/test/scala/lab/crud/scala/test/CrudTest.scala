package lab.crud.scala.test
import java.util.ArrayList
import java.util.Date
import java.util.{ List => JavaList }

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import lab.crud.scala.service.TestService
import org.springframework.test.context.junit4.SpringRunner
import java.util.Random
import lab.crud.suport.FieldCondition
import lab.crud.suport.FieldCondition
import lab.crud.suport.Operator
import lab.crud.suport.XOR
import lab.crud.suport.Sortor
import lab.crud.suport.SortDirection
import lab.crud.suport.Pagination

@RunWith(classOf[SpringRunner])
@SpringBootTest(classes=Array(classOf[RunConfig]))
class CrudTest {
  @Autowired
  private var testService:TestService = _
  /*
  def getTestService:TestService={
    this.testService
  }
  def setTestService(testService:TestService):Unit = {
    this.testService = testService
  }
  */
  @Test
  def testAdd:Unit = {
    val t:lab.crud.scala.entity.Test = new lab.crud.scala.entity.Test()
    t.setCreateTime(new Date());
    t.setUpdateTime(new Date());
    println(testService.saveOrUpdate(t))
  }
  @Test
  def testBatchAdd:Unit = {
    val tests:JavaList[lab.crud.scala.entity.Test]  = new ArrayList
    for (i <- 0 until 50) {
      val t:lab.crud.scala.entity.Test = new lab.crud.scala.entity.Test()
//      t.setId(String.valueOf(i))
      t.setName(String.valueOf(i) + "name")
      
      t.setSex(new Random().nextInt(1-0+1)+0)
      t.setAge(new Random().nextInt(20-18+1)+18)
      tests.add(t)
    }
    testService.batchSaveOrUpdate(tests)
  }
  @Test
  def testRemoveBySerializable:Unit = {
    testService.remove("1");
  }
  @Test
  def testRemoveByEntity:Unit = {
    val t:lab.crud.scala.entity.Test = new lab.crud.scala.entity.Test()
    t.setId("2")
    testService.remove(t)
  }
  @Test
  def testBatchRemoveBySerializable:Unit = {
//    testService.batchRemove("1","2","3")
  }
  @Test
  def testBatchRemoveByEntity:Unit = {
    val t1:lab.crud.scala.entity.Test = new lab.crud.scala.entity.Test()
    t1.setId("1")
    val t2:lab.crud.scala.entity.Test = new lab.crud.scala.entity.Test()
    t2.setId("2")
    val t3:lab.crud.scala.entity.Test = new lab.crud.scala.entity.Test()
    t3.setId("3")
    val entities:JavaList[lab.crud.scala.entity.Test] = new ArrayList
    entities.add(t1)
    entities.add(t2)
    entities.add(t3)
    testService.batchRemove(entities)
  }
  
  @Test
  def testGet:Unit = {
    val test = testService.get("40288881643f49e701643f49ec3600001");
    println("--------------------------------",test.getId())
  }
  @Test
  def testFind:Unit = {
    val fieldConditions = new ArrayList[FieldCondition]
    val nameFieldCondition = new FieldCondition("name",Operator.LIKE,"1")
    fieldConditions.add(nameFieldCondition);
//    var list = testService.find(fieldConditions)
//    for(i<-0 until list.size()){
//      println(list.get(i).getName)
//    }
//    println(list.size(),"--------------------------------")
//    val name_FieldCondition = new FieldCondition("name",Operator.LIKE,XOR.Or,"2")
//    fieldConditions.add(name_FieldCondition)
//    list = testService.find(fieldConditions)
//    for(i<-0 until list.size()){
//      println(list.get(i).getName)
//    }
//    println(list.size(),"--------------------------------")
//    
//    val ageFieldCondition = new FieldCondition("age",Operator.BETWEEN)
//    ageFieldCondition.setFrom(new Integer(18));
//    ageFieldCondition.setTo(new Integer(19));
//    fieldConditions.add(ageFieldCondition)
//    list = testService.find(fieldConditions)
//    for(i<-0 until list.size()){
//      println(list.get(i).getName)
//    }
//    println(list.size(),"--------------------------------")
//    val sexFieldCondition = new FieldCondition("sex",Operator.EQ,1)
//    fieldConditions.add(sexFieldCondition)
//    list = testService.find(fieldConditions)
//    for(i<-0 until list.size()){
//      println(list.get(i).getName)
//    }
//    println(list.size(),"--------------------------------")
    val sorts:Array[Sortor] = Array(new Sortor("age",SortDirection.ASC),new Sortor("name",SortDirection.ASC))
//    sorts.add(new Sortor("age",SortDirection.ASC))
//    sorts.add(new Sortor("name",SortDirection.ASC))
    
    val pagination:Pagination = new Pagination
    pagination.setPageNo(1);
    pagination.setPageSize(3);
    var list = testService.find(fieldConditions,pagination,sorts).getContent
     for(i<-0 until list.size()){
      println(list.get(i).getName,list.get(i).getAge)
    }
  }
  
}
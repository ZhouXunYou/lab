package lab.crud.scala.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import lab.crud.entity.superdomain.UUIDBaseEntity

@Entity
@Table(name="t_test")
class Test extends UUIDBaseEntity{
  private var name:String=_
  private var age:Integer=_
  private var sex:Integer=_
  @Column(name="_name",length=100)
  def getName:String = {
    this.name
  }
  def setName(name:String):Unit = {
    this.name = name
  }
  @Column(name="age")
  def getAge:Integer = {
    this.age
  }
  def setAge(age:Integer):Unit = {
    this.age = age
  }
  @Column(name="sex")
  def getSex:Integer = {
    this.sex
  }
  def setSex(sex:Integer):Unit = {
    this.sex = sex
  }
  
}
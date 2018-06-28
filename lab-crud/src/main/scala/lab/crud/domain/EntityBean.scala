package lab.crud.domain

import java.io.Serializable

trait EntityBean {
  def getPrimaryKey:Serializable
  def setPrimaryKey(id:Serializable)
}
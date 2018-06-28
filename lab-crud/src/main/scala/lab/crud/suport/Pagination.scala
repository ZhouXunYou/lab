package lab.crud.suport

class Pagination {
  private var pageNo:Integer = 1
  private var pageSize:Integer = 20
  
  def setPageNo(pageNo:Integer):Unit = {
    this.pageNo = pageNo
  }
  def setPageSize(pageSize:Integer):Unit = {
    this.pageSize = pageSize
  }
  
  def getPageNo:Integer = {
    this.pageNo
  }
  def getPageSize:Integer = {
    this.pageSize
  }
}
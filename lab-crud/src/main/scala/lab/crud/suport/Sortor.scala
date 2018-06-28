package lab.crud.suport

class Sortor($sortField:String) {
  
  private var sortField:String = $sortField
  private var sortDirection:SortDirection.Value = SortDirection.ASC
  def this(field:String,sortDirection:SortDirection.Value){
    this(field)
    this.sortDirection = sortDirection
  }
  
  def getSortField:String = {
    this.sortField
  }
  def getSortDirection:SortDirection.Value = {
    this.sortDirection
  }
  
  
}
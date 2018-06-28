package lab.crud.suport
class FieldCondition($field:String,$operator:Operator.Value,$value:Any) {
  
  def this(field:String,operator:Operator.Value,xor:XOR.Value,value:Any){
    this(field,operator,value)
    this.xor = xor
    this.value = value
  }
  def this(field:String,operator:Operator.Value){
    this(field,operator,null)
  }
  def getField:String = {
    this.field
  }
  def getOperator:Operator.Value = {
    this.operator
  }
  def getValue:Any={
    this.value
  }
  def getFromValue:Any={
    getValue
  }
  def getToValue:Any={
    this.toValue
  }
  def getXor:XOR.Value = {
    this.xor
  }
  def setFrom(value:Any):Unit = {
    this.value = value
  }
  def setTo(value:Any):Unit = {
    this.toValue = value
  }
  
  /**
   * 查询字段
   */
  private var field: String = $field
  /**
   * 需要执行的条件逻辑运算
   */
  private var operator:Operator.Value = $operator
  /**
   * 条件连接
   */
  private var xor = XOR.And
  /**
   * 条件值
   */
  private var value: Any = $value;
  /**
   * 范围查询结束值
   */
  private var toValue: Any = _;
}
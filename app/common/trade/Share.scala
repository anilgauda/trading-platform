package common.trade

case class Share(name: String,tickerCode: String,price: BigDecimal =BigDecimal(0),avgPrice50: BigDecimal=BigDecimal(0)
                 ,avgPrice200: BigDecimal=BigDecimal(0), ebitda:BigDecimal=BigDecimal(0),oneYearTargetPrice: BigDecimal=BigDecimal(0)){
}

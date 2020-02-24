package common.trade

case class Share(name: String,tickerCode: String,price: String,avgPrice50Percent: String
                   ,avgPrice200Percent: String, askValue:String,bid: String, prevClose: String,
                 eps: String,pe: String, annDate: String,priceHistory: Any,dividendHistory: Any,currency: String )
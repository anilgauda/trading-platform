package common.trade

import yahoofinance.{Stock, YahooFinance}

import scala.collection.mutable

class TradeApiConsumer {
  def getStock(ticker: String):Share={
    println(s"Ticker value $ticker")
    val stock: Stock=YahooFinance.get(ticker)
    val ebitda :BigDecimal={ if(stock.getStats.getEBITDA==null) BigDecimal(0) else stock.getStats.getEBITDA}
    val getOneYearTargetPrice :BigDecimal={ if(stock.getStats.getOneYearTargetPrice==null) BigDecimal(0) else stock.getStats.getOneYearTargetPrice}
    val share: Share=Share(stock.getName,
      stock.getSymbol,
      stock.getQuote.getPrice,
      stock.getQuote.getChangeFromAvg50,
      stock.getQuote.getChangeFromAvg200,
      ebitda,
      getOneYearTargetPrice)
    return share
  }
    }


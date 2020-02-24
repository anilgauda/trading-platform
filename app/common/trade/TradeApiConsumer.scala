package common.trade

import yahoofinance.{Stock, YahooFinance}

class TradeApiConsumer {
  def getStock(ticker: String):Share={
    val stock: Stock=YahooFinance.get(ticker)
    val share: Share=Share(stock.getName,
      stock.getSymbol,
      stock.getQuote.getPrice.toString,
      stock.getQuote.getChangeFromAvg50InPercent.toString,
      stock.getQuote.getChangeFromAvg200InPercent.toString,
      stock.getQuote.getAsk.toString,
      stock.getQuote.getBid.toString,
      stock.getQuote.getPreviousClose.toString,
      stock.getStats.getEps match {
         case null => "-"
         case _ => stock.getStats.getEps.toString
       },
      stock.getStats.getPe match {
        case null => "-"
        case _ =>stock.getStats.getPe.toString
      },
      stock.getStats.getEarningsAnnouncement match {
        case null => "-"
        case _ => stock.getStats.getEarningsAnnouncement.getTime.toString
      },
      stock.getHistory,
      stock.getDividendHistory,
      stock.getCurrency
    )
     share
  }
    }


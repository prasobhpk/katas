package katas.groovy.yahoofinance

import katas.groovy.yahoofinance.log.YLog
import katas.groovy.yahoofinance.quotes.Quote
import katas.groovy.yahoofinance.quotes.QuoteSource

import static katas.groovy.yahoofinance.quotes.Quote.parseDate
/**
 * User: dima
 * Date: 31/08/2012
 */
class Y2 {
  public static void main(String[] args) {
    BigDecimal money = 0
    int position = 0
    def var = new IndicatorService.VarianceCalc(7)

    def buy = { Quote quote, int amount ->
      position += amount
      money -= quote.open * amount
      println(quote)
    }

    def sell = { Quote quote, int amount ->
      position -= amount
      money += quote.open * amount
      println(quote)
    }

    def log = new YLog()
    def quoteSource = new QuoteSource(log)
    println(position + " " + money)
    quoteSource.quotesFor("YHOO", "2000-01-01", "2001-01-01").reverse().each { Quote quote ->
      println(quote)
      println(var.calc(quote.open))
      if (quote.date == parseDate("2009-09-14")) {
        buy(quote, 1)
      }
      if (quote.date == parseDate("2009-10-14")) {
        sell(quote, 1)
      }
    }
    println(position + " " + money)
  }

}

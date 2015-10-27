package houseprices.csv

/*
 * Copyright (C) 2014 Juergen Pfundt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.parboiled2._

import scala.util.{ Failure, Success }

trait CSVParboiledParser extends Parser {
  /* start of csv parser */
  def csvfile = rule { (hdr ~ zeroOrMore(row)) ~> makeListOfList ~ zeroOrMore(optional("\r") ~ "\n") ~ EOI }
  def hdr = rule { row }
  def row = rule { oneOrMore(field).separatedBy(",") ~> makeList ~ optional("\r") ~ "\n" }
  def field = rule { string | text | MATCH ~> makeEmpty }
  def text = rule { capture(oneOrMore(noneOf(",\"\n\r"))) ~> makeText }
  def string = rule { WS ~ "\"" ~ capture(zeroOrMore("\"\"" | noneOf("\""))) ~> makeString ~ "\"" ~ WS }

  val whitespace = CharPredicate(" \t")
  def WS = rule { zeroOrMore(whitespace) }

  /* type conversion */
  def makeList = (r: Seq[String]) => r.toList: List[String]
  def makeListOfList = (h: List[String], r: Seq[List[String]]) => h :: (r.toList: List[List[String]])

  /* parser action */
  def makeText: (String) => String
  def makeString: (String) => String
  def makeEmpty: () => String
}

trait CSVParserAction {
  // remove leading and trailing blanks
  def makeText = (text: String) => text.trim
  // replace sequence of two double quotes by a single double quote
  def makeString = (string: String) => string.replaceAll("\"\"", "\"")
  // modify result of EMPTY token if required
  def makeEmpty = () => ""
}

trait CSVParserIETFAction extends CSVParserAction {
  // no trimming of WhiteSpace
  override def makeText = (text: String) => text
}

class CSVParboiledParserCLI(val input: ParserInput) extends CSVParboiledParser with CSVParserIETFAction {
  csvfile.run() match {
    case Success(result) => println(result)
    case Failure(e: ParseError) => println("Expression is not valid: " + formatError(e))
    case Failure(e) => println("Unexpected error during parsing run: " + e)
  }
}

object CSVParserCLI {
  def main(args: Array[String]) {
    lazy val inputfile: ParserInput = io.Source.fromFile(args(0)).mkString
    new CSVParboiledParserCLI(inputfile)
  }
}


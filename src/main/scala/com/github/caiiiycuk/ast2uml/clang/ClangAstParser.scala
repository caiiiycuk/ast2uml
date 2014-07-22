package com.github.caiiiycuk.ast2uml.clang

import scala.annotation.tailrec
import scala.collection.immutable.Stream
import com.github.caiiiycuk.ast2uml._

object ClangAstParser {

  def apply(lines: Stream[String]): Ast =  AstRoot(extractChilds(lines)._2)

  private final val CLASS = """CXXRecordDecl.*class\s(.*)\sdefinition""".r
  private final val STRUCT = """CXXRecordDecl.*struct\s(.*)\sdefinition""".r
  private final val NAMESPACE = """NamespaceDecl.*\s(.*)$""".r
  private final val FIELD = """FieldDecl.*\s(.*)\s'(.*)'(| invalid)$""".r
  private final val METHOD = """CXXMethodDecl.*\s(.*)\s'(.*)'(| invalid)$""".r

  private def extractChilds(lines: Stream[String], childs: List[Ast] = List(), nesting: Int = 0): (Stream[String], List[Ast]) = if (lines.isEmpty) {
    (Stream.empty[String], childs)
  } else if (lineNesting(lines.head) - nesting >= 0) {
    val (astRest, astChilds) = extractChilds(lines.tail, List(), nesting + 1)
    extractChilds(astRest, childs :+ toAst("c(" + lineNesting(lines.head)  + ")"+ lines.head, astChilds), nesting)
  } else {
    (lines, childs)
  }

  private def lineNesting(line: String) =
    Math.max(Math.max(line.indexOf("|-"), line.indexOf("`-")) / 2, 0)

  private def toAst(line: String, childs: List[Ast]): Ast = {
    val node = List[Option[Ast]](
      CLASS.findFirstMatchIn(line).map(m => {
        AstClass(m.group(1), childs)
      }),
      STRUCT.findFirstMatchIn(line).map(m => {
        AstStruct(m.group(1), childs)
      }),
      NAMESPACE.findFirstMatchIn(line).map(m => {
        AstNamespace(m.group(1), childs)
      }),
      FIELD.findFirstMatchIn(line).map(m => {
        AstField(m.group(1), Some(m.group(2).replaceAll("'", "")), childs)
      }),
      METHOD.findFirstMatchIn(line).map(m => {
        AstMethod(m.group(1), Some(m.group(2).replaceAll("'", "")), childs)
      }))
	
    node.flatten.headOption.getOrElse(AstUnknown(line, childs))
  }

}